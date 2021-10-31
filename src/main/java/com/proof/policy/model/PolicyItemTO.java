package com.proof.policy.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;


@Value
@Builder
public class PolicyItemTO {

    private String name;

    private List<PolicySubItemTO> subItems;
}
