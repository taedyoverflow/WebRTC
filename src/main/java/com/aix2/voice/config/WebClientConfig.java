package com.aix2.voice.config;

import io.netty.channel.ChannelOption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();

    HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);

    @Bean
    public WebClient webClient() {
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
        return WebClient.builder()
                .uriBuilderFactory(factory)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean
    public ConnectionProvider connectionProvider() {
        return ConnectionProvider.builder("http-pool")
                .maxConnections(100)
                .pendingAcquireTimeout(Duration.ofMillis(0))
                .pendingAcquireMaxCount(-1)
                .maxIdleTime(Duration.ofMillis(1000L))
                .build();
    }

    /**
     * DefaultUriBuilderFactory 설정: 이는 URI를 생성할 때 사용되며, EncodingMode.VALUES_ONLY를 설정함으로써 URI의 값 부분만 인코딩되도록 합니다. 이는 일반적으로 URL을 생성할 때 특정 문자(예: 슬래시 /나 콜론 :)가 예상치 않게 인코딩되는 것을 방지하려는 목적으로 사용됩니다.
     *
     * HttpClient 설정: Reactor Netty의 HttpClient를 사용하여 HTTP 클라이언트를 생성하고, 연결 시간 초과를 10초로 설정합니다. 이는 서버에 연결을 시도할 때 최대 10초 동안 기다린 후 타임아웃이 발생하게 합니다.
     *
     * WebClient 빈 생성: 이 설정은 WebClient의 인스턴스를 생성합니다. 여기에는 위에서 설정한 DefaultUriBuilderFactory와 HTTP 클라이언트 커넥터, 그리고 메모리 내 최대 코덱 크기를 2MB로 설정하는 코드가 포함됩니다. 이 WebClient는 비동기 방식으로 HTTP 요청을 보내고 응답을 처리하는 데 사용됩니다.
     *
     * ConnectionProvider 빈 생성: 이는 Reactor Netty에서 HTTP 클라이언트의 연결을 관리하기 위한 커넥션 프로바이더를 생성합니다. 여기서는 최대 100개의 동시 연결, 연결 대기 시간 제한 없음, 무제한 대기 연결 수, 최대 유휴 시간 1초를 설정합니다. 이러한 설정은 서버와의 통신에 있어 연결 풀을 효율적으로 관리하는 데 도움이 됩니다.
     *
     * 이 설정은 FastAPI 서버와의 통신을 위해 음성 파일을 비동기 방식으로 전송할 때 유용합니다. WebClient를 사용하면 FastAPI 서버로의 요청을 효율적으로 처리할 수 있으며, 설정된 타임아웃과 연결 관리 방법은 서비스의 안정성과 성능에 긍정적인 영향을 미칠 수 있습니다. FastAPI 서버가 음성 파일 처리에 최적화되어 있고 적절한 엔드포인트를 제공한다면, 이러한 설정은 백엔드에서 AI 서버로의 음성 파일 전송을 위한 우수한 구성이 될 수 있습니다.
     * */
}