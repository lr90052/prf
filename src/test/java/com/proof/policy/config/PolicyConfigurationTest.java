package com.proof.policy.config;

import com.proof.policy.model.RiskType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class PolicyConfigurationTest {

    private PolicyConfiguration policyConfiguration;

    @Test
    void shouldGetOrderdConfigToType() {
        // given
        List<RiskTypeConfig> coefficientConfigs = List.of(
                RiskTypeConfig.builder()
                        .type(RiskType.THEFT)
                        .upperBound(null)
                        .build(),
                RiskTypeConfig.builder()
                        .type(RiskType.THEFT)
                        .upperBound(BigDecimal.ZERO)
                        .build(),
                RiskTypeConfig.builder()
                        .type(RiskType.FIRE)
                        .upperBound(null)
                        .build(),
                RiskTypeConfig.builder()
                        .type(RiskType.THEFT)
                        .upperBound(new BigDecimal("14"))
                        .build(),
                RiskTypeConfig.builder()
                        .type(RiskType.FIRE)
                        .upperBound(new BigDecimal("80"))
                        .build(),
                RiskTypeConfig.builder()
                        .type(RiskType.THEFT)
                        .upperBound(new BigDecimal("8.5"))
                        .build()
                                                         );
        policyConfiguration = new PolicyConfiguration(coefficientConfigs);

        // when
        List<RiskTypeConfig> theftCoeficients = policyConfiguration.getByType(RiskType.THEFT);

        // then
        assertThat(theftCoeficients).hasSize(4);
        assertThat(theftCoeficients.get(0).getUpperBound()).isEqualTo(BigDecimal.ZERO);
        assertThat(theftCoeficients.get(1).getUpperBound()).isEqualTo(new BigDecimal("8.5"));
        assertThat(theftCoeficients.get(2).getUpperBound()).isEqualTo(new BigDecimal("14"));
        assertThat(theftCoeficients.get(3).getUpperBound()).isNull();
    }
}
