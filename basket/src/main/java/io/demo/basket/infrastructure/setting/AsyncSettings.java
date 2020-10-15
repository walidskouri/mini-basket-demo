package io.demo.basket.infrastructure.setting;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 9fbef606107a605d69c0edbcd8029e5d
 */
@Configuration
@ConfigurationProperties(prefix = "async")
@Data
public class AsyncSettings {

    private ThreadPoolExecutorSettings main;
    private ThreadPoolExecutorSettings stock;

    @Data
    public static class ThreadPoolExecutorSettings {
        private int corePoolSize;
        private int maxPoolSize;
        private int queueCapacity;
        private String threadNamePrefix;
    }

}
