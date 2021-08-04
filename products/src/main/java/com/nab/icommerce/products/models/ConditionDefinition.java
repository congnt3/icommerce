package com.nab.icommerce.products.models;

import lombok.Getter;
import lombok.Setter;

public class ConditionDefinition{
    public ConditionDefinition(String comparisonOperand, String value){
        this.setComparisonOperand(comparisonOperand);
        this.setValue(value);
    }

    public ConditionDefinition(){}

    @Getter
    @Setter
    private String comparisonOperand;

    @Getter
    @Setter
    private String value;
}
