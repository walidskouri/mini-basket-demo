package io.demo.basket.domain.exception;

import lombok.ToString;

@ToString
public class DomainException extends BasketException {

    public DomainException(String message, ErrorMessageType error, String rejectedObjectName) {
        super(message, error, rejectedObjectName);
    }

    public static DomainException.DomainExceptionBuilder builder() {
        return new DomainException.DomainExceptionBuilder();
    }

    public static class DomainExceptionBuilder extends BasketExceptionBuilder<DomainExceptionBuilder> {
        public DomainException build() {
            return new DomainException(this.message, this.error, this.rejectedObjectName);
        }
    }
}
