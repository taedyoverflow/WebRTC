package com.aix2.voice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@EnableWebSocket
@EnableJpaAuditing
@SpringBootApplication
public class VoiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(VoiceApplication.class, args);
	}

}
