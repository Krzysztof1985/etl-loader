package com.superdevs.etlloader.config;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicates;
import com.superdevs.etlloader.filters.MainFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Set;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api(TypeResolver typeResolver) {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .paths(PathSelectors.any())
                .build()
                .additionalModels(
                        typeResolver.resolve(MainFilter.class)
                )
                .apiInfo(apiInfo())
                .consumes(Set.of(APPLICATION_JSON_VALUE))
                .produces(Set.of(APPLICATION_JSON_VALUE))
                .useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("ETL Loader Portal")
                .version("1.0")
                .build();
    }
}
