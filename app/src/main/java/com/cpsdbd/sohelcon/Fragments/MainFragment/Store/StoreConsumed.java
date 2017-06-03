package com.cpsdbd.sohelcon.Fragments.MainFragment.Store;


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


import com.cpsdbd.sohelcon.BaseClass.Project;
import com.cpsdbd.sohelcon.BaseClass.StoreData;
import com.cpsdbd.sohelcon.ChartAndGraph.MyListener;
import com.cpsdbd.sohelcon.CustomView.MyAutoCompleteTextView;
import com.cpsdbd.sohelcon.CustomView.MyEditText;
import com.cpsdbd.sohelcon.DialogFragments.StoreConsumedConfirmDialog;
import com.cpsdbd.sohelcon.R;
import com.cpsdbd.sohelcon.Utility.Constant;
import com.cpsdbd.sohelcon.Utility.FontUtils;
import com.cpsdbd.sohelcon.Utility.MyUtils;
import com.cpsdbd.sohelcon.Utility.UserLocalStore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreConsumed extends Fragment implements View.OnClickListener{
    private MyEditText etDate,etQuantity;
    private MyAutoCompleteTextView acParticular,acIssueTo,acWhereUsed;
    private Button btnAdd;
    private TextView title;

    private UserLocalStore userLocalStore;

    private List<StoreData> storeDataList;
    private TreeSet<String> uniqueParticular;
    private TreeSet<String> uniqueIssueTo;
    private TreeSet<String> uniqueWhereUsed;

    private ArrayAdapter particularAdapter;
    private ArrayAdapter issueToAdapter;
    private ArrayAdapter whereUsedAdapter;

    private String completionDate;

    private MyListener listener;



    public StoreConsumed() {
        // Required empty public constructor
    }

    public void setMyListener(MyListener listener){
        this.listener=listener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(listener!= null){
            listener.fragmentChange(1);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userLocalStore = new UserLocalStore(getActivity());

        completionDate = userLocalStore.getCurrentProject().getProject_completion_date();

        if(getArguments().getSerializable(Constant.STORE_DATA_LIST)!= null){
            storeDataList = (List<StoreData>) getArguments().getSerializable(Constant.STORE_DATA_LIST);

            if(storeDataList != null){
                uniqueParticular=getUniqueParticular();
                uniqueIssueTo=getUniqueIssueTo();
                uniqueWhereUsed=getUniqueWhwreUsed();


                List<String> tempParticularList = new ArrayList<>();
                List<String> tempIssueToList = new ArrayList<>();
                List<String> tempWhereUsedList = new ArrayList<>();

                for (String x : uniqueParticular){
                    tempParticularList.add(x);
                }

                for (String x : uniqueIssueTo){
                    tempIssueToList.add(x);
                }

                for (String x : uniqueWhereUsed){
                    tempWhereUsedList.add(x);
                }


                particularAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_dropdown_item_1line, tempParticularList);
                issueToAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_dropdown_item_1line, tempIssueToList);
                whereUsedAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_dropdown_item_1line, tempWhereUsedList);

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_store_consumed, container, false);

        initView(view);
        if(particularAdapter!=null){
            acParticular.setAdapter(particularAdapter);
        }

        if(issueToAdapter!=null){
            acIssueTo.setAdapter(issueToAdapter);
        }

        if(whereUsedAdapter!=null){
            acWhereUsed.setAdapter(whereUsedAdapter);
        }
        return view;
    }

    private void initView(View view) {
        etDate = (MyEditText) view.findViewById(R.id.store_consumed_date);
        etQuantity = (MyEditText) view.findViewById(R.id.store_consumed_quantity);

        acParticular = (MyAutoCompleteTextView) view.findViewById(R.id.store_consumed_particular);
        acParticular.setThreshold(1);

        acIssueTo = (MyAutoCompleteTextView) view.findViewById(R.id.store_consumed_issue_to);
        acIssueTo.setThreshold(1);

        acWhereUsed = (MyAutoCompleteTextView) view.findViewById(R.id.store_consumed_where_used);
        acWhereUsed.setThreshold(1);

        btnAdd = (Button) view.findViewById(R.id.store_consumed_add);

        title = (TextView) view.findViewById(R.id.title);

        List<View> views = generateViewList();

        // Set Font
        FontUtils.setFonts(views,Constant.T_RAILWAY_REGULAR);

    }

    private List<View> generateViewList() {
        List<View> viewList = new ArrayList<>();
        viewList.add(etDate);
        viewList.add(etQuantity);
        viewList.add(acParticular);
        viewList.add(acIssueTo);
        viewList.add(acWhereUsed);
        viewList.add(btnAdd);
        viewList.add(title);

        return viewList;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        etDate.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.store_consumed_date:
                MyUtils.showDialogAndSetTime(getActivity(),etDate);
                break;

            case R.id.store_consumed_add:
                String rec_con_date = etDate.getText().toString().trim();
                String quantity = etQuantity.getText().toString().trim();
                String particular = acParticular.getText().toString().trim();
                String issue_to= acIssueTo.getText().toString().trim();
                String where_used= acWhereUsed.getText().toString().trim();

                Date compmDate = MyUtils.getDateFromString(completionDate);

                if(TextUtils.isEmpty(rec_con_date)){
                    etDate.requestFocus();
                    Toast.makeText(getActivity(), "Date Field is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!MyUtils.isThisDateValid(rec_con_date,"dd-MM-yyyy")){
                    etDate.requestFocus();
                    Toast.makeText(getActivity(), "Date is not Valid", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(MyUtils.getDateFromString(rec_con_date).after(compmDate)){
                    etDate.requestFocus();
                    Toast.makeText(getActivity(), "Date is after Project Completion Date", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(particular)){
                    acParticular.requestFocus();
                    Toast.makeText(getActivity(), "Particular Field is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(where_used)){
                    acWhereUsed.requestFocus();
                    Toast.makeText(getActivity(), "Where Used Field is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(quantity)){
                    etQuantity.requestFocus();
                    Toast.makeText(getActivity(), "Quantity Field is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                Project currentProject = userLocalStore.getCurrentProject();

                if(currentProject.getProject_id()!=-1){
                    String project_id = String.valueOf(currentProject.getProject_id());
                    String user_id = String.valueOf(userLocalStore.getLoggedInUser().getUser_id());

                    StoreData data = new StoreData();
                    data.setStore_rec_con_date(rec_con_date);
                    data.setStore_project_id(Integer.parseInt(project_id));
                    data.setStore_user_id(Integer.parseInt(user_id));
                    data.setStore_particular(particular);
                    data.setStore_quantity(Double.parseDouble(quantity));
                    data.setStore_issue_to(issue_to);
                    data.setStore_where_used(where_used);
                    data.setStore_status(1);

                    Bundle args = new Bundle();
                    args.putSerializable(Constant.GET_STORE, data);

                    StoreConsumedConfirmDialog scd = new StoreConsumedConfirmDialog();
                    scd.setArguments(args);
                    scd.show(getFragmentManager(),Constant.DEFAULAT_FRAGMENT_TAG);


                }



                break;
        }
    }

    private TreeSet<String> getUniqueParticular() {
        TreeSet<String> returnSet = new TreeSet<>();

        for(StoreData x : storeDataList){
            if(x.getStore_status()==1){
                returnSet.add(x.getStore_particular());
            }
        }

        return returnSet;
    }

    private TreeSet<String> getUniqueIssueTo() {
        TreeSet<String> returnSet = new TreeSet<>();

        for(StoreData x : storeDataList){
            if(x.getStore_status()==1){
                returnSet.add(x.getStore_issue_to());
            }
        }

        return returnSet;
    }

    private TreeSet<String> getUniqueWhwreUsed() {
        TreeSet<String> returnSet = new TreeSet<>();

        for(StoreData x : storeDataList){
            if(x.getStore_status()==1){
                returnSet.add(x.getStore_where_used());
            }
        }

        return returnSet;
    }
}
