package com.dezrill.support;

import java.io.Serializable;

public class Settings implements Serializable {
    private boolean UAHcoins, USDcoins, EURcoins, RUBcoins;
    private int default_value;
    private String language;
    private Operation[] operations=new Operation[4];

    public void setEURcoins(boolean EURcoins) {
        this.EURcoins = EURcoins;
    }

    public void setUAHcoins(boolean UAHcoins) {
        this.UAHcoins = UAHcoins;
    }

    public void setRUBcoins(boolean RUBcoins) {
        this.RUBcoins = RUBcoins;
    }

    public void setUSDcoins(boolean USDcoins) {
        this.USDcoins = USDcoins;
    }

    public void setDefaultValue(int default_value) {
        this.default_value = default_value;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setOperations(Operation[] operations) {
        this.operations = operations;
    }

    public boolean isEURcoins() {
        return EURcoins;
    }

    public boolean isRUBcoins() {
        return RUBcoins;
    }

    public boolean isUAHcoins() {
        return UAHcoins;
    }

    public boolean isUSDcoins() {
        return USDcoins;
    }

    public int getDefaultValue() {
        return default_value;
    }

    public String getLanguage() {
        return language;
    }

    public Operation[] getOperations() {
        return operations;
    }
}
