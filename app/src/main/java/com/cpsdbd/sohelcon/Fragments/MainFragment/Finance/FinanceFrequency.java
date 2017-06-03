package com.cpsdbd.sohelcon.Fragments.MainFragment.Finance;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;


import com.cpsdbd.sohelcon.BaseClass.FinanceData;
import com.cpsdbd.sohelcon.ChartAndGraph.MyIntegerFormatter;
import com.cpsdbd.sohelcon.ChartAndGraph.MyListener;
import com.cpsdbd.sohelcon.R;
import com.cpsdbd.sohelcon.Utility.Constant;
import com.cpsdbd.sohelcon.Utility.MyUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FinanceFrequency extends Fragment {

    private List<FinanceData> expenditureList;

    private List<String> uniqueParticular;

    private List<String> particularList;

    private List<Integer> frequencyList;

    private FrameLayout chartContainer;

    private MyListener listener;



    public FinanceFrequency() {
        // Required empty public constructor
    }

    public void setMyListener(MyListener listener){
        this.listener=listener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        listener.fragmentChange(4);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!= null){
            expenditureList= (List<FinanceData>) getArguments().getSerializable(Constant.FINANCE_LIST);

            uniqueParticular = MyUtils.getUniqueParticular(expenditureList);

            particularList = MyUtils.getParticularList(expenditureList);

            frequencyList = getFrequencyList(particularList,uniqueParticular);

        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_finance_frequency, container, false);

        chartContainer = (FrameLayout) view.findViewById(R.id.chart_container);
        chartContainer.addView(getBarChart());

        return view;
    }

    private List<Integer> getFrequencyList(List<String> particularList, List<String> uniqueParticular) {
        List<Integer> returnList = new ArrayList<>();

        for(String x: uniqueParticular){
            returnList.add(Collections.frequency(particularList,x));
        }

        return returnList;
    }

    private View getBarChart(){
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),Constant.T_RAILWAY_REGULAR);
        BarChart barChart = new BarChart(getActivity());

        barChart.setMaxVisibleValueCount(60);

        barChart.setPinchZoom(false);

        barChart.setDrawBarShadow(false);
        barChart.setDrawGridBackground(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setTypeface(tf);
        xAxis.setDrawGridLines(false);



        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisLeft().setTypeface(tf);
        barChart.getAxis(YAxis.AxisDependency.RIGHT).setDrawAxisLine(false);
        barChart.getAxis(YAxis.AxisDependency.RIGHT).setDrawLabels(false);

        barChart.animateXY(1000,1000);
        //barChart.setOnChartValueSelectedListener(this);

        barChart.getLegend().setEnabled(false);

        if(frequencyList!= null){

            List<BarEntry> entries = getBarEntries();
            List<String> labels = getLabels();

            BarDataSet dataset = new BarDataSet(entries,Constant.EXPENDITURE);
            dataset.setColors(ColorTemplate.JOYFUL_COLORS);
            dataset.setValueTextSize(6);
            dataset.setValueTypeface(tf);
            BarData data = new BarData(labels,dataset);
            data.setValueFormatter(new MyIntegerFormatter());

            barChart.setData(data);

            //barChart.animateX(1000);
            barChart.setDescription("");



        }

        barChart.invalidate();

        return barChart;
    }

    private List<BarEntry> getBarEntries() {
        List<BarEntry> barEntries = new ArrayList<>();

        for(int i=0;i<frequencyList.size();i++){
            barEntries.add(new BarEntry(frequencyList.get(i),i));
        }

        return barEntries;

    }

    private List<String> getLabels() {
        List<String> labels = new ArrayList<>();

        for(int i=0;i<uniqueParticular.size();i++){
            labels.add(uniqueParticular.get(i));
        }

        return labels;
    }

}
