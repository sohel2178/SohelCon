package net.optimusbs.sohelcon.Fragments.MainFragment;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.gson.Gson;


import net.optimusbs.sohelcon.BaseClass.ActivityData;
import net.optimusbs.sohelcon.BaseClass.Project;
import net.optimusbs.sohelcon.BaseClass.User;
import net.optimusbs.sohelcon.ChartAndGraph.MyListener;
import net.optimusbs.sohelcon.Fragments.MainFragment.Activity.GanttChartFragment;
import net.optimusbs.sohelcon.Fragments.MainFragment.Activity.InsertActivityFragment;
import net.optimusbs.sohelcon.Fragments.NoDataFragment;
import net.optimusbs.sohelcon.LoginActivity;
import net.optimusbs.sohelcon.R;
import net.optimusbs.sohelcon.Response.ActivityResponse;
import net.optimusbs.sohelcon.Utility.Constant;
import net.optimusbs.sohelcon.Utility.FieldConstant;
import net.optimusbs.sohelcon.Utility.URLs;
import net.optimusbs.sohelcon.Utility.UserLocalStore;
import net.optimusbs.sohelcon.Volley.MyPostVolley;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewActivityFragment extends Fragment implements View.OnClickListener,MyListener {

    private RelativeLayout chartContainer,addActivityContainer,activityHomeContainer;

    private UserLocalStore userLocalStore;
    private Gson gson;

    private List<ActivityData> activitiesDataList;


    public NewActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gson= new Gson();
        userLocalStore = new UserLocalStore(getActivity());

        activitiesDataList = new ArrayList<>();



        Project currentProject = userLocalStore.getCurrentProject();
        User user = userLocalStore.getLoggedInUser();

        if(currentProject.getProject_id()!=-1){
            int project_id = currentProject.getProject_id();
            String user_id = String.valueOf(user.getUser_id());

            Map<String,String> map = new HashMap<>();

            map.put(FieldConstant.ACTIVITY_USER_ID,user_id);
            map.put(FieldConstant.ACTIVITY_PROJECT_ID, String.valueOf(project_id));

            MyPostVolley myPostVolley = new MyPostVolley(getActivity(),map, URLs.GET_ALL_ACTIVITIES, Constant.GET_ALL_ACTIVITIES);
            myPostVolley.applyPostVolley();
            myPostVolley.setVolleyPostListener(new MyPostVolley.VolleyPostListener() {
                @Override
                public void getResposefromVolley(String response) {
                    Log.d("ActivityResponse",response);

                    ActivityResponse activityResponse = gson.fromJson(response,ActivityResponse.class);



                    if(activityResponse.getSuccess()==1){
                        activitiesDataList.addAll(activityResponse.getActivities());

                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constant.ACTIVITY_LIST, (Serializable) activitiesDataList);

                        ActivityFragment af = new ActivityFragment();
                        // Set Listener
                        af.setMyListener(NewActivityFragment.this);
                        af.setArguments(bundle);

                        getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_top,R.anim.exit_to_bottom)
                                .replace(R.id.activity_main_container,af).commit();
                        chartContainer.setVisibility(View.VISIBLE);
                        activityHomeContainer.setVisibility(View.VISIBLE);

                    }else if(activityResponse.getSuccess()==2){
                        NoDataFragment noDataFragment = new NoDataFragment();
                        Bundle args = new Bundle();
                        args.putString(Constant.TEXT,activityResponse.getMessage());
                        noDataFragment.setArguments(args);
                        getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_top,R.anim.exit_to_bottom)
                                .replace(R.id.activity_main_container,noDataFragment).commit();
                    }else if(activityResponse.getSuccess()==0){
                        //Todo Not Enabled User
                        userLocalStore.setUserLoggedIn(false);
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        getActivity().finish();
                        startActivity(intent);
                    }

                }
            });
        }
    }

    private void showDialog(String message){
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Activity")
                .setContentText(message)
                .show();
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activityHomeContainer.setOnClickListener(this);
        addActivityContainer.setOnClickListener(this);
        chartContainer.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_activity, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){
        chartContainer = (RelativeLayout) view.findViewById(R.id.chart_container);
        addActivityContainer = (RelativeLayout) view.findViewById(R.id.addactivity_container);
        activityHomeContainer = (RelativeLayout) view.findViewById(R.id.activity_home_container);
    }

    @Override
    public void onResume() {
        super.onResume();
        String projectName = userLocalStore.getCurrentProject().getProject_name();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(projectName+"/"+"Activities");

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        if(activitiesDataList.size()>0){
            chartContainer.setVisibility(View.VISIBLE);
            activityHomeContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.ACTIVITY_LIST, (Serializable) activitiesDataList);

        switch (view.getId()){
            case R.id.addactivity_container:
                InsertActivityFragment insertActivityFragment = new InsertActivityFragment();
                insertActivityFragment.setMyListener(this);
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_top,R.anim.exit_to_bottom).replace(R.id.activity_main_container,insertActivityFragment).commit();
                break;

            case R.id.chart_container:
                GanttChartFragment ganttChartFragment = new GanttChartFragment();
                ganttChartFragment.setArguments(bundle);
                ganttChartFragment.setMyListener(this);
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_top,R.anim.exit_to_bottom)
                        .replace(R.id.activity_main_container,ganttChartFragment).commit();
                break;

            case R.id.activity_home_container:
                ActivityFragment af = new ActivityFragment();
                af.setArguments(bundle);
                af.setMyListener(this);
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_top,R.anim.exit_to_bottom)
                        .replace(R.id.activity_main_container,af).commit();
                break;



        }

    }

    @Override
    public void fragmentChange(int fraID) {
        switch (fraID){
            case 1:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setDefaultBackGround();
                    addActivityContainer.setBackground(getActivity().getDrawable(R.drawable.my_selector));
                }
                break;

            case 2:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setDefaultBackGround();
                    chartContainer.setBackground(getActivity().getDrawable(R.drawable.my_selector));
                }
                break;

            case 3:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setDefaultBackGround();
                    activityHomeContainer.setBackground(getActivity().getDrawable(R.drawable.my_selector));
                }
                break;
        }
    }

    private void setDefaultBackGround() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activityHomeContainer.setBackground(getActivity().getDrawable(R.drawable.custom_button_default));
            addActivityContainer.setBackground(getActivity().getDrawable(R.drawable.custom_button_default));
            chartContainer.setBackground(getActivity().getDrawable(R.drawable.custom_button_default));
        }
    }


}
