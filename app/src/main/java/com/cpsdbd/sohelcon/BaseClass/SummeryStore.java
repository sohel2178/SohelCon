package com.cpsdbd.sohelcon.BaseClass;

/**
 * Created by Sohel on 10/6/2016.
 */

public class SummeryStore {

    private String item;
    private double total_received;
    private double total_consumed;
    private double balance;

    public SummeryStore() {
    }

    public SummeryStore(String item, double total_received, double total_consumed, double balance) {
        this.item = item;
        this.total_received = total_received;
        this.total_consumed = total_consumed;
        this.balance = balance;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public double getTotal_received() {
        return total_received;
    }

    public void setTotal_received(double total_received) {
        this.total_received = total_received;
    }

    public double getTotal_consumed() {
        return total_consumed;
    }

    public void setTotal_consumed(double total_consumed) {
        this.total_consumed = total_consumed;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
