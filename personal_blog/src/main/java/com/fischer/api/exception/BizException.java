package com.fischer.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BizException extends RuntimeException{
    private HttpStatus status;
    private String message;

    public BizException(HttpStatus status,String message){
        super(message);
        this.status=status;
        this.message=message;

    }
    public BizException(HttpStatus status){
        this(status,status.getReasonPhrase());
    }

}
