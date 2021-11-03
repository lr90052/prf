package com.proof.policy.config;

import com.proof.policy.model.RiskType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class RiskTypeConfig {

    private BigDecimal upperBound;

    private BigDecimal coefficient;

    private RiskType type;
}
