package com.cpsdbd.sohelcon.Fragments.MainFragment.Store;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.cpsdbd.sohelcon.Adapter.StoreSummeryAdapter;
import com.cpsdbd.sohelcon.BaseClass.StoreData;
import com.cpsdbd.sohelcon.BaseClass.SummeryStore;
import com.cpsdbd.sohelcon.ChartAndGraph.MyListener;
import com.cpsdbd.sohelcon.R;
import com.cpsdbd.sohelcon.Utility.Constant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreSummery extends Fragment implements StoreSummeryAdapter.StoreSummeryListener {

    private RecyclerView rvStoreSummery;

    private List<StoreData> storeDataList;

    List<SummeryStore> summeryStores;

    private StoreSummeryAdapter adapter;

    private MyListener listener;


    public StoreSummery() {
        // Required empty public constructor
    }

    public void setMyListener(MyListener listener){
        this.listener=listener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(listener!= null){
            listener.fragmentChange(3);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!= null){
            storeDataList = (List<StoreData>) getArguments().getSerializable(Constant.STORE_DATA_LIST);

            if(storeDataList!= null){
                summeryStores = getSummeryStore();
            }

            if(summeryStores!= null){
                adapter = new StoreSummeryAdapter(getActivity(),summeryStores);
                adapter.setStoreSummeryListener(this);
            }
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_store_summery, container, false);
        initView(view);
        rvStoreSummery.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvStoreSummery.setAdapter(adapter);
        return view;
    }

    private void initView(View view) {
        rvStoreSummery = (RecyclerView) view.findViewById(R.id.rv_storeSummery);

    }


    private List<SummeryStore> getSummeryStore() {
        List<SummeryStore> returnList = new ArrayList<>();

        HashSet<String> items = new HashSet<>();

        for(StoreData x : storeDataList){
            items.add(x.getStore_particular());
        }

        for(String y : items){
            returnList.add(getIndividualSummeryStore(y));
        }

        return returnList;

    }

    private SummeryStore getIndividualSummeryStore(String item){

        double totalReceived=0;
        double totalConsumed =0;

        for(StoreData x: storeDataList){
            if(x.getStore_particular().equals(item)){

                if(x.getStore_status()==0){
                    totalReceived = totalReceived+x.getStore_quantity();
                }else{
                    totalConsumed = totalConsumed+x.getStore_quantity();
                }

            }
        }

        double balance = totalReceived-totalConsumed;

        SummeryStore ss = new SummeryStore(item,totalReceived,totalConsumed,balance);

        return ss;
    }

    @Override
    public void onLongClickItem(SummeryStore store) {
        if(storeDataList!= null){
            List<StoreData> newDataList = getDataList(store.getItem());

            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.FILTER_STORE_DATA, (Serializable) newDataList);
            bundle.putDouble(Constant.RECEIVED,store.getTotal_received());
            bundle.putDouble(Constant.CONSUMED,store.getTotal_consumed());
            bundle.putDouble(Constant.BALANCE,store.getBalance());


            IndividualStoreItemDetails isd = new IndividualStoreItemDetails();
            isd.setArguments(bundle);

            getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_top,R.anim.exit_to_bottom)
                    .replace(R.id.store_main_container,isd).addToBackStack(null).commit();
        }
    }

    private List<StoreData> getDataList(String item) {
        List<StoreData> returnList = new ArrayList<>();

        for(StoreData x : storeDataList){
            if(x.getStore_particular().equals(item)){
                returnList.add(x);
            }
        }

        return returnList;
    }
}
