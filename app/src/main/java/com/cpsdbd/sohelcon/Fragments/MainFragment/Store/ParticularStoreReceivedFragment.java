package com.cpsdbd.sohelcon.Fragments.MainFragment.Store;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.cpsdbd.sohelcon.Adapter.StoreAdapter;
import com.cpsdbd.sohelcon.BaseClass.StoreData;
import com.cpsdbd.sohelcon.DetailDialogFragmentLongPress.ShowDetailStoreConsumedDialog;
import com.cpsdbd.sohelcon.DetailDialogFragmentLongPress.ShowDetailStoreReceivedDialog;
import com.cpsdbd.sohelcon.R;
import com.cpsdbd.sohelcon.Utility.Constant;
import com.cpsdbd.sohelcon.Utility.FontUtils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParticularStoreReceivedFragment extends Fragment implements StoreAdapter.StoreLongClickListener {

    private List<StoreData> storeDataList;

    private TextView tvStatus,tvTotal,tvTotalText;
    private RecyclerView rvStoreReceived;
    private StoreAdapter adapter;
    private double total;


    public ParticularStoreReceivedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!= null){
            storeDataList= (List<StoreData>) getArguments().getSerializable(Constant.PARTICULAR_STORE_RECEIVED_ITEMS);
            if(storeDataList!= null){
                adapter = new StoreAdapter(getActivity(),storeDataList);
                adapter.setGravitySetter(Constant.GRAVITY_END);
                adapter.setStoreLongClickListener(this);
                total= getTotal();
            }

        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_particular_store_received, container, false);
        initView(view);
        rvStoreReceived.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvStoreReceived.setAdapter(adapter);
        return view;
    }

    private void initView(View view){
        tvStatus = (TextView) view.findViewById(R.id.store_list_header_status);
        tvStatus.setText(Constant.RECEIVED);
        tvStatus.setGravity(Gravity.END);
        tvTotal = (TextView) view.findViewById(R.id.particular_store_received_total);
        tvTotalText = (TextView) view.findViewById(R.id.total);
        rvStoreReceived = (RecyclerView) view.findViewById(R.id.rv_particular_store_received);

        // Set Font
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvTotalText);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvTotal);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvTotal.setText(String.valueOf(total));
    }

    private double getTotal() {
        double tempTotal =0;
        for(StoreData x : storeDataList){
            tempTotal= tempTotal+x.getStore_quantity();
        }
        return tempTotal;
    }


    @Override
    public void onLongClick(int position) {

        StoreData data = storeDataList.get(position);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.GET_STORE,data);

        if(data.getStore_status()==0){
            ShowDetailStoreReceivedDialog rd = new ShowDetailStoreReceivedDialog();
            rd.setArguments(bundle);
            rd.show(getFragmentManager(),Constant.DEFAULAT_FRAGMENT_TAG);
        }else{
            ShowDetailStoreConsumedDialog rc = new ShowDetailStoreConsumedDialog();
            rc.setArguments(bundle);
            rc.show(getFragmentManager(),Constant.DEFAULAT_FRAGMENT_TAG);
        }

    }
}
