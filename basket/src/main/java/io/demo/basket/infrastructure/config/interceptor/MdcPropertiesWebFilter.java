package io.demo.basket.infrastructure.config.interceptor;

import io.demo.basket.infrastructure.config.ConfigConstants;
import io.jaegertracing.tracerresolver.internal.JaegerTracerResolver;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;


/**
 * This Filter Set the correlation id of the request
 * <p>
 * At the end of the request, it remove all MDC that was set during the run of the request
 */
@Component
public class MdcPropertiesWebFilter implements javax.servlet.Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        String correlationId = ((HttpServletRequest) request).getHeader(ConfigConstants.CORRELATION_ID_HEADER);
        String requestId = UUID.randomUUID().toString();

        if (Objects.isNull(correlationId)) {
            correlationId = UUID.randomUUID().toString();
        }

        JaegerTracerResolver.resolveTracer().activeSpan().setTag("correlationId", correlationId);

        try {
            MDC.put(ConfigConstants.REQUEST_ID, requestId);
            MDC.put(ConfigConstants.CALL_ID, ((HttpServletRequest) request).getMethod() + " : " + ((HttpServletRequest) request).getServletPath());
            MDC.put(ConfigConstants.CORRELATION_ID, correlationId);
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

    @Override
    public void destroy() {
    }
}