package com.cpsdbd.sohelcon.Fragments.MainFragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;


import com.cpsdbd.sohelcon.Adapter.ActivitiesAdapter;
import com.cpsdbd.sohelcon.BaseClass.ActivityData;
import com.cpsdbd.sohelcon.ChartAndGraph.MyListener;
import com.cpsdbd.sohelcon.DetailDialogFragmentLongPress.ActivityDetailDialog;
import com.cpsdbd.sohelcon.DialogFragments.WorkDoneDialogFragment;
import com.cpsdbd.sohelcon.Fragments.MainFragment.Activity.UpdateActivityFragment;
import com.cpsdbd.sohelcon.R;
import com.cpsdbd.sohelcon.Response.RegisterResponse;
import com.cpsdbd.sohelcon.Utility.Constant;
import com.cpsdbd.sohelcon.Utility.URLs;
import com.cpsdbd.sohelcon.Utility.UserLocalStore;
import com.cpsdbd.sohelcon.Volley.MyPostVolley;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import jp.wasabeef.recyclerview.animators.ScaleInAnimator;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityFragment extends Fragment implements ActivitiesAdapter.ActivityItemClickListener {

    private RecyclerView rvActivities;



    private ActivitiesAdapter adapter;
    private List<ActivityData> activitiesDataList;

    private UserLocalStore userLocalStore;
    private Gson gson;

    private MyListener listener;


    public ActivityFragment() {
        // Required empty public constructor
    }

    public void setMyListener(MyListener listener){
        this.listener=listener;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitiesDataList = new ArrayList<>();
        adapter = new ActivitiesAdapter(getActivity(),activitiesDataList);

        /*if(savedInstanceState!= null){
            List<ActivityData> myData = (List<ActivityData>) savedInstanceState.getSerializable(Constant.ACTIVITY_LIST);
            activitiesDataList.addAll(myData);
            adapter.notifyDataSetChanged();
        }*/




        userLocalStore = new UserLocalStore(getActivity());
        gson = new Gson();

        if(getArguments()!= null){
            List<ActivityData> dataList = (List<ActivityData>) getArguments().getSerializable(Constant.ACTIVITY_LIST);
            activitiesDataList.clear();
            activitiesDataList.addAll(dataList);
            adapter.notifyDataSetChanged();

        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_activity, container, false);

        initView(view);
        rvActivities.setLayoutManager(new LinearLayoutManager(getActivity()));

        //SEt Item Animator in Recycler View
        ScaleInAnimator animator = new ScaleInAnimator();
        animator.setAddDuration(1000);
        animator.setRemoveDuration(3000);
        rvActivities.setItemAnimator(animator);

        /*DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setRemoveDuration(2000);
        rvActivities.setItemAnimator(animator);*/

        rvActivities.setAdapter(adapter);

        return view;
    }

    private void initView(View view) {

        rvActivities = (RecyclerView) view.findViewById(R.id.rv_activities);





    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter.setActivityItemClickListener(this);
    }

    @Override
    public void deteteItem(int position) {

        ActivityData data = activitiesDataList.get(position);
        dialogPopUpandDeleteActivity(data);

    }

    @Override
    public void onResume() {
        super.onResume();

        if(listener!= null){
            listener.fragmentChange(3);
        }
    }

    @Override
    public void updateItem(int position) {
        ActivityData data = activitiesDataList.get(position);
        loadUpdateActivity(data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("YYYYY","YYYYYY");
        outState.putSerializable(Constant.ACTIVITY_LIST, (Serializable) activitiesDataList);
    }

    @Override
    public void showDetail(int position) {
        ActivityData data = activitiesDataList.get(position);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.GET_ACTIVITY,data);

        ActivityDetailDialog add = new ActivityDetailDialog();
        add.setArguments(bundle);
        add.show(getFragmentManager(),Constant.DEFAULAT_FRAGMENT_TAG);
    }

    @Override
    public void workDone(int position) {
        ActivityData data = activitiesDataList.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.GET_ACTIVITY,data);

        WorkDoneDialogFragment workDoneDialogFragment = new WorkDoneDialogFragment();
        workDoneDialogFragment.setArguments(bundle);

        workDoneDialogFragment.show(getFragmentManager(),Constant.DEFAULAT_FRAGMENT_TAG);
    }


    // Method Popup Dialog and Delete Project
    private void dialogPopUpandDeleteActivity( final ActivityData data){
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Do You Really Want to delete the Activity!!!!")
                .setCancelText("No,cancel plx!")
                .setConfirmText("Yes,delete it!")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                        deleteActivity(data);
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();
    }

    private void deleteActivity(final ActivityData data) {

        Map<String,String> map = new HashMap<>();
        map.put("activity_id", String.valueOf(data.getActivity_id()));
        map.put("user_id", String.valueOf(userLocalStore.getLoggedInUser().getUser_id()));


        MyPostVolley myPostVolley = new MyPostVolley(getActivity(),map, URLs.DELETE_ACTIVITY,Constant.DELETE_ACTIVITY);
        myPostVolley.applyPostVolley();
        myPostVolley.setVolleyPostListener(new MyPostVolley.VolleyPostListener() {
            @Override
            public void getResposefromVolley(String response) {
                RegisterResponse registerResponse = gson.fromJson(response,RegisterResponse.class);

                if(registerResponse.getSuccess()==1){
                    adapter.removeItem(data);
                    Toast.makeText(getActivity(), registerResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), registerResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void loadUpdateActivity(ActivityData data) {

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.UPDATE_ACTIVITY,data);

        UpdateActivityFragment uaf = new UpdateActivityFragment();
        uaf.setArguments(bundle);
        getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_top_left,R.anim.exit_to_tom)
                .replace(R.id.activity_main_container,uaf).addToBackStack(null).commit();
    }


}
