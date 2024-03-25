package com.aix2.voice.repository.auth;

import com.aix2.voice.domain.user.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByUserEmail(String userEmail);
    Optional<Token> findByRefreshToken(String refreshToken);

/**
 * 토큰 저장의 목적과 장점:
 * 보안 강화: 데이터베이스에 리프레시 토큰을 저장함으로써, 애플리케이션은 발급된 리프레시 토큰의 사용을 보다 엄격하게 제어할 수 있습니다. 예를 들어, 특정 리프레시 토큰이 도난되었거나 오용되고 있다고 판단될 경우, 해당 토큰을 데이터베이스에서 직접 제거하거나 무효화함으로써 보안 위협을 줄일 수 있습니다.
 * 액세스 토큰 갱신: 사용자의 액세스 토큰이 만료되었을 때, 저장된 리프레시 토큰을 사용하여 새로운 액세스 토큰을 자동으로 재발급받을 수 있습니다. 이 과정은 사용자 경험을 향상시키며, 사용자가 자주 로그인을 반복하지 않아도 되게 합니다.
 * 세션 관리: 사용자가 로그아웃하거나 장기간 활동하지 않는 경우, 데이터베이스에 저장된 리프레시 토큰을 사용하여 해당 사용자의 세션을 종료하거나 제한할 수 있습니다.
 */

}
