package com.aix2.voice.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;

@Service
public class AiServingService {

    private final WebClient webClient;

    public AiServingService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://127.0.0.1:8000")
                .defaultHeader("Content-Type", MediaType.MULTIPART_FORM_DATA_VALUE)
                .build();
    }

    public Mono<String> sendFileToFastAPI(File voiceFile) {
        // FileSystemResource를 사용하여 파일을 리소스로 변환
        Resource resource = new FileSystemResource(voiceFile);
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        // 'file' 파트로 파일 추가
        bodyBuilder.part("file", resource);

        return webClient.post()
                .uri("/request")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class);
    }
}
