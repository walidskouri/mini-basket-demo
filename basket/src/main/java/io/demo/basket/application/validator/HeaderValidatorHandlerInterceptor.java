package io.demo.basket.application.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.demo.basket.application.rest.exception.RestErrorResponse;
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


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isNotEmpty(request.getHeader(USER_LOGIN_LABEL))) {
            return true;
        } else {

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


            return false;
        }
    }
}
