package net.optimusbs.sohelcon.Fragments.MainFragment.Activity;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;


import net.optimusbs.sohelcon.BaseClass.Project;
import net.optimusbs.sohelcon.ChartAndGraph.MyListener;
import net.optimusbs.sohelcon.CustomView.MyAutoCompleteTextView;
import net.optimusbs.sohelcon.CustomView.MyEditText;
import net.optimusbs.sohelcon.Fragments.MainFragment.NewActivityFragment;
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
public class InsertActivityFragment extends Fragment implements View.OnClickListener{

    private MyEditText etStartDate,etFinishedDate,etVolumeofWorks,etUnitRate;
    private MyAutoCompleteTextView acActivityName;
    private Button btnAdd;
    private TextView title;

    private UserLocalStore userLocalStore;
    private Gson gson;

    String projectStartDate,projectFinishedDate;

    private MyListener listener;


    public InsertActivityFragment() {
        // Required empty public constructor
    }

    public void setMyListener(MyListener listener){
        this.listener =listener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener.fragmentChange(1);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userLocalStore = new UserLocalStore(getActivity());
        gson = new Gson();

        Project project = userLocalStore.getCurrentProject();

        projectStartDate = project.getProject_start_date();
        projectFinishedDate= project.getProject_completion_date();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_insert_activity, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        etStartDate = (MyEditText) view.findViewById(R.id.insert_activity_start_date);
        etFinishedDate = (MyEditText) view.findViewById(R.id.insert_activity_finished_date);
        etVolumeofWorks = (MyEditText) view.findViewById(R.id.insert_activity_volume_of_works);
        etUnitRate = (MyEditText) view.findViewById(R.id.insert_activity_unit_rate);
        acActivityName = (MyAutoCompleteTextView) view.findViewById(R.id.insert_activity_activity_name);
        acActivityName.setThreshold(1);
        title= (TextView) view.findViewById(R.id.title);
        btnAdd = (Button) view.findViewById(R.id.insert_activity_add);

        setFont();


    }

    private void setFont(){
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,etStartDate);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,etFinishedDate);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,etVolumeofWorks);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,etUnitRate);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,acActivityName);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,title);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,btnAdd);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        etStartDate.setOnClickListener(this);
        etFinishedDate.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.insert_activity_start_date:
                MyUtils.showDialogAndSetTime(getActivity(),etStartDate);
                break;

            case R.id.insert_activity_finished_date:
                MyUtils.showDialogAndSetTime(getActivity(),etFinishedDate);
                break;

            case R.id.insert_activity_add:

                String start_date = etStartDate.getText().toString().trim();
                String finished_date = etFinishedDate.getText().toString().trim();
                String activity_name = acActivityName.getText().toString().trim();
                String volume_of_works = etVolumeofWorks.getText().toString().trim();
                String unit_rate = etUnitRate.getText().toString().trim();

                Date startDateOfProject = MyUtils.getDateFromString(projectStartDate);
                Date completionDateOfProject = MyUtils.getDateFromString(projectFinishedDate);

                Date stDate = MyUtils.getDateFromString(start_date);
                Date finDate = MyUtils.getDateFromString(finished_date);

                if(TextUtils.isEmpty(start_date)){
                    etStartDate.requestFocus();
                    Toast.makeText(getActivity(), "Start Date Field is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!(stDate.after(startDateOfProject) && stDate.before(completionDateOfProject))){
                    etStartDate.requestFocus();
                    Toast.makeText(getActivity(), "Start Date is Before OR Project Start Date", Toast.LENGTH_SHORT).show();
                    return;
                }


                if(!MyUtils.isThisDateValid(start_date,"dd-MM-yyyy")){
                    etStartDate.requestFocus();
                    Toast.makeText(getActivity(), "Start Date is not Valid", Toast.LENGTH_SHORT).show();
                    return;
                }



                if(TextUtils.isEmpty(finished_date)){
                    etFinishedDate.requestFocus();
                    Toast.makeText(getActivity(), "Start Date Field is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!(finDate.after(startDateOfProject) && finDate.before(completionDateOfProject))){
                    etFinishedDate.requestFocus();
                    Toast.makeText(getActivity(), "Finished Date is Before OR Project Start Date", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!MyUtils.isThisDateValid(finished_date,"dd-MM-yyyy")){
                    etFinishedDate.requestFocus();
                    Toast.makeText(getActivity(), "Start Date is not Valid", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(activity_name)){
                    acActivityName.requestFocus();
                    Toast.makeText(getActivity(), "Activity Name Field is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(volume_of_works)){
                    etVolumeofWorks.requestFocus();
                    Toast.makeText(getActivity(), "Volume of Works Field is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(Double.parseDouble(volume_of_works)==0){
                    etVolumeofWorks.requestFocus();
                    Toast.makeText(getActivity(), "Volume of works is 0.00", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(unit_rate)){
                    etUnitRate.requestFocus();
                    Toast.makeText(getActivity(), "Unit Rate Field is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                Project currentProject = userLocalStore.getCurrentProject();

                int project_id = currentProject.getProject_id();
                String user_id = String.valueOf(userLocalStore.getLoggedInUser().getUser_id());


                Map<String,String> map =new HashMap<>();
                map.put(FieldConstant.ACTIVITY_PROJECT_ID, String.valueOf(project_id));
                map.put(FieldConstant.ACTIVITY_USER_ID,user_id);
                map.put(FieldConstant.ACTIVITY_START_DATE,start_date);
                map.put(FieldConstant.ACTIVITY_FINISHED_DATE,finished_date);
                map.put(FieldConstant.ACTIVITY_NAME,activity_name);
                map.put(FieldConstant.ACTIVITY_VOLUME_OF_WORKS,volume_of_works);
                map.put(FieldConstant.ACTIVITY_UNIT_RATE,unit_rate);

                MyPostVolley postVolley = new MyPostVolley(getActivity(),map, URLs.INSERT_ACTIVITY, Constant.INSERT_ACTIVITY);
                postVolley.applyPostVolley();

                postVolley.setVolleyPostListener(new MyPostVolley.VolleyPostListener() {
                    @Override
                    public void getResposefromVolley(String response) {
                        RegisterResponse rr = gson.fromJson(response,RegisterResponse.class);

                        if(rr.getSuccess()==1){
                            getFragmentManager().popBackStack();
                            getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right)
                                    .replace(R.id.main_container,new NewActivityFragment()).commit();
                        }
                    }
                });



                break;
        }
    }
}
