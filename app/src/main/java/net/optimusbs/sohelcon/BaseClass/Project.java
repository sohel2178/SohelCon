package net.optimusbs.sohelcon.BaseClass;

import java.io.Serializable;

/**
 * Created by Sohel on 9/24/2016.
 */
public class Project implements Serializable {

    private int project_id ;
    private int project_user_id ;
    private int project_coordinator_id;
    private int project_manager_id ;
    private int project_child_id ;
    private String project_name;
    private String project_coordinator_name;
    private String project_coordinator_photo;
    private String project_manager_name;
    private String project_manager_photo;
    private String project_child_name;
    private double project_total_cost;
    private double project_financial_progress;
    private double project_physical_progress;
    private double project_expenditure ;
    private double project_debit ;
    private String project_desceiption ;
    private String project_location ;
    private String project_start_date ;
    private String project_completion_date ;
    private String project_create_date ;
    private String project_update_date;


    public Project(){

    }

    public Project(int project_id, int project_user_id, int project_coordinator_id, int project_manager_id,int project_child_id ,String project_name,String project_coordinator_name,
                   String project_coordinator_photo,String project_manager_name,String project_manager_photo,
                   String project_child_name,double project_total_cost,double project_financial_progress, double project_physical_progress, double project_expenditure,double project_debit,
                   String project_desceiption, String project_location, String project_start_date, String project_completion_date, String project_create_date,
                   String project_update_date) {
        this.project_id = project_id;
        this.project_user_id = project_user_id;
        this.project_coordinator_id = project_coordinator_id;
        this.project_manager_id = project_manager_id;
        this.project_child_id=project_child_id;
        this.project_name = project_name;
        this.project_coordinator_name = project_coordinator_name;
        this.project_coordinator_photo=project_coordinator_photo;
        this.project_manager_name = project_manager_name;
        this.project_manager_photo=project_manager_photo;
        this.project_child_name=project_child_name;
        this.project_total_cost=project_total_cost;
        this.project_financial_progress = project_financial_progress;
        this.project_physical_progress = project_physical_progress;
        this.project_expenditure = project_expenditure;
        this.project_debit=project_debit;
        this.project_desceiption = project_desceiption;
        this.project_location = project_location;
        this.project_start_date = project_start_date;
        this.project_completion_date = project_completion_date;
        this.project_create_date = project_create_date;
        this.project_update_date = project_update_date;
    }


    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public int getProject_user_id() {
        return project_user_id;
    }

    public void setProject_user_id(int project_user_id) {
        this.project_user_id = project_user_id;
    }

    public int getProject_coordinator_id() {
        return project_coordinator_id;
    }

    public void setProject_coordinator_id(int project_coordinator_id) {
        this.project_coordinator_id = project_coordinator_id;
    }

    public int getProject_manager_id() {
        return project_manager_id;
    }

    public void setProject_manager_id(int project_manager_id) {
        this.project_manager_id = project_manager_id;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public double getProject_financial_progress() {
        return project_financial_progress;
    }

    public void setProject_financial_progress(double project_financial_progress) {
        this.project_financial_progress = project_financial_progress;
    }

    public double getProject_physical_progress() {
        return project_physical_progress;
    }

    public void setProject_physical_progress(double project_physical_progress) {
        this.project_physical_progress = project_physical_progress;
    }

    public double getProject_total_cost() {
        return project_total_cost;
    }

    public void setProject_total_cost(double project_total_cost) {
        this.project_total_cost = project_total_cost;
    }

    public double getProject_expenditure() {
        return project_expenditure;
    }

    public void setProject_expenditure(double project_expenditure) {
        this.project_expenditure = project_expenditure;
    }

    public String getProject_desceiption() {
        return project_desceiption;
    }

    public void setProject_desceiption(String project_desceiption) {
        this.project_desceiption = project_desceiption;
    }

    public String getProject_location() {
        return project_location;
    }

    public void setProject_location(String project_location) {
        this.project_location = project_location;
    }

    public String getProject_start_date() {
        return project_start_date;
    }

    public void setProject_start_date(String project_start_date) {
        this.project_start_date = project_start_date;
    }

    public String getProject_completion_date() {
        return project_completion_date;
    }

    public void setProject_completion_date(String project_completion_date) {
        this.project_completion_date = project_completion_date;
    }

    public String getProject_create_date() {
        return project_create_date;
    }

    public void setProject_create_date(String project_create_date) {
        this.project_create_date = project_create_date;
    }

    public String getProject_update_date() {
        return project_update_date;
    }

    public void setProject_update_date(String project_update_date) {
        this.project_update_date = project_update_date;
    }

    public String getProject_coordinator_name() {
        return project_coordinator_name;
    }

    public void setProject_coordinator_name(String project_coordinator_name) {
        this.project_coordinator_name = project_coordinator_name;
    }

    public String getProject_manager_name() {
        return project_manager_name;
    }

    public void setProject_manager_name(String project_manager_name) {
        this.project_manager_name = project_manager_name;
    }

    public int getProject_child_id() {
        return project_child_id;
    }

    public void setProject_child_id(int project_child_id) {
        this.project_child_id = project_child_id;
    }

    public String getProject_child_name() {
        return project_child_name;
    }

    public void setProject_child_name(String project_child_name) {
        this.project_child_name = project_child_name;
    }

    public double getProject_debit() {
        return project_debit;
    }

    public void setProject_debit(double project_debit) {
        this.project_debit = project_debit;
    }

    public String getProject_coordinator_photo() {
        return project_coordinator_photo;
    }

    public void setProject_coordinator_photo(String project_coordinator_photo) {
        this.project_coordinator_photo = project_coordinator_photo;
    }

    public String getProject_manager_photo() {
        return project_manager_photo;
    }

    public void setProject_manager_photo(String project_manager_photo) {
        this.project_manager_photo = project_manager_photo;
    }
}
