package com.anuj.qrscanner.model.dto;

import com.anuj.qrscanner.model.db.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("current_balance")
    private double currentBalance;
    @JsonProperty("mpin_set")
    private boolean mpinSet;

    public static UserDto getUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setCurrentBalance(user.getCurrentBalance());
        if (user.getMpin() != null) {
            userDto.setMpinSet(true);
        }else{
            userDto.setMpinSet(false);
        }
        return userDto;
    }

}
