package com.cpsdbd.sohelcon.DialogFragments;


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

import com.google.gson.Gson;


import com.cpsdbd.sohelcon.BaseClass.FinanceData;
import com.cpsdbd.sohelcon.CustomView.MyTextView;
import com.cpsdbd.sohelcon.Fragments.MainFragment.NewFinanceFragment;
import com.cpsdbd.sohelcon.R;
import com.cpsdbd.sohelcon.Response.RegisterResponse;
import com.cpsdbd.sohelcon.Utility.Constant;
import com.cpsdbd.sohelcon.Utility.FieldConstant;
import com.cpsdbd.sohelcon.Utility.FontUtils;
import com.cpsdbd.sohelcon.Utility.URLs;
import com.cpsdbd.sohelcon.Volley.MyPostVolley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreditConfirmDialog extends DialogFragment implements View.OnClickListener {

    private MyTextView tvDate,tvDescription,tvHead,tvAmount,
                        date,description,head,amount;
    private ImageButton btnNo,btnConfirm;
    FinanceData data;
    private Gson gson;

    private TextView title;


    public CreditConfirmDialog() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson= new Gson();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View view= inflater.inflate(R.layout.fragment_credit_confirm_dialog, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(getArguments()!= null){
            data = (FinanceData) getArguments().getSerializable(Constant.GET_FINANCE);

            if(data!= null){
                String date = data.getFinance_transaction_date();
                String desc = data.getFinance_particular();
                double amount = data.getFinance_amount();
                String head = data.getFinance_head();

                tvDate.setText(date);
                tvDescription.setText(desc);
                tvAmount.setText(String.valueOf(amount));
                tvHead.setText(head);
            }

        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.credit_dialog_no:
                getDialog().dismiss();
                break;

            case R.id.credit_dialog_confirm:
                if(data!=null){
                    Map<String,String> map = new HashMap<>();
                    map.put(FieldConstant.FINANCE_PROJECT_ID, String.valueOf(data.getFinance_project_id()));
                    map.put(FieldConstant.FINANCE_USER_ID, String.valueOf(data.getFinance_user_id()));
                    map.put(FieldConstant.FINANCE_TRANSACTION_DATE,data.getFinance_transaction_date());
                    map.put(FieldConstant.FINANCE_PARTICULAR,data.getFinance_particular());
                    map.put(FieldConstant.FINANCE_AMOUNT, String.valueOf(data.getFinance_amount()));
                    map.put(FieldConstant.FINANCE_HEAD,data.getFinance_head());
                    map.put(FieldConstant.FINANCE_STATUS, String.valueOf(data.getFinance_status()));

                    MyPostVolley myPostVolley = new MyPostVolley(getActivity(), map, URLs.INSERT_TRANSACTION, Constant.INSERT_TRANSACTION);
                    myPostVolley.applyPostVolley();
                    myPostVolley.setVolleyPostListener(new MyPostVolley.VolleyPostListener() {


                        @Override
                        public void getResposefromVolley(String response) {

                            RegisterResponse rr = gson.fromJson(response, RegisterResponse.class);
                            if (rr.getSuccess() == 1) {
                                getFragmentManager().popBackStack();
                                getFragmentManager().beginTransaction().replace(R.id.main_container, new NewFinanceFragment()).commit();
                                getDialog().dismiss();
                            }
                        }
                    });
                }

                break;
        }

    }

    private void initView(View view) {
        tvDate = (MyTextView) view.findViewById(R.id.credit_dialog_date);
        tvDescription = (MyTextView) view.findViewById(R.id.credit_dialog_description);
        tvAmount = (MyTextView) view.findViewById(R.id.credit_dialog_amount);
        tvHead = (MyTextView) view.findViewById(R.id.credit_dialog_head);

        //,,,;

        date = (MyTextView) view.findViewById(R.id.date);
        description = (MyTextView) view.findViewById(R.id.description);
        head = (MyTextView) view.findViewById(R.id.head);
        amount = (MyTextView) view.findViewById(R.id.amount);

        title = (TextView) view.findViewById(R.id.title);
        title.setText(Constant.CREDIT_DIALOG_TITLE);

        btnNo = (ImageButton) view.findViewById(R.id.credit_dialog_no);
        btnConfirm = (ImageButton) view.findViewById(R.id.credit_dialog_confirm);

        btnNo.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        List<View> views = generateViewList();
        // Set Font
        FontUtils.setFonts(views,Constant.T_RAILWAY_REGULAR);
    }

    private List<View> generateViewList() {
        List<View> viewList = new ArrayList<>();
        viewList.add(tvDate);
        viewList.add(tvDescription);
        viewList.add(tvAmount);
        viewList.add(tvHead);
        viewList.add(date);
        viewList.add(description);
        viewList.add(head);
        viewList.add(amount);
        viewList.add(btnConfirm);
        viewList.add(btnNo);
        viewList.add(title);

        return viewList;
    }
}
