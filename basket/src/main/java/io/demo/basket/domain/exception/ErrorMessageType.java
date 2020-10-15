package io.demo.basket.domain.exception;


import org.springframework.http.HttpStatus;

public enum ErrorMessageType {

    GENERAL_ERROR_MESSAGE(HttpStatus.INTERNAL_SERVER_ERROR, 4, "Error message"),
    SEARCH_PRODUCTS_WITH_PRODUCT_BAD_REQUEST(HttpStatus.BAD_REQUEST, 1001, "Bad request when searching from products"),
    SEARCH_PRODUCTS_WITH_PRODUCT_GENERIC(HttpStatus.INTERNAL_SERVER_ERROR, 1002, "Search products from product-ms failed"),
    GET_STOCK_INFO_BAD_REQUEST(HttpStatus.BAD_REQUEST, 1003, "Bad request when getting stock info"),
    GET_STOCK_INFO_BAD_REQUEST_GENERIC(HttpStatus.INTERNAL_SERVER_ERROR, 1004, "Get stock availability from stock-ms failed");

    /**
     * The http status.
     */
    private final HttpStatus httpStatus;

    /**
     * The code.
     */
    private final int code;

    /**
     * The message pattern.
     */
    private final String messagePattern;

    /**
     * Instantiates a new error message type.
     *
     * @param httpStatus     the http status
     * @param code           the code
     * @param messagePattern the message pattern
     */
    ErrorMessageType(HttpStatus httpStatus, int code, String messagePattern) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.messagePattern = messagePattern;
    }

    /**
     * Gets the message.
     *
     * @param msgArgs the msg args
     * @return the message
     */
    public String getMessage(Object... msgArgs) {
        return String.format(messagePattern, msgArgs);
    }


    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public int getCode() {
        return code;
    }

    public String getMessagePattern() {
        return messagePattern;
    }
}
