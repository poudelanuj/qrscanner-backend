package com.anuj.qrscanner.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

@Accessors(fluent = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class AuthenticationException extends QrScannerException {
    private HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
    private String code = "AUTHENTICATION_EXCEPTION";
    private String error="Please authenticate to access this resource.";
    private String desc;
    private String data;
}
