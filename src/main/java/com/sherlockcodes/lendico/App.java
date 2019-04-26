package com.sherlockcodes.lendico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication()
@EnableSwagger2
@EnableCaching
public class App {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public Docket ProductIntegrationApi() {

        Set<String> produceTypes = new HashSet<>();
        produceTypes.add("application/json");
        //produceTypes.add("application/xml");

        Set<String> consumeTypes = new HashSet<>();
        consumeTypes.addAll(produceTypes);
        consumeTypes.add("application/json");

        return new Docket(DocumentationType.SWAGGER_2)
                .consumes(consumeTypes)
                .produces(produceTypes)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sherlockcodes.lendico"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "Spring Boot demo  REST API",
                "Spring Boot demo REST API for lendico",
                "1.0",
                "Terms of com.sherlockcodes",
                new Contact("Ayush Kulshrestha", "https://sherlockcode.blogspot.com", "geniusayush@gmail.com"),
                "Apache License Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0");
        return apiInfo;
    }
}
