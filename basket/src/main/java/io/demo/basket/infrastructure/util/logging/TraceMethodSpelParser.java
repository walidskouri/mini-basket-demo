package io.demo.basket.infrastructure.util.logging;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class TraceMethodSpelParser {

    public Map<String, Object> parseParameters(String[] parameterNames, Object[] args, String annotationParameter) {
        try {
            if (StringUtils.isEmpty(annotationParameter)) {
                return new HashMap<>();
            }
            StandardEvaluationContext context = new StandardEvaluationContext();
            ExpressionParser parser = new SpelExpressionParser();
            for (int i = 0; i < parameterNames.length; i++) {
                context.setVariable(parameterNames[i], args[i]);
            }
            return (Map<String, Object>) parser.parseExpression(annotationParameter).getValue(context, Object.class);
        } catch (Exception e) {
            log.error(String.format("Error during evaluation of @TraceMethodCall params : %s", annotationParameter));
            return new HashMap<>();
        }
    }
}
