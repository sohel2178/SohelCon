package net.optimusbs.sohelcon.Response;




import net.optimusbs.sohelcon.BaseClass.StoreData;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sohel on 10/2/2016.
 */
public class StoreResponse implements Serializable {
    private int success;
    private String message;
    private List<StoreData> store;

    public StoreResponse() {
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

    public List<StoreData> getStore() {
        return store;
    }

    public void setStore(List<StoreData> store) {
        this.store = store;
    }
}
