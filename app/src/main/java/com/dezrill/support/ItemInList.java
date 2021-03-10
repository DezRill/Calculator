package com.dezrill.support;

import java.io.Serializable;

public class ItemInList implements Serializable {
    private String currency, denomination, sum, count;

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDenomination() {
        return denomination;
    }

    public String getSum() {
        return sum;
    }

    public String getCount() {
        return count;
    }
}
