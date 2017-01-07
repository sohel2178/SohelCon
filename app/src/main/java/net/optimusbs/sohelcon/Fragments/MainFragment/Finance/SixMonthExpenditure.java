package net.optimusbs.sohelcon.Fragments.MainFragment.Finance;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.joanzapata.iconify.widget.IconButton;


import net.optimusbs.sohelcon.BaseClass.FinanceData;
import net.optimusbs.sohelcon.BaseClass.MonthlyChartData;
import net.optimusbs.sohelcon.BaseClass.MyDate;
import net.optimusbs.sohelcon.ChartAndGraph.MyListener;
import net.optimusbs.sohelcon.ChartAndGraph.MyValueFormatter;
import net.optimusbs.sohelcon.R;
import net.optimusbs.sohelcon.Utility.Constant;
import net.optimusbs.sohelcon.Utility.MyUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SixMonthExpenditure extends Fragment implements View.OnClickListener, OnChartValueSelectedListener{

    private List<FinanceData> financeDataList;

    private IconButton btnPlus,btnMinus,btnFore,btnBack;

    private FrameLayout chartContainer;
    private MyListener listener;


    int progress = 0;

    private int correctSize;

    private int monthDiff;

    private List<MonthlyChartData> monthLyChartDataList;

    private List<MyDate> myDateList;


    public SixMonthExpenditure() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!= null){
            financeDataList = (List<FinanceData>) getArguments().getSerializable(Constant.FINANCE_LIST);

            List<Date> dateList = MyUtils.getDateListFromFinanceData(financeDataList);

            Date maxDate = MyUtils.maxDate(dateList);
            Date minDate = MyUtils.minDate(dateList);

            monthDiff = MyUtils.monthDifference(minDate,maxDate);



            myDateList = MyUtils.getMydateList(minDate,monthDiff);

            monthLyChartDataList = MyUtils.getMonthLiChartData(myDateList,financeDataList);

        }
    }

    public void setMyListener(MyListener listener){
        this.listener = listener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener.fragmentChange(6);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_six_month_expenditure, container, false);

        initView(view);

        View chartView = getBarChart(0);
        chartContainer.addView(chartView);
        return view;
    }

    private void initView(View view) {
        btnPlus = (IconButton) view.findViewById(R.id.btn_seek_plus);
        btnMinus = (IconButton) view.findViewById(R.id.btn_seek_minus);
        btnFore = (IconButton) view.findViewById(R.id.btn_seek_fore);
        btnBack = (IconButton) view.findViewById(R.id.btn_seek_back);

        chartContainer = (FrameLayout) view.findViewById(R.id.monthly_chart_container);



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnPlus.setOnClickListener(this);
        btnMinus.setOnClickListener(this);
        btnFore.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_seek_plus:
                if(progress>=0 && progress<monthDiff-5){
                    progress++;
                }
                break;

            case R.id.btn_seek_minus:

                if(progress>0 && progress<=monthDiff-5){
                    progress--;
                }

                break;

            case R.id.btn_seek_fore:
                if(progress>=0 && progress<monthDiff-5){
                    progress=progress+6;

                    if(progress>=monthDiff-5){
                        progress=monthDiff-5;
                    }
                }
                break;

            case R.id.btn_seek_back:

                if(progress>0 && progress<=monthDiff-5){
                    progress = progress-6;

                    if(progress<=0){
                        progress=0;
                    }

                }

                break;
        }




        View chart = getBarChart(progress);
        chartContainer.removeAllViews();
        chartContainer.addView(chart);

    }

    private View getBarChart(int startPosition){
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),Constant.T_RAILWAY_REGULAR);
        BarChart barChart = new BarChart(getActivity());

        barChart.setMaxVisibleValueCount(30);

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



        List<BarEntry> barEntries = getBarEntries(startPosition);
        List<String> barLabels = getBarLabels(startPosition);

        BarDataSet dataset = new BarDataSet(barEntries,Constant.EXPENDITURE);
        dataset.setColors(ColorTemplate.JOYFUL_COLORS);
        dataset.setValueTypeface(tf);
        dataset.setValueTextSize(6);
        dataset.setDrawValues(true);
        BarData data = new BarData(barLabels,dataset);
        data.setValueFormatter(new MyValueFormatter());

        barChart.setData(data);

        //barChart.animateX(1000);
        barChart.setDescription("");

        barChart.invalidate();

        return barChart;
    }


    private List<BarEntry> getBarEntries(int startPosition){
        List<BarEntry> returnList = new ArrayList<>();

        if(monthLyChartDataList!= null){
            int size = monthLyChartDataList.size();

            if(size<6){
                correctSize=size;
            }else {
                correctSize=6;
            }

            Log.d("CORRECTSIZE",correctSize+"");

            for (int i=0;i<correctSize;i++){
                returnList.add(new BarEntry((float) monthLyChartDataList.get(i+startPosition).getAmount(),i));
            }
        }

        return returnList;
    }

    private List<String> getBarLabels(int startPosition){
        List<String> returnList = new ArrayList<>();

        if(monthLyChartDataList!= null){

            int size = monthLyChartDataList.size();

            if(size<6){
                correctSize=size;
            }else {
                correctSize=6;
            }

            for( int i=0;i<correctSize;i++){
                returnList.add(monthLyChartDataList.get(i+startPosition).getMonth_year());
            }
        }

        return returnList;
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        int exactPosition = e.getXIndex()+progress;

        if(myDateList!= null){
            MyDate myDate = myDateList.get(exactPosition);

            Date startDate = myDate.getStartDateofMonth();
            Date endDate = myDate.getEndDateofMonth();

            if(financeDataList!= null){
                List<FinanceData> monthlyFinanceList = getListOfExpenditureInAMonth(startDate,endDate);

                Log.d("Test",monthlyFinanceList.size()+"");

                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.MONTHLY_DATA, (Serializable) monthlyFinanceList);

                HeadwiseExpenditureFragment hwe = new HeadwiseExpenditureFragment();
                hwe.setArguments(bundle);
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_top,R.anim.exit_to_bottom)
                        .replace(R.id.finance_main_container,hwe).addToBackStack(null).commit();
            }
        }


    }

    @Override
    public void onNothingSelected() {

    }


    private List<FinanceData> getListOfExpenditureInAMonth(Date startDate, Date endDate){
        List<FinanceData> returnList = new ArrayList<>();

        for(FinanceData x: financeDataList){
            if(x.getFinance_status()==1){
                String dateStr = x.getFinance_transaction_date();

                Date thisDate = MyUtils.getDateFromString(dateStr);

                if(thisDate.compareTo(startDate)>=0 && thisDate.compareTo(endDate)<=0){
                    returnList.add(x);
                }
            }
        }

        return returnList;
    }
}
