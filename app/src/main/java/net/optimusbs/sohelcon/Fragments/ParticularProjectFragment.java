package net.optimusbs.sohelcon.Fragments;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cardiomood.android.controls.gauge.SpeedometerGauge;
import com.google.gson.Gson;

import com.squareup.picasso.Picasso;

import net.optimusbs.sohelcon.BaseClass.Project;
import net.optimusbs.sohelcon.BaseClass.User;
import net.optimusbs.sohelcon.Fragments.MainFragment.NewActivityFragment;
import net.optimusbs.sohelcon.Fragments.MainFragment.NewFinanceFragment;
import net.optimusbs.sohelcon.Fragments.MainFragment.NewStoreFragment;
import net.optimusbs.sohelcon.Navigation.ViewUserProfile;
import net.optimusbs.sohelcon.R;
import net.optimusbs.sohelcon.Response.UserResponse;
import net.optimusbs.sohelcon.Utility.Constant;
import net.optimusbs.sohelcon.Utility.FontUtils;
import net.optimusbs.sohelcon.Utility.MyDialog;
import net.optimusbs.sohelcon.Utility.URLs;
import net.optimusbs.sohelcon.Utility.UserLocalStore;
import net.optimusbs.sohelcon.Volley.MyPostVolley;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ParticularProjectFragment extends Fragment implements View.OnClickListener{

    private static final int DEFAULT_LABEL_TEXT_SIZE_DP=10;



    private Project currentProject;


    private RelativeLayout tvActivity,tvFinance,tvStore;

    private RelativeLayout rlContainer;
    //TextView tvCompanyName,tvCompanyAddress;

    private TextView tvProjectName,tvProjectLocation,tvCoordinatorName,tvManagerName,
                tvProjectValue,tvProjectDebit,tvProjectCredit,tvPhtsicalProgress,tvFinancialProgress,
                coordinator,manager,physical_progress,financial_progress,value,debit,credit;
    CircleImageView ivCoordinatorImage,ivManagerImage;

    SpeedometerGauge gaugePhysical,gaugeFinance;


    private List<User> listOfChilds;
    private Gson gson;


    private double physicalProgress,financialProgress,projectValue,projectDebit,projectCredit;

    private String companyName,companyAddress,projectName,projectLocation,coordinatorName,managerName,coordinatorImage,managerImage;





    private List<Fragment> fragmentList;
    private List<String> titles;

    // Save Current Project in User Local Store
    private UserLocalStore userLocalStore;


    private int user_type;
    private int user_access;

    public ParticularProjectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Todo Configure Next
       // HomeActivity.button.setVisibility(View.VISIBLE);

        userLocalStore= new UserLocalStore(getActivity());

        User user = userLocalStore.getLoggedInUser();
        // init 3 Fields
        user_type= user.getUser_type();
        user_access=user.getUser_access();


        if(getArguments()!=null){

            currentProject= (Project) getArguments().getSerializable(Constant.PROJECT);


            userLocalStore.setCurrentProject(currentProject);
            gson= new Gson();

        }else{

            currentProject=userLocalStore.getCurrentProject();

            if(currentProject.getProject_id()==-1){
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("You Have No Project Yet");
                new MyDialog(getActivity(), "Project Status","You Have not Yet Assigned Any Project");

            }
        }

        physicalProgress = currentProject.getProject_physical_progress();
        financialProgress = currentProject.getProject_financial_progress();
        projectValue = currentProject.getProject_total_cost();
        projectDebit = currentProject.getProject_expenditure();
        projectCredit = currentProject.getProject_debit();

        companyName = user.getUser_companyname();
        companyAddress=user.getUser_address();
        projectName=currentProject.getProject_name();
        projectLocation=currentProject.getProject_location();
        coordinatorName=currentProject.getProject_coordinator_name();
        managerName=currentProject.getProject_manager_name();
        managerImage=currentProject.getProject_manager_photo();
        coordinatorImage=currentProject.getProject_coordinator_photo();


    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_particular_project, container, false);
        initView(view);

        setUpSpeedometer(gaugePhysical,physicalProgress*100);
        setUpSpeedometer(gaugeFinance,financialProgress*100);

        if(userLocalStore.getCurrentProject().getProject_id()==-1){
            rlContainer.setVisibility(View.GONE);
        }

        if(user_type==4){
            switch (user_access){
                case 1:
                    tvFinance.setVisibility(View.GONE);
                    tvStore.setVisibility(View.GONE);
                    break;

                case 2:
                    tvActivity.setVisibility(View.GONE);
                    tvStore.setVisibility(View.GONE);
                    break;

                case 3:
                    tvFinance.setVisibility(View.GONE);
                    tvActivity.setVisibility(View.GONE);
                    break;
            }
        }
        return view;
    }






    private void initView(View view){
        // Container
        rlContainer = (RelativeLayout) view.findViewById(R.id.whole_container);


        // ToolBar Project Control View
        tvActivity= (RelativeLayout) view.findViewById(R.id.activity_container);
        tvFinance= (RelativeLayout) view.findViewById(R.id.finance_container);
        tvStore= (RelativeLayout) view.findViewById(R.id.store_container);

        // Project Content Initialize
        tvProjectName= (TextView) view.findViewById(R.id.dashboard_project_name);
        tvProjectLocation= (TextView) view.findViewById(R.id.dashboard_project_location);
        tvCoordinatorName= (TextView) view.findViewById(R.id.dashboard_coordinator_name);
        tvManagerName= (TextView) view.findViewById(R.id.dashboard_manager_name);
        tvProjectValue= (TextView) view.findViewById(R.id.dashboard_project_value);
        tvProjectDebit= (TextView) view.findViewById(R.id.dashboard_project_debit);
        tvProjectCredit= (TextView) view.findViewById(R.id.dashboard_project_credit);
        tvPhtsicalProgress= (TextView) view.findViewById(R.id.dashboard_physical_progress);
        tvFinancialProgress= (TextView) view.findViewById(R.id.dashboard_financial_progress);

        //,,,,,,

        coordinator= (TextView) view.findViewById(R.id.co_ordinator);
        manager= (TextView) view.findViewById(R.id.manager);
        physical_progress= (TextView) view.findViewById(R.id.physical_progress);
        financial_progress= (TextView) view.findViewById(R.id.financial_progress);
        value= (TextView) view.findViewById(R.id.value);
        debit= (TextView) view.findViewById(R.id.debit);
        credit= (TextView) view.findViewById(R.id.credit);

        // Set Font
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,coordinator);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,manager);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,physical_progress);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,financial_progress);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,value);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,debit);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,credit);

        // Set The Value of TextView
        final DecimalFormat df = new DecimalFormat("#0.0");


        ValueAnimator animation = ValueAnimator.ofFloat(0f,1f);
        animation.setDuration(1000);
        animation.start();
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedProjectValue = (float) (projectValue*valueAnimator.getAnimatedFraction());
                float animatedDebitValue = (float) (projectDebit*valueAnimator.getAnimatedFraction());
                float animatedCreditValue = (float) (projectCredit*valueAnimator.getAnimatedFraction());
                float animatedPhysicalProgress= (float) (physicalProgress*100*valueAnimator.getAnimatedFraction());
                float animatedFinancialProgress = (float) (financialProgress*100*valueAnimator.getAnimatedFraction());

                tvProjectValue.setText(df.format(animatedProjectValue));
                tvProjectDebit.setText(df.format(animatedDebitValue));
                tvProjectCredit.setText(df.format(animatedCreditValue));
                tvPhtsicalProgress.setText(df.format(animatedPhysicalProgress));
                tvFinancialProgress.setText(df.format(animatedFinancialProgress));
            }
        });
        //Set Font
        FontUtils.setFont(Constant.T_SEVEN_SEGMENT,tvPhtsicalProgress);
        FontUtils.setFont(Constant.T_SEVEN_SEGMENT,tvFinancialProgress);
        FontUtils.setFont(Constant.T_SEVEN_SEGMENT,tvProjectValue);
        FontUtils.setFont(Constant.T_SEVEN_SEGMENT,tvProjectDebit);
        FontUtils.setFont(Constant.T_SEVEN_SEGMENT,tvProjectCredit);

        //tvCompanyName.setText(companyName);
        //tvCompanyAddress.setText(companyAddress);
        tvProjectName.setText(projectName);
        tvProjectLocation.setText(projectLocation);
        tvCoordinatorName.setText(coordinatorName);
        tvManagerName.setText(managerName);

        // Set Font
        //FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvCompanyName);
       // FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvCompanyAddress);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvProjectName);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvProjectLocation);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvCoordinatorName);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvManagerName);

        ivCoordinatorImage = (CircleImageView) view.findViewById(R.id.dashboard_coordinator_image);
        ivManagerImage = (CircleImageView) view.findViewById(R.id.dashboard_manager_image);

        // ProfileImage

        if(managerImage.equals("")){
            ivManagerImage.setImageResource(R.drawable.placeholder);
        }else{
            Picasso.with(getActivity())
                    .load(managerImage)
                    .placeholder(R.drawable.placeholder)
                    .into(ivManagerImage);
        }

        if(coordinatorImage.equals("")){
            ivCoordinatorImage.setImageResource(R.drawable.placeholder);
        }else{
            Picasso.with(getActivity())
                    .load(coordinatorImage)
                    .placeholder(R.drawable.placeholder)
                    .into(ivCoordinatorImage);
        }

        gaugePhysical = (SpeedometerGauge) view.findViewById(R.id.physical_progress_speedometer);
        gaugeFinance = (SpeedometerGauge) view.findViewById(R.id.financial_progress_speedometer);

    }


    // Fab Button Click
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        //toolbarControl.setOnClickListener(this);

        tvActivity.setOnClickListener(this);
        tvFinance.setOnClickListener(this);
        tvStore.setOnClickListener(this);
        ivCoordinatorImage.setOnClickListener(this);
        ivManagerImage.setOnClickListener(this);

    }

    private void setUpSpeedometer(SpeedometerGauge speedometer,double progress){

        speedometer.setLabelConverter(new SpeedometerGauge.LabelConverter() {
            @Override
            public String getLabelFor(double progress, double maxProgress) {
                return String.valueOf((int) Math.round(progress));
            }
        });

        double density=getResources().getDisplayMetrics().density;

        // Add label converter
        speedometer.setSpeed(progress,2000,1500);


        // configure value range and ticks
        speedometer.setMaxSpeed(100);
        speedometer.setMajorTickStep(10);
        speedometer.setMinorTicks(4);

        speedometer.setLabelTextSize((int) Math.round(DEFAULT_LABEL_TEXT_SIZE_DP * density));

        // Configure value range colors
        speedometer.addColoredRange(30, 50, Color.GREEN);
        speedometer.addColoredRange(50, 75, Color.YELLOW);
        speedometer.addColoredRange(75, 100, Color.RED);

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){

            //Bottom ToolBar Navigation
            case R.id.activity_container:
                Bundle bundle1 = new Bundle();
                bundle1.putString(Constant.ACTIVITY_TITLE,Constant.ACTIVITY_TITLE);

                NewActivityFragment af = new NewActivityFragment();
                af.setArguments(bundle1);
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left)
                        .replace(R.id.main_container,af).addToBackStack(null).commit();

                break;

            case R.id.finance_container:

                Bundle bundle = new Bundle();
                bundle.putString(Constant.FINANCE_TITLE,Constant.FINANCE_TITLE);

                NewFinanceFragment financeFragment = new NewFinanceFragment();
                financeFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left).replace(R.id.main_container,financeFragment).addToBackStack(null).commit();

                break;

            case R.id.store_container:

                Bundle bundle2 = new Bundle();
                bundle2.putString(Constant.STORE_TITLE,Constant.STORE_TITLE);

                NewStoreFragment sf = new NewStoreFragment();
                sf.setArguments(bundle2);
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right)
                        .replace(R.id.main_container,sf).addToBackStack(null).commit();

                break;

            case R.id.dashboard_coordinator_image:
                int coordinator_id = currentProject.getProject_coordinator_id();
                getUserAndSeeProfile(coordinator_id);
                break;

            case R.id.dashboard_manager_image:
                int manager_id = currentProject.getProject_manager_id();
                getUserAndSeeProfile(manager_id);
                break;
        }



    }

    private void getUserAndSeeProfile(int user_id){
        Map<String,String> map = new HashMap<>();
        map.put("user_id", String.valueOf(user_id));

        MyPostVolley myPostVolley = new MyPostVolley(getActivity(),map, URLs.GET_USER,"Please wait While getting user....");
        myPostVolley.applyPostVolley();
        myPostVolley.setVolleyPostListener(new MyPostVolley.VolleyPostListener() {
            @Override
            public void getResposefromVolley(String response) {
                Log.d("RESPONSE",response);
                UserResponse userResponse = gson.fromJson(response,UserResponse.class);

                if(userResponse.getSuccess()==1){
                    User user = userResponse.getUser();

                    if(user==null){
                        showDialog("User Not Found");
                    }else{
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constant.USER,user);

                        ViewUserProfile viewUserProfile = new ViewUserProfile();
                        viewUserProfile.setArguments(bundle);

                        getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_top,R.anim.exit_to_bottom)
                                .replace(R.id.main_container,viewUserProfile).addToBackStack(null).commit();
                    }



                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(currentProject.getProject_name());
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    private void showDialog(String message){
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("User")
                .setContentText(message)
                .show();
    }
}
