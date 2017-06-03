package com.cpsdbd.sohelcon.Fragments.MainFragment.Activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;


import com.cpsdbd.sohelcon.BaseClass.ActivityData;
import com.cpsdbd.sohelcon.CustomView.MyEditText;
import com.cpsdbd.sohelcon.CustomView.MyTextView;
import com.cpsdbd.sohelcon.Fragments.MainFragment.NewActivityFragment;
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
public class UpdateActivityFragment extends Fragment implements View.OnClickListener{

    private MyEditText etStartDate,etFinishedDate,etVolumeofWorks,etUnitRate,etActivityName;
    private MyTextView startDate,finishedDate,activityName,volumeOfWorks,unitRate;

    private Button btnOk;

    private TextView title;

    private Gson gson;


    private ActivityData data;
    private UserLocalStore userLocalStore;
    private int user_id;

    private MyUpdateListener listener;


    public UpdateActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
        userLocalStore= new UserLocalStore(getActivity());
        user_id = userLocalStore.getLoggedInUser().getUser_id();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_update_activity, container, false);

        initView(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(getArguments()!= null){
            data = (ActivityData) getArguments().getSerializable(Constant.UPDATE_ACTIVITY);

            if(data!= null){
                etStartDate.setText(data.getActivity_start_date());
                etFinishedDate.setText(data.getActivity_finished_date());
                etVolumeofWorks.setText(String.valueOf(data.getActivity_volume_of_works()));
                etUnitRate.setText(String.valueOf(data.getActivity_unit_rate()));
                etActivityName.setText(data.getActivity_name());
            }
        }
    }

    public void setMyUpdateListener(MyUpdateListener listener){
        this.listener = listener;
    }

    private void initView(View view) {
        etStartDate = (MyEditText) view.findViewById(R.id.update_activity_start_date);
        etFinishedDate = (MyEditText) view.findViewById(R.id.update_activity_finished_date);
        etVolumeofWorks = (MyEditText) view.findViewById(R.id.update_activity_volume_of_works);
        etUnitRate = (MyEditText) view.findViewById(R.id.update_activity_unit_rate);
        etActivityName = (MyEditText) view.findViewById(R.id.update_activity_activity_name);
        title = (TextView) view.findViewById(R.id.title);

        //,,,,
        startDate = (MyTextView) view.findViewById(R.id.start_date);
        finishedDate = (MyTextView) view.findViewById(R.id.finished_date);
        activityName = (MyTextView) view.findViewById(R.id.activity_name);
        volumeOfWorks = (MyTextView) view.findViewById(R.id.vol_of_works);
        unitRate = (MyTextView) view.findViewById(R.id.unit_rate);


        btnOk = (Button) view.findViewById(R.id.update_activity_ok);
        btnOk.setOnClickListener(this);
        etStartDate.setOnClickListener(this);
        etFinishedDate.setOnClickListener(this);

        title.setText("Update Activity");

        setFont();


    }

    private void setFont(){
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,etStartDate);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,etFinishedDate);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,etVolumeofWorks);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,etUnitRate);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,etActivityName);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,startDate);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,finishedDate);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,activityName);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,volumeOfWorks);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,unitRate);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,btnOk);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,title);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.update_activity_start_date:
                MyUtils.showDialogAndSetTime(getActivity(),etStartDate);
                break;

            case R.id.update_activity_finished_date:
                MyUtils.showDialogAndSetTime(getActivity(),etFinishedDate);
                break;

            case R.id.update_activity_ok:
                final String start_date = etStartDate.getText().toString().trim();
                final String finished_date = etFinishedDate.getText().toString().trim();
                final String activity_name = etActivityName.getText().toString().trim();
                final String volume_of_works = etVolumeofWorks.getText().toString().trim();
                final String unit_rate = etUnitRate.getText().toString().trim();

                if(TextUtils.isEmpty(start_date)){
                    etStartDate.requestFocus();
                    Toast.makeText(getActivity(), "Start Date Field is Empty", Toast.LENGTH_SHORT).show();
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

                if(!MyUtils.isThisDateValid(finished_date,"dd-MM-yyyy")){
                    etFinishedDate.requestFocus();
                    Toast.makeText(getActivity(), "Start Date is not Valid", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(activity_name)){
                    etActivityName.requestFocus();
                    Toast.makeText(getActivity(), "Activity Name Field is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(volume_of_works)){
                    etVolumeofWorks.requestFocus();
                    Toast.makeText(getActivity(), "Volume of Works Field is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(unit_rate)){
                    etUnitRate.requestFocus();
                    Toast.makeText(getActivity(), "Unit Rate Field is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }


                Map<String,String> map =new HashMap<>();
                map.put(FieldConstant.ACTIVITY_ID, String.valueOf(data.getActivity_id()));
                map.put(FieldConstant.ACTIVITY_USER_ID, String.valueOf(user_id));
                map.put(FieldConstant.ACTIVITY_START_DATE,start_date);
                map.put(FieldConstant.ACTIVITY_FINISHED_DATE,finished_date);
                map.put(FieldConstant.ACTIVITY_NAME,activity_name);
                map.put(FieldConstant.ACTIVITY_VOLUME_OF_WORKS,volume_of_works);
                map.put(FieldConstant.ACTIVITY_UNIT_RATE,unit_rate);

                MyPostVolley postVolley = new MyPostVolley(getActivity(),map, URLs.UPDATE_ACTIVITY,Constant.UPDATE_ACTIVITY_MESSAGE);
                postVolley.applyPostVolley();

                postVolley.setVolleyPostListener(new MyPostVolley.VolleyPostListener() {
                    @Override
                    public void getResposefromVolley(String response) {

                        Log.d("MYRES",response);
                        RegisterResponse rr = gson.fromJson(response,RegisterResponse.class);

                        if(rr.getSuccess()==1){
                            getFragmentManager().popBackStack();


                            getFragmentManager().beginTransaction().replace(R.id.main_container,new NewActivityFragment()).commit();
                            Toast.makeText(getActivity(), rr.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                break;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public interface MyUpdateListener{
        public void updateActivity(ActivityData activityData);
    }
}
