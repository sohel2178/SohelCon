package net.optimusbs.sohelcon;

import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import net.optimusbs.sohelcon.Response.RegisterResponse;
import net.optimusbs.sohelcon.Utility.Constant;
import net.optimusbs.sohelcon.Utility.FieldConstant;
import net.optimusbs.sohelcon.Utility.FontUtils;
import net.optimusbs.sohelcon.Utility.MyAlertDialog;
import net.optimusbs.sohelcon.Utility.MyUtils;
import net.optimusbs.sohelcon.Utility.URLs;
import net.optimusbs.sohelcon.Volley.MyPostVolley;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener,View.OnFocusChangeListener,MyPostVolley.VolleyPostListener{

    private EditText etUserName,etPassword,etConfirmPassword,etEmail,etCompanyName,etDesignation;
    private Button btnRegister,btnLogin;
    private TextView tvTitle;

    private Gson gson;

    // User Field
    private String user_name;
    private String password;
    private String confirmPassword;
    private String email;
    private String company_name;
    private String designation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Init Gson Object
        gson = new Gson();

        //Init View and ClickListener
        initViewandClickListener();
    }

    private void initViewandClickListener() {
        etUserName= (EditText) findViewById(R.id.et_register_user_name);
        etPassword= (EditText) findViewById(R.id.et_register_password);
        etConfirmPassword= (EditText) findViewById(R.id.et_register_confirm_password);
        etEmail= (EditText) findViewById(R.id.et_register_email);
        etCompanyName= (EditText) findViewById(R.id.et_register_company_name);
        etDesignation= (EditText) findViewById(R.id.et_register_designation);
        btnRegister= (Button) findViewById(R.id.button_register_register);
        btnLogin= (Button) findViewById(R.id.button_register_login);
        tvTitle = (TextView) findViewById(R.id.registration_form_title);

        setFontTypeface();

        etUserName.setOnFocusChangeListener(this);
        etPassword.setOnFocusChangeListener(this);
        etConfirmPassword.setOnFocusChangeListener(this);
        etEmail.setOnFocusChangeListener(this);
        etCompanyName.setOnFocusChangeListener(this);
        etDesignation.setOnFocusChangeListener(this);


        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    private void setFontTypeface() {
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,etUserName);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,etEmail);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,etCompanyName);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,etDesignation);

        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,btnRegister);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,btnLogin);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvTitle);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.button_register_login:
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.button_register_register:

                user_name =etUserName.getText().toString().trim();
                password =etPassword.getText().toString().trim();
                confirmPassword =etConfirmPassword.getText().toString().trim();
                email =etEmail.getText().toString().trim();
                company_name =etCompanyName.getText().toString().trim();
                designation =etDesignation.getText().toString().trim();


                //Todo Validation of Fields


                if(TextUtils.isEmpty(user_name)){
                    etUserName.requestFocus();
                    Toast.makeText(this, "User Name is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    etPassword.requestFocus();
                    Toast.makeText(this, "Password Field is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password.length()<6){
                    etPassword.requestFocus();
                    Toast.makeText(this, "Password at Least six characters/digit", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!password.equals(confirmPassword)){
                    etConfirmPassword.requestFocus();
                    Toast.makeText(this, "Password Does not Match", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(email)){
                    etEmail.requestFocus();
                    Toast.makeText(this, "Email Field is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    if(!MyUtils.validateEmail(email)){
                        //Log.d("Test","Test");
                        etEmail.requestFocus();
                        Toast.makeText(RegisterActivity.this, "Please Enter an Valid Email Address", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }


                registerUser(user_name,password,email,company_name,designation);

                break;
        }




    }

    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()){

            case R.id.et_register_user_name:
                focusChange(view);
                break;
            case R.id.et_register_password:
                focusChange(view);
                break;
            case R.id.et_register_email:
                focusChange(view);
                break;
            case R.id.et_register_company_name:
                focusChange(view);
                break;
            case R.id.et_register_designation:
                focusChange(view);
                break;
        }
    }


    private void focusChange(View view){
        if(view.hasFocus()){
            view.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.edit_text_background,null));
        }else{
            view.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.my_edit_text_back,null));
        }
    }

    private void registerUser(String user_name,String password,String email,String company_name,String designation
    ){

        Map<String,String> registerMap = new HashMap<>();
        registerMap.put(FieldConstant.USER_NAME,user_name);
        registerMap.put(FieldConstant.USER_PASSWORD,password);
        registerMap.put(FieldConstant.USER_EMAIL,email);
        registerMap.put(FieldConstant.USER_COMPANYNAME,company_name);
        registerMap.put(FieldConstant.USER_DESIGNATION,designation);
        registerMap.put(FieldConstant.USER_STATUS,String.valueOf(1));
        registerMap.put(FieldConstant.USER_TYPE,String.valueOf(1));


        MyPostVolley myPostVolly = new MyPostVolley(this,registerMap, URLs.REGISTER_USER, Constant.REGISTERING_USER);
        myPostVolly.setVolleyPostListener(this);
        myPostVolly.applyPostVolley();

    }

    @Override
    public void getResposefromVolley(String response) {

        RegisterResponse registerResponse = gson.fromJson(response,RegisterResponse.class);
        if(registerResponse.getSuccess()==1){

            Intent intent = new Intent(this,HomeActivity.class);
            startActivity(intent);
            finish();
        }else{
            MyAlertDialog myAlertDialog = new MyAlertDialog(this);
            myAlertDialog.showSimpleDialog("Register User",registerResponse.getMessage());

        }

    }
}
