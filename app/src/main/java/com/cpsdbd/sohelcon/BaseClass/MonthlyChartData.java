package com.cpsdbd.sohelcon.BaseClass;

/**
 * Created by Sohel on 10/9/2016.
 */

public class MonthlyChartData {

    private String month_year;
    private double amount;

    public MonthlyChartData() {
    }

    public MonthlyChartData(String month_year, double amount) {
        this.month_year = month_year;
        this.amount = amount;
    }

    public String getMonth_year() {
        return month_year;
    }

    public void setMonth_year(String month_year) {
        this.month_year = month_year;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
