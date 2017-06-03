package com.cpsdbd.sohelcon.DetailDialogFragmentLongPress;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import com.cpsdbd.sohelcon.BaseClass.FinanceData;
import com.cpsdbd.sohelcon.CustomView.MyTextView;
import com.cpsdbd.sohelcon.R;
import com.cpsdbd.sohelcon.Utility.Constant;
import com.cpsdbd.sohelcon.Utility.FontUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowDetailCreditDialog extends DialogFragment {
    private MyTextView tvDate,tvDescription,tvHead,tvAmount,head,date,amount,description;
    TextView tvTitle;
    private ImageButton btnOK;

    private FinanceData data;


    public ShowDetailCreditDialog() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Iconify
                .with(new FontAwesomeModule());

        if(getArguments()!=null) {
            data = (FinanceData) getArguments().getSerializable(Constant.GET_FINANCE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //window.requestFeature(Window.FEATURE_NO_TITLE);*/
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_show_detail_credit_dialog, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tvDate = (MyTextView) view.findViewById(R.id.show_credit_dialog_date);
        tvDescription = (MyTextView) view.findViewById(R.id.show_credit_dialog_description);
        tvAmount = (MyTextView) view.findViewById(R.id.show_credit_dialog_amount);
        tvHead = (MyTextView) view.findViewById(R.id.show_credit_dialog_head);
        tvTitle= (TextView) view.findViewById(R.id.title_dialog);

        head = (MyTextView) view.findViewById(R.id.head);
        date = (MyTextView) view.findViewById(R.id.date);
        amount = (MyTextView) view.findViewById(R.id.amount);
        description = (MyTextView) view.findViewById(R.id.description);



        tvTitle.setText(Constant.FINANCE_ITEM_DETAIL);

        if(data.getFinance_status()==0){
            tvHead.setVisibility(View.GONE);
            head.setVisibility(View.GONE);
        }

        btnOK = (ImageButton) view.findViewById(R.id.show_credit_dialog_ok);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvTitle);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvDate);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvDescription);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvAmount);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvHead);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,head);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,date);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,amount);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,description);

        //FontUtils.setFont(Constant.T_RAILWAY_REGULAR,btnOK);

    }

    @Override
    public void onStart() {
        super.onStart();

        if(data!=null){
            tvDate.setText(data.getFinance_transaction_date());
            tvDescription.setText(data.getFinance_particular());
            tvAmount.setText(String.valueOf(data.getFinance_amount()));
            tvHead.setText(data.getFinance_head());
        }

    }
}
