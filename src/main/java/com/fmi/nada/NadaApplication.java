package com.fmi.nada;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude= DataSourceAutoConfiguration.class)
public class NadaApplication {

    public static void main(String[] args) {
        SpringApplication.run(NadaApplication.class, args);
    }

}
