package net.optimusbs.sohelcon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import net.optimusbs.sohelcon.BaseClass.User;
import net.optimusbs.sohelcon.Response.LoginResponse;
import net.optimusbs.sohelcon.Utility.Constant;
import net.optimusbs.sohelcon.Utility.FieldConstant;
import net.optimusbs.sohelcon.Utility.FontUtils;
import net.optimusbs.sohelcon.Utility.MyDialog;
import net.optimusbs.sohelcon.Utility.URLs;
import net.optimusbs.sohelcon.Utility.UserLocalStore;
import net.optimusbs.sohelcon.Volley.MyPostVolley;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,MyPostVolley.VolleyPostListener{

    private EditText etUserName,etPassword;
    private Button btnLogin,btnRegister;
    private UserLocalStore userLocalStore;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userLocalStore= new UserLocalStore(this);
        gson= new Gson();

        //Init View
        initView();



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_register:
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                break;

            case R.id.button_login:

                String user_name=etUserName.getText().toString().trim();
                String password=etPassword.getText().toString().trim();

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

                loginUser(user_name,password);

                break;
        }


    }

    private void initView(){
        etUserName = (EditText) findViewById(R.id.login_user_name);
        etPassword = (EditText) findViewById(R.id.login_password);
        btnRegister = (Button) findViewById(R.id.button_register);
        btnLogin = (Button) findViewById(R.id.button_login);

        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,btnLogin);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,btnRegister);
    }

    private void loginUser(String user_name,String password){

        Map<String,String> loginMap = new HashMap<>();
        loginMap.put(FieldConstant.USER_NAME,user_name);
        loginMap.put(FieldConstant.USER_PASSWORD,password);

        MyPostVolley myPostVolley = new MyPostVolley(this,loginMap, URLs.LOGIN_USER,Constant.LOGIN_USER);
        myPostVolley.setVolleyPostListener(this);
        myPostVolley.applyPostVolley();

    }


    @Override
    public void getResposefromVolley(String response) {
        Log.d("RESPONSE",response);

        LoginResponse loginResponse = gson.fromJson(response,LoginResponse.class);

       // Log.d("RESPONSE",response);



        if(loginResponse.getSuccess()==1){
            User user = loginResponse.getUser();



            userLocalStore.clearUserData();

            userLocalStore.storeUserData(user);
            userLocalStore.setUserLoggedIn(true);


            Intent intent = new Intent(this,HomeActivity.class);
            startActivity(intent);
            finish();
        }else{
            new MyDialog(this, Constant.LOGIN_TITLE,loginResponse.getMessage());
        }


    }
}
