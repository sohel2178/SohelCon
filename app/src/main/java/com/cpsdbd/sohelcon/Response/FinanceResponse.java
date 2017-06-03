package com.cpsdbd.sohelcon.Response;





import com.cpsdbd.sohelcon.BaseClass.FinanceData;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sohel on 9/28/2016.
 */
public class FinanceResponse implements Serializable {

    private int success;
    private String message;
    private List<FinanceData> finance;

    public FinanceResponse(){

    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FinanceData> getFinance() {
        return finance;
    }

    public void setFinance(List<FinanceData> finance) {
        this.finance = finance;
    }
}
