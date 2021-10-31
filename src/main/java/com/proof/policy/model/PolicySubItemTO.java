package com.proof.policy.model;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;


@Value
@Builder
public class PolicySubItemTO {

    private String name;

    private BigDecimal sumInsured;

    private RiskType riskType;
}
