package com.cpsdbd.sohelcon.BaseClass;

import java.io.Serializable;

/**
 * Created by Sohel on 4/16/2016.
 */
public class User implements Serializable {
    private int user_id;
    private int parent_id;
    private String user_name;
    private String user_email;
    private String user_password;
    private String user_phone;
    private String user_companyname;
    private String user_designation;

    private String user_address;
    private String user_photo;
    private int user_type;
    private int user_status;
    private int user_access;

    private String user_create_date;
    private String user_update_date;




    public User(){

    }




    public User(int user_id,int parent_id, String user_name, String user_email, String password, String user_phone,String user_companyname,
            String user_designation,String user_address, String user_photo, int user_type, int user_status,int user_access, String user_create_date, String user_update_date) {
        this.user_id = user_id;
        this.parent_id =parent_id;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_password = password;
        this.user_phone = user_phone;
        this.user_address = user_address;
        this.user_photo = user_photo;
        this.user_type = user_type;
        this.user_status = user_status;
        this.user_access=user_access;
        this.user_create_date = user_create_date;
        this.user_update_date = user_update_date;
        this.user_companyname=user_companyname;
        this.user_designation=user_designation;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public int getUser_status() {
        return user_status;
    }

    public void setUser_status(int user_status) {
        this.user_status = user_status;
    }

    public String getUser_create_date() {
        return user_create_date;
    }

    public void setUser_create_date(String user_create_date) {
        this.user_create_date = user_create_date;
    }

    public String getUser_update_date() {
        return user_update_date;
    }

    public void setUser_update_date(String user_update_date) {
        this.user_update_date = user_update_date;
    }

    public String getUser_companyname() {
        return user_companyname;
    }

    public void setUser_companyname(String user_companyname) {
        this.user_companyname = user_companyname;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public String getUser_designation() {
        return user_designation;
    }

    public void setUser_designation(String user_designation) {
        this.user_designation = user_designation;
    }

    public int getUser_access() {
        return user_access;
    }

    public void setUser_access(int user_access) {
        this.user_access = user_access;
    }
}
