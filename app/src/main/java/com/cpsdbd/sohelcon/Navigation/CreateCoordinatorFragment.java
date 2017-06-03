package com.cpsdbd.sohelcon.Navigation;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;


import com.cpsdbd.sohelcon.Fragments.DashBoardFragment;
import com.cpsdbd.sohelcon.R;
import com.cpsdbd.sohelcon.Response.RegisterResponse;
import com.cpsdbd.sohelcon.Utility.Constant;
import com.cpsdbd.sohelcon.Utility.FieldConstant;
import com.cpsdbd.sohelcon.Utility.FontUtils;
import com.cpsdbd.sohelcon.Utility.MyAlertDialog;
import com.cpsdbd.sohelcon.Utility.URLs;
import com.cpsdbd.sohelcon.Utility.UserLocalStore;
import com.cpsdbd.sohelcon.Volley.MyPostVolley;

import java.util.HashMap;
import java.util.Map;



/**
 * A simple {@link Fragment} subclass.
 */
public class CreateCoordinatorFragment extends Fragment implements View.OnClickListener{

    private TextView tvFormTitle;
    private EditText etUserName,etPassword,etConfirmPassword;
    private Button btnCreate;

    private LinearLayout llAccess_container;
    private Spinner spinner;

    private UserLocalStore userLocalStore;
    private Gson gson;

    private int user_type;
    private String company_name;
    private int parent_id;

    private ArrayAdapter adapter;
    private String[] accessList = {"Activity","Finance","Store"};


    public CreateCoordinatorFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userLocalStore = new UserLocalStore(getActivity());
        gson = new Gson();

        user_type = userLocalStore.getLoggedInUser().getUser_type();
        company_name= userLocalStore.getLoggedInUser().getUser_companyname();
        parent_id= userLocalStore.getLoggedInUser().getUser_id();

        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line,accessList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_create_coordinator, container, false);
        initView(view);

        if(user_type==1){
           tvFormTitle.setText(Constant.COORDINATOR_CREATE_TITLE);
        }else if(user_type==2){
            tvFormTitle.setText(Constant.MANAGER_CREATE_TITLE);
        }else if(user_type==3){
            tvFormTitle.setText(Constant.CHILD_CREATE_TITLE);
        }

        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvFormTitle);

        return view;
    }

    private void initView(View view) {
        tvFormTitle = (TextView) view.findViewById(R.id.form_title);

        etUserName = (EditText) view.findViewById(R.id.et_create_user_name);
        etPassword = (EditText) view.findViewById(R.id.et_create_password);
        etConfirmPassword = (EditText) view.findViewById(R.id.et_create_confirmpassword);

        llAccess_container = (LinearLayout) view.findViewById(R.id.access_container);
        spinner = (Spinner) view.findViewById(R.id.user_access);

        if(user_type==3){
            llAccess_container.setVisibility(View.VISIBLE);
            spinner.setAdapter(adapter);
        }

        btnCreate = (Button) view.findViewById(R.id.button_create_child_create);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(user_type==1){
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Create Co-ordinator");
        }else if(user_type==2){
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Create Manager");
        }else if(user_type==3){
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Create Child");
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnCreate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        String user_name =etUserName.getText().toString().trim();
        String password =etPassword.getText().toString().trim();
        String confirm_password =etConfirmPassword.getText().toString().trim();

        if(TextUtils.isEmpty(user_name)){
            etUserName.requestFocus();
            Toast.makeText(getActivity(), "User Name is Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            etPassword.requestFocus();
            Toast.makeText(getActivity(), "Password Field is Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.length()<6){
            etPassword.requestFocus();
            Toast.makeText(getActivity(), "Password at least Six character or digit", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(confirm_password)){
            etConfirmPassword.requestFocus();
            Toast.makeText(getActivity(), "Confirm Password Field is Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!password.equals(confirm_password)){
            etConfirmPassword.requestFocus();
            Toast.makeText(getActivity(), "Password Does not Match", Toast.LENGTH_SHORT).show();
            return;
        }

        int newUserType = user_type+1;
        int accessInt=0;

        if(user_type==3){
            accessInt = spinner.getSelectedItemPosition()+1;
        }

        registerUser(user_name,password,company_name,parent_id,newUserType,accessInt);


    }

    private void registerUser(String user_name, String password, String company_name, int parent_id, int user_type, int accessInt) {
        Map<String,String> map = new HashMap<>();
        map.put(FieldConstant.USER_NAME,user_name);
        map.put(FieldConstant.USER_PASSWORD,password);
        map.put(FieldConstant.USER_COMPANYNAME,company_name);
        map.put(FieldConstant.PARENT_ID, String.valueOf(parent_id));
        map.put(FieldConstant.USER_TYPE, String.valueOf(user_type));
        map.put(FieldConstant.USER_ACCESS, String.valueOf(accessInt));

        MyPostVolley myPOstVolley = new MyPostVolley(getActivity(),map, URLs.CREATE_CHILD,Constant.REGISTERING_USER);
        myPOstVolley.applyPostVolley();
        myPOstVolley.setVolleyPostListener(new MyPostVolley.VolleyPostListener() {
            @Override
            public void getResposefromVolley(String response) {
                Log.d("RERRRR",response);
                RegisterResponse registerResponse = gson.fromJson(response,RegisterResponse.class);
                if(registerResponse.getSuccess()==1){

                    getFragmentManager().popBackStack();
                    getFragmentManager().beginTransaction().replace(R.id.main_container,new DashBoardFragment()).commit();
                }else{
                    MyAlertDialog myAlertDialog = new MyAlertDialog(getActivity());
                    myAlertDialog.showSimpleDialog("Register User",registerResponse.getMessage());

                }

            }
        });
    }
}
