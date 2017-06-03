package com.cpsdbd.sohelcon.Utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.cpsdbd.sohelcon.BaseClass.Project;
import com.cpsdbd.sohelcon.BaseClass.User;


/**
 * Created by Sohel on 4/12/2016.
 */
public class UserLocalStore {

    private static final String SP_NAME ="userDetails";

    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context){
        userLocalDatabase = context.getSharedPreferences(SP_NAME,0);
    }


    // StoreUserData Method

    public void storeUserData(User user){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putInt("user_id",user.getUser_id());
        spEditor.putInt("parent_id",user.getParent_id());
        spEditor.putString("user_name",user.getUser_name());
        spEditor.putString("user_email",user.getUser_email());
        spEditor.putString("user_password",user.getUser_password());
        spEditor.putString("user_phone",user.getUser_phone());
        spEditor.putString("user_designation",user.getUser_designation());
        spEditor.putString("user_companyname",user.getUser_companyname());
        spEditor.putString("user_address",user.getUser_address());
        spEditor.putString("user_photo",user.getUser_photo());
        spEditor.putInt("user_type",user.getUser_type());
        spEditor.putInt("user_status",user.getUser_status());
        spEditor.putInt(FieldConstant.USER_ACCESS,user.getUser_status());
        spEditor.putString("user_create_date",user.getUser_create_date());
        spEditor.putString("user_update_date",user.getUser_update_date());

        //spEditor.putBoolean("loginstatus",false);
        spEditor.apply();

    }

    public User getLoggedInUser(){
        int user_id = userLocalDatabase.getInt("user_id",-1);
        int parent_id = userLocalDatabase.getInt("parent_id",-1);
        String user_name = userLocalDatabase.getString("user_name","");
        String user_email = userLocalDatabase.getString("user_email","");
        String user_password = userLocalDatabase.getString("user_password","");
        String user_phone = userLocalDatabase.getString("user_phone","");
        String user_designation = userLocalDatabase.getString("user_designation","");
        String user_companyname = userLocalDatabase.getString("user_companyname","");
        String user_address = userLocalDatabase.getString("user_address","");
        String user_photo = userLocalDatabase.getString("user_photo","");
        int user_type = userLocalDatabase.getInt("user_type",-1);
        int user_status = userLocalDatabase.getInt("user_status",-1);
        int user_access = userLocalDatabase.getInt(FieldConstant.USER_ACCESS,-1);
        String user_create_date = userLocalDatabase.getString("user_create_date","");
        String user_update_date = userLocalDatabase.getString("user_update_date","");

        User loggedInuser = new User(user_id,parent_id,user_name,user_email,user_password,user_phone,user_companyname,user_designation,user_address,user_photo,user_type,user_status,user_access,user_create_date,user_update_date);

        return loggedInuser;
    }


    public void setCurrentProject(Project project){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();

        spEditor.putInt(FieldConstant.PROJECT_ID,project.getProject_id());
        spEditor.putInt(FieldConstant.PROJECT_USER_ID,project.getProject_user_id());
        spEditor.putInt(FieldConstant.PROJECT_COORDINATOR_ID,project.getProject_coordinator_id());
        spEditor.putInt(FieldConstant.PROJECT_MANAGER_ID,project.getProject_manager_id());
        spEditor.putInt(FieldConstant.PROJECT_CHILD_ID,project.getProject_child_id());

        spEditor.putString(FieldConstant.PROJECT_NAME,project.getProject_name());
        spEditor.putString(FieldConstant.PROJECT_COORDINATOR_NAME,project.getProject_coordinator_name());
        spEditor.putString(FieldConstant.PROJECT_COORDINATOR_PHOTO,project.getProject_coordinator_photo());
        spEditor.putString(FieldConstant.PROJECT_MANAGER_NAME,project.getProject_manager_name());
        spEditor.putString(FieldConstant.PROJECT_MANAGER_PHOTO,project.getProject_manager_photo());
        spEditor.putString(FieldConstant.PROJECT_CHILD_NAME,project.getProject_child_name());

        spEditor.putFloat(FieldConstant.PROJECT_TOTAL_COST, (float) project.getProject_total_cost());
        spEditor.putFloat(FieldConstant.PROJECT_FINANCIAL_PROGRESS, (float) project.getProject_financial_progress());
        spEditor.putFloat(FieldConstant.PROJECT_PHYSICAL_PROGRESS, (float) project.getProject_physical_progress());
        spEditor.putFloat(FieldConstant.PROJECT_EXPENDITURE, (float) project.getProject_expenditure());
        spEditor.putFloat(FieldConstant.PROJECT_DEBIT, (float) project.getProject_debit());

        spEditor.putString(FieldConstant.PROJECT_DESCEIPTION,project.getProject_desceiption());
        spEditor.putString(FieldConstant.PROJECT_LOCATION,project.getProject_location());
        spEditor.putString(FieldConstant.PROJECT_START_DATE,project.getProject_start_date());
        spEditor.putString(FieldConstant.PROJECT_COMPLETION_DATE,project.getProject_completion_date());
        spEditor.putString(FieldConstant.PROJECT_CREATE_DATE,project.getProject_create_date());
        spEditor.putString(FieldConstant.PROJECT_UPDATE_DATE,project.getProject_update_date());


        spEditor.apply();

    }

    public Project getCurrentProject(){
        int project_id=userLocalDatabase.getInt(FieldConstant.PROJECT_ID,-1);
        int user_id=userLocalDatabase.getInt(FieldConstant.PROJECT_USER_ID,-1);
        int coordinator_id=userLocalDatabase.getInt(FieldConstant.PROJECT_COORDINATOR_ID,-1);
        int manager_id=userLocalDatabase.getInt(FieldConstant.PROJECT_MANAGER_ID,-1);
        int child_id=userLocalDatabase.getInt(FieldConstant.PROJECT_CHILD_ID,-1);

        String project_name=userLocalDatabase.getString(FieldConstant.PROJECT_NAME,null);
        String coordinator_name=userLocalDatabase.getString(FieldConstant.PROJECT_COORDINATOR_NAME,null);
        String coordinator_photo=userLocalDatabase.getString(FieldConstant.PROJECT_COORDINATOR_PHOTO,"");
        String manager_name=userLocalDatabase.getString(FieldConstant.PROJECT_MANAGER_NAME,null);
        String manager_photo=userLocalDatabase.getString(FieldConstant.PROJECT_MANAGER_PHOTO,"");
        String child_name=userLocalDatabase.getString(FieldConstant.PROJECT_CHILD_NAME,null);

        double financial_progress = userLocalDatabase.getFloat(FieldConstant.PROJECT_FINANCIAL_PROGRESS,0);
        double total_cost = userLocalDatabase.getFloat(FieldConstant.PROJECT_TOTAL_COST,0);
        double physical_progress = userLocalDatabase.getFloat(FieldConstant.PROJECT_PHYSICAL_PROGRESS,0);
        double expenditure = userLocalDatabase.getFloat(FieldConstant.PROJECT_EXPENDITURE,0);
        double debit = userLocalDatabase.getFloat(FieldConstant.PROJECT_DEBIT,0);

        String description = userLocalDatabase.getString(FieldConstant.PROJECT_DESCEIPTION,null);
        String location = userLocalDatabase.getString(FieldConstant.PROJECT_LOCATION,null);
        String start_date = userLocalDatabase.getString(FieldConstant.PROJECT_START_DATE,null);
        String completion_date = userLocalDatabase.getString(FieldConstant.PROJECT_COMPLETION_DATE,null);
        String create_date = userLocalDatabase.getString(FieldConstant.PROJECT_CREATE_DATE,null);
        String update_date = userLocalDatabase.getString(FieldConstant.PROJECT_UPDATE_DATE,null);


        Project project = new Project(project_id,user_id,coordinator_id,manager_id,child_id,project_name,coordinator_name,
                coordinator_photo,manager_name,manager_photo,child_name,total_cost,financial_progress,physical_progress,expenditure,debit,description,location,start_date,completion_date,create_date,update_date);

        return project;
    }

    public  void setUserLoggedIn(boolean loggedin){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn",loggedin);
        spEditor.commit();

    }

    public boolean getUserLoggedIn(){
        return userLocalDatabase.getBoolean("loggedIn",false);
    }

    public void clearUserData(){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.apply();
    }

    public void setUserName(String userName){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("username",userName);
        spEditor.apply();
    }

    public String getUserName(){
        return userLocalDatabase.getString("username","");
    }

    public void setProjectName(String userName){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("projectname",userName);
        spEditor.apply();
    }

    public String getProjectName(){
        return userLocalDatabase.getString("projectname","");
    }


}
