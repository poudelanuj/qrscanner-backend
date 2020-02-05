package com.anuj.qrscanner.controller;

import com.anuj.qrscanner.model.db.User;
import com.anuj.qrscanner.model.dto.UserDto;
import com.anuj.qrscanner.model.dto.response.TransactionResponseDto;
import com.anuj.qrscanner.model.dto.response.UserResponseDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import static com.anuj.qrscanner.constant.Urls.BASE_URL;

@RestController
@RequestMapping(BASE_URL)
public class UserController {


    @ApiOperation(value = "Get User Info")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Transaction initiated successfully", response = UserResponseDto.class),
       })
    @GetMapping(value = "/user")
    public ResponseEntity<?> getUser(@ApiIgnore  User user){
        return ResponseEntity.ok(new UserResponseDto(UserDto.getUserDto(user)));
    }

}
