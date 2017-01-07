package net.optimusbs.sohelcon.Navigation;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;


import net.optimusbs.sohelcon.Fragments.DashBoardFragment;
import net.optimusbs.sohelcon.R;
import net.optimusbs.sohelcon.Response.RegisterResponse;
import net.optimusbs.sohelcon.Utility.Constant;
import net.optimusbs.sohelcon.Utility.FieldConstant;
import net.optimusbs.sohelcon.Utility.FontUtils;
import net.optimusbs.sohelcon.Utility.MyUtils;
import net.optimusbs.sohelcon.Utility.URLs;
import net.optimusbs.sohelcon.Utility.UserLocalStore;
import net.optimusbs.sohelcon.Volley.MyPostVolley;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateProjectFragment extends Fragment implements View.OnClickListener{

    private EditText etProjectName,etDescription,etSiteLocation,etStartDate,etCompletionDate;
    private TextView tvFormTitle;
    private Button btnCreateProject;
    private UserLocalStore userLocalStore;
    private Gson gson;
    private String user_id;


    public CreateProjectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userLocalStore = new UserLocalStore(getActivity());
        user_id = String.valueOf(userLocalStore.getLoggedInUser().getUser_id());

        gson = new Gson();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_project, container, false);

        initView(view);

        return view;
    }


    private void initView(View view){
        etProjectName= (EditText) view.findViewById(R.id.et_create_project_name);
        etDescription= (EditText) view.findViewById(R.id.et_create_project_description);
        etSiteLocation= (EditText) view.findViewById(R.id.et_create_project_location);
        etStartDate= (EditText) view.findViewById(R.id.et_create_project_start_date);
        etCompletionDate= (EditText) view.findViewById(R.id.et_create_project_completion_date);

        tvFormTitle= (TextView) view.findViewById(R.id.project_form_title);

        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvFormTitle);

        btnCreateProject = (Button) view.findViewById(R.id.button_create_project_create);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        etStartDate.setOnClickListener(this);
        etCompletionDate.setOnClickListener(this);
        btnCreateProject.setOnClickListener(this);


    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Create Project");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.et_create_project_start_date:
                MyUtils.showDialogAndSetTime(getActivity(),etStartDate);
                break;

            case R.id.et_create_project_completion_date:
                MyUtils.showDialogAndSetTime(getActivity(),etCompletionDate);
                break;

            case R.id.button_create_project_create:
                String project_name= etProjectName.getText().toString().trim();
                String project_description= etDescription.getText().toString().trim();
                String project_location= etSiteLocation.getText().toString().trim();
                String project_start_date= etStartDate.getText().toString().trim();
                String project_completion_date= etCompletionDate.getText().toString().trim();

                Date date = new Date();
                String dateString = MyUtils.dateToString(date);


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
                    etCompletionDate.requestFocus();
                    return;

                }

                insertProject(user_id,dateString,project_name,project_description,project_location,project_start_date,project_completion_date);


                break;
        }
    }

    private void insertProject(String user_id, String dateString, String project_name, String project_description,
                               String project_location, String project_start_date, String project_completion_date) {
        Map<String,String> map = new HashMap<>();
        map.put(FieldConstant.USER_ID,user_id);
        map.put(FieldConstant.PROJECT_CREATE_DATE,dateString);
        map.put(FieldConstant.PROJECT_NAME,project_name);
        map.put(FieldConstant.PROJECT_DESCEIPTION,project_description);
        map.put(FieldConstant.PROJECT_LOCATION,project_location);
        map.put(FieldConstant.PROJECT_START_DATE,project_start_date);
        map.put(FieldConstant.PROJECT_COMPLETION_DATE,project_completion_date);

        MyPostVolley myPostVolley = new MyPostVolley(getActivity(),map, URLs.INSERT_PROJECT,Constant.INSERT_PROJECT);
        myPostVolley.applyPostVolley();
        myPostVolley.setVolleyPostListener(new MyPostVolley.VolleyPostListener() {
            @Override
            public void getResposefromVolley(String response) {
                Log.d("PRES",response);
                RegisterResponse rr = gson.fromJson(response,RegisterResponse.class);

                if(rr.getSuccess()==1){
                    Toast.makeText(getActivity(), rr.getMessage(), Toast.LENGTH_SHORT).show();
                    getFragmentManager().popBackStack();
                    getFragmentManager().beginTransaction().replace(R.id.main_container,new DashBoardFragment()).commit();
                }
            }
        });
    }
}
