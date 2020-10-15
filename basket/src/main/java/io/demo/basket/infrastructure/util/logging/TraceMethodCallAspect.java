package io.demo.basket.infrastructure.util.logging;

import io.demo.basket.domain.exception.BasketException;
import io.demo.basket.domain.exception.DomainException;
import io.demo.basket.domain.exception.Infrastructure4xxException;
import io.demo.basket.domain.exception.InfrastructureException;
import io.opentracing.Span;
import io.opentracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.argument.StructuredArgument;
import net.logstash.logback.marker.ObjectAppendingMarker;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.demo.basket.domain.exception.ErrorMessageType.GENERAL_ERROR_MESSAGE;
import static io.demo.basket.infrastructure.util.Utility.buildStacktraceForLog;
import static io.demo.basket.infrastructure.util.logging.TracingConstant.*;
import static net.logstash.logback.argument.StructuredArguments.value;
import static org.springframework.util.CollectionUtils.isEmpty;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class TraceMethodCallAspect {

    private final TraceMethodSpelParser traceMethodSpelParser;
    private final Tracer tracer;

    @Value("${info.version-contract}")
    private String version;

    @Around("traceMethodCallAnnotation()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        PointCutDescription pointCutDescription = getDescription(joinPoint);
        Span serverSpan = tracer.activeSpan();
        Span span = tracer.buildSpan(String.format("[%S] Method Call [%s], ", pointCutDescription.getServiceType(), pointCutDescription.getServiceName()))
                .asChildOf(serverSpan.context())
                .start();
        span.setTag("type", pointCutDescription.getServiceType());
        Map<String, Object> logParameters = traceMethodSpelParser.parseParameters(
                pointCutDescription.getMethodParametersName(),
                pointCutDescription.getMethodArguments(),
                pointCutDescription.getAnnotationParams()
        );
        logServiceStart(pointCutDescription, logParameters);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object returnValue;
        try {
            returnValue = joinPoint.proceed();
        } catch (Throwable throwable) {
            span.setTag("error", true);
            logException(pointCutDescription.getServiceName(), pointCutDescription.serviceType, throwable, span);
            throw throwable;
        } finally {
            stopWatch.stop();
            logServiceEnd(pointCutDescription.getServiceName(), pointCutDescription.serviceType, stopWatch.getTotalTimeMillis());
            if (!isEmpty(logParameters)) {
                logParameters.forEach((k, v) -> span.setTag(k, v.toString()));
                span.log(logParameters.toString());
            }
            span.finish();
        }
        return returnValue;
    }

    private void logServiceStart(PointCutDescription pointCutDescription, Map<String, Object> logParameters) {
        log.info(
                "Call " + pointCutDescription.getServiceType() + " start",
                value(MORE_INFO, pointCutDescription.getServiceName()),
                value(PARAMS, logParameters),
                value(VERSION_LABEL, version));

    }

    private void logServiceEnd(String serviceName, String serviceType, long totalTimeMillis) {
        log.info(
                "Call " + serviceType + " end",
                value(MORE_INFO, serviceName),
                value(STOP_WATCH, totalTimeMillis),
                value(VERSION_LABEL, version));
    }

    public void logException(String serviceName, String serviceType, Throwable ex, Span span) {


        StringBuilder message = new StringBuilder("Error in " + serviceType);
        List<StructuredArgument> logsParam = new ArrayList<>();
        logsParam.add(value(VERSION_LABEL, version));
        logsParam.add(value(ERROR, true));

        logsParam.add(value(MORE_INFO, serviceName));
        if (ex.getMessage() != null) {
            logsParam.add(value(ERROR_MESSAGE, ex.getMessage()));
        }
        if (ex.getCause() != null) {
            logsParam.add(value(CAUSE_ERROR, ex.getCause().getMessage()));
        }
        if (ex instanceof BasketException) {
            BasketException basketException = (BasketException) ex;
            if (basketException.getError() != null) {
                message.append(" : ");
                message.append(basketException.getError().getMessage());
                logsParam.add(value(ERROR_CODE, String.valueOf(basketException.getError().getCode())));
            }
            Map<String, String> subErrorMap = new HashMap();
            subErrorMap.put("object", basketException.getRejectedObjectName());
            subErrorMap.put("message", basketException.getMessage());
            logsParam.add(value(SUB_ERROR, subErrorMap));

            if (basketException instanceof Infrastructure4xxException) {
                logsParam.add(value(EXCEPTION_TYPE, "infrastructure4xx"));
            } else if (basketException instanceof InfrastructureException) {
                logsParam.add(value(EXCEPTION_TYPE, "infrastructure"));
            } else if (basketException instanceof DomainException) {
                logsParam.add(value(EXCEPTION_TYPE, "domain"));
            }
        } else {
            logsParam.add(value(ERROR_CODE, GENERAL_ERROR_MESSAGE.getCode()));
            logsParam.add(value(STACKTRACE, buildStacktraceForLog(ex)));
        }

        logsParam.stream().map(ObjectAppendingMarker.class::cast)
                .forEach(arg -> span.setTag(arg.getFieldName(), arg.getFieldValue().toString()));

        log.error(message.toString(), logsParam.toArray());

    }

    private PointCutDescription getDescription(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        PointCutDescription pointCutDescription = new PointCutDescription();

        TraceMethodCall methodAnnotation = method.getAnnotation(TraceMethodCall.class);
        if (methodAnnotation != null) {
            pointCutDescription.setAnnotationParams(methodAnnotation.params());
            pointCutDescription.setServiceType(methodAnnotation.type().name());
        }
        pointCutDescription.setServiceName(signature.toShortString());
        pointCutDescription.setMethodParametersName(signature.getParameterNames());
        pointCutDescription.setMethodArguments(joinPoint.getArgs());

        return pointCutDescription;
    }

    @Pointcut("@annotation(io.demo.basket.infrastructure.util.logging.TraceMethodCall)")
    public void traceMethodCallAnnotation() {
        // Pointcut method
    }

    class PointCutDescription {
        private String serviceName;
        private String annotationParams;
        private String[] methodParametersName;
        private Object[] methodArguments;
        private String serviceType;

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public String getAnnotationParams() {
            return annotationParams;
        }

        public void setAnnotationParams(String annotationParams) {
            this.annotationParams = annotationParams;
        }

        public String[] getMethodParametersName() {
            return methodParametersName;
        }

        public void setMethodParametersName(String[] methodParametersName) {
            this.methodParametersName = methodParametersName;
        }

        public Object[] getMethodArguments() {
            return methodArguments;
        }

        public void setMethodArguments(Object[] methodArguments) {
            this.methodArguments = methodArguments;
        }

        public String getServiceType() {
            return serviceType;
        }

        public void setServiceType(String serviceType) {
            this.serviceType = serviceType;
        }
    }
}
