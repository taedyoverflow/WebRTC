package com.aix2.voice.advice.assertThat;

import com.aix2.voice.advice.error.DefaultAuthenticationException;
import com.aix2.voice.advice.error.DefaultException;
import com.aix2.voice.advice.error.DefaultNullPointerException;
import com.aix2.voice.advice.error.InvalidParameterException;
import com.aix2.voice.advice.payload.ErrorCode;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.Optional;

public class DefaultAssert extends Assert {


    /**
     * DefaultAssert 클래스는 스프링 프레임워크의 Assert 클래스를 확장하여,
     * 특정 조건이 참인지 검증하고, 그렇지 않을 경우 사용자 정의 예외를 발생시키는 정적 메서드들을 제공합니다.
     * 이 클래스는 주로 서비스 또는 컨트롤러 레이어에서 입력 값의 유효성 검증, 객체의 상태 확인 등을 위해 사용됩니다.
     * 예외 상황에서는 ErrorCode에 정의된 에러 코드를 기반으로 적절한 사용자 정의 예외를 발생시킵니다.
     */

    /**
     * 사용 예시
     * 예를 들어, 어떤 메서드에서 파라미터 값의 유효성을 검증할 필요가 있을 때, DefaultAssert.isTrue 메서드를 호출하여 해당 조건이 참인지 확인할 수 있습니다.
     * 조건이 거짓이라면 메서드는 DefaultException을 발생시켜, 호출자에게 에러 상황을 알립니다.
     *
     * public void someMethod(String param) {
     *     DefaultAssert.isTrue(param != null, "파라미터가 null입니다.");
     *     // 파라미터 검증 후의 로직...
     * }
     */

    public static void isTrue(boolean value){
        if(!value){
            throw new DefaultException(ErrorCode.INVALID_CHECK);
        }
    }

    public static void isTrue(boolean value, String message){
        if(!value){
            throw new DefaultException(ErrorCode.INVALID_CHECK, message);
        }
    }

    public static void isValidParameter(Errors errors){
        if(errors.hasErrors()){
            throw new InvalidParameterException(errors);
        }
    }

    public static void isObjectNull(Object object){
        if(object == null){
            throw new DefaultNullPointerException(ErrorCode.INVALID_CHECK);
        }
    }

    public static void isListNull(List<Object> values){
        if(values.isEmpty()){
            throw new DefaultException(ErrorCode.INVALID_FILE_PATH);
        }
    }

    public static void isListNull(Object[] values){
        if(values == null){
            throw new DefaultException(ErrorCode.INVALID_FILE_PATH);
        }
    }

    public static void isOptionalPresent(Optional<?> value){
        if(!value.isPresent()){
            throw new DefaultException(ErrorCode.INVALID_PARAMETER);
        }
    }

    public static void isAuthentication(String message){
        throw new DefaultAuthenticationException(message);
    }

    public static void isAuthentication(boolean value){
        if(!value){
            throw new DefaultAuthenticationException(ErrorCode.INVALID_AUTHENTICATION);
        }
    }
}


