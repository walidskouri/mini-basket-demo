package io.demo.basket.application.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.demo.basket.application.rest.exception.RestErrorResponse;
import io.opentracing.Span;
import io.opentracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static io.demo.basket.application.rest.BasketResourcesConstants.USER_LOGIN_LABEL;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Component
@Slf4j
@RequiredArgsConstructor
public class HeaderValidatorHandlerInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;
    private final Tracer tracer;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isNotEmpty(request.getHeader(USER_LOGIN_LABEL))) {
            return true;
        } else {
            Span currentSpan = tracer.activeSpan();
            Span span = tracer.buildSpan("headerMissingLogin")
                    .asChildOf(currentSpan.context())
                    .start();
            span.setTag("error", true).log("BAD_REQUEST : " + String.format("The header must contain the '%s' element ", USER_LOGIN_LABEL));
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            RestErrorResponse restErrorResponse = RestErrorResponse.builder()
                    .message("Access not allowed")
                    .detailedMessage(String.format("The header must contain the '%s' element ", USER_LOGIN_LABEL))
                    .build();
            try {
                PrintWriter out = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                out.print(objectMapper.writeValueAsString(restErrorResponse));
                out.flush();
            } catch (IOException e) {
                log.error("Error while sending validation error message");
            }

            span.finish();

            return false;
        }
    }
}
