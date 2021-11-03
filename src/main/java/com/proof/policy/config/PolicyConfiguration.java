package com.proof.policy.config;

import com.proof.policy.model.RiskType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;
import java.util.stream.Collectors;


@ConstructorBinding
@ConfigurationProperties(prefix = "policy")
public class PolicyConfiguration {

    private final List<RiskTypeConfig> configs;

    public PolicyConfiguration(final List<RiskTypeConfig> configs) {
        this.configs = configs;
    }

    public List<RiskTypeConfig> getByType(RiskType riskType) {
        return configs.stream().filter(con -> con.getType() == riskType)
                .sorted((a, b) -> {
                    if (a.getUpperBound() == null) {
                        return 1;
                    }
                    if (b.getUpperBound() == null) {
                        return -1;
                    }
                    return a.getUpperBound().compareTo(b.getUpperBound());
                })
                .collect(Collectors.toList());
    }
}
