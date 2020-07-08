package io.demo.basket.infrastructure.gateway;


import io.demo.basket.domain.exception.ErrorMessageType;
import io.demo.basket.domain.exception.InfrastructureException;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.apache.commons.lang3.StringUtils.truncate;

public abstract class GatewayErrorHandler extends DefaultResponseErrorHandler {

    private static final int MAX_LENGTH_OF_MESSAGE_EXCEPTION = 1000;

    @Override
    public void handleError(ClientHttpResponse response) {
        HttpStatus statusCode;
        String message;

        try {
            statusCode = HttpStatus.resolve(response.getRawStatusCode());
            message = IOUtils.toString(response.getBody(), StandardCharsets.UTF_8.name());
        } catch (IOException ioe) {
            statusCode = HttpStatus.SERVICE_UNAVAILABLE;
            message = "Service unavailable for unknown reason";
        }


        handleGatewayError(statusCode, message);
    }

    public abstract void handleGatewayError(HttpStatus httpStatus, String message);

    public static InfrastructureException toInfrastructureException(Exception e) {
        if (e instanceof InfrastructureException) {
            return (InfrastructureException) e;
        }

        return InfrastructureException.builder()
                .gatewayHttpStatus(HttpStatus.SERVICE_UNAVAILABLE)
                .error(ErrorMessageType.GENERAL_ERROR_MESSAGE)
                .message(truncateErrorMessage(e.getMessage()))
                .build();
    }

    public static void throwInfrastructureException(Exception e, ErrorMessageType errorMessageType, String rejectedObjectName) {
        if (e instanceof InfrastructureException) {
            throw (InfrastructureException) e;
        }

        throw InfrastructureException.builder()
                .gatewayHttpStatus(HttpStatus.SERVICE_UNAVAILABLE)
                .error(errorMessageType)
                .rejectedObjectName(rejectedObjectName)
                .message(truncateErrorMessage(e.getLocalizedMessage()))
                .build();
    }

    protected static String truncateErrorMessage(String message) {
        return message != null ? truncate(message, MAX_LENGTH_OF_MESSAGE_EXCEPTION) : null;
    }

}
