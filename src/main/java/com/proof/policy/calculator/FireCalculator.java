package com.proof.policy.calculator;

import com.proof.policy.config.PolicyConfiguration;
import com.proof.policy.config.RiskTypeConfig;
import com.proof.policy.model.RiskType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
@AllArgsConstructor
public class FireCalculator implements RiskCalculator {

    final PolicyConfiguration policyConfiguration;

    @Override
    public BigDecimal calculate(final Map<RiskType, BigDecimal> sumInsured2type) {
        BigDecimal insuranceSum = sumInsured2type.getOrDefault(RiskType.FIRE, BigDecimal.ZERO);
        BigDecimal coefficient = findMatchingCoefficient(insuranceSum);
        BigDecimal premiumAddend = insuranceSum.multiply(coefficient).setScale(2, RoundingMode.HALF_UP);
        log.debug("Partial premium result {}: {}", RiskType.FIRE, premiumAddend);
        return premiumAddend;
    }

    private BigDecimal findMatchingCoefficient(BigDecimal value) {
        List<RiskTypeConfig> coefficients = policyConfiguration.getByType(RiskType.FIRE);
        return coefficients.stream()
                .filter(config ->  config.getUpperBound() == null || config.getUpperBound().compareTo(value) >= 0)
                .map(RiskTypeConfig::getCoefficient)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "no coefficient config found for " + RiskType.FIRE + ", sum=" + value));
    }
}
