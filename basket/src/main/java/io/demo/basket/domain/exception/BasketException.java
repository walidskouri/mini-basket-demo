package io.demo.basket.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BasketException extends RuntimeException {

    private ErrorMessageType error;
    private String rejectedObjectName;

    public BasketException(String message, ErrorMessageType error, String rejectedObjectName) {
        super(message);
        this.error = error;
        this.rejectedObjectName = rejectedObjectName;
    }

    public static abstract class BasketExceptionBuilder<B extends BasketExceptionBuilder<B>> {
        protected ErrorMessageType error;
        protected String rejectedObjectName;
        protected String message;

        BasketExceptionBuilder() {
        }

        public B error(final ErrorMessageType error) {
            this.error = error;
            return (B) this;
        }

        public B rejectedObjectName(final String rejectedObjectName) {
            this.rejectedObjectName = rejectedObjectName;
            return (B) this;
        }

        public B message(final String message) {
            this.message = message;
            return (B) this;
        }

        public abstract BasketException build();

        public String toString() {
            return "DomainException.DomainExceptionBuilder(error=" + this.error + ", rejectedObjectName=" + this.rejectedObjectName + ", serviceMessage=" + this.message + ")";
        }
    }

}
