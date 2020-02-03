package com.anuj.qrscanner.exception;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

@Accessors(fluent = true)
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ApiModel(parent = QrScannerException.class, description = "Class representing p2p QR Wallet general error")
public class QrScannerErrorMessage extends QrScannerException {
    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    private String code = "ERROR_OCCURRED";
    private String error="Error Occurred";
    private String desc;
    private String data;

    public QrScannerErrorMessage(String errorMessage){
        this.error = errorMessage;
    }
}
