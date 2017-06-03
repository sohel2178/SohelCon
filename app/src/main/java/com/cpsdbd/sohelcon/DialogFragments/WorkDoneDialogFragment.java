package com.cpsdbd.sohelcon.DialogFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joanzapata.iconify.widget.IconButton;


import com.cpsdbd.sohelcon.BaseClass.ActivityData;
import com.cpsdbd.sohelcon.CustomView.MyTextView;
import com.cpsdbd.sohelcon.Fragments.MainFragment.NewActivityFragment;
import com.cpsdbd.sohelcon.R;
import com.cpsdbd.sohelcon.Response.RegisterResponse;
import com.cpsdbd.sohelcon.Utility.Constant;
import com.cpsdbd.sohelcon.Utility.FontUtils;
import com.cpsdbd.sohelcon.Utility.MyUtils;
import com.cpsdbd.sohelcon.Utility.URLs;
import com.cpsdbd.sohelcon.Utility.UserLocalStore;
import com.cpsdbd.sohelcon.Volley.MyPostVolley;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkDoneDialogFragment extends DialogFragment implements View.OnClickListener {
    private EditText etDate,etVolumeoFWorkDone;

    private TextView tvTitle;
    MyTextView date,workDone;

    private ImageButton btnOk,btnCancel;

    private IconButton btnCalendar;

    private ActivityData data;


    private Gson gson;

    private Date today;

    private int activity_id,user_id;

    private UserLocalStore userLocalStore;





    public WorkDoneDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!= null){
            data= (ActivityData) getArguments().getSerializable(Constant.GET_ACTIVITY);
            today= new Date();
            gson = new Gson();

            userLocalStore = new UserLocalStore(getActivity());
            user_id = userLocalStore.getLoggedInUser().getUser_id();



            if (data!= null){
                activity_id=data.getActivity_id();


            }


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Window window=getDialog().getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_work_done_dialog, container, false);
        initView(view);
        return view;

    }

    private void initView(View view){
        etDate = (EditText) view.findViewById(R.id.work_done_date);
        etDate.setText(MyUtils.dateToString(today));
        etVolumeoFWorkDone = (EditText) view.findViewById(R.id.dialog_work_done);
        btnOk = (ImageButton) view.findViewById(R.id.work_done_dialog_ok);
        btnCancel = (ImageButton) view.findViewById(R.id.work_done_dialog_cancel);
        tvTitle = (TextView) view.findViewById(R.id.workdone_title);
        date = (MyTextView) view.findViewById(R.id.date);
        workDone = (MyTextView) view.findViewById(R.id.work_done);
        tvTitle.setText(data.getActivity_name());


        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvTitle);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,date);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,workDone);
        /*FontUtils.setFont(Constant.T_RAILWAY_REGULAR,btnOk);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,btnCancel);*/

        btnCalendar = (IconButton) view.findViewById(R.id.work_done_dialog_calender);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnCalendar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.work_done_dialog_cancel:
                dismiss();
                break;
            case R.id.work_done_dialog_ok:

                String date = etDate.getText().toString().trim();
                String volume_of_work_done = etVolumeoFWorkDone.getText().toString().trim();

                if(TextUtils.isEmpty(date)){
                    etDate.requestFocus();
                    Toast.makeText(getActivity(), "Date field is Empty", Toast.LENGTH_SHORT).show();
                    break;
                }

                if(TextUtils.isEmpty(volume_of_work_done)){
                    etVolumeoFWorkDone.requestFocus();
                    Toast.makeText(getActivity(), "Work done field is Empty", Toast.LENGTH_SHORT).show();
                    break;
                }

                double work_done = Double.parseDouble(volume_of_work_done);

                if((work_done+data.getActivity_volume_of_work_done())>data.getActivity_volume_of_works()){
                    etVolumeoFWorkDone.requestFocus();
                    Toast.makeText(getActivity(), "Remaining Works "+(data.getActivity_volume_of_works()-data.getActivity_volume_of_work_done()), Toast.LENGTH_SHORT).show();
                    break;
                }



                Map<String,String> map = new HashMap<>();
                map.put("user_id", String.valueOf(user_id));
                map.put("activity_id", String.valueOf(activity_id));
                map.put("work_done_date", String.valueOf(date));
                map.put("volume_of_work_done", String.valueOf(volume_of_work_done));

                MyPostVolley myPostVolley = new MyPostVolley(getActivity(),map, URLs.INSERT_WORK_DONE,Constant.INSERT_WORK_DONE);
                myPostVolley.applyPostVolley();
                myPostVolley.setVolleyPostListener(new MyPostVolley.VolleyPostListener() {
                    @Override
                    public void getResposefromVolley(String response) {
                        //Log.d("TEST",response);
                        RegisterResponse rr = gson.fromJson(response,RegisterResponse.class);

                        Log.d("MMMMM",response);

                        Toast.makeText(getActivity(), rr.getMessage(), Toast.LENGTH_SHORT).show();

                        dismiss();

                        getFragmentManager().beginTransaction().replace(R.id.main_container,new NewActivityFragment()).commit();
                    }
                });

                break;

            case R.id.work_done_dialog_calender:
                MyUtils.showDialogAndSetTime(getActivity(),etDate);
                break;
        }
    }
}
