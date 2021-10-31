package com.proof.policy.calculator;

import com.proof.policy.model.Policy;
import com.proof.policy.model.PolicyItemTO;
import com.proof.policy.model.PolicySubItemTO;
import com.proof.policy.model.RiskType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


class PremiumCalculatorTest {

    private PremiumCalculator premiumCalculator = new PremiumCalculator(Collections.emptyList());

    @Test
    void shouldFlattenAndAggregatePolicyItemsWhenDifferentProvided() {
        // given
        PolicyItemTO item1 = PolicyItemTO.builder()
                .subItems(List.of(
                        PolicySubItemTO.builder()
                                .riskType(RiskType.FIRE)
                                .sumInsured(new BigDecimal("18.51"))
                                .build(),
                        PolicySubItemTO.builder()
                                .riskType(RiskType.THEFT)
                                .sumInsured(new BigDecimal("14.08"))
                                .build()))
                .build();

        PolicyItemTO item2 = PolicyItemTO.builder()
                .subItems(List.of(
                        PolicySubItemTO.builder()
                                .riskType(RiskType.THEFT)
                                .sumInsured(new BigDecimal("0.12"))
                                .build(),
                        PolicySubItemTO.builder()
                                .riskType(RiskType.FIRE)
                                .sumInsured(new BigDecimal("151.00"))
                                .build(),
                        PolicySubItemTO.builder()
                                .riskType(RiskType.THEFT)
                                .sumInsured(new BigDecimal("92.4"))
                                .build()))
                .build();

        Policy policy = Policy.builder()
                .items(List.of(item1, item2)).build();

        // when
        Map<RiskType, BigDecimal> sumsByRiskType = premiumCalculator.flattenByRiskType(policy);

        // then
        assertThat(sumsByRiskType).hasSize(2);
        assertThat(sumsByRiskType.get(RiskType.THEFT)).isEqualTo(new BigDecimal("106.60"));
        assertThat(sumsByRiskType.get(RiskType.FIRE)).isEqualTo(new BigDecimal("169.51"));
    }

    @Test
    void shouldNotFailWhenEmptyPolicyGiven() {
        // given
        Policy policy = Policy.builder()
                .items(Collections.emptyList()).build();

        // when
        Map<RiskType, BigDecimal> sumsByRiskType = premiumCalculator.flattenByRiskType(policy);

        assertThat(sumsByRiskType).hasSize(0);
    }

    @Test
    void shouldFlattenAndAggregatePolicyWhenGivenIncompleteSubItems() {
        // given
        PolicyItemTO item1 = PolicyItemTO.builder()
                .subItems(List.of(
                        PolicySubItemTO.builder()
                                .riskType(RiskType.THEFT)
                                .sumInsured(new BigDecimal("18.51"))
                                .build(),
                        PolicySubItemTO.builder()
                                .riskType(RiskType.THEFT)
                                .sumInsured(null)
                                .build()))
                .build();


        Policy policy = Policy.builder()
                .items(List.of(item1)).build();

        // when
        Map<RiskType, BigDecimal> sumsByRiskType = premiumCalculator.flattenByRiskType(policy);

        // then
        assertThat(sumsByRiskType).hasSize(1);
        assertThat(sumsByRiskType.get(RiskType.THEFT)).isEqualTo(new BigDecimal("18.51"));
    }
}
