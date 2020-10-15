package io.demo.basket.infrastructure.config.interceptor;

import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.Objects;

import static io.demo.basket.infrastructure.config.ConfigConstants.CORRELATION_ID;
import static io.demo.basket.infrastructure.config.ConfigConstants.CORRELATION_ID_HEADER;


public class HeaderClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String correlationID = MDC.get(CORRELATION_ID);
        if (!Objects.isNull(correlationID)) {
            headers.add(CORRELATION_ID_HEADER, correlationID);
        }
        return execution.execute(request, body);
    }

}
