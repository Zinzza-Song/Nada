package com.fmi.nada;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.fmi.nada"})
@EntityScan(basePackages = {"com.fmi.nada"})
public class NadaApplication {

    public static void main(String[] args) {
        SpringApplication.run(NadaApplication.class, args);
    }

    @Bean
    public org.springframework.web.filter.HiddenHttpMethodFilter hiddenHttpMethodFilter(){
        return new HiddenHttpMethodFilter();
    }
}
