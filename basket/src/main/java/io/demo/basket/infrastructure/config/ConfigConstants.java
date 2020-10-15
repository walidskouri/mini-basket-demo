package io.demo.basket.infrastructure.config;

/**
 * 9fbef606107a605d69c0edbcd8029e5d
 */
public final class ConfigConstants {

    private ConfigConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String CORRELATION_ID = "correlationId";
    public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";

    public static final String REQUEST_ID = "requestId";
    public static final String CALL_ID = "callId";

}
