package com.dezrill.support;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class HistoryItem implements Serializable {
    String date, time, currency, sum;
    Map<String, String> values;

    public HistoryItem() {
        values=new HashMap<>();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }

    public Map<String, String> getValues() {
        return values;
    }
}
