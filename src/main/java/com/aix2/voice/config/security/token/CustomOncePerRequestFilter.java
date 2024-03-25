package com.aix2.voice.config.security.token;

import com.aix2.voice.service.auth.CustomTokenProviderService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class CustomOncePerRequestFilter extends OncePerRequestFilter {

    /**
     * 이 클래스, CustomOncePerRequestFilter,는 스프링 시큐리티의 OncePerRequestFilter를 확장하여 정의된 커스텀 필터입니다.
     * 이 필터의 주 목적은 HTTP 요청마다 실행되어 JWT 토큰의 유효성을 검증하고, 유효한 토큰이 발견될 경우, 해당 사용자의 인증 정보를 SecurityContext에 설정하는 것입니다.
     * 이를 통해 스프링 시큐리티가 요청을 처리할 때 사용자가 인증되었다는 것을 알 수 있게 됩니다.
     *
     * 주요 기능 및 구현
     * JWT 추출: getJwtFromRequest(HttpServletRequest request) 메서드는 HTTP 요청의 Authorization 헤더에서 JWT를 추출합니다.
     * 이때, 토큰은 "Bearer"라는 접두사를 가지고 있음을 가정합니다.
     * 토큰 유효성 검증 및 인증 정보 설정: doFilterInternal 메서드 내에서, 추출된 JWT의 유효성을 검사하고,
     * 유효한 경우 customTokenProviderService를 사용하여 해당 토큰에 대한 인증 객체(UsernamePasswordAuthenticationToken)를 생성합니다.
     * 그 후, 이 인증 객체를 SecurityContextHolder의 컨텍스트에 설정하여, 스프링 시큐리티가 현재 사용자가 인증된 것으로 인식하게 합니다.*/

    private final CustomTokenProviderService customTokenProviderService;

    // 생성자를 통한 CustomTokenProviderService 주입
    public CustomOncePerRequestFilter(CustomTokenProviderService customTokenProviderService) {
        this.customTokenProviderService = customTokenProviderService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = getJwtFromRequest(request);

        if (StringUtils.hasText(jwt) && customTokenProviderService.validateToken(jwt)) {
            UsernamePasswordAuthenticationToken authentication = customTokenProviderService.getAuthenticationById(jwt);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            log.info("bearerToken = {}", bearerToken.substring(7, bearerToken.length()));
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

}

