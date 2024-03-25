package com.aix2.voice.advice.error;

import com.aix2.voice.advice.payload.ErrorCode;
import lombok.Getter;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
public class InvalidParameterException extends DefaultException{

    /**
    * 이 클래스는 유효하지 않은 파라미터에 대한 예외를 처리하도록 설계되었으며,주로 스프링의 검증(validation) 프로세스에서 입력 값에 문제가 있을 때 사용됩니다.
     *
     * 핵심 구성요소
     * ErrorCode.INVALID_PARAMETER: InvalidParameterException이 발생하는 상황을 특정하는 데 사용되는 에러 코드입니다. 이 코드는 예외의 원인이 되는 구체적인 상황(여기서는 유효하지 않은 파라미터)을 설명합니다.
     * Errors 객체: 스프링의 Errors 인터페이스는 데이터 바인딩과 검증 과정에서 발생할 수 있는 모든 오류를 보관합니다. 이 객체를 통해 어떤 필드에서 어떤 유형의 검증 오류가 발생했는지에 대한 상세 정보를 알 수 있습니다.
     * getFieldErrors 메서드: InvalidParameterException 클래스에 정의된 getFieldErrors 메서드는 Errors 객체에 저장된 필드 오류들(FieldError 객체들)의 리스트를 반환합니다. 이 메서드를 사용함으로써, 예외 처리 로직에서 구체적인 필드 오류들에 대해 접근하고, 이를 기반으로 사용자에게 더 상세한 오류 메시지를 제공할 수 있습니다.
     * 사용 예시
     * 예를 들어, 스프링 MVC의 컨트롤러에서 클라이언트로부터 받은 요청 데이터를 바인딩한 후 검증 과정을 수행합니다. 이때 Errors 객체를 통해 입력 데이터에 문제가 있는지 확인할 수 있습니다. 만약 검증 과정에서 오류가 발견되면, InvalidParameterException을 던짐으로써 이를 처리할 수 있습니다.
     *
     * java
     * Copy code
     * public ResponseEntity<?> someControllerMethod(@Valid SomeRequestDto requestDto, Errors errors) {
     *     if (errors.hasErrors()) {
     *         throw new InvalidParameterException(errors);
     *     }
     *     // 비즈니스 로직 수행...
     * }
     * 위 예시에서 @Valid 어노테이션을 사용하여 requestDto 객체에 대한 검증을 요청하고, Errors 객체를 통해 검증 결과를 받습니다. 검증 과정에서 발견된 오류들은 InvalidParameterException을 통해 처리되며, 이 예외의 getFieldErrors 메서드를 사용하여 발생한 구체적인 필드 오류들에 대한 정보를 얻을 수 있습니다.
     *
     * 이러한 접근 방식은 API의 사용자에게 유효하지 않은 입력에 대해 구체적이고 명확한 피드백을 제공하는 데 유용하며, API의 사용성을 개선하는 데 도움이 됩니다.
    */

    private final Errors errors;

    public InvalidParameterException(Errors errors) {
        super(ErrorCode.INVALID_PARAMETER);
        this.errors = errors;
    }

    public List<FieldError> getFieldErrors() {
        return errors.getFieldErrors();
    }
    
}