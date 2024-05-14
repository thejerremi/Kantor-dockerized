package com.konteneryzacja.kantor;

import com.konteneryzacja.kantor.auth.AuthenticationService;
import com.konteneryzacja.kantor.auth.RegisterRequest;
import static com.konteneryzacja.kantor.user.Role.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableMongoAuditing
@EnableScheduling
public class KantorApplication {

	public static void main(String[] args) {
		SpringApplication.run(KantorApplication.class, args);
	}
}
