package com.zhanxin.tbiops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EntityScan(basePackages = {"com.zhanxin.tbiops.repository"})
public class TbiopsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TbiopsApplication.class, args);
    }

}
