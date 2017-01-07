package net.optimusbs.sohelcon.BaseClass;

import java.io.Serializable;

/**
 * Created by Sohel on 10/2/2016.
 */
public class StoreData implements Serializable {
    private int store_id;
    private int store_user_id;
    private int store_project_id;
    private String store_rec_con_date;
    private String store_particular;
    private String store_received_from;
    private String store_challan_no;
    private String store_mrr_no;
    private String store_issue_to;
    private String store_where_used;
    private double store_quantity;
    private int store_status;

    public StoreData(){

    }


    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public int getStore_user_id() {
        return store_user_id;
    }

    public void setStore_user_id(int store_user_id) {
        this.store_user_id = store_user_id;
    }

    public int getStore_project_id() {
        return store_project_id;
    }

    public void setStore_project_id(int store_project_id) {
        this.store_project_id = store_project_id;
    }

    public String getStore_rec_con_date() {
        return store_rec_con_date;
    }

    public void setStore_rec_con_date(String store_rec_con_date) {
        this.store_rec_con_date = store_rec_con_date;
    }

    public String getStore_particular() {
        return store_particular;
    }

    public void setStore_particular(String store_particular) {
        this.store_particular = store_particular;
    }

    public String getStore_received_from() {
        return store_received_from;
    }

    public void setStore_received_from(String store_received_from) {
        this.store_received_from = store_received_from;
    }

    public String getStore_challan_no() {
        return store_challan_no;
    }

    public void setStore_challan_no(String store_challan_no) {
        this.store_challan_no = store_challan_no;
    }

    public String getStore_mrr_no() {
        return store_mrr_no;
    }

    public void setStore_mrr_no(String store_mrr_no) {
        this.store_mrr_no = store_mrr_no;
    }

    public String getStore_issue_to() {
        return store_issue_to;
    }

    public void setStore_issue_to(String store_issue_to) {
        this.store_issue_to = store_issue_to;
    }

    public String getStore_where_used() {
        return store_where_used;
    }

    public void setStore_where_used(String store_where_used) {
        this.store_where_used = store_where_used;
    }

    public double getStore_quantity() {
        return store_quantity;
    }

    public void setStore_quantity(double store_quantity) {
        this.store_quantity = store_quantity;
    }

    public int getStore_status() {
        return store_status;
    }

    public void setStore_status(int store_status) {
        this.store_status = store_status;
    }
}
