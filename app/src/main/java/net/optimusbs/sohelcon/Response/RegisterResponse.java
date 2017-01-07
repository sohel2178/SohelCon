package net.optimusbs.sohelcon.Response;

import java.io.Serializable;

/**
 * Created by Sohel on 9/21/2016.
 */
public class RegisterResponse implements Serializable {
    private int success;
    private String message;

    public RegisterResponse(){

    }

    public RegisterResponse(int success, String message) {
        this.success = success;
        this.message = message;
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
}
