package net.optimusbs.sohelcon.Fragments.MainFragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import net.optimusbs.sohelcon.Adapter.FinanceAdapter;
import net.optimusbs.sohelcon.BaseClass.FinanceData;
import net.optimusbs.sohelcon.ChartAndGraph.MyListener;
import net.optimusbs.sohelcon.DetailDialogFragmentLongPress.ShowDetailCreditDialog;
import net.optimusbs.sohelcon.R;
import net.optimusbs.sohelcon.Utility.Constant;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FinanceFragment extends Fragment implements View.OnClickListener,FinanceAdapter.FinanceLongClickListener{
    private static final int REQUEST_CODE=22;

    private RecyclerView rvFinace;
    private LinearLayout header;

    private List<FinanceData> financeDataList;
    private FinanceAdapter adapter;


    private int acdesIndicator=0;

    private MyListener listener;



    public FinanceFragment() {
        // Required empty public constructor
    }

    public void setMyLisener(MyListener lisener){
        this.listener=lisener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!= null){
            financeDataList = (List<FinanceData>) getArguments().getSerializable(Constant.FINANCE_LIST);
            adapter = new FinanceAdapter(getActivity(),financeDataList);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(listener!=null){
            listener.fragmentChange(7);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_finance, container, false);
        //Init View
        initView(view);

        rvFinace.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(adapter!=null){
            rvFinace.setAdapter(adapter);
        }



        return view;
    }




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter.setFinanceLongClickListener(this);
        header.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.FINANCE_LIST, (Serializable) financeDataList);

        switch (view.getId()){

            case R.id.finance_layout_first:

                acdesIndicator++;
                if(acdesIndicator%2==1){
                    accendingList();
                }else{
                    decendingList();
                }
                adapter.notifyDataSetChanged();

                break;


        }
    }


    private void initView(View view) {
        rvFinace = (RecyclerView) view.findViewById(R.id.finace_rv);
        header = (LinearLayout) view.findViewById(R.id.finance_layout_first);

    }


   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_CODE){
            String dateStr = data.getStringExtra(Constant.SELECTED_DATE);

            List<FinanceData> dailyDataList =getDataList(dateStr);

            if(dailyDataList.size()>0){
                DailyTransaction dt = new DailyTransaction();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.FINANCE_LIST, (Serializable) dailyDataList);
                bundle.putString(Constant.SELECTED_DATE,dateStr);
                dt.setArguments(bundle);
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left)
                        .replace(R.id.main_container,dt).addToBackStack(null).commit();

            }else{
                new MyDialog(getActivity(), "Daily Transaction","No Data Found in "+dateStr);
            }

        }
    }*/

    /*private List<FinanceData> getDataList(String dateStr) {
        List<FinanceData> returnList = new ArrayList<>();

        for(FinanceData x: financeDataList){
            if(x.getFinance_transaction_date().equals(dateStr)){
                returnList.add(x);
            }
        }
        return returnList;
    }*/

    // Handle Long Press
    @Override
    public void onItemLongClick(int position) {

        FinanceData data = financeDataList.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.GET_FINANCE,data);

        ShowDetailCreditDialog cd = new ShowDetailCreditDialog();
        cd.setArguments(bundle);
        cd.show(getFragmentManager(),Constant.DEFAULAT_FRAGMENT_TAG);

    }





    private void accendingList(){

        Collections.sort(financeDataList, new Comparator<FinanceData>() {
            @Override
            public int compare(FinanceData t1, FinanceData t2) {
                String dateStr1 = t1.getFinance_transaction_date();
                String dateStr2 = t2.getFinance_transaction_date();

                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                Date d1 = null;
                Date d2 = null;
                try {
                    d1 = formatter.parse(dateStr1);
                    d2 = formatter.parse(dateStr2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return d1.compareTo(d2);
            }
        });

    }

    private void decendingList(){
        Collections.sort(financeDataList, new Comparator<FinanceData>() {
            @Override
            public int compare(FinanceData t1, FinanceData t2) {
                String dateStr1 = t1.getFinance_transaction_date();
                String dateStr2 = t2.getFinance_transaction_date();

                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                Date d1 = null;
                Date d2 = null;
                try {
                    d1 = formatter.parse(dateStr1);
                    d2 = formatter.parse(dateStr2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return d2.compareTo(d1);
            }
        });
    }


}
