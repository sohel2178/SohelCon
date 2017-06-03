package com.cpsdbd.sohelcon.BaseClass;

import java.io.Serializable;

/**
 * Created by Sohel on 10/4/2016.
 */
public class ActivityData implements Serializable {



    private int activity_id;
    private int activity_user_id;
    private int activity_project_id;
    private String activity_name;
    private String activity_start_date;
    private String activity_finished_date;
    private double activity_volume_of_works;
    private double activity_unit_rate;
    private double activity_volume_of_work_done;
    private String activity_last_modified;


    public ActivityData() {
    }

    public int getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(int activity_id) {
        this.activity_id = activity_id;
    }

    public int getActivity_user_id() {
        return activity_user_id;
    }

    public void setActivity_user_id(int activity_user_id) {
        this.activity_user_id = activity_user_id;
    }

    public int getActivity_project_id() {
        return activity_project_id;
    }

    public void setActivity_project_id(int activity_project_id) {
        this.activity_project_id = activity_project_id;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public String getActivity_start_date() {
        return activity_start_date;
    }

    public void setActivity_start_date(String activity_start_date) {
        this.activity_start_date = activity_start_date;
    }

    public String getActivity_finished_date() {
        return activity_finished_date;
    }

    public void setActivity_finished_date(String activity_finished_date) {
        this.activity_finished_date = activity_finished_date;
    }

    public double getActivity_volume_of_works() {
        return activity_volume_of_works;
    }

    public void setActivity_volume_of_works(double activity_volume_of_works) {
        this.activity_volume_of_works = activity_volume_of_works;
    }

    public double getActivity_unit_rate() {
        return activity_unit_rate;
    }

    public void setActivity_unit_rate(double activity_unit_rate) {
        this.activity_unit_rate = activity_unit_rate;
    }

    public double getActivity_volume_of_work_done() {
        return activity_volume_of_work_done;
    }

    public void setActivity_volume_of_work_done(double activity_volume_of_work_done) {
        this.activity_volume_of_work_done = activity_volume_of_work_done;
    }

    public String getActivity_last_modified() {
        return activity_last_modified;
    }

    public void setActivity_last_modified(String activity_last_modified) {
        this.activity_last_modified = activity_last_modified;
    }
}
