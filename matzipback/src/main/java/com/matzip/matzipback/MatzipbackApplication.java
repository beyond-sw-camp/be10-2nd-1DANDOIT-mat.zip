package com.matzip.matzipback;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableDiscoveryClient
@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default Server URL")})
@EnableFeignClients
public class MatzipbackApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatzipbackApplication.class, args);
    }

}
