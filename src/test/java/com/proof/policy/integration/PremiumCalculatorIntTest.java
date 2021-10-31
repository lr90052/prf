package com.proof.policy.integration;

import com.proof.policy.Application;
import com.proof.policy.calculator.PremiumCalculator;
import com.proof.policy.model.Policy;
import com.proof.policy.model.PolicyItemTO;
import com.proof.policy.model.PolicySubItemTO;
import com.proof.policy.model.RiskType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(classes = Application.class)
class PremiumCalculatorIntTest {

    @Autowired
    private PremiumCalculator premiumCalculator;

    @Test
    void shouldCalculatePremiumBaseScenario1() {
        // given
        PolicyItemTO item = PolicyItemTO.builder()
                .subItems(List.of(
                        PolicySubItemTO.builder()
                                .riskType(RiskType.FIRE)
                                .sumInsured(new BigDecimal("100.00"))
                                .build(),
                        PolicySubItemTO.builder()
                                .riskType(RiskType.THEFT)
                                .sumInsured(new BigDecimal("8.00"))
                                .build()))
                .build();

        Policy policy = Policy.builder().items(List.of(item)).build();

        // when
        BigDecimal premium = premiumCalculator.calculate(policy);

        // then
        assertThat(premium).isEqualTo(new BigDecimal("2.28"));
    }

    @Test
    void shouldCalculatePremiumBaseScenario2() {
        // given

        // Risk type = FIRE, Sum insured = 500.00
        // Risk type = THEFT, Sum insured = 102.51
        PolicyItemTO item = PolicyItemTO.builder()
                .subItems(List.of(
                        PolicySubItemTO.builder()
                                .riskType(RiskType.FIRE)
                                .sumInsured(new BigDecimal("500.00"))
                                .build(),
                        PolicySubItemTO.builder()
                                .riskType(RiskType.THEFT)
                                .sumInsured(new BigDecimal("102.51"))
                                .build()))
                .build();

        Policy policy = Policy.builder().items(List.of(item)).build();

        // when
        BigDecimal premium = premiumCalculator.calculate(policy);

        // then
        assertThat(premium).isEqualTo(new BigDecimal("17.13"));
    }

    @Test
    void shouldCalculatePremiumWhenLowerBoundsInsuranceValues() {
        // given
        PolicyItemTO item1 = PolicyItemTO.builder()
                .subItems(List.of(
                        PolicySubItemTO.builder()
                                .riskType(RiskType.FIRE)
                                .sumInsured(new BigDecimal("18.51"))
                                .build(),
                        PolicySubItemTO.builder()
                                .riskType(RiskType.THEFT)
                                .sumInsured(new BigDecimal("9"))
                                .build()))
                .build();

        PolicyItemTO item2 = PolicyItemTO.builder()
                .subItems(List.of(
                        PolicySubItemTO.builder()
                                .riskType(RiskType.THEFT)
                                .sumInsured(new BigDecimal("2.33"))
                                .build(),
                        PolicySubItemTO.builder()
                                .riskType(RiskType.FIRE)
                                .sumInsured(new BigDecimal("80.01"))
                                .build(),
                        PolicySubItemTO.builder()
                                .riskType(RiskType.THEFT)
                                .sumInsured(new BigDecimal("3.66"))
                                .build()))
                .build();

        Policy policy = Policy.builder()
                .items(List.of(item1, item2)).build();

        // when
        BigDecimal premium = premiumCalculator.calculate(policy);

        // then
        // 18.51
        // 80.01
        // -------------
        // 98.51 * 0.014 = 1.38

        //   9.00
        //   2.33
        //   3.66
        //  ------------
        //  14.99 * 0.11  = 1.65
        // total = 1.38 + 1.65 = 3.03
        assertThat(premium).isEqualTo(new BigDecimal("3.03"));
    }

    @Test
    void shouldCalculatePremiumWhenUpperBoundsInsuranceValues() {
        // given
        PolicyItemTO item1 = PolicyItemTO.builder()
                .subItems(List.of(
                        PolicySubItemTO.builder()
                                .riskType(RiskType.FIRE)
                                .sumInsured(new BigDecimal("29600"))
                                .build(),
                        PolicySubItemTO.builder()
                                .riskType(RiskType.FIRE)
                                .sumInsured(new BigDecimal("890.50"))
                                .build()))
                .build();

        PolicyItemTO item2 = PolicyItemTO.builder()
                .subItems(List.of(
                        PolicySubItemTO.builder()
                                .riskType(RiskType.THEFT)
                                .sumInsured(new BigDecimal("1.11"))
                                .build(),
                        PolicySubItemTO.builder()
                                .riskType(RiskType.THEFT)
                                .sumInsured(new BigDecimal("29.17"))
                                .build(),
                        PolicySubItemTO.builder()
                                .riskType(RiskType.THEFT)
                                .sumInsured(new BigDecimal("0.01"))
                                .build()))
                .build();

        Policy policy = Policy.builder()
                .items(List.of(item1, item2)).build();

        // when
        BigDecimal premium = premiumCalculator.calculate(policy);

        // then
        // 29600
        //   890.50
        // -------------
        // 30490.50 * 0.024 = 731.77

        //     1.11
        //    29.17
        //     0.01
        //  ------------
        //   30.29 * 0.05  = 1.51
        // total = 1.731.77 + 1.51 = 733.28
        assertThat(premium).isEqualTo(new BigDecimal("733.28"));
    }

    @Test
    void shouldCalculatePremiumWhenItemEmpty() {
        // given
        PolicyItemTO item1 = PolicyItemTO.builder()
                .subItems(List.of(
                        PolicySubItemTO.builder()
                                .riskType(RiskType.FIRE)
                                .sumInsured(new BigDecimal("19.20"))
                                .build(),
                        PolicySubItemTO.builder()
                                .riskType(RiskType.THEFT)
                                .sumInsured(new BigDecimal("19.20"))
                                .build()))
                .build();

        PolicyItemTO item2 = PolicyItemTO.builder()
                .subItems(Collections.emptyList())
                .build();

        Policy policy = Policy.builder()
                .items(List.of(item1, item2)).build();

        // when
        BigDecimal premium = premiumCalculator.calculate(policy);

        // 19.20 * 0.014 = 0.27

        //  19.20 * 0.05  = 0.96
        // total = 0.27 + 0.96 = 1.23
        assertThat(premium).isEqualTo(new BigDecimal("1.23"));
    }
}
