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
@ApiModel(parent = Q.class, description = "Class representing docpress general error")
public class DocpressErrorMessage extends DocPressException {
    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    private String code = "ERROR_OCCURRED";
    private String error="Error Occurred";
    private String desc;
    private String data;

    public DocpressErrorMessage(String errorMessage){
        this.error = errorMessage;
    }
}
