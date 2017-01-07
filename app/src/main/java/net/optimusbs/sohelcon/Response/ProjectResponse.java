package net.optimusbs.sohelcon.Response;





import net.optimusbs.sohelcon.BaseClass.Project;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sohel on 9/24/2016.
 */
public class ProjectResponse implements Serializable {
    private int success;
    private String message;
    private List<Project> projects;

    public ProjectResponse(){

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

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}
