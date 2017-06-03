package com.cpsdbd.sohelcon;

import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import com.cpsdbd.sohelcon.Response.RegisterResponse;
import com.cpsdbd.sohelcon.Utility.Constant;
import com.cpsdbd.sohelcon.Utility.MyDialog;
import com.cpsdbd.sohelcon.Utility.URLs;
import com.cpsdbd.sohelcon.Utility.UserLocalStore;
import com.cpsdbd.sohelcon.Volley.MyPostVolley;

import java.util.HashMap;
import java.util.Map;

public class CreateProjectActivity extends AppCompatActivity implements View.OnClickListener,View.OnFocusChangeListener{

    private EditText etProjectName,etCompanyAddress,etSiteLocation;
    private Button btnCreateProject;
    private UserLocalStore userLocalStore;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);
        userLocalStore= new UserLocalStore(this);
        gson= new Gson();

        //InitView and Set Listener
        initViewAndSetListener();
    }

    private void initViewAndSetListener() {
        etProjectName= (EditText) findViewById(R.id.et_project_name);
        etCompanyAddress= (EditText) findViewById(R.id.et_company_address);
        etSiteLocation= (EditText) findViewById(R.id.et_site_location);

        etProjectName.setOnFocusChangeListener(this);
        etCompanyAddress.setOnFocusChangeListener(this);
        etSiteLocation.setOnFocusChangeListener(this);

        btnCreateProject= (Button) findViewById(R.id.button_create_project);
        btnCreateProject.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        String project_name= etProjectName.getText().toString().trim();
        String company_address= etCompanyAddress.getText().toString().trim();
        String site_location= etSiteLocation.getText().toString().trim();

        // Validation for the Fields
        if(TextUtils.isEmpty(project_name)){
            Toast.makeText(CreateProjectActivity.this, "Project Name is Empty", Toast.LENGTH_SHORT).show();
            etProjectName.requestFocus();
            return;

        }

        if(TextUtils.isEmpty(company_address)){
            Toast.makeText(CreateProjectActivity.this, "Company Address is Empty", Toast.LENGTH_SHORT).show();
            etCompanyAddress.requestFocus();
            return;

        }

        if(TextUtils.isEmpty(site_location)){
            Toast.makeText(CreateProjectActivity.this, "Site Location is Empty", Toast.LENGTH_SHORT).show();
            etSiteLocation.requestFocus();
            return;

        }

        insertProject(project_name,company_address,site_location);

    }



    @Override
    public void onFocusChange(View view, boolean b) {

        switch (view.getId()){
            case R.id.et_project_name:
                focusChange(view);
                break;
            case R.id.et_company_address:
                focusChange(view);
                break;
            case R.id.et_site_location:
                focusChange(view);
                break;
        }

    }

    private void focusChange(View view){
        if(view.hasFocus()){
            view.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.edit_text_background,null));
        }else{
            view.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.edit_text_background_deselect,null));
        }
    }

    private void insertProject(String project_name, String company_address, String site_location) {
        Map<String,String> map = new HashMap<>();
        map.put("project_name",project_name);
        map.put("user_id", String.valueOf(userLocalStore.getLoggedInUser().getUser_id()));
        map.put("company_name",userLocalStore.getLoggedInUser().getUser_companyname());
        map.put("company_address",company_address);
        map.put("site_location",site_location);

        MyPostVolley myPostVolley = new MyPostVolley(this,map, URLs.INSERT_PROJECT, Constant.INSERT_PROJECT);
        myPostVolley.applyPostVolley();
        myPostVolley.setVolleyPostListener(new MyPostVolley.VolleyPostListener() {
            @Override
            public void getResposefromVolley(String response) {
                RegisterResponse registerResponse = gson.fromJson(response,RegisterResponse.class);

                if(registerResponse.getSuccess()==1){
                    Intent intent = new Intent(CreateProjectActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    new MyDialog(CreateProjectActivity.this,Constant.CREATE_PROJECT_TITLE,registerResponse.getMessage());
                }

            }
        });
    }
}
