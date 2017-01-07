package net.optimusbs.sohelcon.Fragments.MainFragment.Store;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import net.optimusbs.sohelcon.Adapter.StoreAdapter;
import net.optimusbs.sohelcon.BaseClass.StoreData;
import net.optimusbs.sohelcon.R;
import net.optimusbs.sohelcon.Utility.Constant;
import net.optimusbs.sohelcon.Utility.FontUtils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndividualStoreItemDetails extends Fragment {

    private RecyclerView rvFilterStore;
    private TextView tvReceived,tvConsumed,tvBalance,txtReceived,txtConsumed,txtBalance;

    private StoreAdapter adapter;

    private List<StoreData> datalist;

    private double received,consumed,balance;


    public IndividualStoreItemDetails() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!= null){
            datalist = (List<StoreData>) getArguments().getSerializable(Constant.FILTER_STORE_DATA);

            if(datalist!= null){
                adapter = new StoreAdapter(getActivity(),datalist);
            }
            received = getArguments().getDouble(Constant.RECEIVED);
            consumed = getArguments().getDouble(Constant.CONSUMED);
            balance = getArguments().getDouble(Constant.BALANCE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_individual_store_item_details, container, false);

        initView(view);

        rvFilterStore.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFilterStore.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        tvReceived.setText(String.valueOf(received));
        tvConsumed.setText(String.valueOf(consumed));
        tvBalance.setText(String.valueOf(balance));
    }

    private void initView(View view) {


        rvFilterStore = (RecyclerView) view.findViewById(R.id.rv_individual_store_item);
        tvReceived= (TextView) view.findViewById(R.id.individual_store_item_received);
        tvConsumed= (TextView) view.findViewById(R.id.individual_store_item_consumed);
        tvBalance= (TextView) view.findViewById(R.id.individual_store_item_balance);
        txtReceived= (TextView) view.findViewById(R.id.received);
        txtConsumed= (TextView) view.findViewById(R.id.consumed);
        txtBalance= (TextView) view.findViewById(R.id.balance);

        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvReceived);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvConsumed);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvBalance);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,txtReceived);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,txtConsumed);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,txtBalance);
    }

}
