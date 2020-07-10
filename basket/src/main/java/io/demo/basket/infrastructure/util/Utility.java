package io.demo.basket.infrastructure.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utility {

    private static final int MAX_NUMBER_OF_STACK_TRACE_LOG_ELEMENT = 5;

    public static HttpServletRequest getCurrentHttpServletRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(requestAttributes -> ServletRequestAttributes.class.isAssignableFrom(requestAttributes.getClass()))
                .map(requestAttributes -> (((ServletRequestAttributes) requestAttributes).getRequest()))
                .orElse(null);
    }

    public static String getHeader(String headerName) {
        String headerValue = null;
        HttpServletRequest httpServletRequest = getCurrentHttpServletRequest();
        if (httpServletRequest != null) {
            headerValue = httpServletRequest.getHeader(headerName);
        }
        return headerValue;
    }

    public static List<String> buildStacktraceForLog(Throwable ex) {
        List<String> listMessages = new ArrayList<>();
        listMessages.add(ex.getMessage());
        if (ex.getStackTrace() != null) {
            for (StackTraceElement stackTraceElement : ex.getStackTrace()) {
                listMessages.add(stackTraceElement.toString());
                if (listMessages.size() > MAX_NUMBER_OF_STACK_TRACE_LOG_ELEMENT) {
                    break;
                }
            }
        }
        return listMessages;
    }
}
