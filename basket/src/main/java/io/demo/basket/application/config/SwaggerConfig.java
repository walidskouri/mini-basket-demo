package io.demo.basket.application.config;

import io.demo.basket.application.rest.BasketResourcesConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;


@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
@Profile(value = "!test & !integration & !prep & !prod")
public class SwaggerConfig {

    private static final String HEADER_PARAMETER_TYPE = "header";

    private final String contractVersion;

    public SwaggerConfig(@Value("${info.version-contract-api}") String contractVersion) {
        this.contractVersion = contractVersion;
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("io.demo.basket"))
                .build()
                .globalOperationParameters(commonHeaders())
                .apiInfo(apiInfo());
    }

    private List<Parameter> commonHeaders() {
        ModelRef stringModelRef = new ModelRef("string");
        return Collections.singletonList(
                new ParameterBuilder()
                        .name(BasketResourcesConstants.USER_LOGIN_LABEL)
                        .description("The user login")
                        .modelRef(stringModelRef)
                        .parameterType(HEADER_PARAMETER_TYPE)
                        .required(false)
                        .build()
        );
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Sample basket API")
                .description("## Simple Demo Application for Minimal Basket Features.")
                .version(contractVersion)
                .build();
    }

}
