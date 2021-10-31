package com.proof.policy.calculator;

import com.proof.policy.model.RiskType;

import java.math.BigDecimal;
import java.util.Map;


public interface RiskCalculator {

    BigDecimal calculate(Map<RiskType, BigDecimal> sumInsured2type);
}
