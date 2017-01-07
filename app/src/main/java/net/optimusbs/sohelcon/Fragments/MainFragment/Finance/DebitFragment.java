package net.optimusbs.sohelcon.Fragments.MainFragment.Finance;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;


import net.optimusbs.sohelcon.BaseClass.FinanceData;
import net.optimusbs.sohelcon.BaseClass.Project;
import net.optimusbs.sohelcon.BaseClass.User;
import net.optimusbs.sohelcon.ChartAndGraph.MyListener;
import net.optimusbs.sohelcon.CustomView.MyAutoCompleteTextView;
import net.optimusbs.sohelcon.CustomView.MyEditText;
import net.optimusbs.sohelcon.DialogFragments.DebitConfirmDialog;
import net.optimusbs.sohelcon.R;
import net.optimusbs.sohelcon.Utility.Constant;
import net.optimusbs.sohelcon.Utility.FontUtils;
import net.optimusbs.sohelcon.Utility.MyUtils;
import net.optimusbs.sohelcon.Utility.UserLocalStore;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * A simple {@link Fragment} subclass.
 */
public class DebitFragment extends Fragment implements View.OnClickListener{

    private MyEditText etDate,etAmount;
    private MyAutoCompleteTextView etParticular;
    private Button btnAdd;

    private TextView title;

    private UserLocalStore userLocalStore;
    private Gson gson;

    private List<FinanceData> financeDataList;
    private TreeSet<String> uniqueDescription;

    private ArrayAdapter adapter;

    private MyListener listener;

    View myView;



    public DebitFragment() {
        // Required empty public constructor
    }

    public void setMyListener(MyListener listener){
        this.listener = listener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener.fragmentChange(2);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userLocalStore= new UserLocalStore(getActivity());
        gson = new Gson();

        if(getArguments()!=null){
            financeDataList= (List<FinanceData>) getArguments().getSerializable(Constant.FINANCE_LIST);

            if(financeDataList != null){
                uniqueDescription=getUniqueDescription();

                List<String> tempList = new ArrayList<>();

                for (String x : uniqueDescription){
                    tempList.add(x);
                }

                adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_dropdown_item_1line, tempList);

            }
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_debit, container, false);
        // Initialize View
        initView(view);
        return view;
    }

    private void initView(View view) {
        etDate = (MyEditText) view.findViewById(R.id.debit_trasaction_date);
        etParticular = (MyAutoCompleteTextView) view.findViewById(R.id.debit_description);
        etParticular.setThreshold(1);
        //Check Adapter and Assign it
        if(adapter != null){
            etParticular.setAdapter(adapter);
        }
        ///
        etAmount = (MyEditText) view.findViewById(R.id.debit_amount);

        btnAdd = (Button) view.findViewById(R.id.debit_add);
        btnAdd.setOnClickListener(this);
        etDate.setOnClickListener(this);
        title = (TextView) view.findViewById(R.id.title);

        setFont();
    }

    private void setFont() {
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,title);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,btnAdd);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,etDate);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,etParticular);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,etAmount);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.debit_add:

                String date = etDate.getText().toString().trim();
                String particular = etParticular.getText().toString().trim();
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
                    etParticular.requestFocus();
                    Toast.makeText(getActivity(), "Description Field is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(amount)){
                    etAmount.requestFocus();
                    Toast.makeText(getActivity(), "Amount Field is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                Project currentProject = userLocalStore.getCurrentProject();

                User user = userLocalStore.getLoggedInUser();




                if(currentProject.getProject_id()!=-1){
                    String project_id = String.valueOf(currentProject.getProject_id());
                    String user_id = String.valueOf(user.getUser_id());


                    Log.d("MYTEST",project_id+"");
                    Log.d("MYTEST",user_id+"");

                    FinanceData data = new FinanceData();
                    data.setFinance_transaction_date(date);
                    data.setFinance_project_id(Integer.parseInt(project_id));
                    data.setFinance_user_id(Integer.parseInt(user_id));
                    data.setFinance_particular(particular);
                    data.setFinance_amount(Double.parseDouble(amount));
                    data.setFinance_head("");
                    data.setFinance_status(0);

                    Bundle bundle2 = new Bundle();
                    bundle2.putSerializable(Constant.GET_FINANCE, data);

                    DebitConfirmDialog dfd = new DebitConfirmDialog();
                    dfd.setArguments(bundle2);
                    dfd.show(getFragmentManager(),Constant.DEFAULAT_FRAGMENT_TAG);


                }

                break;

            case R.id.debit_trasaction_date:
                MyUtils.showDialogAndSetTime(getActivity(),etDate);
                break;



        }





    }



    //get Unique Description from FinancedataList
    private TreeSet<String> getUniqueDescription() {
        TreeSet<String> returnSet = new TreeSet<>();

        for(FinanceData x : financeDataList){
            if(x.getFinance_status()==0){
                returnSet.add(x.getFinance_particular());
            }
        }

        return returnSet;
    }






}
