package com.adp.challenge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket invoiceApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.adp"))
                .build()
                .apiInfo(metaInfo());
    }

    @SuppressWarnings("rawtypes")
    private ApiInfo metaInfo() {

        return new ApiInfo(
                "Bill API REST",
                "API REST of bill.",
                "1.0.0",
                "Termos do serviço",
                new Contact("Vitor Furini", "https://github.com/vitorfurini", "vitorfurini@hotmail.com"),
                "Apache License Version 2.0",
                "https://www.apache.org/licesen.html", new ArrayList<VendorExtension>()
        );
    }
}
