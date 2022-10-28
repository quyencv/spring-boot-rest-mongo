package com.example.demo.exception.custom;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusinessException extends Exception {

    private static final long serialVersionUID = -8057489853133441986L;

    private String messageCode;

    public BusinessException(String messageCode) {
        super("Business Exception");
        this.messageCode = messageCode;
    }

    public BusinessException(String messageCode, String message) {
        super(message);
        this.messageCode = messageCode;
    }
}