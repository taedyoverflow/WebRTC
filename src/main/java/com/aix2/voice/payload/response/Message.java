package com.aix2.voice.payload.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class Message {

    /**
     * Message 클래스: 단일 메시지를 담는 응답을 위한 간단한 클래스입니다. 오류 메시지나 단순 정보 전달에 사용될 수 있으며, message 필드에 메시지 내용을 담습니다.
     * */

    /**
     * payload 패키지는 주로 애플리케이션에서 데이터 전송에 사용되는 객체들을 포함합니다.
     * 이는 클라이언트와 서버 간에 교환되는 데이터의 본문(body)을 의미하는 용어로,
     * API 요청(request) 또는 응답(response)에 포함되는 구체적인 데이터를 담는 객체들을 저장하는 데 사용됩니다.
     * 예를 들어, 클라이언트가 API를 통해 서버에 보내는 데이터나 서버가 클라이언트에 응답으로 보내는 데이터 등이 이에 해당합니다.
     * payload 패키지 안에 위치하는 클래스들은 보통 DTO(Data Transfer Object)나 VO(Value Object)와 같은 역할을 하며,
     * 이들은 애플리케이션의 비즈니스 로직과 데이터 접근 계층 사이에서 데이터를 전달하는 데 사용됩니다.
     * */

    @Schema( type = "string", example = "메시지 문구를 출력합니다.", description="메시지 입니다.")
    private String message;

    public Message(){};

    @Builder
    public Message(String message) {
        this.message = message;
    }
}
