package com.aix2.voice.controller.file;

import com.aix2.voice.controller.AiServingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class AudioController {

    private final AiServingService aiServingService;

    @PostMapping("/send-audio")
    public Mono<ResponseEntity<String>> processAndSendAudio(@RequestParam("audioFile") MultipartFile audioFile) {
        return Mono.just(audioFile)
                .filter(file -> !file.isEmpty())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "File upload failed: Audio file is empty")))
                .flatMap(file -> {
                    String desktopPath = System.getProperty("user.home") + "\\Desktop\\record";
                    String fileName = desktopPath + "\\" + file.getOriginalFilename();
                    try {
                        File directory = new File(desktopPath);
                        if (!directory.exists()) directory.mkdirs();
                        File localFile = new File(fileName);
                        file.transferTo(localFile); // 파일을 로컬에 저장
                        // 로컬 파일을 FastAPI로 전송
                        return aiServingService.sendFileToFastAPI(localFile)
                                .map(response -> ResponseEntity.ok().body(response));
                    } catch (IOException e) {
                        String errorMessage = String.format("File upload and sending failed for file %s: %s", file.getOriginalFilename(), e.getMessage());
                        log.error(errorMessage, e);
                        return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage));
                    }
                })
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage())));
    }
}
