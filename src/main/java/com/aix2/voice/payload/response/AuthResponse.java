package com.aix2.voice.payload.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
public class AuthResponse {

    /**
     * AuthResponse 클래스: 인증 관련 API 응답을 위한 클래스입니다. 여기에는 생성된 accessToken과 refreshToken, 그리고 토큰 타입(보통 "Bearer")을 포함합니다.
     * 이 클래스는 로그인이나 토큰 갱신과 같은 인증 과정에서 사용자에게 토큰 정보를 반환할 때 사용됩니다.
     * */

    @Schema( type = "string", example = "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2NTI3OTgxOTh9.6CoxHB_siOuz6PxsxHYQCgUT1_QbdyKTUwStQDutEd1-cIIARbQ0cyrnAmpIgi3IBoLRaqK7N1vXO42nYy4g5g" , description="access token 을 출력합니다.")
    private String accessToken;

    @Schema( type = "string", example = "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2NTI3OTgxOTh9.asdf8as4df865as4dfasdf65_asdfweioufsdoiuf_432jdsaFEWFSDV_sadf" , description="refresh token 을 출력합니다.")
    private String refreshToken;
    
    @Schema( type = "string", example ="Bearer", description="권한(Authorization) 값 해더의 명칭을 지정합니다.")
    private String tokenType = "Bearer";

    public AuthResponse(){};

    @Builder
    public AuthResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
