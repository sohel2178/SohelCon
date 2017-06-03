package com.cpsdbd.sohelcon.Fragments.MainFragment.Store;


import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;


import com.cpsdbd.sohelcon.Adapter.StoreAdapter;
import com.cpsdbd.sohelcon.BaseClass.StoreData;
import com.cpsdbd.sohelcon.DetailDialogFragmentLongPress.ShowDetailStoreConsumedDialog;
import com.cpsdbd.sohelcon.DetailDialogFragmentLongPress.ShowDetailStoreReceivedDialog;
import com.cpsdbd.sohelcon.R;
import com.cpsdbd.sohelcon.Utility.Constant;
import com.cpsdbd.sohelcon.Utility.FontUtils;
import com.cpsdbd.sohelcon.Utility.MyUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParticularStoreConsumedFragment extends Fragment implements StoreAdapter.StoreLongClickListener {

    private List<StoreData> storeDataList;

    // Declare View Componenet
    private TextView tvStatus,tvTotal,tvTotalText;
    private RecyclerView rvStoreConsumed;
    private FrameLayout lineChartContainer;
    private StoreAdapter adapter;
    private double total;

    // BackGround Thread
    private Thread myGraphThread;

    private Handler myGraphHandler;

    private View lineChart;

    private List<StoreData> mychartData;


    public ParticularStoreConsumedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!= null){
            mychartData= new ArrayList<>();
            storeDataList = (List<StoreData>) getArguments().getSerializable(Constant.PARTICULAR_STORE_CONSUMED_ITEMS);


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
        View view= inflater.inflate(R.layout.fragment_particular_store_consumed, container, false);
        initView(view);
        rvStoreConsumed.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvStoreConsumed.setAdapter(adapter);

        if(storeDataList!= null){

            myGraphHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    List<StoreData> mychartData = (List<StoreData>) msg.getData().getSerializable(Constant.CHART_DATA);

                    if(mychartData!= null){
                        lineChart = getLineChart(mychartData,"");
                        lineChartContainer.addView(lineChart);
                    }

                }
            };

            myGraphThread = new Thread(new MyParticularConsumption(storeDataList));
            myGraphThread.start();
        }


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    private void initView(View view){
        tvStatus = (TextView) view.findViewById(R.id.store_list_header_status);
        tvStatus.setText(Constant.CONSUMED);
        tvStatus.setGravity(Gravity.END);

        tvTotalText = (TextView) view.findViewById(R.id.total);
        tvTotal = (TextView) view.findViewById(R.id.particular_store_consumed_total);
        rvStoreConsumed = (RecyclerView) view.findViewById(R.id.rv_particular_store_consumed);
        lineChartContainer = (FrameLayout) view.findViewById(R.id.store_consumption_line_chart_container);

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
        double tempTotal = 0;
        for (StoreData x : storeDataList) {
            tempTotal = tempTotal + x.getStore_quantity();
        }
        return tempTotal;
    }



    private View getLineChart(List<StoreData> chartDataList, String description){
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),Constant.T_RAILWAY_REGULAR);
        LineChart lineChart = new LineChart(getActivity());

        lineChart.setPinchZoom(true);

        lineChart.setDrawGridBackground(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setTypeface(tf);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(ResourcesCompat.getColor(getResources(),R.color.ash,null));

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setTypeface(tf);
        yAxis.setTextColor(ResourcesCompat.getColor(getResources(),R.color.ash,null));

        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxis(YAxis.AxisDependency.RIGHT).setDrawAxisLine(false);
        lineChart.getAxis(YAxis.AxisDependency.RIGHT).setDrawLabels(false);

        lineChart.animateXY(600,600);

        lineChart.getLegend().setEnabled(false);




        List<Entry> lineEntris = getLineEntries(chartDataList);
        List<String> lineLabels = getLineLabels(chartDataList);

        LineDataSet dataset = new LineDataSet(lineEntris,Constant.EXPENDITURE);
        dataset.setColors(ColorTemplate.JOYFUL_COLORS);
        dataset.setDrawValues(true);
        dataset.setDrawCubic(true);
        dataset.setValueTextColor(ResourcesCompat.getColor(getResources(),R.color.colorPrimary,null));
        dataset.setValueTypeface(tf);
        dataset.setValueTextSize(6);
        LineData data = new LineData(lineLabels,dataset);

        lineChart.setData(data);
        //barChart.animateX(1000);
        lineChart.setDescription(description);
        lineChart.invalidate();

        return lineChart;
    }

    private List<Entry> getLineEntries(List<StoreData> chartDataList){
        List<Entry> entries = new ArrayList<>();

        for(int i=0;i<chartDataList.size();i++){
            entries.add(new Entry((float) chartDataList.get(i).getStore_quantity(),i));
        }
        return entries;
    }

    private List<String> getLineLabels(List<StoreData> chartDataList){
        List<String> stringList = new ArrayList<>();

        for(int i=0;i<chartDataList.size();i++){
            stringList.add(chartDataList.get(i).getStore_rec_con_date());
        }
        return stringList;
    }


    private class MyParticularConsumption implements Runnable {
        private List<StoreData> storeDataList;

        public MyParticularConsumption(List<StoreData> storeDataList){
            this.storeDataList=storeDataList;
        }

        @Override
        public void run() {
            List<Date> dateList = MyUtils.getDateListFromStoreData(storeDataList);
            Date maxDate=MyUtils.maxDate(dateList);
            Date minDate=MyUtils.minDate(dateList);

            int dateDiff = MyUtils.dayDiff(minDate,maxDate);

            List<StoreData> myData = MyUtils.getDailyConsumptionGraphData(minDate,dateDiff,storeDataList);

            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.CHART_DATA, (Serializable) myData);

            Message message = Message.obtain();
            message.setData(bundle);

            myGraphHandler.sendMessage(message);


        }
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
