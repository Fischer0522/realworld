package com.fischer.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BizException extends RuntimeException{
    private HttpStatus status;
    private String msg;
    private Object data;

    public BizException(HttpStatus status,String msg){
        super(msg);
        this.status=status;
        this.msg=msg;
        this.data=null;

    }

    public BizException(HttpStatus status){
        this(status,status.getReasonPhrase());
    }

}
