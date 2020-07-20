package com.devdojo.springboot.docs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket apiDoc() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                    .apis(RequestHandlerSelectors.basePackage("com.devdojo.springboot.endpoint"))
                    .paths(PathSelectors.regex("/v1.*"))
                    .build()
                .globalOperationParameters(Collections.singletonList(new ParameterBuilder()
                        .name("Authorization")
                        .description("Bearer token")
                        .modelRef(new ModelRef("string"))
                        .parameterType("header")
                        .required(true)
                        .build()
                ))
                .apiInfo(metaData());
    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("Spring boot essencials by D3z40")
                .description("Spring boot tutorial")
                .version("1.0")
                .contact(new Contact("André de Andrade Pereira",
                        "https://github.com/d3z40",
                        "andre.pereira22@fatec.sp.gov.br"))
                .license("Apache License Version 2.4.29")
                .licenseUrl("https://httpd.apache.org/docs/2.4/")
                .build();
    }
}
