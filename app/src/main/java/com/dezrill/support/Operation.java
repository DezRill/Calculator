package com.dezrill.support;

import java.io.Serializable;

public class Operation implements Serializable {
    private boolean operation, rememberOperation;
    private double operationValue;
    private String currencyOperation;


    public void setOperation(boolean operation) {
        this.operation = operation;
    }

    public void setOperationValue(double operationValue) {
        this.operationValue = operationValue;
    }

    public void setCurrencyOperation(String currencyOperation) {
        this.currencyOperation = currencyOperation;
    }

    public void setRememberOperation(boolean rememberOperation) {
        this.rememberOperation = rememberOperation;
    }

    public boolean isOperation() {
        return operation;
    }

    public double getOperationValue() {
        return operationValue;
    }

    public String getCurrencyOperation() {
        return currencyOperation;
    }

    public boolean isRememberOperation() {
        return rememberOperation;
    }
}
