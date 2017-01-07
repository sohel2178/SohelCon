package net.optimusbs.sohelcon.Fragments.MainFragment.Finance;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;


import net.optimusbs.sohelcon.Adapter.FinanceAdapter;
import net.optimusbs.sohelcon.BaseClass.FinanceData;
import net.optimusbs.sohelcon.ChartAndGraph.MyChangeListener;
import net.optimusbs.sohelcon.ChartAndGraph.MyListener;
import net.optimusbs.sohelcon.CustomView.MyTextView;
import net.optimusbs.sohelcon.DetailDialogFragmentLongPress.ShowDetailCreditDialog;
import net.optimusbs.sohelcon.R;
import net.optimusbs.sohelcon.Utility.Constant;
import net.optimusbs.sohelcon.Utility.FontUtils;
import net.optimusbs.sohelcon.Utility.MyUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DailyTransaction extends Fragment implements FinanceAdapter.FinanceLongClickListener,View.OnClickListener,MyChangeListener {

    private TextView tvTitle,tvDebitValue,tvCreditValue,tvTotal;

    private MyTextView tvDate;
    private RecyclerView rvDailyTransaction;

    private IconTextView calendar;

    private FinanceAdapter adapter;

    private List<FinanceData> financeDataList;
    private List<FinanceData> selectedDateList;
    private String date;

    private MyListener listener;


    public DailyTransaction() {
        // Required empty public constructor
    }

    public void setMyListener(MyListener listener){
        this.listener = listener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener.fragmentChange(3);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Date today = new Date();
        date = MyUtils.dateToString(today);


        if(getArguments()!= null){
            financeDataList= (List<FinanceData>) getArguments().getSerializable(Constant.FINANCE_LIST);
            if(financeDataList!= null){
                selectedDateList = getSelectedDateList(date);
                adapter = new FinanceAdapter(getActivity(),selectedDateList);
                adapter.setFinanceLongClickListener(this);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daily_transaction, container, false);
        initView(view);
        rvDailyTransaction.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvDailyTransaction.setAdapter(adapter);
        return view;
    }

    private void initView(View view) {
        tvTitle = (TextView) view.findViewById(R.id.title);
        tvDebitValue = (TextView) view.findViewById(R.id.debitValue);
        tvCreditValue = (TextView) view.findViewById(R.id.creditValue);
        tvDate = (MyTextView) view.findViewById(R.id.tvDate_sohel);
        tvTotal = (TextView) view.findViewById(R.id.totalText);

        tvDate.setMyChangeListener(this);


        calendar = (IconTextView) view.findViewById(R.id.calendar);

        tvDate.setText(date);


        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvTitle);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvDebitValue);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvCreditValue);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvDate);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvTotal);

        //tvTitle.setText("Transaction On "+date);
        rvDailyTransaction = (RecyclerView) view.findViewById(R.id.rvDailyTransaction);

        setDabitCreditValue();
    }

    private void setDabitCreditValue() {
        double debit=0;
        double credit=0;
        for (FinanceData x: selectedDateList){
            if(x.getFinance_status()==0){
                debit=debit+x.getFinance_amount();
            }else if(x.getFinance_status()==1){
                credit=credit+x.getFinance_amount();
            }
        }

        tvDebitValue.setText(String.valueOf(debit));
        tvCreditValue.setText(String.valueOf(credit));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        calendar.setOnClickListener(this);

    }

    @Override
    public void onItemLongClick(int position) {

        FinanceData data = financeDataList.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.GET_FINANCE,data);

        ShowDetailCreditDialog cd = new ShowDetailCreditDialog();
        cd.setArguments(bundle);
        cd.show(getFragmentManager(),Constant.DEFAULAT_FRAGMENT_TAG);

    }

    private List<FinanceData> getSelectedDateList(String date){
        List<FinanceData> returnList = new ArrayList<>();

        for(FinanceData x: financeDataList){
            if(x.getFinance_transaction_date().equals(date)){
                returnList.add(x);
            }
        }
        return returnList;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.calendar:
                MyUtils.showDialogAndSetTime(getActivity(),tvDate);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void textChange(String value) {

        Log.d("MYVAL",value);

        List<FinanceData> selData = getSelectedDateList(value);
        selectedDateList.clear();
        selectedDateList.addAll(selData);
        adapter.notifyDataSetChanged();
        setDabitCreditValue();


    }
}
