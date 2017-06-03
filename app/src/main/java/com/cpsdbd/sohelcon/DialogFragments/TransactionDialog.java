package com.cpsdbd.sohelcon.DialogFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconButton;

import com.cpsdbd.sohelcon.R;
import com.cpsdbd.sohelcon.Utility.Constant;
import com.cpsdbd.sohelcon.Utility.FontUtils;
import com.cpsdbd.sohelcon.Utility.MyUtils;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionDialog extends DialogFragment implements View.OnClickListener{
    public static final int RESULT_CODE = 10;
    private TextView tvTitle,tvDate;
    private EditText etDate;
    private IconButton btnCalender;
    private ImageButton btnCancel,btnOk;


    public TransactionDialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Window window=getDialog().getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction_dialog, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tvTitle = (TextView) view.findViewById(R.id.tran_title);
        tvDate = (TextView) view.findViewById(R.id.tvDate);

        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvTitle);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvDate);
        tvTitle.setText("Transaction Dialog");

        etDate = (EditText) view.findViewById(R.id.etDate);

        Date date = new Date();
        String dateStr = MyUtils.dateToString(date);
        etDate.setText(dateStr);

        btnCalender = (IconButton) view.findViewById(R.id.dialog_calender);

        btnCancel = (ImageButton) view.findViewById(R.id.dialog_cancel);
        btnOk = (ImageButton) view.findViewById(R.id.dialog_ok);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnCalender.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialog_calender:
                MyUtils.showDialogAndSetTime(getActivity(),etDate);
                break;

            case R.id.dialog_cancel:
                getDialog().dismiss();
                break;

            case R.id.dialog_ok:
                //Todo .....................

                String selectedDate = etDate.getText().toString().trim();

                Intent intent = new Intent();
                intent.putExtra(Constant.SELECTED_DATE,selectedDate);
                getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_CODE, intent);
                dismiss();

                break;
        }
    }
}
