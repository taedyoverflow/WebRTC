package com.aix2.voice.advice.error;

import com.aix2.voice.advice.payload.ErrorCode;
import lombok.Getter;

@Getter
public class DefaultNullPointerException extends NullPointerException{

    /**
     * DefaultNullPointerException: NullPointerException을 상속받아, 널 포인터 접근과 같은 예외 상황에서 사용됩니다.
     */


    private ErrorCode errorCode;

    public DefaultNullPointerException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}