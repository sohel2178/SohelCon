package net.optimusbs.sohelcon.Response;





import net.optimusbs.sohelcon.BaseClass.User;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sohel on 10/25/2016.
 */

public class ChildResponse implements Serializable {
    private int success;
    private String message;
    private List<User> users;

    public ChildResponse() {
    }

    public ChildResponse(int success, String message, List<User> users) {
        this.success = success;
        this.message = message;
        this.users = users;
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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
