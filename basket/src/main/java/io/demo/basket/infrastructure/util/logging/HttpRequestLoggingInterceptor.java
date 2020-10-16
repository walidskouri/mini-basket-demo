package io.demo.basket.infrastructure.util.logging;

import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.argument.StructuredArgument;
import net.logstash.logback.marker.ObjectAppendingMarker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.stream.Stream;

import static io.demo.basket.infrastructure.util.logging.TracingConstant.*;
import static net.logstash.logback.argument.StructuredArguments.value;

@Component
@Slf4j
public class HttpRequestLoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final String REQUEST_START = "Request start";
    private static final String REQUEST_BODY = "request-body";
    private static final String RESPONSE_BODY = "response-body";
    private static final String METHOD = "method";
    private static final String REQUEST_END = "Request end";
    @Value("${info.version-contract}")
    private String version;




    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] body, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {



        logRequestStart(httpRequest, body);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            ClientHttpResponse response = clientHttpRequestExecution.execute(httpRequest, body);
            stopWatch.stop();
            logRequestEnd(httpRequest, stopWatch, response);
            return response;
        } catch (Exception e) {
            stopWatch.stop();
            throw e;
        }
    }

    private void logRequestEnd(HttpRequest httpRequest, StopWatch stopWatch, ClientHttpResponse response) {
        try {
            byte[] responseBody = FileCopyUtils.copyToByteArray(response.getBody());
            String responseBodyStr = new String(responseBody, StandardCharsets.UTF_8);
            StructuredArgument[] logsParam = {
                    value(TYPE, RESPONSE),
                    value(URI, httpRequest.getURI().toString()),
                    value(METHOD, httpRequest.getMethod().name()),
                    value(STATUS_CODE, String.valueOf(response.getRawStatusCode())),
                    value(STOP_WATCH, stopWatch.getTotalTimeMillis()),
                    value(MEMORY, String.valueOf(responseBody.length)),
                    value(VERSION_LABEL, version),
                    value(RESPONSE_BODY, responseBodyStr)};
            log.info(REQUEST_END, logsParam);


        } catch (Exception e) {
            log.error(e.getMessage(), MORE_INFO, log.getName());
        }
    }


    private void logRequestStart(HttpRequest request, byte[] body) {
        String bodyStr = new String(body, StandardCharsets.UTF_8);
        StructuredArgument[] logsParam = {
                value(TracingConstant.TYPE, REQUEST),
                value(URI, request.getURI()),
                value(METHOD, request.getMethod().name()),
                value(REQUEST_BODY, bodyStr),
                value(VERSION_LABEL, version)
        };

        log.info(REQUEST_START, logsParam);
    }

}
