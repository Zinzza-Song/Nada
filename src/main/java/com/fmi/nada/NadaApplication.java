package com.fmi.nada;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.fmi.nada"})
@EntityScan(basePackages = {"com.fmi.nada"})
public class NadaApplication {

    public static void main(String[] args) {
        SpringApplication.run(NadaApplication.class, args);
    }

}
