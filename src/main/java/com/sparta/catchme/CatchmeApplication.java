package com.sparta.catchme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class CatchmeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatchmeApplication.class, args);
    }

}
