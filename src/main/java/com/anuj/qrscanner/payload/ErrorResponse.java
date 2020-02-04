package com.anuj.qrscanner.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    @JsonProperty("error_message")
    private String errorMessage;
    @JsonProperty("validation_error")
    private ValidationError validationError;

}