package uk.co.sethnavsoft.accountservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@Configuration
@EnableSwagger2
@EnableJpaRepositories(value = "uk.co.sethnavsoft.accountservice.dao")
public class AccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }

    @Bean
    /**
     * Enable swagger for the application.
     */
    public Docket swagger() {
        return new Docket(DocumentationType.SWAGGER_2).
                select().apis(RequestHandlerSelectors.basePackage("uk.co.sethnavsoft.accountservice"))
                .paths(PathSelectors.any()).build();
    }

}
