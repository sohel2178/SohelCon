package com.cpsdbd.sohelcon.Fragments.MainFragment.Finance;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;


import com.cpsdbd.sohelcon.BaseClass.FinanceData;
import com.cpsdbd.sohelcon.BaseClass.Project;
import com.cpsdbd.sohelcon.BaseClass.User;
import com.cpsdbd.sohelcon.ChartAndGraph.MyListener;
import com.cpsdbd.sohelcon.CustomView.MyAutoCompleteTextView;
import com.cpsdbd.sohelcon.CustomView.MyEditText;
import com.cpsdbd.sohelcon.DialogFragments.CreditConfirmDialog;
import com.cpsdbd.sohelcon.R;
import com.cpsdbd.sohelcon.Utility.Constant;
import com.cpsdbd.sohelcon.Utility.FontUtils;
import com.cpsdbd.sohelcon.Utility.MyUtils;
import com.cpsdbd.sohelcon.Utility.UserLocalStore;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreditFragment extends Fragment implements View.OnClickListener{

    private MyAutoCompleteTextView acParticular,acHead;
    private MyEditText etDate,etAmount;
    private Button btnAdd;

    private TextView title;

    private UserLocalStore userLocalStore;
    private Gson gson;

    private List<FinanceData> financeDataList;
    private TreeSet<String> uniqueDescription;
    private TreeSet<String> uniqueHead;

    private ArrayAdapter headAdapter;
    private ArrayAdapter descriptionAdapter;

    private MyListener listener;




    public CreditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        listener.fragmentChange(1);

    }

    public void setMyListener(MyListener listener){
        this.listener=listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userLocalStore= new UserLocalStore(getActivity());
        gson= new Gson();




        if(getArguments()!=null){
            financeDataList= (List<FinanceData>) getArguments().getSerializable(Constant.FINANCE_LIST);

            if(financeDataList != null){
                uniqueDescription=getUniqueDescription();
                uniqueHead=getUniqueHead();


                List<String> tempDescList = new ArrayList<>();
                List<String> tempHeadList = new ArrayList<>();

                for (String x : uniqueDescription){
                    tempDescList.add(x);
                }

                for (String x : uniqueHead){
                    tempHeadList.add(x);
                }


                descriptionAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_dropdown_item_1line, tempDescList);
                headAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_dropdown_item_1line, tempHeadList);

            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_credit, container, false);

        initView(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }



    private void initView(View view) {
        acHead = (MyAutoCompleteTextView) view.findViewById(R.id.credit_head);
        acHead.setThreshold(1);
        acParticular = (MyAutoCompleteTextView) view.findViewById(R.id.credit_description);
        acParticular.setThreshold(1);

        if(headAdapter!= null){
            acHead.setAdapter(headAdapter);
        }

        if(descriptionAdapter!= null){
            acParticular.setAdapter(descriptionAdapter);
        }

        etDate = (MyEditText) view.findViewById(R.id.credit_trasaction_date);
        etDate.setOnClickListener(this);
        etAmount = (MyEditText) view.findViewById(R.id.credit_amount);
        title = (TextView) view.findViewById(R.id.title);
        btnAdd = (Button) view.findViewById(R.id.credit_add);
        btnAdd.setOnClickListener(this);

        setFont();
    }

    private void setFont(){
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,title);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,etDate);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,etAmount);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,btnAdd);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,acHead);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,acParticular);
    }

    //get Unique Description from FinancedataList
    private TreeSet<String> getUniqueDescription() {
        TreeSet<String> returnSet = new TreeSet<>();

        for(FinanceData x : financeDataList){
            if(x.getFinance_status()==1){
                returnSet.add(x.getFinance_particular());
            }
        }

        return returnSet;
    }

    private TreeSet<String> getUniqueHead() {
        TreeSet<String> returnSet = new TreeSet<>();

        for(FinanceData x : financeDataList){
            if(x.getFinance_status()==1){
                returnSet.add(x.getFinance_head());
            }
        }

        return returnSet;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.credit_add:
                String date = etDate.getText().toString().trim();
                String particular = acParticular.getText().toString().trim();
                String head = acHead.getText().toString().trim();
                String amount = etAmount.getText().toString().trim();

                if(TextUtils.isEmpty(date)){
                    etDate.requestFocus();
                    Toast.makeText(getActivity(), "Date Field is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!MyUtils.isThisDateValid(date,"dd-MM-yyyy")){
                    etDate.requestFocus();
                    Toast.makeText(getActivity(), "Date is not Valid", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(particular)){
                    acParticular.requestFocus();
                    Toast.makeText(getActivity(), "Description Field is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(head)){
                    acHead.requestFocus();
                    Toast.makeText(getActivity(), "Description Field is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(amount)){
                    etAmount.requestFocus();
                    Toast.makeText(getActivity(), "Amount Field is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                Project currentProject = userLocalStore.getCurrentProject();
                User user =userLocalStore.getLoggedInUser();

                if(currentProject.getProject_id()!=-1) {

                    String project_id = String.valueOf(currentProject.getProject_id());
                    String user_id = String.valueOf(user.getUser_id());
                    FinanceData data = new FinanceData();
                    data.setFinance_transaction_date(date);
                    data.setFinance_project_id(Integer.parseInt(project_id));
                    data.setFinance_user_id(Integer.parseInt(user_id));
                    data.setFinance_particular(particular);
                    data.setFinance_amount(Double.parseDouble(amount));
                    data.setFinance_head(head);
                    data.setFinance_status(1);

                    Bundle bundle2 = new Bundle();
                    bundle2.putSerializable(Constant.GET_FINANCE, data);

                    CreditConfirmDialog cfd = new CreditConfirmDialog();
                    cfd.setArguments(bundle2);
                    cfd.show(getFragmentManager(),Constant.DEFAULAT_FRAGMENT_TAG);




                }

                break;


            case R.id.credit_trasaction_date:
                MyUtils.showDialogAndSetTime(getActivity(),etDate);
                break;
        }
    }
}
