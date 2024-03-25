package com.aix2.voice.advice.error;

import com.aix2.voice.advice.payload.ErrorCode;
import lombok.Getter;

@Getter
public class DefaultException extends RuntimeException{

    /**
    * DefaultException: 일반적인 예외 상황을 처리하기 위한 클래스로, RuntimeException을 상속받습니다.
     * 이 클래스는 애플리케이션에서 발생할 수 있는 다양한 예외 상황을 나타내기 위해 사용됩니다.
    */
    
    private ErrorCode errorCode;

    public DefaultException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public DefaultException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}
