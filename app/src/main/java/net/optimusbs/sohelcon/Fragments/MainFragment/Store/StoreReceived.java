package net.optimusbs.sohelcon.Fragments.MainFragment.Store;


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


import net.optimusbs.sohelcon.BaseClass.Project;
import net.optimusbs.sohelcon.BaseClass.StoreData;
import net.optimusbs.sohelcon.ChartAndGraph.MyListener;
import net.optimusbs.sohelcon.CustomView.MyAutoCompleteTextView;
import net.optimusbs.sohelcon.CustomView.MyEditText;
import net.optimusbs.sohelcon.DialogFragments.StoreReceivedConfirmDialog;
import net.optimusbs.sohelcon.R;
import net.optimusbs.sohelcon.Utility.Constant;
import net.optimusbs.sohelcon.Utility.FontUtils;
import net.optimusbs.sohelcon.Utility.MyUtils;
import net.optimusbs.sohelcon.Utility.UserLocalStore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreReceived extends Fragment implements View.OnClickListener {

    private MyEditText etDate,etQuantity;
    private MyAutoCompleteTextView acParticular,acReceivedFrom,acChallanNo,acMRRNo;
    private Button btnAdd;

    private TextView title;

    private UserLocalStore userLocalStore;

    private List<StoreData> storeDataList;
    private TreeSet<String> uniqueParticular;
    private TreeSet<String> uniqueReceivedFrom;

    private ArrayAdapter particularAdapter;
    private ArrayAdapter receivedFromAdapter;

    private String completionDate;

    private MyListener listener;



    public StoreReceived() {
        // Required empty public constructor
    }

    public void setMyListener(MyListener listener){
        this.listener=listener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(listener!= null){
            listener.fragmentChange(2);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userLocalStore= new UserLocalStore(getActivity());

        completionDate= userLocalStore.getCurrentProject().getProject_completion_date();

        if(getArguments().getSerializable(Constant.STORE_DATA_LIST)!= null){
            storeDataList = (List<StoreData>) getArguments().getSerializable(Constant.STORE_DATA_LIST);

            if(storeDataList != null){
                uniqueParticular=getUniqueParticular();
                uniqueReceivedFrom=getUniqueReceivedFrom();


                List<String> tempParticularList = new ArrayList<>();
                List<String> tempReceivedFromList = new ArrayList<>();

                for (String x : uniqueParticular){
                    tempParticularList.add(x);
                }

                for (String x : uniqueReceivedFrom){
                    tempReceivedFromList.add(x);
                }


                particularAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_dropdown_item_1line, tempParticularList);
                receivedFromAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_dropdown_item_1line, tempReceivedFromList);

            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_store_received, container, false);
        initView(view);

        if(particularAdapter!=null){
            acParticular.setAdapter(particularAdapter);
        }

        if(receivedFromAdapter!=null){
            acReceivedFrom.setAdapter(receivedFromAdapter);
        }
        return view;
    }


    private void initView(View view){
        etDate = (MyEditText) view.findViewById(R.id.store_received_date);
        etQuantity = (MyEditText) view.findViewById(R.id.store_received_quantity);

        acParticular= (MyAutoCompleteTextView) view.findViewById(R.id.store_received_particular);
        acParticular.setThreshold(1);
        acReceivedFrom= (MyAutoCompleteTextView) view.findViewById(R.id.store_received_received_from);
        acReceivedFrom.setThreshold(1);

        acChallanNo= (MyAutoCompleteTextView) view.findViewById(R.id.store_received_challan_no);
        acMRRNo= (MyAutoCompleteTextView) view.findViewById(R.id.store_received_mrr_no);

        btnAdd = (Button) view.findViewById(R.id.store_received_add);

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
        viewList.add(acReceivedFrom);
        viewList.add(acChallanNo);
        viewList.add(acMRRNo);
        viewList.add(btnAdd);
        viewList.add(title);

        return viewList;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnAdd.setOnClickListener(this);
        etDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.store_received_add:
                String date = etDate.getText().toString().trim();
                String quantity = etQuantity.getText().toString().trim();
                String particular = acParticular.getText().toString().trim();
                String received_from = acReceivedFrom.getText().toString().trim();
                String challan_no = acChallanNo.getText().toString().trim();
                String mrr_no = acMRRNo.getText().toString().trim();

                Date compDate = MyUtils.getDateFromString(completionDate);

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

                if(MyUtils.getDateFromString(date).after(compDate)){
                    etDate.requestFocus();
                    Toast.makeText(getActivity(), "Date is after Project Completion Date is not Valid", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(particular)){
                    acParticular.requestFocus();
                    Toast.makeText(getActivity(), "Particular Field is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(received_from)){
                    acReceivedFrom.requestFocus();
                    Toast.makeText(getActivity(), "Received From Field is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(challan_no)){
                    acChallanNo.requestFocus();
                    Toast.makeText(getActivity(), "Challan No Field is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(mrr_no)){
                    acMRRNo.requestFocus();
                    Toast.makeText(getActivity(), "MRR No Field is Empty", Toast.LENGTH_SHORT).show();
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
                    data.setStore_rec_con_date(date);
                    data.setStore_project_id(Integer.parseInt(project_id));
                    data.setStore_user_id(Integer.parseInt(user_id));
                    data.setStore_particular(particular);
                    data.setStore_quantity(Double.parseDouble(quantity));
                    data.setStore_challan_no(challan_no);
                    data.setStore_mrr_no(mrr_no);
                    data.setStore_received_from(received_from);
                    data.setStore_status(0);

                    Bundle args = new Bundle();
                    args.putSerializable(Constant.GET_STORE, data);

                    StoreReceivedConfirmDialog srd = new StoreReceivedConfirmDialog();
                    srd.setArguments(args);
                    srd.show(getFragmentManager(),Constant.DEFAULAT_FRAGMENT_TAG);


                }

                break;

            case R.id.store_received_date:
                MyUtils.showDialogAndSetTime(getActivity(),etDate);
                break;
        }
    }

    private TreeSet<String> getUniqueParticular() {
        TreeSet<String> returnSet = new TreeSet<>();

        for(StoreData x : storeDataList){
            if(x.getStore_status()==0){
                returnSet.add(x.getStore_particular());
            }
        }

        return returnSet;
    }

    private TreeSet<String> getUniqueReceivedFrom() {
        TreeSet<String> returnSet = new TreeSet<>();

        for(StoreData x : storeDataList){
            if(x.getStore_status()==0){
                returnSet.add(x.getStore_received_from());
            }
        }

        return returnSet;
    }
}
