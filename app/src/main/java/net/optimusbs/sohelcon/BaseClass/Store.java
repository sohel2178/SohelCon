package net.optimusbs.sohelcon.BaseClass;

import java.io.Serializable;

/**
 * Created by Sohel on 10/16/2016.
 */

public class Store implements Serializable {

    private String item;
    private float quantity;

    public Store() {
    }

    public Store(String item, float quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }
}
