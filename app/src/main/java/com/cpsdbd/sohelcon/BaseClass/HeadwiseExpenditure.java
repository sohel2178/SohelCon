package com.cpsdbd.sohelcon.BaseClass;

import java.io.Serializable;

/**
 * Created by Sohel on 10/8/2016.
 */

public class HeadwiseExpenditure implements Serializable {

    private String head;
    private float amount;

    public HeadwiseExpenditure() {
    }

    public HeadwiseExpenditure(String head, float amount) {
        this.head = head;
        this.amount = amount;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
