package com.proof.policy.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties(value = {
        PolicyConfiguration.class
})
public class BindingPropertiesConfig {

}
