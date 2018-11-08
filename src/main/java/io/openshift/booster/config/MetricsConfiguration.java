package io.openshift.booster.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.spring.autoconfigure.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfiguration{
    @Bean
    MeterRegistryCustomizer meterRegistryCustomizer (MeterRegistry meterRegistry){
        return meterRegistry1 -> {
           meterRegistry.config()
           .commonTags("application","myapp");
        };
    }
}
