package net.optimusbs.sohelcon.BaseClass;

import java.io.Serializable;

/**
 * Created by Sohel on 9/27/2016.
 */
public class FinanceData implements Serializable {

    private int finance_transaction_id;
    private int finance_user_id;
    private int finance_project_id;
    private String finance_transaction_date;
    private String finance_particular;
    private String finance_head;
    private double finance_amount;
    private int finance_status;

    public FinanceData(){

    }

    public int getFinance_transaction_id() {
        return finance_transaction_id;
    }

    public void setFinance_transaction_id(int finance_transaction_id) {
        this.finance_transaction_id = finance_transaction_id;
    }

    public int getFinance_user_id() {
        return finance_user_id;
    }

    public void setFinance_user_id(int finance_user_id) {
        this.finance_user_id = finance_user_id;
    }

    public int getFinance_project_id() {
        return finance_project_id;
    }

    public void setFinance_project_id(int finance_project_id) {
        this.finance_project_id = finance_project_id;
    }

    public String getFinance_transaction_date() {
        return finance_transaction_date;
    }

    public void setFinance_transaction_date(String finance_transaction_date) {
        this.finance_transaction_date = finance_transaction_date;
    }

    public String getFinance_particular() {
        return finance_particular;
    }

    public void setFinance_particular(String finance_particular) {
        this.finance_particular = finance_particular;
    }

    public String getFinance_head() {
        return finance_head;
    }

    public void setFinance_head(String finance_head) {
        this.finance_head = finance_head;
    }

    public double getFinance_amount() {
        return finance_amount;
    }

    public void setFinance_amount(double finance_amount) {
        this.finance_amount = finance_amount;
    }

    public int getFinance_status() {
        return finance_status;
    }

    public void setFinance_status(int finance_status) {
        this.finance_status = finance_status;
    }
}
