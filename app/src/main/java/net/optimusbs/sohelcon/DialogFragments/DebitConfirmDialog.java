package net.optimusbs.sohelcon.DialogFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;


import net.optimusbs.sohelcon.BaseClass.FinanceData;
import net.optimusbs.sohelcon.CustomView.MyTextView;
import net.optimusbs.sohelcon.Fragments.MainFragment.NewFinanceFragment;
import net.optimusbs.sohelcon.R;
import net.optimusbs.sohelcon.Response.RegisterResponse;
import net.optimusbs.sohelcon.Utility.Constant;
import net.optimusbs.sohelcon.Utility.FieldConstant;
import net.optimusbs.sohelcon.Utility.FontUtils;
import net.optimusbs.sohelcon.Utility.URLs;
import net.optimusbs.sohelcon.Volley.MyPostVolley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class DebitConfirmDialog extends DialogFragment implements View.OnClickListener {

    private MyTextView tvDate,tvDescription,tvAmount,
                        date,description,amount;
    private ImageButton btnNo,btnConfirm;
    private TextView title;
    FinanceData data;
    private Gson gson;


    public DebitConfirmDialog() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        data = (FinanceData) getArguments().getSerializable(Constant.GET_FINANCE);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_debit_confirm_dialog, container, false);

        initView(view);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(getArguments()!= null){
            data = (FinanceData) getArguments().getSerializable(Constant.GET_FINANCE);
            String date = data.getFinance_transaction_date();
            String desc = data.getFinance_particular();
            double amount = data.getFinance_amount();

            tvDate.setText(date);
            tvDescription.setText(desc);
            tvAmount.setText(String.valueOf(amount));

        }
    }

    private void initView(View view) {
        tvDate = (MyTextView) view.findViewById(R.id.debit_dialog_date);
        tvDescription = (MyTextView) view.findViewById(R.id.debit_dialog_description);
        tvAmount = (MyTextView) view.findViewById(R.id.debit_dialog_amount);

        //,,;
        date = (MyTextView) view.findViewById(R.id.date);
        description = (MyTextView) view.findViewById(R.id.description);
        amount = (MyTextView) view.findViewById(R.id.amount);

        title= (TextView) view.findViewById(R.id.title);
        title.setText(Constant.DEBIT_DIALOG_TITLE);

        btnNo = (ImageButton) view.findViewById(R.id.debit_dialog_no);
        btnConfirm = (ImageButton) view.findViewById(R.id.debit_dialog_confirm);

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
        viewList.add(date);
        viewList.add(description);
        viewList.add(amount);
        viewList.add(btnConfirm);
        viewList.add(btnNo);
        viewList.add(title);

        return viewList;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.debit_dialog_no:
                getDialog().dismiss();
                break;

            case R.id.debit_dialog_confirm:
                if(data!=null){
                    Map<String,String> map = new HashMap<>();
                    map.put(FieldConstant.FINANCE_PROJECT_ID, String.valueOf(data.getFinance_project_id()));
                    map.put(FieldConstant.FINANCE_USER_ID, String.valueOf(data.getFinance_user_id()));
                    map.put(FieldConstant.FINANCE_TRANSACTION_DATE,data.getFinance_transaction_date());
                    map.put(FieldConstant.FINANCE_PARTICULAR,data.getFinance_particular());
                    map.put(FieldConstant.FINANCE_AMOUNT, String.valueOf(data.getFinance_amount()));
                    map.put(FieldConstant.FINANCE_HEAD,data.getFinance_head());
                    map.put(FieldConstant.FINANCE_STATUS, String.valueOf(data.getFinance_status()));

                    MyPostVolley myPostVolley = new MyPostVolley(getActivity(),map, URLs.INSERT_TRANSACTION, Constant.INSERT_TRANSACTION);
                    myPostVolley.applyPostVolley();
                    myPostVolley.setVolleyPostListener(new MyPostVolley.VolleyPostListener() {
                        @Override
                        public void getResposefromVolley(String response) {

                            RegisterResponse rr = gson.fromJson(response,RegisterResponse.class);
                            if(rr.getSuccess()==1){
                                getDialog().dismiss();
                                getFragmentManager().popBackStack();
                                getFragmentManager().beginTransaction().replace(R.id.main_container,new NewFinanceFragment()).commit();
                            }
                        }
                    });
                }

                break;
        }
    }
}
