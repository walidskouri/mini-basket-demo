package io.demo.basket.domain.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public class InfrastructureException extends BasketException {

    private HttpStatus gatewayHttpStatus;
    private String gatewayErrorCode;

    public InfrastructureException(String message,
                                   ErrorMessageType error,
                                   String rejectedObjectName,
                                   HttpStatus gatewayHttpStatus,
                                   String gatewayErrorCode) {
        super(message, error, rejectedObjectName);
        this.gatewayHttpStatus = gatewayHttpStatus;
        this.gatewayErrorCode = gatewayErrorCode;
    }

    public Infrastructure4xxException toInfrastructure4xxException() {
        return new Infrastructure4xxException(this.getMessage(), this.getError(), this.getRejectedObjectName(), this.gatewayHttpStatus, this.gatewayErrorCode);
    }



    public static InfrastructureException.InfrastructureExceptionBuilder builder() {
        return new InfrastructureException.InfrastructureExceptionBuilder();
    }

    public InfrastructureException.InfrastructureExceptionBuilder toBuilder() {
        return (new InfrastructureException.InfrastructureExceptionBuilder())
                .gatewayHttpStatus(this.gatewayHttpStatus)
                .gatewayErrorCode(this.gatewayErrorCode)
                .error(this.getError())
                .rejectedObjectName(this.getRejectedObjectName())
                .message(this.getMessage());
    }


    public static class InfrastructureExceptionBuilder extends BasketExceptionBuilder<InfrastructureExceptionBuilder> {

        protected HttpStatus gatewayHttpStatus;
        protected String gatewayErrorCode;

        InfrastructureExceptionBuilder() {
        }

        public InfrastructureException.InfrastructureExceptionBuilder gatewayHttpStatus(final HttpStatus gatewayHttpStatus) {
            this.gatewayHttpStatus = gatewayHttpStatus;
            return this;
        }

        public InfrastructureException.InfrastructureExceptionBuilder gatewayErrorCode(final String gatewayErrorCode) {
            this.gatewayErrorCode = gatewayErrorCode;
            return this;
        }

        public InfrastructureException build() {
            return new InfrastructureException(this.message, this.error, this.rejectedObjectName, this.gatewayHttpStatus, this.gatewayErrorCode);
        }

        public String toString() {
            return "InfrastructureException.InfrastructureExceptionBuilder(error=" + this.error + ", rejectedObjectName=" + this.rejectedObjectName + ", serviceMessage=" + this.message + ", gatewayHttpStatus=" + this.gatewayHttpStatus + ", gatewayErrorCode=" + this.gatewayErrorCode + ")";
        }
    }

}
