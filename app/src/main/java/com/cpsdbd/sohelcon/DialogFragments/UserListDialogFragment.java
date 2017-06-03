package com.cpsdbd.sohelcon.DialogFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;


import com.cpsdbd.sohelcon.Adapter.MyRadioAdapter;
import com.cpsdbd.sohelcon.BaseClass.User;
import com.cpsdbd.sohelcon.R;
import com.cpsdbd.sohelcon.Response.RegisterResponse;
import com.cpsdbd.sohelcon.Utility.Constant;
import com.cpsdbd.sohelcon.Utility.FieldConstant;
import com.cpsdbd.sohelcon.Utility.URLs;
import com.cpsdbd.sohelcon.Utility.UserLocalStore;
import com.cpsdbd.sohelcon.Volley.MyPostVolley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserListDialogFragment extends DialogFragment implements View.OnClickListener,MyRadioAdapter.MyRadioListener {


    private TextView tvTitle;
    private ImageButton btnAssign,btnNo;

    private RecyclerView rvRadio;

    private UserLocalStore userLocalStore;
    private Gson gson;

    private int user_type;

    private List<User> userList;

    private int assign_project_id;

    int selected_id;

    private MyRadioAdapter adapter;


    public UserListDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            userList = (List<User>) getArguments().getSerializable(Constant.USERS);
            assign_project_id= getArguments().getInt(Constant.PROJECT_ID);
            selected_id= getArguments().getInt(Constant.SELECTED_ID);

            adapter = new MyRadioAdapter(getActivity(),userList,selected_id);

            userLocalStore = new UserLocalStore(getActivity());
            user_type = userLocalStore.getLoggedInUser().getUser_type();
            gson= new Gson();

        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Window window=getDialog().getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user_list_dialog, container, false);

        initView(view);

        rvRadio.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvRadio.setAdapter(adapter);





        return view;
    }

    private void initView(View view) {
        rvRadio = (RecyclerView) view.findViewById(R.id.radioRecycler);

        tvTitle = (TextView) view.findViewById(R.id.user_dialog_title);
        if(user_type==1){
            tvTitle.setText("Co-ordinators");
        }else if(user_type==2){
            tvTitle.setText("Managers");
        }

        btnAssign = (ImageButton) view.findViewById(R.id.btn_assign_child);
        btnNo = (ImageButton) view.findViewById(R.id.btn_assign_child_no);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnNo.setOnClickListener(this);
        btnAssign.setOnClickListener(this);

        adapter.setMyRadioListener(this);

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_assign_child_no:
                getDialog().dismiss();
                break;

            case R.id.btn_assign_child:

                User user = getUserById(selected_id);
                int user_id = user.getUser_id();
                String name = user.getUser_name();
                String photo=user.getUser_photo();
                updateProject(assign_project_id,user_type,user_id,name,photo);

                getDialog().dismiss();


                break;
        }
    }

    private User getUserById(int id){
        User user = null;

        for(User x :userList){
            if(x.getUser_id()==id){
                user=x;
                break;
            }
        }

        return user;
    }

    private void updateProject(final int assign_project_id, int user_type, final int user_id, final String name, final String photo) {
        Map<String,String> map = new HashMap<>();
        map.put(FieldConstant.PROJECT_ID, String.valueOf(assign_project_id));
        map.put(FieldConstant.USER_TYPE, String.valueOf(user_type));
        map.put(FieldConstant.ID, String.valueOf(user_id));
        map.put(FieldConstant.NAME,name);
        map.put(FieldConstant.PHOTO,photo);


        MyPostVolley myPostVolley = new MyPostVolley(getActivity(),map, URLs.ASSGN_PROJECT,Constant.ASSGN_PROJECT);
        myPostVolley.applyPostVolley();
        myPostVolley.setVolleyPostListener(new MyPostVolley.VolleyPostListener() {
            @Override
            public void getResposefromVolley(String response) {

                RegisterResponse rr = gson.fromJson(response,RegisterResponse.class);

                if(rr.getSuccess()==1){

                    Intent intent = new Intent();
                    intent.putExtra(FieldConstant.PROJECT_ID,assign_project_id);
                    intent.putExtra(FieldConstant.ID,user_id);
                    intent.putExtra(FieldConstant.NAME,name);

                    getTargetFragment().onActivityResult(getTargetRequestCode(), Constant.RESULT_CODE, intent);

                }
            }
        });
    }


    @Override
    public void onClickRadio(int id) {
        selected_id=id;
    }
}
