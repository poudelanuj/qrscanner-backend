package com.anuj.qrscanner.service;

import com.anuj.qrscanner.constant.TokenStatus;
import com.anuj.qrscanner.model.db.User;
import com.anuj.qrscanner.model.db.VerificationToken;
import com.anuj.qrscanner.model.dto.request.OtpRequestDto;
import com.anuj.qrscanner.payload.AuthResponse;
import com.anuj.qrscanner.repository.UserRepository;
import com.anuj.qrscanner.repository.VerificationTokenRepository;
import com.nexmo.client.sms.MessageStatus;
import com.nexmo.client.sms.SmsSubmissionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static io.jsonwebtoken.Claims.EXPIRATION;

@Service("tokenService")
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    private final UserRepository userRepository;

    private final MessageService messageService;

    @Autowired
    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository,
                                    UserRepository userRepository,
                                    MessageService messageService) {
        this.verificationTokenRepository = verificationTokenRepository;
        this.userRepository = userRepository;
        this.messageService = messageService;
    }


    public TokenStatus validateVerificationToken(OtpRequestDto otpRequestDto) {
        Optional<VerificationToken> optionalVerificationToken = verificationTokenRepository.findByUser_PhoneNumber(otpRequestDto.getPhoneNumber());
        if (optionalVerificationToken.isPresent()) {
            VerificationToken verificationToken = optionalVerificationToken.get();
            if (verificationToken.getToken().equals(otpRequestDto.getOtp())) {
                final Calendar cal = Calendar.getInstance();
                if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
                    User user = optionalVerificationToken.get().getUser();
                    VerificationToken newVerificationToken = generateNewVerificationToken(user);
                    SmsSubmissionResponse response = messageService.sendSMS(newVerificationToken);
                    if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
                        return TokenStatus.TOKEN_EXPIRED_NEW_TOKEN_SENT;
                    } else {
                        return TokenStatus.TOKEN_EXPIRED_NEW_TOKEN_NOT_SENT;
                    }
                }
                return TokenStatus.TOKEN_VALID;
            } else {
                return TokenStatus.TOKEN_INVALID;
            }
        }
        return TokenStatus.TOKEN_INVALID;
    }

    public VerificationToken generateNewVerificationToken(User user) {
        Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByUser(user);
        VerificationToken verificationToken;
        if (verificationTokenOptional.isPresent()) {
            verificationToken = verificationTokenOptional.get();
            verificationToken.setToken(getToken());
            verificationToken.setExpiryDate(VerificationToken.calculateExpiryDate(VerificationToken.EXPIRATION));
        } else {
            verificationToken = new VerificationToken(getToken(), user);
        }
        verificationToken = verificationTokenRepository.save(verificationToken);
        return verificationToken;

    }


    private String getToken() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

}
