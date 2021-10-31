package com.proof.policy.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;


@Getter
@Setter
@Builder
public class Policy {

    @NotNull
    private  String number;

    private List<PolicyItemTO> items;

    private PolicyStatus status;


}
