package com.dezrill.support;

public class Operation {
    private boolean operation;
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

    public boolean isOperation() {
        return operation;
    }

    public double getOperationValue() {
        return operationValue;
    }

    public String getCurrencyOperation() {
        return currencyOperation;
    }
}
