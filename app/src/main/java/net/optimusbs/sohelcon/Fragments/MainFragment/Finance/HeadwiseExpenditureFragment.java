package net.optimusbs.sohelcon.Fragments.MainFragment.Finance;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;


import net.optimusbs.sohelcon.Adapter.FinanceAdapter;
import net.optimusbs.sohelcon.BaseClass.FinanceData;
import net.optimusbs.sohelcon.BaseClass.HeadwiseExpenditure;
import net.optimusbs.sohelcon.BaseClass.User;
import net.optimusbs.sohelcon.ChartAndGraph.MyValueFormatter;
import net.optimusbs.sohelcon.DetailDialogFragmentLongPress.ShowDetailCreditDialog;
import net.optimusbs.sohelcon.R;
import net.optimusbs.sohelcon.Utility.Constant;
import net.optimusbs.sohelcon.Utility.FontUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HeadwiseExpenditureFragment extends Fragment implements FinanceAdapter.FinanceLongClickListener,
        OnChartValueSelectedListener,View.OnClickListener{

    private RecyclerView rvHeadwiseExpenditure;
    private TextView tvTotal,totalText;

    private FinanceAdapter adapter;

    private double total;
    private List<FinanceData> financeDataList;

    private List<HeadwiseExpenditure> headwiseExpenditureList;

    private List<HeadwiseExpenditure> particularWiseExpenditureList;

    private FrameLayout pieContainer;

    private View pieChart;

    private boolean indicator = false;


    public HeadwiseExpenditureFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!= null){


            List<FinanceData> tempList= (List<FinanceData>) getArguments().getSerializable(Constant.HEAD_WISE_EXP_DATA);

            if(tempList!=null){
                financeDataList = (List<FinanceData>) getArguments().getSerializable(Constant.HEAD_WISE_EXP_DATA);
                particularWiseExpenditureList = getParticularWiseExpenditureList();
                indicator =true;
            }else{
                financeDataList = (List<FinanceData>) getArguments().getSerializable(Constant.MONTHLY_DATA);
                headwiseExpenditureList = getHeadwiseHependitureList();
                indicator =true;
            }





            if(financeDataList!= null){
                adapter = new FinanceAdapter(getActivity(),financeDataList);
                adapter.setFinanceLongClickListener(this);
                total = getTotal();
            }

        }


    }

    private double getTotal() {
        total =0;

        for(FinanceData x : financeDataList ){
            total=total+x.getFinance_amount();
        }

        return total;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_headwise_expenditure, container, false);
        initView(view);
        rvHeadwiseExpenditure.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(adapter!= null){
            rvHeadwiseExpenditure.setAdapter(adapter);
        }

        if(headwiseExpenditureList!= null){
            pieChart = getPieChart();
            pieContainer.addView(pieChart);

            if(indicator){
                pieContainer.setVisibility(View.VISIBLE);
            }
        }

        if(particularWiseExpenditureList!= null){
            pieChart = getParticularPieChart();

            pieContainer.addView(pieChart);

            if(indicator){
                pieContainer.setVisibility(View.VISIBLE);
            }
        }


        return  view;
    }

    private void initView(View view) {

        tvTotal = (TextView) view.findViewById(R.id.headwise_expenditure_total);
        totalText = (TextView) view.findViewById(R.id.totalText);
        rvHeadwiseExpenditure = (RecyclerView) view.findViewById(R.id.rv_head_wise_expenditure);
        pieContainer = (FrameLayout) view.findViewById(R.id.pie_container);

        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvTotal);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,totalText);
    }

    @Override
    public void onStart() {
        super.onStart();

        tvTotal.setText(String.valueOf(total));



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        pieContainer.setOnClickListener(this);
    }

    private List<HeadwiseExpenditure> getHeadwiseHependitureList() {

        List<HeadwiseExpenditure> rerurnList = new ArrayList<>();

        HashSet<String> uniqueHead = new HashSet<>();

        // First get the Unique Head

        for (FinanceData x: financeDataList){
            if(x.getFinance_status()==1){
                uniqueHead.add(x.getFinance_head());
            }
        }

        for(String head: uniqueHead){

            float amount = 0;

            for(FinanceData exp : financeDataList){
                if(exp.getFinance_head().equals(head)){
                    amount= (float) (amount+exp.getFinance_amount());
                }
            }

            HeadwiseExpenditure hwe = new HeadwiseExpenditure(head,amount);

            rerurnList.add(hwe);
        }

        return rerurnList;
    }

    private List<HeadwiseExpenditure> getParticularWiseExpenditureList() {

        List<HeadwiseExpenditure> rerurnList = new ArrayList<>();

        HashSet<String> uniqueParticular = new HashSet<>();

        // First get the Unique Head

        for (FinanceData x: financeDataList){
            if(x.getFinance_status()==1){
                uniqueParticular.add(x.getFinance_particular());
            }
        }

        for(String particular: uniqueParticular){

            float amount = 0;

            for(FinanceData exp : financeDataList){
                if(exp.getFinance_particular().equals(particular)){
                    amount= (float) (amount+exp.getFinance_amount());
                }
            }

            HeadwiseExpenditure hwe = new HeadwiseExpenditure(particular,amount);

            rerurnList.add(hwe);
        }

        return rerurnList;
    }

    private List<String> getLabels() {
        List<String> labels = new ArrayList<>();

        for(int i=0;i<headwiseExpenditureList.size();i++){
            labels.add(headwiseExpenditureList.get(i).getHead());
        }

        return labels;
    }

    private List<String> getParticularLabels() {
        List<String> labels = new ArrayList<>();

        for(int i=0;i<particularWiseExpenditureList.size();i++){
            labels.add(particularWiseExpenditureList.get(i).getHead());
        }

        return labels;
    }


    private View getPieChart(){
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),Constant.T_RAILWAY_REGULAR);

        PieChart pieChart = new PieChart(getActivity());

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.setDrawCenterText(true);
        pieChart.getLegend().setEnabled(false);

        pieChart.setRotationAngle(0);
        pieChart.setOnChartValueSelectedListener(this);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);

        pieChart.animateXY(1000,1000);

        if(headwiseExpenditureList!= null){
            List<Entry> pieEntries = getPieEntries();
            List<String> labels = getLabels();

            PieDataSet dataSet = new PieDataSet(pieEntries,Constant.EXPENDITURE);
            dataSet.setSliceSpace(3f);
            dataSet.setSelectionShift(5f);
            dataSet.setValueTextSize(6);
            dataSet.setValueTypeface(tf);

            dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

            PieData data = new PieData(labels,dataSet);
            data.setValueFormatter(new MyValueFormatter());
            pieChart.setData(data);

            pieChart.setDescription("");



        }

        pieChart.invalidate();

        return pieChart;

    }

    private View getParticularPieChart(){
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),Constant.T_RAILWAY_REGULAR);

        PieChart pieChart = new PieChart(getActivity());

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.getLegend().setEnabled(false);

        pieChart.setDrawCenterText(true);

        pieChart.setOnChartValueSelectedListener(this);

        pieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);

        pieChart.animateXY(1000,1000);

        if(particularWiseExpenditureList!= null){
            List<Entry> pieEntries = getParticularPieEntries();
            List<String> labels = getParticularLabels();

            PieDataSet dataSet = new PieDataSet(pieEntries,Constant.PARTICULAR_WISE_EXPENDITURE);
            dataSet.setSliceSpace(3f);
            dataSet.setSelectionShift(5f);

            dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
            dataSet.setValueTextSize(6);
            dataSet.setValueTypeface(tf);

            PieData data = new PieData(labels,dataSet);
            data.setValueFormatter(new MyValueFormatter());
            pieChart.setData(data);

            pieChart.setDescription("");



        }

        pieChart.invalidate();

        return pieChart;

    }

    private List<Entry> getPieEntries() {
        List<Entry> pieEntries = new ArrayList<>();

        for(int i=0;i<headwiseExpenditureList.size();i++){
            pieEntries.add(new Entry(headwiseExpenditureList.get(i).getAmount(),i));
        }

        return pieEntries;

    }

    private List<Entry> getParticularPieEntries() {
        List<Entry> pieEntries = new ArrayList<>();

        for(int i=0;i<particularWiseExpenditureList.size();i++){
            pieEntries.add(new Entry(particularWiseExpenditureList.get(i).getAmount(),i));
        }

        return pieEntries;

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

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        int selectedIndex = e.getXIndex();

        if(headwiseExpenditureList!= null){
            String head = headwiseExpenditureList.get(selectedIndex).getHead();

            List<FinanceData> myData = getOnClickData(head);

            adapter = new FinanceAdapter(getActivity(),myData);
            adapter.setFinanceLongClickListener(this);

            rvHeadwiseExpenditure.setAdapter(adapter);
            total = getNewTotal(myData);
            tvTotal.setText(String.valueOf(total));



        }

        if(particularWiseExpenditureList!= null){
            String particular = particularWiseExpenditureList.get(selectedIndex).getHead();

            List<FinanceData> myData = getParticulars(particular);

            adapter = new FinanceAdapter(getActivity(),myData);
            adapter.setFinanceLongClickListener(this);

            rvHeadwiseExpenditure.setAdapter(adapter);
            total = getNewTotal(myData);
            tvTotal.setText(String.valueOf(total));
        }

    }

    private double getNewTotal(List<FinanceData> myData) {
        double total=0;

        for(FinanceData x: myData){
            total = total+x.getFinance_amount();
        }
        return total;
    }

    private List<FinanceData> getOnClickData(String head) {
        List<FinanceData> returnList = new ArrayList<>();

        for(FinanceData x :financeDataList){
            if(x.getFinance_head().equals(head)){
                returnList.add(x);
            }
        }

        return returnList;
    }

    private List<FinanceData> getParticulars(String particular){
        List<FinanceData> returnList = new ArrayList<>();

        for(FinanceData x :financeDataList){
            if(x.getFinance_particular().equals(particular)){
                returnList.add(x);
            }
        }

        return returnList;
    }

    @Override
    public void onNothingSelected() {


    }

    @Override
    public void onClick(View view) {
        Log.d("Test","TTTT");
        adapter= new FinanceAdapter(getActivity(),financeDataList);
        adapter.setFinanceLongClickListener(this);

        rvHeadwiseExpenditure.setAdapter(adapter);
        total = getTotal();
        tvTotal.setText(String.valueOf(total));


    }
}
