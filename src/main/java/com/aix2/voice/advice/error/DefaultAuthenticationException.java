package com.aix2.voice.advice.error;


import com.aix2.voice.advice.payload.ErrorCode;
import org.springframework.security.core.AuthenticationException;

import lombok.Getter;


@Getter
public class DefaultAuthenticationException extends AuthenticationException{

    /**
    * DefaultAuthenticationException: 인증 과정에서 발생할 수 있는 예외를 처리하기 위한 클래스로,
     * 스프링 시큐리티의 AuthenticationException을 상속받습니다.
     * 이 클래스는 주로 인증 실패나 유효하지 않은 인증 시도를 나타내기 위해 사용됩니다.
    */

    private ErrorCode errorCode;

    public DefaultAuthenticationException(String msg, Throwable t) {
        super(msg, t);
        this.errorCode = ErrorCode.INVALID_REPRESENTATION;
    }

    public DefaultAuthenticationException(String msg) {
        super(msg);
    }

    public DefaultAuthenticationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
