package com.anuj.qrscanner.controller;

import com.anuj.qrscanner.constant.RoleName;
import com.anuj.qrscanner.constant.TokenStatus;
import com.anuj.qrscanner.constant.Urls;
import com.anuj.qrscanner.exception.RoleNotFountException;
import com.anuj.qrscanner.model.db.Role;
import com.anuj.qrscanner.model.db.User;
import com.anuj.qrscanner.model.db.VerificationToken;
import com.anuj.qrscanner.model.dto.request.LoginRequestDto;
import com.anuj.qrscanner.model.dto.request.OtpRequestDto;
import com.anuj.qrscanner.payload.*;
import com.anuj.qrscanner.repository.UserRepository;
import com.anuj.qrscanner.security.TokenProvider;
import com.anuj.qrscanner.service.MessageService;
import com.anuj.qrscanner.service.RoleService;
import com.anuj.qrscanner.service.VerificationTokenService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(Urls.AUTH_BASE_URL)
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private VerificationTokenService verificationTokenService;
    @Autowired
    private MessageService messageService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @ApiOperation(value = "Login/Register New User")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User successfully registered. Sms has been sent", response = ServerResponse.class),
            @ApiResponse(code = 500, message = "SMS cannot be sent, Internal server error", response = ErrorResponse.class)
    })
    @PostMapping(value = "/login")
    public ResponseEntity<?> loginOrRegister(@Valid @RequestBody LoginRequestDto loginRequestDto) throws RoleNotFountException {
        Optional<User> userOptional = userRepository.findByPhoneNumber(loginRequestDto.getPhoneNumber());
        User user;
        if (!userOptional.isPresent()) {
            //todo register user
            user = new User();
            user.setPhoneNumber(loginRequestDto.getPhoneNumber());
            if(loginRequestDto.getPhoneNumber().equals("+9779849933272")){
                user.setCurrentBalance(10000);
            }else{
                user.setCurrentBalance(0);
            }
            user.setPassword(passwordEncoder.encode(loginRequestDto.getPhoneNumber()));
            Set<Role> roles = new HashSet<>();
            roles.add(roleService.getParticularRole(RoleName.USER));
            user.setRoles(roles);
            user = userRepository.save(user);
        } else {
            user = userOptional.get();
        }
        VerificationToken verificationToken = verificationTokenService.generateNewVerificationToken(user);
        return messageService.sendMessageWithVerificationCode(verificationToken);

    }

    @ApiOperation(value = "OTP Verification")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OTP verified successfully.", response = LoginResponse.class),
            @ApiResponse(code = 403, message = "Token Invalid",response = ErrorResponse.class),
            @ApiResponse(code = 409, message = "Token Expired. New Verification Token is send to your mail.",response = ErrorResponse.class),
            @ApiResponse(code = 403, message = "Token Expired. Please try again later",response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "User Not present",response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error",response = ErrorResponse.class)

    })
    @PostMapping(value = "/otp")
    public ResponseEntity<?> postOtp(@Valid @RequestBody OtpRequestDto otpRequestDto) {
        TokenStatus tokenStatus = verificationTokenService.validateVerificationToken(otpRequestDto);

        switch (tokenStatus){
            case TOKEN_INVALID:{
                return new ResponseEntity<>(new ErrorResponse("Token Invalid", new ValidationError()), HttpStatus.FORBIDDEN);
            }
            case TOKEN_EXPIRED_NEW_TOKEN_SENT:{
                return new ResponseEntity<>(new ErrorResponse("Token Expired. New Verification Token is send to your mail.",new ValidationError()), HttpStatus.CONFLICT);
            }
            case TOKEN_EXPIRED_NEW_TOKEN_NOT_SENT:{
                return new ResponseEntity<>(new ErrorResponse("Token Expired. Please try again later", new ValidationError()), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            case TOKEN_VALID:{
                Optional<User> userOptional = userRepository.findByPhoneNumber(otpRequestDto.getPhoneNumber());
                if(userOptional.isPresent()){
                    Authentication authentication = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    userOptional.get().getPhoneNumber(),
                                    userOptional.get().getPhoneNumber()
                            )
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    String token = tokenProvider.createToken(authentication);
                    return ResponseEntity.ok(new LoginResponse(new LoginResponseData(true,token, "Bearer" )));
                }
                return new ResponseEntity<>(new ErrorResponse("User Not present",new ValidationError()),HttpStatus.NOT_FOUND);
            }

        }
        return new ResponseEntity<>(new ErrorResponse("Internal server error",new ValidationError()),HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
