package net.optimusbs.sohelcon.Response;





import net.optimusbs.sohelcon.BaseClass.ActivityData;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sohel on 10/4/2016.
 */
public class ActivityResponse implements Serializable {
    private int success;
    private String message;
    private List<ActivityData> activities;

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

    public List<ActivityData> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityData> activities) {
        this.activities = activities;
    }
}
