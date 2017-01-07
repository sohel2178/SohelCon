package net.optimusbs.sohelcon.Response;



import net.optimusbs.sohelcon.BaseClass.User;

import java.io.Serializable;

/**
 * Created by Sohel on 11/22/2016.
 */

public class UserResponse implements Serializable {
    private int success;
    private String message;
    private User user;

    public UserResponse() {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
