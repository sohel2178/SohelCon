package com.cpsdbd.sohelcon.Fragments.MainFragment.Activity;


import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;


import com.cpsdbd.sohelcon.BaseClass.ActivityData;
import com.cpsdbd.sohelcon.BaseClass.Gannt.Gantt;
import com.cpsdbd.sohelcon.ChartAndGraph.MyListener;
import com.cpsdbd.sohelcon.DetailDialogFragmentLongPress.ActivityDetailDialog;
import com.cpsdbd.sohelcon.R;
import com.cpsdbd.sohelcon.Utility.Constant;
import com.cpsdbd.sohelcon.Utility.UserLocalStore;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GanttChartFragment extends Fragment implements Gantt.GanttChartLongPressListener {
    private HorizontalScrollView gantt_Chart;

    private List<ActivityData> activityDataList;
    Gantt gantt;

    private MyListener listener;

    private UserLocalStore userLocalStore;

    private Typeface tf;






    public GanttChartFragment() {
        // Required empty public constructor
    }

    public void setMyListener(MyListener listener){
        this.listener=listener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        listener.fragmentChange(2);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!= null){

            tf =Typeface.createFromAsset(getActivity().getAssets(),Constant.T_RAILWAY_REGULAR);

            userLocalStore= new UserLocalStore(getActivity());
            activityDataList= (List<ActivityData>) getArguments().getSerializable(Constant.ACTIVITY_LIST);
            gantt = new Gantt(getActivity(),activityDataList,userLocalStore.getCurrentProject().getProject_name());
            gantt.setTableHeaderTextColor(ResourcesCompat.getColor(getResources(), R.color.text_color_white,null));
            gantt.setMainBarColor(ResourcesCompat.getColor(getResources(),R.color.colorPrimary,null));
            gantt.setTopTableHeaderBackGroundColor(ResourcesCompat.getColor(getResources(),R.color.another_color,null));

            // TypefaceSet
            gantt.setTopTableHeaderTypeface(tf);
            gantt.setTableHeaderTypeface(tf);
            gantt.setTableDataTypeface(tf);

            // Text Size
            gantt.setTopTableHeaderTextSize(14);
            gantt.setTableHeaderTextSize(11);
            gantt.setTableDataTextSize(10);

            //Text Color
            gantt.setTopTableHeaderTextColor(ResourcesCompat.getColor(getResources(),R.color.white,null));






            gantt.setGanttChartLongPressListener(this);







        }
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_gantt_chart, container, false);
        gantt_Chart = (HorizontalScrollView) view.findViewById(R.id.gantt_chart);


        gantt_Chart.addView(gantt.test());

        return view;
    }

    @Override
    public void OnLongClickTrackinBar(List<ActivityData> activityDataList) {
        Log.d("MYTEST",activityDataList.size()+"");
    }

    @Override
    public void OnLongClickMainBar(ActivityData data) {
        showDialogFragment(data);


    }

    @Override
    public void onLongClickProgressBar(ActivityData data) {
        showDialogFragment(data);
    }


    private void showDialogFragment(ActivityData data){
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.GET_ACTIVITY,data);
        ActivityDetailDialog add = new ActivityDetailDialog();
        add.setArguments(bundle);
        add.show(getFragmentManager(),Constant.DEFAULAT_FRAGMENT_TAG);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);


    }

}
