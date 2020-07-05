package io.demo.basket.application.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.demo.basket.application.security.CurrentConnectedUserArgumentResolver;
import io.demo.basket.application.validator.HeaderValidatorHandlerInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

import static io.demo.basket.application.rest.BasketResourcesConstants.API_VERSION;
import static io.demo.basket.application.rest.BasketResourcesConstants.BASE_BASKET_URL;

@Configuration
@Profile(value = "!test")
@RequiredArgsConstructor
public class ApplicationConfig extends WebMvcConfigurationSupport {

    private final CurrentConnectedUserArgumentResolver currentCustomerRefArgumentResolver;

    private final ObjectMapper objectMapper;

    private final HeaderValidatorHandlerInterceptor headerValidatorHandlerInterceptor;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(currentCustomerRefArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(headerValidatorHandlerInterceptor)
                .addPathPatterns(API_VERSION + BASE_BASKET_URL + "/**");
    }

    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter(this.objectMapper));
        converters.add(new StringHttpMessageConverter());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // these are resources handler for swagger
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}
