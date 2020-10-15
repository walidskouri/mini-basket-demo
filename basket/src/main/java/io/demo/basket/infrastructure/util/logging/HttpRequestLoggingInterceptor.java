package io.demo.basket.infrastructure.util.logging;

import io.opentracing.Span;
import io.opentracing.Tracer;
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

    private final Tracer tracer;

    public HttpRequestLoggingInterceptor(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] body, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {

        Span serverSpan = tracer.activeSpan();
        if (null == serverSpan) {
            serverSpan = tracer.buildSpan("Async").start();
        }
        Span span = tracer.buildSpan(String.format("[GATEWAY] Intercept %s %s",
                Objects.requireNonNull(httpRequest.getMethod()).toString(),
                httpRequest.getURI().toString()))
                .asChildOf(serverSpan.context())
                .start();

        logRequestStart(httpRequest, body, span);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            ClientHttpResponse response = clientHttpRequestExecution.execute(httpRequest, body);
            stopWatch.stop();
            logRequestEnd(httpRequest, stopWatch, response, span);
            return response;
        } catch (Exception e) {
            stopWatch.stop();
            throw e;
        } finally {
            span.finish();
        }
    }

    private void logRequestEnd(HttpRequest httpRequest, StopWatch stopWatch, ClientHttpResponse response, Span span) {
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
            Stream.of(logsParam).map(ObjectAppendingMarker.class::cast)
                    .forEach(arg -> span.setTag(arg.getFieldName(), arg.getFieldValue().toString()));

        } catch (Exception e) {
            log.error(e.getMessage(), MORE_INFO, log.getName());
        }
    }


    private void logRequestStart(HttpRequest request, byte[] body, Span span) {
        String bodyStr = new String(body, StandardCharsets.UTF_8);
        StructuredArgument[] logsParam = {
                value(TracingConstant.TYPE, REQUEST),
                value(URI, request.getURI()),
                value(METHOD, request.getMethod().name()),
                value(REQUEST_BODY, bodyStr),
                value(VERSION_LABEL, version)
        };
        Stream.of(logsParam).map(ObjectAppendingMarker.class::cast)
                .forEach(arg -> span.setTag(arg.getFieldName(), arg.getFieldValue().toString()));
        log.info(REQUEST_START, logsParam);
    }

}
