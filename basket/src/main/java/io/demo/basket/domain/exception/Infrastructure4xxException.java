package io.demo.basket.domain.exception;


import org.springframework.http.HttpStatus;

public class Infrastructure4xxException extends InfrastructureException {

    public Infrastructure4xxException(String message, ErrorMessageType error, String rejectedObjectName, HttpStatus gatewayHttpStatus, String gatewayErrorCode) {
        super(message, error, rejectedObjectName, gatewayHttpStatus, gatewayErrorCode);
    }
}
