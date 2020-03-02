package com.anuj.qrscanner.model.dto.response;

import com.anuj.qrscanner.model.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    @JsonProperty("data")
    private UserDto userDto;
}
