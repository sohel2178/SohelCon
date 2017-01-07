package net.optimusbs.sohelcon.Fragments.MainFragment.Store;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;


import net.optimusbs.sohelcon.BaseClass.Store;
import net.optimusbs.sohelcon.BaseClass.StoreData;
import net.optimusbs.sohelcon.ChartAndGraph.MyListener;
import net.optimusbs.sohelcon.R;
import net.optimusbs.sohelcon.Utility.Constant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreGraphFragment extends Fragment {

    private FrameLayout receivedContainer,consumedContainer;

    private List<StoreData> storeDataList;

    private List<Store> storeReceivedList;
    private List<Store> storeConsumedList;

    private Thread receivedThread,consumedThread;
    private Handler receivedHandler,consumedHandler;

    private View receivedChart,consumedChart;

    private MyListener listener;





    public StoreGraphFragment() {
        // Required empty public constructor
    }

    public void setMyListener(MyListener listener){
        this.listener=listener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(listener!= null){
            listener.fragmentChange(4);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!= null){
            storeDataList = (List<StoreData>) getArguments().getSerializable(Constant.STORE_DATA_LIST);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_store_graph, container, false);

        initView(view);

        if(storeDataList!= null){
            //BackGround Received
            receivedHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    Bundle bundle = msg.getData();
                    storeReceivedList = (List<Store>) bundle.getSerializable(Constant.STORE_RECEIVED_LIST);
                    receivedChart = getBarChart(storeReceivedList, Constant.STORE_RECEIVED);
                    BarChart rc = (BarChart) receivedChart;
                    rc.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                        @Override
                        public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                            String item = storeReceivedList.get(e.getXIndex()).getItem();
                            List<StoreData> particularReceivedItem = getParticularReceivedItem(item);

                            Bundle args = new Bundle();
                            args.putSerializable(Constant.PARTICULAR_STORE_RECEIVED_ITEMS, (Serializable) particularReceivedItem);

                            ParticularStoreReceivedFragment psrf = new ParticularStoreReceivedFragment();
                            psrf.setArguments(args);
                            getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_top,R.anim.exit_to_bottom)
                                    .replace(R.id.store_main_container,psrf).addToBackStack(null).commit();
                        }

                        @Override
                        public void onNothingSelected() {

                        }
                    });
                    receivedContainer.addView(receivedChart);
                }
            };
            receivedThread = new Thread(new MyReceivedThread());
            receivedThread.start();

            //BackGround Consumed


            consumedHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    Bundle bundle = msg.getData();
                    storeConsumedList = (List<Store>) bundle.getSerializable(Constant.STORE_CONSUMED_LIST);
                    consumedChart = getBarChart(storeConsumedList,Constant.STORE_CONSUMED);
                    BarChart cc = (BarChart) consumedChart;
                    cc.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                        @Override
                        public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                            String item = storeConsumedList.get(e.getXIndex()).getItem();
                            List<StoreData> particularConsumedItem = getParticularConsumedItem(item);

                            Bundle args = new Bundle();
                            args.putSerializable(Constant.PARTICULAR_STORE_CONSUMED_ITEMS, (Serializable) particularConsumedItem);

                            ParticularStoreConsumedFragment pscf = new ParticularStoreConsumedFragment();
                            pscf.setArguments(args);
                            getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_top,R.anim.exit_to_bottom)
                                    .replace(R.id.store_main_container,pscf).addToBackStack(null).commit();
                        }

                        @Override
                        public void onNothingSelected() {

                        }
                    });
                    consumedContainer.addView(consumedChart);

                }
            };
            consumedThread = new Thread(new MyConsumedThread());
            consumedThread.start();


        }



        return view;
    }

    private void initView(View view){
        receivedContainer = (FrameLayout) view.findViewById(R.id.graph_store_received);
        consumedContainer = (FrameLayout) view.findViewById(R.id.graph_store_consumed);
    }

   // getAll Particular Item Received List when Clicked
    private List<StoreData> getParticularReceivedItem(String item){
        List<StoreData> returnList = new ArrayList<>();
        for(StoreData x: storeDataList){
            if(x.getStore_status()==0){
                if(x.getStore_particular().equals(item)){
                    returnList.add(x);
                }
            }
        }
        return returnList;
    }

    private List<StoreData> getParticularConsumedItem(String item){
        List<StoreData> returnList = new ArrayList<>();
        for(StoreData x: storeDataList){
            if(x.getStore_status()==1){
                if(x.getStore_particular().equals(item)){
                    returnList.add(x);
                }
            }
        }
        return returnList;
    }

    private List<Store> getStoreReceivedList(){
        List<Store> storeList = new ArrayList<>();

        HashSet<String> items = new HashSet<>();

        for(StoreData x: storeDataList){
            items.add(x.getStore_particular());
        }

        for(String y : items){
            storeList.add(getItemReceived(y));

        }

        return storeList;
    }

    private List<Store> getStoreConsumedList(){
        List<Store> storeList = new ArrayList<>();

        HashSet<String> items = new HashSet<>();

        for(StoreData x: storeDataList){
            items.add(x.getStore_particular());
        }

        for(String y : items){
            storeList.add(getItemConsumed(y));

        }

        return storeList;
    }



    private Store getItemReceived(String item){
        double quantity =0;

        for(StoreData x: storeDataList){
            if(x.getStore_particular().equals(item)){
                if(x.getStore_status()==0){
                    quantity=quantity+x.getStore_quantity();
                }
            }
        }

        Store store = new Store(item, (float) quantity);
        return store;
    }

    private Store getItemConsumed(String item){
        double quantity =0;

        for(StoreData x: storeDataList){
            if(x.getStore_particular().equals(item)){
                if(x.getStore_status()==1){
                    quantity=quantity+x.getStore_quantity();
                }
            }
        }

        Store store = new Store(item, (float) quantity);
        return store;
    }

    private View getBarChart(List<Store> storeList, String description){
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

        barChart.animateXY(600,600);

        barChart.getLegend().setEnabled(false);




        List<BarEntry> barEntries = getBarEntries(storeList);
        List<String> barLabels = getBarLabels(storeList);

        BarDataSet dataset = new BarDataSet(barEntries,Constant.EXPENDITURE);
        dataset.setColors(ColorTemplate.JOYFUL_COLORS);
        dataset.setValueTypeface(tf);
        dataset.setDrawValues(true);
        BarData data = new BarData(barLabels,dataset);

        barChart.setData(data);

        //barChart.animateX(1000);
        barChart.setDescription("");
        barChart.setDescriptionTypeface(tf);


        barChart.invalidate();

        return barChart;
    }

    private List<BarEntry> getBarEntries(List<Store> storeList){
        List<BarEntry> barEntryList = new ArrayList<>();

        for(int i=0;i<storeList.size();i++){
            barEntryList.add(new BarEntry(storeList.get(i).getQuantity(),i));
        }

        return barEntryList;
    }

    private List<String> getBarLabels(List<Store> storeList){
        List<String> returnList = new ArrayList<>();

        for(int i=0;i<storeList.size();i++){
            returnList.add(storeList.get(i).getItem());
        }

        return returnList;
    }



    private class MyReceivedThread implements Runnable {

        @Override
        public void run() {
            Message message = Message.obtain();
            List<Store> storeReList = getStoreReceivedList();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.STORE_RECEIVED_LIST, (Serializable) storeReList);

            message.setData(bundle);

            receivedHandler.sendMessage(message);
        }
    }

    private class MyConsumedThread implements Runnable {

        @Override
        public void run() {
            Message message = Message.obtain();
            List<Store> consumedList = getStoreConsumedList();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.STORE_CONSUMED_LIST, (Serializable) consumedList);
            message.setData(bundle);

            consumedHandler.sendMessage(message);
        }
    }

}
