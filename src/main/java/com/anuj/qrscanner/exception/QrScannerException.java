package com.anuj.qrscanner.exception;


import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Data
@ApiModel(subTypes = {QrScannerErrorMessage.class}, discriminator = "type",
        description = "Supertype of all error thrown.")
public abstract class QrScannerException extends Exception {
    public abstract HttpStatus httpStatus();
    public  abstract String code();
    public abstract String error();
    public abstract String desc();
    public abstract String data();

}
