package net.optimusbs.sohelcon.Fragments.MainFragment;


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
import com.joanzapata.iconify.widget.IconTextView;


import net.optimusbs.sohelcon.BaseClass.Project;
import net.optimusbs.sohelcon.BaseClass.StoreData;
import net.optimusbs.sohelcon.ChartAndGraph.MyListener;
import net.optimusbs.sohelcon.Fragments.MainFragment.Store.StoreConsumed;
import net.optimusbs.sohelcon.Fragments.MainFragment.Store.StoreGraphFragment;
import net.optimusbs.sohelcon.Fragments.MainFragment.Store.StoreReceived;
import net.optimusbs.sohelcon.Fragments.MainFragment.Store.StoreSummery;
import net.optimusbs.sohelcon.Fragments.NoDataFragment;
import net.optimusbs.sohelcon.LoginActivity;
import net.optimusbs.sohelcon.R;
import net.optimusbs.sohelcon.Response.StoreResponse;
import net.optimusbs.sohelcon.Utility.Constant;
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
public class NewStoreFragment extends Fragment implements View.OnClickListener,MyListener {

    private IconTextView btnMinus,btnPlus,btnStoreSummery,btnGraph;

    private RelativeLayout homeContainer,summeryContainer,graphContainer,receivedContainer,consumedContainer;

    private List<StoreData> storeDataList;

    private UserLocalStore userLocalStore;
    private Gson gson;


    public NewStoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userLocalStore = new UserLocalStore(getActivity());
        gson = new Gson();
        storeDataList = new ArrayList<>();

        Project currentProject = userLocalStore.getCurrentProject();
        // Check Project Exist or Not
        if(currentProject.getProject_id()!=-1) {
            int project_id = currentProject.getProject_id();
            String user_id = String.valueOf(userLocalStore.getLoggedInUser().getUser_id());

            Map<String, String> map = new HashMap<>();
            map.put("user_id", user_id);
            map.put("project_id", String.valueOf(project_id));

            MyPostVolley myPostVolley = new MyPostVolley(getActivity(), map, URLs.GET_ALL_STORE_DATA, Constant.GET_ALL_STORE_DATA);
            myPostVolley.applyPostVolley();
            myPostVolley.setVolleyPostListener(new MyPostVolley.VolleyPostListener() {
                @Override
                public void getResposefromVolley(String response) {


                    StoreResponse storeResponse = gson.fromJson(response, StoreResponse.class);
                    if (storeResponse.getSuccess() == 1) {
                        //financeDataList.clear();
                        storeDataList.addAll(storeResponse.getStore());

                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constant.STORE_DATA_LIST, (Serializable) storeDataList);

                        StoreFragment sf = new StoreFragment();
                        sf.setArguments(bundle);
                        sf.setMyListener(NewStoreFragment.this);

                        getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right)
                                .replace(R.id.store_main_container,sf).commit();


                        if (storeDataList.size() > 0) {
                            summeryContainer.setVisibility(View.VISIBLE);
                            graphContainer.setVisibility(View.VISIBLE);
                            homeContainer.setVisibility(View.VISIBLE);
                        }

                    } else if (storeResponse.getSuccess() == 0) {
                        userLocalStore.clearUserData();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        getActivity().finish();
                        startActivity(intent);
                    } else if (storeResponse.getSuccess() == 2) {
                        NoDataFragment noDataFragment = new NoDataFragment();
                        Bundle args = new Bundle();
                        args.putString(Constant.TEXT,storeResponse.getMessage());

                        noDataFragment.setArguments(args);

                        getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_top,R.anim.exit_to_bottom)
                                .replace(R.id.store_main_container,noDataFragment).commit();
                    }
                }
            });

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_new_store, container, false);

        initView(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        homeContainer.setOnClickListener(this);
        consumedContainer.setOnClickListener(this);
        receivedContainer.setOnClickListener(this);
        summeryContainer.setOnClickListener(this);
        graphContainer.setOnClickListener(this);
    }

    private void initView(View view) {

        btnMinus = (IconTextView) view.findViewById(R.id.store_minus);
        btnPlus = (IconTextView) view.findViewById(R.id.store_plus);
        btnStoreSummery = (IconTextView) view.findViewById(R.id.store_summery);
        btnGraph = (IconTextView) view.findViewById(R.id.store_graph);

        summeryContainer =(RelativeLayout) view.findViewById(R.id.summery_container);
        graphContainer =(RelativeLayout) view.findViewById(R.id.graph_container);
        receivedContainer =(RelativeLayout) view.findViewById(R.id.received_container);
        consumedContainer =(RelativeLayout) view.findViewById(R.id.consumed_container);
        homeContainer =(RelativeLayout) view.findViewById(R.id.home_container);
    }

    @Override
    public void onResume() {
        super.onResume();

        String projectName = userLocalStore.getCurrentProject().getProject_name();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(projectName+"/"+"Store");

        //Orientation Setting
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        if(storeDataList.size()>0){
            summeryContainer.setVisibility(View.VISIBLE);
            graphContainer.setVisibility(View.VISIBLE);
            homeContainer.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.STORE_DATA_LIST, (Serializable) storeDataList);

        switch (view.getId()){

            case R.id.home_container:
                if(userLocalStore.getCurrentProject().getProject_id()!=-1){
                    StoreFragment storeFragment = new StoreFragment();
                    storeFragment.setMyListener(this);
                    storeFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right)
                            .replace(R.id.store_main_container,storeFragment).commit();
                }
                break;

            case R.id.received_container:
                if(userLocalStore.getCurrentProject().getProject_id()!=-1){
                    StoreReceived sr = new StoreReceived();
                    sr.setMyListener(this);
                    sr.setArguments(bundle);
                    getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_top,R.anim.exit_to_bottom)
                            .replace(R.id.store_main_container,sr).commit();
                }
                break;
            case R.id.consumed_container:
                if(userLocalStore.getCurrentProject().getProject_id()!=-1){
                    StoreConsumed sc = new StoreConsumed();
                    sc.setMyListener(this);
                    sc.setArguments(bundle);
                    getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right)
                            .replace(R.id.store_main_container,sc).commit();
                }
                break;

            case R.id.summery_container:
                if(userLocalStore.getCurrentProject().getProject_id()!=-1){
                    StoreSummery ss = new StoreSummery();
                    ss.setMyListener(this);
                    ss.setArguments(bundle);
                    getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left)
                            .replace(R.id.store_main_container,ss).commit();
                }
                break;

            case R.id.graph_container:
                if(userLocalStore.getCurrentProject().getProject_id()!=-1){
                    StoreGraphFragment sgf = new StoreGraphFragment();
                    sgf.setMyListener(this);
                    sgf.setArguments(bundle);
                    getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_bottom,R.anim.exit_to_tom)
                            .replace(R.id.store_main_container,sgf).commit();
                }
                break;


        }

    }

    private void showDialog(String message){
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Store")
                .setContentText(message)
                .show();
    }

    @Override
    public void fragmentChange(int fraID) {
        setDefaultBackGround();

        switch (fraID){
            case 1:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    consumedContainer.setBackground(getActivity().getDrawable(R.drawable.my_selector));
                }
                break;

            case 2:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    receivedContainer.setBackground(getActivity().getDrawable(R.drawable.my_selector));
                }
                break;

            case 3:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    summeryContainer.setBackground(getActivity().getDrawable(R.drawable.my_selector));
                }
                break;

            case 4:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    graphContainer.setBackground(getActivity().getDrawable(R.drawable.my_selector));
                }
                break;

            case 5:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    homeContainer.setBackground(getActivity().getDrawable(R.drawable.my_selector));
                }
                break;
        }
    }

    private void setDefaultBackGround() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            consumedContainer.setBackground(getActivity().getDrawable(R.drawable.custom_button_default));
            receivedContainer.setBackground(getActivity().getDrawable(R.drawable.custom_button_default));
            summeryContainer.setBackground(getActivity().getDrawable(R.drawable.custom_button_default));
            graphContainer.setBackground(getActivity().getDrawable(R.drawable.custom_button_default));
            homeContainer.setBackground(getActivity().getDrawable(R.drawable.custom_button_default));
        }
    }
}
