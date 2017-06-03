package com.cpsdbd.sohelcon.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;


import com.cpsdbd.sohelcon.BaseClass.Project;
import com.cpsdbd.sohelcon.LoginActivity;
import com.cpsdbd.sohelcon.R;
import com.cpsdbd.sohelcon.Response.RegisterResponse;
import com.cpsdbd.sohelcon.Utility.Constant;
import com.cpsdbd.sohelcon.Utility.FieldConstant;
import com.cpsdbd.sohelcon.Utility.FontUtils;
import com.cpsdbd.sohelcon.Utility.MyUtils;
import com.cpsdbd.sohelcon.Utility.URLs;
import com.cpsdbd.sohelcon.Utility.UserLocalStore;
import com.cpsdbd.sohelcon.Volley.MyPostVolley;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateProjectFragment extends Fragment implements View.OnClickListener {

    private EditText etProjectName,etDescription,etSiteLocation,etStartDate,etFinishedDate;
    private TextView tvProjectName,tvCompanyAddress,tvSiteLocation,tvStartDate,tvFinishedDate;
    private Button btnUpdate;

    private Project currentProject;

    private TextView tvFormTitle;
    private UserLocalStore userLocalStore;
    private Gson gson;
    private int user_id;



    public UpdateProjectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userLocalStore = new UserLocalStore(getActivity());
        user_id = userLocalStore.getLoggedInUser().getUser_id();

        if(getArguments()!= null){
            currentProject = (Project) getArguments().getSerializable(Constant.PROJECT);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Update "+currentProject.getProject_name());
            gson= new Gson();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_project, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        etProjectName = (EditText) view.findViewById(R.id.et_create_project_name);
        etDescription = (EditText) view.findViewById(R.id.et_create_project_description);
        etSiteLocation = (EditText) view.findViewById(R.id.et_create_project_location);
        etStartDate = (EditText) view.findViewById(R.id.et_create_project_start_date);
        etFinishedDate = (EditText) view.findViewById(R.id.et_create_project_completion_date);

        tvFormTitle = (TextView) view.findViewById(R.id.project_form_title);

        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvFormTitle);

        btnUpdate= (Button) view.findViewById(R.id.button_create_project_update);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        etStartDate.setOnClickListener(this);
        etFinishedDate.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
    }


    @Override
    public void onStart() {
        super.onStart();

        String projectName = currentProject.getProject_name();
        String startDate = currentProject.getProject_start_date();
        String completionDate = currentProject.getProject_completion_date();
        String description = currentProject.getProject_desceiption();
        String location = currentProject.getProject_location();

        etProjectName.setText(projectName);
        etDescription.setText(description);
        etSiteLocation.setText(location);
        etStartDate.setText(startDate);
        etFinishedDate.setText(completionDate);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.et_create_project_start_date:
                MyUtils.showDialogAndSetTime(getActivity(),etStartDate);
                break;

            case R.id.et_create_project_completion_date:
                MyUtils.showDialogAndSetTime(getActivity(),etFinishedDate);
                break;

            case R.id.button_create_project_update:
                String project_name= etProjectName.getText().toString().trim();
                String project_description= etDescription.getText().toString().trim();
                String project_location= etSiteLocation.getText().toString().trim();
                String project_start_date= etStartDate.getText().toString().trim();
                String project_completion_date= etFinishedDate.getText().toString().trim();





                if(TextUtils.isEmpty(project_name)){
                    Toast.makeText(getActivity(), "Project Name is Empty", Toast.LENGTH_SHORT).show();
                    etProjectName.requestFocus();
                    return;

                }

                if(TextUtils.isEmpty(project_description)){
                    Toast.makeText(getActivity(), "Project Description is Empty", Toast.LENGTH_SHORT).show();
                    etDescription.requestFocus();
                    return;

                }

                if(TextUtils.isEmpty(project_location)){
                    Toast.makeText(getActivity(), "Project Location is Empty", Toast.LENGTH_SHORT).show();
                    etSiteLocation.requestFocus();
                    return;

                }

                if(TextUtils.isEmpty(project_start_date)){
                    Toast.makeText(getActivity(), "Project Start Date is Empty", Toast.LENGTH_SHORT).show();
                    etStartDate.requestFocus();
                    return;

                }

                if(TextUtils.isEmpty(project_completion_date)){
                    Toast.makeText(getActivity(), "Project Completion Date is Empty", Toast.LENGTH_SHORT).show();
                    etFinishedDate.requestFocus();
                    return;

                }

                updateProject(user_id,currentProject.getProject_id(),project_name,project_description,project_location,project_start_date,project_completion_date);


                break;
        }
    }

    private void updateProject(int user_id, int project_id, String project_name, String project_description,
                               String project_location, String project_start_date, String project_completion_date) {

        Map<String,String> map = new HashMap<>();
        map.put("user_id", String.valueOf(user_id));
        map.put("project_id", String.valueOf(project_id));
        map.put(FieldConstant.PROJECT_NAME,project_name);
        map.put(FieldConstant.PROJECT_DESCEIPTION,project_description);
        map.put(FieldConstant.PROJECT_LOCATION,project_location);
        map.put(FieldConstant.PROJECT_START_DATE,project_start_date);
        map.put(FieldConstant.PROJECT_COMPLETION_DATE,project_completion_date);

        MyPostVolley myPostVolley = new MyPostVolley(getActivity(),map, URLs.UPDATE_PROJECT,Constant.UPDATE_PROJECT);
        myPostVolley.applyPostVolley();
        myPostVolley.setVolleyPostListener(new MyPostVolley.VolleyPostListener() {
            @Override
            public void getResposefromVolley(String response) {
                RegisterResponse rr = gson.fromJson(response,RegisterResponse.class);

                if(rr.getSuccess()==1){
                    Toast.makeText(getActivity(), rr.getMessage(), Toast.LENGTH_SHORT).show();
                    getFragmentManager().popBackStack();
                    getFragmentManager().beginTransaction().replace(R.id.main_container,new DashBoardFragment()).commit();
                }else if(rr.getSuccess()==0){
                    userLocalStore.clearUserData();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }else{

                }
            }
        });

    }


}
