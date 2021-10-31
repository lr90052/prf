package com.proof.policy.calculator;

import com.proof.policy.model.Policy;
import com.proof.policy.model.PolicyItemTO;
import com.proof.policy.model.PolicyStatus;
import com.proof.policy.model.PolicySubItemTO;
import com.proof.policy.model.RiskType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service
@AllArgsConstructor
@Component
public class PremiumCalculator {

    final List<RiskCalculator> calculators;

    public BigDecimal calculate(Policy policy) {
        policy.setStatus(PolicyStatus.CALCULATED);
        Map<RiskType, BigDecimal> aggregatedByRiskType = flattenByRiskType(policy);
        return calculators.stream()
                .map(calc -> calc.calculate(aggregatedByRiskType))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    Map<RiskType, BigDecimal> flattenByRiskType(Policy policy) {
        Map<RiskType, ArrayList<BigDecimal>> proRiskType = policy.getItems().stream()
                .map(PolicyItemTO::getSubItems)
                .flatMap(Collection::stream)
                .filter(subitem -> subitem.getSumInsured() != null)
                .collect(Collectors.toMap(
                        PolicySubItemTO::getRiskType,
                        subItem -> new ArrayList<>(List.of(subItem.getSumInsured())),
                        (existingSums, newSums) -> {
                            existingSums.addAll(newSums);
                            return existingSums;
                        }));

        return proRiskType.entrySet().stream().map(
                entry -> Map.entry(
                        entry.getKey(),
                        entry.getValue().stream().reduce(BigDecimal.ZERO, BigDecimal::add))
                                                  )
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
