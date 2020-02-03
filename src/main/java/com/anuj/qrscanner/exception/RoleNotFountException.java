package com.anuj.qrscanner.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

@Accessors(fluent = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleNotFountException extends QrScannerException{
    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    private String code = "ROLE_NOT_FOUND";
    private String error="Particular Role Not found";
    private String desc;
    private String data;
}
