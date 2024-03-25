package com.aix2.voice.service.auth;

import java.security.Key;
import java.util.Date;

import com.aix2.voice.config.security.token.UserPrincipal;
import com.aix2.voice.domain.mapping.TokenMapping;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomTokenProviderService {

    private final String secretKey;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;

    /**
     *  @RequiredArgsConstructor를 사용하면, secretKey, accessTokenValidityInMilliseconds, refreshTokenValidityInMilliseconds 필드들을 초기화하는 생성자를 Lombok이 생성합니다.
     *  그러나 @Value 어노테이션을 사용하여 이러한 필드들에 외부에서 정의된 값을 주입하려는 경우,
     *  Lombok이 생성한 생성자는 이 @Value 어노테이션을 인식하지 못하고, 따라서 스프링 의존성 주입이 정상적으로 작동하지 않습니다.
     *
     * 따라서, @Value 어노테이션을 사용하여 외부 설정 파일에서 값을 주입받고자 한다면, 명시적으로 생성자를 정의하고 @Autowired 어노테이션을 사용하는 것이 좋습니다.
     * 이 방법은 스프링이 런타임에 의존성을 주입할 때 필요한 모든 메타데이터를 제공하기 때문에, 예상대로 의존성 주입이 이루어질 수 있도록 합니다.
     *
     * 요약하자면, @Value 어노테이션과 함께 필드에 직접 값을 주입하는 경우에는 @RequiredArgsConstructor를 사용하지 않는 것이 좋습니다.
     * 대신, @Autowired를 사용한 명시적 생성자 주입 방법을 사용하거나, 필드 주입(@Autowired를 필드에 직접 사용하는 방법) 또는 @Setter 어노테이션과 같은 다른 방법을 고려해야 합니다.
     * */


    private CustomUserDetailsService customUserDetailsService;

    public CustomTokenProviderService(
            @Value("${security.jwt.secretKey}") String secretKey,

            @Value("${security.jwt.accessTokenValidityInMilliseconds}")
            long accessTokenValidityInMilliseconds,

            @Value("${security.jwt.refreshTokenValidityInMilliseconds}")
            long refreshTokenValidityInMilliseconds,

            CustomUserDetailsService customUserDetailsService) {
        this.secretKey = secretKey;
        this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds;
        this.customUserDetailsService = customUserDetailsService;
    }


    public TokenMapping refreshToken(Authentication authentication, String existingRefreshToken) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date now = new Date();

        // Refresh 토큰의 유효성 검증 (추가 로직 필요 시 여기에 구현)
        // 여기서는 단순히 새로운 액세스 토큰을 생성하는 것으로 가정
        Date accessTokenExpiration = new Date(now.getTime() + accessTokenValidityInMilliseconds);
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

        String newAccessToken = Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(now)
                .setExpiration(accessTokenExpiration)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        // Refresh 토큰 재사용 또는 새로 생성 로직에 따라 변경 가능
        return TokenMapping.builder()
                .userEmail(userPrincipal.getEmail())
                .accessToken(newAccessToken)
                .refreshToken(existingRefreshToken) // 기존 리프레시 토큰 재사용
                .build();
    }



    public TokenMapping createToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date now = new Date();

        Date accessTokenExpiresIn = new Date(now.getTime() + accessTokenValidityInMilliseconds);
        Date refreshTokenExpiresIn = new Date(now.getTime() + refreshTokenValidityInMilliseconds);

        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

        String accessToken = Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(now)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        String refreshToken = Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(refreshTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return TokenMapping.builder()
                .userEmail(userPrincipal.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(secretKey.getBytes())
            .build()
            .parseClaimsJws(token)
            .getBody();

        return Long.parseLong(claims.getSubject());
    }


    public UsernamePasswordAuthenticationToken getAuthenticationById(String token){
        Long userId = getUserIdFromToken(token);
        UserDetails userDetails = customUserDetailsService.loadUserById(userId);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        return authentication;
    }

    public UsernamePasswordAuthenticationToken getAuthenticationByEmail(String email){
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        return authentication;
    }

    public Long getExpiration(String token) {
        // accessToken 남은 유효시간을 계산
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        // 현재 시간
        long now = new Date().getTime();
        // 남은 시간 계산
        return expiration.getTime() - now;
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token);
            return true; // 토큰이 유효한 경우 true 반환
        } catch (io.jsonwebtoken.security.SecurityException ex) {
            log.error("잘못된 JWT 서명입니다.");
        } catch (MalformedJwtException ex) {
            log.error("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException ex) {
            log.error("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException ex) {
            log.error("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException ex) {
            log.error("JWT 토큰이 잘못되었습니다.");
        }
        return false; // 토큰이 유효하지 않은 경우 false 반환
    }

}
