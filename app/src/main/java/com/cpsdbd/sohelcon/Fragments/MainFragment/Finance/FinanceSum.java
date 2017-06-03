package com.cpsdbd.sohelcon.Fragments.MainFragment.Finance;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;


import com.cpsdbd.sohelcon.BaseClass.FinanceData;
import com.cpsdbd.sohelcon.BaseClass.HeadwiseExpenditure;
import com.cpsdbd.sohelcon.ChartAndGraph.MyListener;
import com.cpsdbd.sohelcon.ChartAndGraph.MyValueFormatter;
import com.cpsdbd.sohelcon.R;
import com.cpsdbd.sohelcon.Utility.Constant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FinanceSum extends Fragment implements View.OnClickListener,OnChartValueSelectedListener{

    private TextView tvDebit,tvCredit,tvDebitIndicator,tvCreditIndicator;

    private FrameLayout chartHolder;

    private List<FinanceData> financeDataList;

    private List<HeadwiseExpenditure> headwiseExpenditureList;

    private MyListener listener;


    public FinanceSum() {
        // Required empty public constructor
    }

    public void setMyListener(MyListener listener){
        this.listener = listener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener.fragmentChange(5);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            financeDataList = (List<FinanceData>) getArguments().getSerializable(Constant.FINANCE_LIST);

            if(financeDataList!= null){
                headwiseExpenditureList = getHeadwiseHependitureList();


            }

        }



    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_finance_sum, container, false);
        initView(view);

        tvCreditIndicator.setVisibility(View.INVISIBLE);

        return view;
    }

    private void initView(View view) {
        tvDebit = (TextView) view.findViewById(R.id.debit_summery);
        tvCredit = (TextView) view.findViewById(R.id.credit_summery);
        tvDebitIndicator = (TextView) view.findViewById(R.id.debit_indicator);
        tvCreditIndicator = (TextView) view.findViewById(R.id.credit_indicator);

        chartHolder = (FrameLayout) view.findViewById(R.id.debit_credit_container);

        chartHolder.addView(getBarChart());
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tvDebit.setOnClickListener(this);
        tvCredit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.debit_summery:
                tvDebitIndicator.setVisibility(View.VISIBLE);
                tvCreditIndicator.setVisibility(View.INVISIBLE);
                chartHolder.removeAllViews();
                chartHolder.addView(getBarChart());
                break;

            case R.id.credit_summery:
                tvDebitIndicator.setVisibility(View.INVISIBLE);
                tvCreditIndicator.setVisibility(View.VISIBLE);
                chartHolder.removeAllViews();
                chartHolder.addView(getPieChart());
                break;
        }
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

    private List<BarEntry> getBarEntries() {
        List<BarEntry> barEntries = new ArrayList<>();

        for(int i=0;i<headwiseExpenditureList.size();i++){
            barEntries.add(new BarEntry(headwiseExpenditureList.get(i).getAmount(),i));
        }

        return barEntries;

    }

    private List<String> getLabels() {
        List<String> labels = new ArrayList<>();

        for(int i=0;i<headwiseExpenditureList.size();i++){
            labels.add(headwiseExpenditureList.get(i).getHead());
        }

        return labels;
    }

    private View getBarChart(){
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),Constant.T_RAILWAY_REGULAR);
        BarChart barChart = new BarChart(getActivity());

        barChart.setMaxVisibleValueCount(60);

        barChart.setPinchZoom(false);

        barChart.setDrawBarShadow(false);
        barChart.setDrawGridBackground(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setTypeface(tf);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);



        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisLeft().setTypeface(tf);
        barChart.getAxis(YAxis.AxisDependency.RIGHT).setDrawAxisLine(false);
        barChart.getAxis(YAxis.AxisDependency.RIGHT).setDrawLabels(false);

        barChart.animateXY(1000,1000);
        barChart.setOnChartValueSelectedListener(this);

        barChart.getLegend().setEnabled(false);

        if(headwiseExpenditureList!= null){

            List<BarEntry> entries = getBarEntries();
            List<String> labels = getLabels();

            BarDataSet dataset = new BarDataSet(entries,Constant.EXPENDITURE);
            dataset.setColors(ColorTemplate.JOYFUL_COLORS);
            dataset.setValueTextSize(6);
            dataset.setValueTypeface(tf);
            BarData data = new BarData(labels,dataset);
            data.setValueFormatter(new MyValueFormatter());

            barChart.setData(data);

            //barChart.animateX(1000);
            barChart.setDescription("");



        }

        barChart.invalidate();

        return barChart;
    }

    private View getPieChart(){
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),Constant.T_RAILWAY_REGULAR);

        PieChart pieChart = new PieChart(getActivity());

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.getLegend().setEnabled(false);

        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.setDrawCenterText(true);

        pieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);

        pieChart.animateXY(1000,1000);

        if(headwiseExpenditureList!= null){
            List<Entry> pieEntries = getPieEntries();
            List<String> labels = getLabels();

            PieDataSet dataSet = new PieDataSet(pieEntries,Constant.EXPENDITURE);
            dataSet.setSliceSpace(3f);
            dataSet.setSelectionShift(5f);

            dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
            dataSet.setValueTypeface(tf);
            dataSet.setValueTextSize(6);

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

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        int selectedIndex = e.getXIndex();

        String head = headwiseExpenditureList.get(selectedIndex).getHead();

        List<FinanceData> selectedHeadData = getSelectedHeadData(head);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.HEAD_WISE_EXP_DATA, (Serializable) selectedHeadData);

        HeadwiseExpenditureFragment hef = new HeadwiseExpenditureFragment();
        hef.setArguments(bundle);


        getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_top,R.anim.exit_to_bottom)
                .replace(R.id.finance_main_container,hef).addToBackStack(null).commit();

    }

    private List<FinanceData> getSelectedHeadData(String selectedHead) {

        List<FinanceData> returnList = new ArrayList<>();

        for(FinanceData x: financeDataList){

            if(x.getFinance_head().equals(selectedHead)){
                returnList.add(x);
            }
        }

        return  returnList;


    }

    @Override
    public void onNothingSelected() {

    }
}
