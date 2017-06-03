package com.cpsdbd.sohelcon.DetailDialogFragmentLongPress;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cpsdbd.sohelcon.BaseClass.ActivityData;
import com.cpsdbd.sohelcon.CustomView.MyTextView;
import com.cpsdbd.sohelcon.R;
import com.cpsdbd.sohelcon.Utility.Constant;
import com.cpsdbd.sohelcon.Utility.FontUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityDetailDialog extends DialogFragment {

    private MyTextView tvActivityName,tvStartDate,tvFinishedDate,tvVolumeofWorks,tvUnitRate,tvVolumeofWorkdone,tvLastModified,
                activityName,startdate,finisheddate,volumeofwork,unitrate,workdone,lastModified;

    private TextView title;

    private ImageButton btnOk;

    private ActivityData data;


    public ActivityDetailDialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_activity_detail_dialog, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tvActivityName = (MyTextView) view.findViewById(R.id.activity_dialog_activity_name);
        tvStartDate = (MyTextView) view.findViewById(R.id.activity_dialog_start_date);
        tvFinishedDate = (MyTextView) view.findViewById(R.id.activity_dialog_finished_date);
        tvVolumeofWorks = (MyTextView) view.findViewById(R.id.activity_dialog_volume_of_works);
        tvUnitRate = (MyTextView) view.findViewById(R.id.activity_dialog_unit_rate);
        tvVolumeofWorkdone = (MyTextView) view.findViewById(R.id.activity_dialog_volume_of_workdone);
        tvLastModified = (MyTextView) view.findViewById(R.id.activity_dialog_last_modified);

        title = (TextView) view.findViewById(R.id.title);
        activityName = (MyTextView) view.findViewById(R.id.activity_name);
        startdate = (MyTextView) view.findViewById(R.id.start_date);
        finisheddate = (MyTextView) view.findViewById(R.id.finished_date);
        volumeofwork = (MyTextView) view.findViewById(R.id.vol_of_works);
        unitrate = (MyTextView) view.findViewById(R.id.unit_rate);
        workdone = (MyTextView) view.findViewById(R.id.work_done);
        lastModified = (MyTextView) view.findViewById(R.id.last_modified);

        btnOk = (ImageButton) view.findViewById(R.id.activity_dialog_ok);

        // Set Font
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvActivityName);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvStartDate);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvFinishedDate);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvVolumeofWorks);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvUnitRate);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvVolumeofWorkdone);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvLastModified);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,title);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,startdate);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,finisheddate);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,volumeofwork);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,unitrate);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,workdone);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,lastModified);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,activityName);

        title.setText(Constant.ACTIVITY_DETAILS);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        if(getArguments()!=null){
            data = (ActivityData) getArguments().getSerializable(Constant.GET_ACTIVITY);

            if(data!=null){
                tvActivityName.setText(data.getActivity_name());
                tvStartDate.setText(data.getActivity_start_date());
                tvFinishedDate.setText(data.getActivity_finished_date());
                tvVolumeofWorks.setText(String.valueOf(data.getActivity_volume_of_works()));
                tvUnitRate.setText(String.valueOf(data.getActivity_unit_rate()));
                tvVolumeofWorkdone.setText(String.valueOf(data.getActivity_volume_of_work_done()));
                tvLastModified.setText(data.getActivity_last_modified());
            }
        }
    }
}
