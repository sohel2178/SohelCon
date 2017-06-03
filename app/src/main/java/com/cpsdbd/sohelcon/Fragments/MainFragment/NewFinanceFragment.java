package com.cpsdbd.sohelcon.Fragments.MainFragment;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.gson.Gson;


import com.cpsdbd.sohelcon.BaseClass.FinanceData;
import com.cpsdbd.sohelcon.BaseClass.Project;
import com.cpsdbd.sohelcon.ChartAndGraph.MyListener;
import com.cpsdbd.sohelcon.Fragments.MainFragment.Finance.CreditFragment;
import com.cpsdbd.sohelcon.Fragments.MainFragment.Finance.DailyTransaction;
import com.cpsdbd.sohelcon.Fragments.MainFragment.Finance.DebitFragment;
import com.cpsdbd.sohelcon.Fragments.MainFragment.Finance.FinanceFrequency;
import com.cpsdbd.sohelcon.Fragments.MainFragment.Finance.FinanceSum;
import com.cpsdbd.sohelcon.Fragments.MainFragment.Finance.SixMonthExpenditure;
import com.cpsdbd.sohelcon.Fragments.NoDataFragment;
import com.cpsdbd.sohelcon.LoginActivity;
import com.cpsdbd.sohelcon.R;
import com.cpsdbd.sohelcon.Response.FinanceResponse;
import com.cpsdbd.sohelcon.Utility.Constant;
import com.cpsdbd.sohelcon.Utility.FieldConstant;
import com.cpsdbd.sohelcon.Utility.URLs;
import com.cpsdbd.sohelcon.Utility.UserLocalStore;
import com.cpsdbd.sohelcon.Volley.MyPostVolley;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewFinanceFragment extends Fragment implements View.OnClickListener,MyListener {

    private RelativeLayout homeCon,plusCon,minusCon,headCon,monthCon,dailyTransact,frequency;

    private UserLocalStore userLocalStore;
    private List<FinanceData> financeDataList;
    private Gson gson;

    private Project currentProject;



    public NewFinanceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userLocalStore= new UserLocalStore(getActivity());
        gson = new Gson();
        financeDataList= new ArrayList<>();

        currentProject = userLocalStore.getCurrentProject();


        if(currentProject.getProject_id()!=-1){
            int project_id = currentProject.getProject_id();
            String user_id = String.valueOf(currentProject.getProject_user_id());

            Map<String,String> map = new HashMap<>();
            map.put(FieldConstant.FINANCE_PROJECT_ID, String.valueOf(project_id));
            map.put(FieldConstant.FINANCE_USER_ID,user_id);

            MyPostVolley myPostVolley = new MyPostVolley(getActivity(),map, URLs.GET_ALL_TRANSACTIONS, Constant.GET_ALL_TRANSACTIONS);
            myPostVolley.applyPostVolley();
            myPostVolley.setVolleyPostListener(new MyPostVolley.VolleyPostListener() {
                @Override
                public void getResposefromVolley(String response) {


                    FinanceResponse financeResponse = gson.fromJson(response,FinanceResponse.class);
                    if(financeResponse.getSuccess()==1){

                        //financeDataList.clear();
                        financeDataList.addAll(financeResponse.getFinance());

                        // Add Finance Fragment
                        FinanceFragment financeFragment = new FinanceFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constant.FINANCE_LIST, (Serializable) financeDataList);
                        financeFragment.setArguments(bundle);
                        financeFragment.setMyLisener(NewFinanceFragment.this);
                        getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right)
                                .replace(R.id.finance_main_container,financeFragment).commit();


                        if(financeDataList.size()>0){
                            headCon.setVisibility(View.VISIBLE);
                            monthCon.setVisibility(View.VISIBLE);
                            dailyTransact.setVisibility(View.VISIBLE);
                            frequency.setVisibility(View.VISIBLE);
                            homeCon.setVisibility(View.VISIBLE);
                        }


                    }else if(financeResponse.getSuccess()==2){

                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.TEXT,financeResponse.getMessage());

                        NoDataFragment noDataFragment = new NoDataFragment();
                        noDataFragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_top,R.anim.exit_to_bottom)
                                .replace(R.id.finance_main_container,noDataFragment).commit();


                    }else if(financeResponse.getSuccess()==0){
                        userLocalStore.setUserLoggedIn(false);
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        getActivity().finish();
                        startActivity(intent);
                    }
                }
            });
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_finance, container, false);

        initView(view);

        return view;
    }




    private void initView(View view) {
        headCon = (RelativeLayout) view.findViewById(R.id.head_expenditure_container);
        monthCon = (RelativeLayout) view.findViewById(R.id.month_exp_container);
        dailyTransact = (RelativeLayout) view.findViewById(R.id.dailytransactionContainer);
        frequency = (RelativeLayout) view.findViewById(R.id.frequencyContainer);
        homeCon = (RelativeLayout) view.findViewById(R.id.home_container);
        plusCon = (RelativeLayout) view.findViewById(R.id.plus_container);
        minusCon = (RelativeLayout) view.findViewById(R.id.minus_container);


    }

    // Dialog if Data Not Found

    private void showDialog(String message){
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Transaction")
                .setContentText(message)
                .show();
    }




    @Override
    public void onResume() {
        super.onResume();

        String projectName = userLocalStore.getCurrentProject().getProject_name();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(projectName+"/"+"Finance");

        //Orientation Setting
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        if(financeDataList.size()>0){
            homeCon.setVisibility(View.VISIBLE);
            headCon.setVisibility(View.VISIBLE);
            monthCon.setVisibility(View.VISIBLE);
            dailyTransact.setVisibility(View.VISIBLE);
            frequency.setVisibility(View.VISIBLE);
        }






    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        homeCon.setOnClickListener(this);
        plusCon.setOnClickListener(this);
        minusCon.setOnClickListener(this);
        headCon.setOnClickListener(this);
        monthCon.setOnClickListener(this);
        dailyTransact.setOnClickListener(this);

        frequency.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.FINANCE_LIST, (Serializable) financeDataList);

        switch (view.getId()){

            case R.id.home_container:
                FinanceFragment ff = new FinanceFragment();
                ff.setArguments(bundle);
                ff.setMyLisener(this);
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_bottom,R.anim.exit_to_tom)
                        .replace(R.id.finance_main_container,ff).commit();

                break;

            case R.id.minus_container:
                if(userLocalStore.getCurrentProject().getProject_id()!=-1){
                    DebitFragment df = new DebitFragment();
                    df.setArguments(bundle);
                    df.setMyListener(this);
                    getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_top,R.anim.exit_to_bottom)
                            .replace(R.id.finance_main_container,df).commit();
                }
                break;

            case R.id.plus_container:
                if(userLocalStore.getCurrentProject().getProject_id()!=-1){
                    CreditFragment cf = new CreditFragment();
                    cf.setMyListener(this);
                    cf.setArguments(bundle);

                    getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right)
                            .replace(R.id.finance_main_container,cf)
                            .commit();
                }
                break;
            case R.id.dailytransactionContainer:
               /* TransactionDialog trDialog = new TransactionDialog();
                trDialog.setTargetFragment(this,REQUEST_CODE);
                trDialog.show(getFragmentManager(),Constant.DEFAULAT_FRAGMENT_TAG);*/

                DailyTransaction dt = new DailyTransaction();

                dt.setMyListener(this);
                dt.setArguments(bundle);

                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left)
                        .replace(R.id.finance_main_container,dt)
                        .commit();

                //Todo
                break;

            case R.id.frequencyContainer:

                List<FinanceData> listForFrequency = getExpenditureList();


                Bundle sohel = new Bundle();
                sohel.putSerializable(Constant.FINANCE_LIST, (Serializable) listForFrequency);

                FinanceFrequency fff = new FinanceFrequency();
                fff.setArguments(sohel);
                fff.setMyListener(this);

                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_bottom,R.anim.exit_to_tom)
                        .replace(R.id.finance_main_container,fff).commit();

                break;


            case R.id.head_expenditure_container:

                if(userLocalStore.getCurrentProject().getProject_id()!=-1){
                    FinanceSum fs = new FinanceSum();
                    fs.setArguments(bundle);
                    fs.setMyListener(this);
                    getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right)
                            .replace(R.id.finance_main_container,fs).commit();
                }
                break;

            case R.id.month_exp_container:
                if(userLocalStore.getCurrentProject().getProject_id()!=-1){
                    SixMonthExpenditure sme = new SixMonthExpenditure();
                    sme.setArguments(bundle);
                    sme.setMyListener(this);
                    getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_top,R.anim.exit_to_bottom)
                            .replace(R.id.finance_main_container,sme).commit();
                }
                break;




        }


    }

    private List<FinanceData> getExpenditureList() {
        List<FinanceData> returnList = new ArrayList<>();

        for(FinanceData x: financeDataList){
            if(x.getFinance_status()==1){
                returnList.add(x);
            }
        }
        return returnList;
    }


    @Override
    public void fragmentChange(int fraID) {
        switch (fraID){
            case 1:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setDefaultBackGround();
                    plusCon.setBackground(getActivity().getDrawable(R.drawable.my_selector));
                }
                break;

            case 2:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setDefaultBackGround();
                    minusCon.setBackground(getActivity().getDrawable(R.drawable.my_selector));
                }

                break;

            case 3:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setDefaultBackGround();
                    dailyTransact.setBackground(getActivity().getDrawable(R.drawable.my_selector));
                }
                break;

            case 4:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setDefaultBackGround();
                    frequency.setBackground(getActivity().getDrawable(R.drawable.my_selector));
                }
                break;

            case 5:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setDefaultBackGround();
                    headCon.setBackground(getActivity().getDrawable(R.drawable.my_selector));
                }
                break;

            case 6:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setDefaultBackGround();
                    monthCon.setBackground(getActivity().getDrawable(R.drawable.my_selector));
                }
                break;

            case 7:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setDefaultBackGround();
                    homeCon.setBackground(getActivity().getDrawable(R.drawable.my_selector));
                }
                break;


        }
    }

    private void setDefaultBackGround() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            homeCon.setBackground(getActivity().getDrawable(R.drawable.custom_button_default));
            plusCon.setBackground(getActivity().getDrawable(R.drawable.custom_button_default));
            minusCon.setBackground(getActivity().getDrawable(R.drawable.custom_button_default));
            dailyTransact.setBackground(getActivity().getDrawable(R.drawable.custom_button_default));
            frequency.setBackground(getActivity().getDrawable(R.drawable.custom_button_default));
            headCon.setBackground(getActivity().getDrawable(R.drawable.custom_button_default));
            monthCon.setBackground(getActivity().getDrawable(R.drawable.custom_button_default));
        }
    }
}
