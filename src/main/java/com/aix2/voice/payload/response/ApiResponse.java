package com.aix2.voice.payload.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class ApiResponse {

    /**
     * ApiResponse 클래스: 일반적인 API 응답 포맷을 정의합니다. check 필드는 요청 처리 성공 여부를 나타내며, information 필드는 요청에 대한 응답 데이터를 담습니다.
     * 이 클래스는 API 호출의 성공 여부와 함께 추가 정보를 클라이언트에게 제공하고자 할 때 사용됩니다.*/

    @Schema( type = "boolean", example = "true", description="올바르게 로직을 처리했으면 True, 아니면 False를 반환합니다.")
    private boolean check;

    @Schema( type = "object", example = "information", description="restful의 정보를 감싸 표현합니다. object형식으로 표현합니다.")
    private Object information;

    public ApiResponse(){};

    @Builder
    public ApiResponse(boolean check, Object information) {
        this.check = check;
        this.information = information;
    }
}