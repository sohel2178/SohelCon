package com.cpsdbd.sohelcon.DetailDialogFragmentLongPress;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.cpsdbd.sohelcon.BaseClass.FinanceData;
import com.cpsdbd.sohelcon.R;
import com.cpsdbd.sohelcon.Utility.Constant;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowDetailDebitDialog extends DialogFragment {

    private FinanceData data;

    private TextView tvTitle,tvDate,tvDescription,tvAmount;
    private ImageButton btnOk;


    public ShowDetailDebitDialog() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!= null){
            data= (FinanceData) getArguments().getSerializable(Constant.GET_FINANCE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Window window = getDialog().getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);

        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_show_detail_debit_dialog, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        tvTitle = (TextView) view.findViewById(R.id.debit_dialog_title);
        tvTitle.setText(Constant.FINANCE_ITEM_DETAIL);

        tvDate = (TextView) view.findViewById(R.id.debit_dialog_date);
        tvDescription = (TextView) view.findViewById(R.id.debit_dialog_description);
        tvAmount = (TextView) view.findViewById(R.id.debit_dialog_amount);
    }

    @Override
    public void onStart() {
        super.onStart();

        if(getArguments()!=null){
            data = (FinanceData) getArguments().getSerializable(Constant.GET_FINANCE);

            if(data!=null){
                tvDate.setText(data.getFinance_transaction_date());
                tvDescription.setText(data.getFinance_particular());
                tvAmount.setText(String.valueOf(data.getFinance_amount()));

            }
        }
    }

}
