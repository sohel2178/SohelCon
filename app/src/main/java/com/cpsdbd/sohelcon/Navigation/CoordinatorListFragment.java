package com.cpsdbd.sohelcon.Navigation;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;


import com.cpsdbd.sohelcon.Adapter.UserListAdapter;
import com.cpsdbd.sohelcon.BaseClass.User;
import com.cpsdbd.sohelcon.R;
import com.cpsdbd.sohelcon.Response.ChildResponse;
import com.cpsdbd.sohelcon.Response.RegisterResponse;
import com.cpsdbd.sohelcon.Utility.Constant;
import com.cpsdbd.sohelcon.Utility.FieldConstant;
import com.cpsdbd.sohelcon.Utility.MyAlertDialog;
import com.cpsdbd.sohelcon.Utility.URLs;
import com.cpsdbd.sohelcon.Utility.UserLocalStore;
import com.cpsdbd.sohelcon.Volley.MyPostVolley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.animators.ScaleInAnimator;

/**
 * A simple {@link Fragment} subclass.
 */
public class CoordinatorListFragment extends Fragment implements UserListAdapter.UserClickListener,UserListAdapter.StatusChangeListener {

    private RecyclerView rvUsers;
    private UserListAdapter adapter;

    private UserLocalStore userLocalStore;
    private Gson gson;

    private int parent_id;

    private List<User> userList;

    private int test;

    private int user_type;



    public CoordinatorListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userLocalStore = new UserLocalStore(getActivity());
        parent_id = userLocalStore.getLoggedInUser().getUser_id();

        User user = userLocalStore.getLoggedInUser();

        user_type = user.getUser_type();


        gson = new Gson();
        userList = new ArrayList<>();

        adapter = new UserListAdapter(getActivity(),userList);

        Log.d("TEST","ONCREATE");





        getAllUsers(userLocalStore.getLoggedInUser().getUser_id());

    }

    @Override
    public void onResume() {
        super.onResume();




        if(user_type==1){
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Co-ordinator List");
        }else if(user_type==2){
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Manager List");
        }else if(user_type==3){
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Child List");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d("TEST","RESUME");

        adapter.setUserClickListener(this);
        adapter.setStatusChangeListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("TEST","ONCREATE VIEW");
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_coordinator_list, container, false);

        rvUsers = (RecyclerView) view.findViewById(R.id.rv_user_list);
        rvUsers.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Animation
        ScaleInAnimator animator = new ScaleInAnimator();
        animator.setAddDuration(1000);
        animator.setRemoveDuration(3000);
        rvUsers.setItemAnimator(animator);

        rvUsers.setAdapter(adapter);

        return view;
    }



    private void getAllUsers(int parent_id) {
        Map<String,String> map = new HashMap<>();
        map.put(FieldConstant.PARENT_ID, String.valueOf(parent_id));

        MyPostVolley myPostVolley = new MyPostVolley(getActivity(),map, URLs.GET_ALL_CHILD, Constant.GET_ALL_CHILD);
        myPostVolley.applyPostVolley();
        myPostVolley.setVolleyPostListener(new MyPostVolley.VolleyPostListener() {
            @Override
            public void getResposefromVolley(String response) {
                ChildResponse cr = gson.fromJson(response,ChildResponse.class);


                if(cr.getSuccess()==1){

                    List<User> childs = cr.getUsers();

                    if(childs.size()==0){
                        MyAlertDialog myAlertDialog = new MyAlertDialog(getActivity());
                        myAlertDialog.showSimpleDialog("Register User","You have No Child Please Create...");
                    }else{
                        for(User x: childs){
                            adapter.addItem(x);
                        }
                    }
                }
            }
        });
    }



    @Override
    public void statusChange(boolean status, int position) {
        int counter=getTrueStatus();
        test++;
        User user = userList.get(position);
        int user_id = user.getUser_id();

        if(status){
            if(user.getUser_status()==1){

            }else{
                updateUserStatus(user_id,status,position);
            }
        }else{
            if(user.getUser_status()==0){

            }else{
                updateUserStatus(user_id,status,position);
            }
        }

        /*if(test>counter){

            if(status){
                updateUserStatus(user_id,1,position);
            }else {
                updateUserStatus(user_id,0,position);
            }
        }*/



    }

    private int getTrueStatus(){
        int counter=0;
        for (User x:userList){
            if(x.getUser_status()==1){
                counter++;
            }
        }

        return counter;

    }




    private void updateUserStatus(int user_id, final boolean status, final int position) {
        int intStatus;

        if(status){
            intStatus=1;
        }else{
            intStatus=0;
        }

        Map<String,String> map  = new HashMap<>();
        map.put(FieldConstant.USER_ID, String.valueOf(user_id));
        map.put(FieldConstant.USER_STATUS, String.valueOf(intStatus));

        MyPostVolley myPostVolley = new MyPostVolley(getActivity(),map,URLs.UPDATE_USER_STATUS,Constant.UPDATE_USER);
        myPostVolley.applyPostVolley();
        myPostVolley.setVolleyPostListener(new MyPostVolley.VolleyPostListener() {
            @Override
            public void getResposefromVolley(String response) {
                RegisterResponse rr = gson.fromJson(response,RegisterResponse.class);

                if(rr.getSuccess()==1){
                    User user =userList.get(position);

                    if(status){
                        user.setUser_status(1);
                    }else{
                        user.setUser_status(0);
                    }

                    userList.remove(user);

                    userList.add(position,user);
                    adapter.notifyDataSetChanged();

                    MyAlertDialog myAlertDialog = new MyAlertDialog(getActivity());
                    myAlertDialog.showSimpleDialog("Update User",rr.getMessage());

                }


            }
        });

    }


    @Override
    public void onClickUser(int position) {
        User user = userList.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.USER,user);

        ViewUserProfile viewUserProfile = new ViewUserProfile();
        viewUserProfile.setArguments(bundle);

        getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_bottom,R.anim.exit_to_tom)
                .replace(R.id.main_container,viewUserProfile).addToBackStack(null).commit();
    }
}
