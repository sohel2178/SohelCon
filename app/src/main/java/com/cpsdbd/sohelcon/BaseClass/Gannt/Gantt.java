package com.cpsdbd.sohelcon.BaseClass.Gannt;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.cpsdbd.sohelcon.BaseClass.ActivityData;
import com.cpsdbd.sohelcon.R;
import com.cpsdbd.sohelcon.Utility.MyUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Sohel on 10/18/2016.
 */

public class Gantt {
    private static final String ID="ID";
    private static final String TASK="TASK";
    private static final String START_DATE="START DATE";
    private static final String FINISHED_DATE="FINISHED DATE";
    private static final String DURATION="DURATION";

    private Context context;
    private List<ActivityData> dataList;
    private String projectName;

    private GanttChartLongPressListener listener;

    private Date firstDate,lastDate;
    private int totalDays;
    private int widthOfEachDay;
    private int progress_text_width;
    private int dataSize;

    private int tableWidth;
    private int tableRowHeight;
    private LinearLayout.LayoutParams tableLayoutParam;
    private LinearLayout.LayoutParams tableRowLayoutParam;
    private LinearLayout.LayoutParams topTableHeaderParam;
    private int idWeight,taskWeight,startDateWeight,finishedDateWeight,durationWeight;

    private  LinearLayout.LayoutParams idParam,taskParam,stParam,finParam,duParam;

    private Typeface tableDataTypeface;
    private int tableDataTextSize;
    private int tableDataTextColor;

    private Typeface tableHeaderTypeface;
    private int tableHeaderTextSize;
    private int tableHeaderTextColor;

    private Typeface topTableHeaderTypeface;
    private int topTableHeaderTextSize;
    private int topTableHeaderTextColor;

    private Typeface yearHeaderTypeFace;
    private int yearHeaderTextSize;
    private int yearHeaderTextColor;

    private Typeface monthHeaderTypeFace;
    private int monthHeaderTextSize;
    private int monthHeaderTextColor;

    private Typeface progressTextTypeface;
    private int progressTextTextSize;
    private int progressTextTextColor;


    private int tableHeaderBackgroundColor;
    private int tableDataBackgroundColor;
    private int topTableHeaderBackGroundColor;
    private int yearHeaderBackgroundColor;
    private int monthHeaderBackgroundColor;
    private int progressTextBackgroundColor;
    private int mainBarColor;
    private int progressBarColor;
    private int trackingBarColor;








    public Gantt(Context context, List<ActivityData> dataList, String projectName) {
        this.context = context;
        this.dataList = dataList;
        this.projectName = projectName;

        Map<String,String> firstLastMap = MyUtils.getFirstDateAndLastDateMap(dataList);
        this.firstDate = MyUtils.getDateFromString(firstLastMap.get("firstDate").toString());
        this.lastDate =MyUtils.getDateFromString(firstLastMap.get("lastDate").toString());
        this.totalDays = MyUtils.dayDiff(firstDate,lastDate);
        this.tableWidth=600;
        this.tableRowHeight =75;
        this.widthOfEachDay=130;
        this.progress_text_width=130;
        this.dataSize = dataList.size();

        // Table Layout Param
        tableLayoutParam = new LinearLayout.LayoutParams(tableWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        topTableHeaderParam = new LinearLayout.LayoutParams(tableWidth,tableRowHeight);
        tableRowLayoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, tableRowHeight);







        // Weight of table Text View
        idWeight=1;
        taskWeight=5;
        startDateWeight=5;
        finishedDateWeight=5;
        durationWeight=4;

        idParam = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,idWeight);
        taskParam = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,taskWeight);
        stParam = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,startDateWeight);
        finParam = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,finishedDateWeight);
        duParam = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,durationWeight);

        //Set Default Table Data Properties
        this.tableDataTypeface = Typeface.DEFAULT;
        this.tableDataTextSize = 12;
        this.tableDataTextColor = Color.BLACK;

        //Set Default Table Header Properties
        this.tableHeaderTypeface = Typeface.DEFAULT;
        this.tableHeaderTextSize = 14;
        this.tableHeaderTextColor = Color.BLACK;

        this.topTableHeaderTypeface=Typeface.DEFAULT;
        this.topTableHeaderTextSize=14;
        this.topTableHeaderTextColor=Color.BLACK;

        this.yearHeaderTypeFace=Typeface.DEFAULT;
        this.yearHeaderTextSize=14;
        this.yearHeaderTextColor=Color.BLACK;

        this.monthHeaderTypeFace=Typeface.DEFAULT;
        this.monthHeaderTextSize=14;
        this.monthHeaderTextColor=Color.BLACK;

        this.progressTextTypeface=Typeface.DEFAULT;
        this.progressTextTextSize=14;
        this.progressTextTextColor=Color.BLACK;

        this.tableHeaderBackgroundColor = Color.BLUE;
        this.tableDataBackgroundColor = Color.LTGRAY;
        this.topTableHeaderBackGroundColor = Color.GREEN;
        this.yearHeaderBackgroundColor = Color.GRAY;
        this.monthHeaderBackgroundColor = Color.GRAY;
        this.mainBarColor = Color.LTGRAY;
        this.progressBarColor = Color.LTGRAY;
        this.progressTextBackgroundColor = Color.GREEN;
        this.trackingBarColor = Color.RED;

    }

    public void setGanttChartLongPressListener(GanttChartLongPressListener listener){
        this.listener=listener;
    }

    public View test(){
        LinearLayout topContainer = new LinearLayout(context);
        topContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        topContainer.setOrientation(LinearLayout.VERTICAL);
        topContainer.addView(test1());
        topContainer.addView(test2());
        topContainer.addView(test3());

        return topContainer;

    }

    public View test1(){
       LinearLayout topContainer = new LinearLayout(context);
        topContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        topContainer.setOrientation(LinearLayout.HORIZONTAL);
        topContainer.addView(getTopTableHeader());
        topContainer.addView(getYearHeaderOfGanntChart());
        return topContainer;
    }

    public View test2(){
        LinearLayout topContainer = new LinearLayout(context);
        topContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        topContainer.setOrientation(LinearLayout.HORIZONTAL);
        topContainer.addView(createTableHeader());
        topContainer.addView(getMonthDayHeaderOfGanntChart());
        return topContainer;
    }

    public View test3(){
        ScrollView scrollView = new ScrollView(context);
        LinearLayout topContainer = new LinearLayout(context);
        topContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        topContainer.setOrientation(LinearLayout.HORIZONTAL);
        topContainer.addView(createMainTableView());
        topContainer.addView(mainGanttChart());

        scrollView.addView(topContainer);
        return scrollView;
    }

    public View createTableView(){
        LinearLayout tableLayout = new LinearLayout(context);
        tableLayout.setLayoutParams(tableLayoutParam);
        tableLayout.setOrientation(LinearLayout.VERTICAL);
        tableLayout.addView(getTopTableHeader());
        tableLayout.addView(createTableHeader());
        tableLayout.addView(createMainTableView());

        return tableLayout;
    }



    private View createMainTableView(){
        LinearLayout tableLayout = new LinearLayout(context);
        tableLayout.setLayoutParams(tableLayoutParam);
        tableLayout.setOrientation(LinearLayout.VERTICAL);

        for(int i=0;i<dataList.size();i++){
            LinearLayout tableRowLayout = new LinearLayout(context);
            tableRowLayout.setLayoutParams(tableRowLayoutParam);
            tableRowLayout.setOrientation(LinearLayout.HORIZONTAL);
            tableRowLayout.setBackgroundColor(tableDataBackgroundColor);

            TextView idText = generateTableDataTextView(String.valueOf(i+1),idParam);
            TextView taskText = generateTableDataTextView(dataList.get(i).getActivity_name(),taskParam);
            TextView startDateText = generateTableDataTextView(dataList.get(i).getActivity_start_date(),stParam);
            TextView finishedDateDateText = generateTableDataTextView(dataList.get(i).getActivity_finished_date(),finParam);
            Date startDate = MyUtils.getDateFromString(dataList.get(i).getActivity_start_date());
            Date finishedDate = MyUtils.getDateFromString(dataList.get(i).getActivity_finished_date());
            int duration =MyUtils.dayDiff(startDate,finishedDate);
            TextView durationText = generateTableDataTextView(duration+"d",duParam);

            tableRowLayout.addView(idText);
            tableRowLayout.addView(taskText);
            tableRowLayout.addView(startDateText);
            tableRowLayout.addView(finishedDateDateText);
            tableRowLayout.addView(durationText);

            ImageView divider = new ImageView(context);
            divider.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            divider.setImageResource(R.drawable.divider);

            tableLayout.addView(tableRowLayout);
            tableLayout.addView(divider);

        }

        return tableLayout;
    }

    private View createTableHeader(){
        LinearLayout tableHeaderLayout = new LinearLayout(context);
        tableHeaderLayout.setBackgroundColor(tableHeaderBackgroundColor);
        tableHeaderLayout.setLayoutParams(tableLayoutParam);
        tableHeaderLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView idText = generateTableHeaderTextView(ID,idParam);
        TextView taskText = generateTableHeaderTextView(TASK,taskParam);
        TextView startDateText = generateTableHeaderTextView(START_DATE,stParam);
        TextView finishedDateText = generateTableHeaderTextView(FINISHED_DATE,finParam);
        TextView durationText = generateTableHeaderTextView(DURATION,duParam);

        tableHeaderLayout.addView(idText);
        tableHeaderLayout.addView(taskText);
        tableHeaderLayout.addView(startDateText);
        tableHeaderLayout.addView(finishedDateText);
        tableHeaderLayout.addView(durationText);

        return tableHeaderLayout;
    }

    private TextView generateTableDataTextView(String text,ViewGroup.LayoutParams param){
        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setTypeface(tableDataTypeface);
        textView.setTextSize(tableDataTextSize);


        //textViewId.setWidth((int) MethodUtil.convertDpToPixel(50,context));
        textView.setTextColor(tableDataTextColor);
        textView.setLayoutParams(param);
        textView.setGravity(Gravity.CENTER);

        return textView;
    }


    private TextView generateTableHeaderTextView(String text,ViewGroup.LayoutParams param){
        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setTypeface(tableHeaderTypeface);
        textView.setTextSize(tableHeaderTextSize);


        //textViewId.setWidth((int) MethodUtil.convertDpToPixel(50,context));
        textView.setTextColor(tableHeaderTextColor);
        textView.setLayoutParams(param);
        textView.setGravity(Gravity.CENTER);

        return textView;
    }

    private View getTopTableHeader(){
        TextView textView = new TextView(context);
        textView.setLayoutParams(topTableHeaderParam);
        textView.setGravity(Gravity.CENTER);
        textView.setText(projectName);
        textView.setBackgroundColor(topTableHeaderBackGroundColor);
        textView.setTypeface(topTableHeaderTypeface);
        textView.setTextColor(topTableHeaderTextColor);
        textView.setTextSize(topTableHeaderTextSize);

        return textView;

    }


    private List<Date> getAllDateList(){

        List<Date> returnList = new ArrayList<>();

        int dayDiff = MyUtils.dayDiff(firstDate,lastDate);

        for(int i=0;i<dayDiff;i++){
            returnList.add(MyUtils.incrementDate(i,firstDate));
        }

        return returnList;
    }

    private View getYearHeaderOfGanntChart(){

        List<Date> dateList = getAllDateList();

        RelativeLayout container = new RelativeLayout(context);
        RelativeLayout.LayoutParams containerParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        for (int i =0;i<dateList.size();i++){
            Button button = new Button(context);
            button.setId(5000+i);
            RelativeLayout.LayoutParams buttonMainBarParams =
                    new RelativeLayout.LayoutParams(widthOfEachDay, tableRowHeight);

            if(i==0){
                buttonMainBarParams.addRule(RelativeLayout.ALIGN_PARENT_START);
            }else{
                buttonMainBarParams.addRule(RelativeLayout.RIGHT_OF,(5000-1)+i);
            }
            button.setLayoutParams(buttonMainBarParams);
            button.setText(MyUtils.getYear(dateList.get(i)));
            button.setTextColor(yearHeaderTextColor);
            button.setTextSize(yearHeaderTextSize);
            button.setTypeface(yearHeaderTypeFace);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                button.setBackgroundTintList(ColorStateList.valueOf(yearHeaderBackgroundColor));
            }

            container.addView(button);
        }

        return container;


    }

    private View getMonthDayHeaderOfGanntChart(){

        List<Date> dateList = getAllDateList();

        RelativeLayout container = new RelativeLayout(context);
        RelativeLayout.LayoutParams containerParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        for (int i =0;i<dateList.size();i++){
            Button button = new Button(context);
            button.setId(5000+i);
            RelativeLayout.LayoutParams buttonMainBarParams =
                    new RelativeLayout.LayoutParams(widthOfEachDay, tableRowHeight);

            if(i==0){
                buttonMainBarParams.addRule(RelativeLayout.ALIGN_PARENT_START);
            }else{
                buttonMainBarParams.addRule(RelativeLayout.RIGHT_OF,(5000-1)+i);
            }
            button.setLayoutParams(buttonMainBarParams);
            button.setText(MyUtils.getDateAndMonth(dateList.get(i)));
            button.setPadding(0,0,0,0);
            button.setTextColor(monthHeaderTextColor);
            button.setTextSize(monthHeaderTextSize);
            button.setTypeface(monthHeaderTypeFace);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                button.setBackgroundTintList(ColorStateList.valueOf(monthHeaderBackgroundColor));
            }

            container.addView(button);
        }

        return container;


    }

    private View mainGanttChart(){
        RelativeLayout container = new RelativeLayout(context);

        RelativeLayout.LayoutParams containerParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        for(int i=0;i<dataList.size();i++){
            RelativeLayout eachRowLayout = new RelativeLayout(context);
            eachRowLayout.setId(500+i);

            RelativeLayout.LayoutParams rowContainerParam =
                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rowContainerParam.addRule(RelativeLayout.ALIGN_PARENT_START);

            if(i==0){
                rowContainerParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            }else{
                rowContainerParam.addRule(RelativeLayout.BELOW,(500-1)+i);
            }

            String startDateStr = dataList.get(i).getActivity_start_date();
            String finishedDateStr = dataList.get(i).getActivity_finished_date();
            Date startDate = MyUtils.getDateFromString(startDateStr);
            Date finishedDate = MyUtils.getDateFromString(finishedDateStr);

            int margin = getLeftMargin(firstDate,startDate);

            rowContainerParam.setMargins(margin,0,0,0);

            Button button = createMainButton(dataList.get(i));
            Button progress_button = createProgressButton(dataList.get(i));
            Button progress_text = createProgressTextView(dataList.get(i));



            eachRowLayout.addView(button);
            eachRowLayout.addView(progress_button);
            eachRowLayout.addView(progress_text);

           // ViewCompat.setTranslationZ(progress_text, 10);

            eachRowLayout.setLayoutParams(rowContainerParam);

            container.addView(eachRowLayout);

            container.setLayoutParams(containerParam);


        }

        Button trackingBar = getTrackingBar();
        container.addView(trackingBar);

        return container;

    }

    private Button createMainButton(final ActivityData activityData){
        Button button = new Button(context);
        Date startDate = MyUtils.getDateFromString(activityData.getActivity_start_date());
        Date finishedDate = MyUtils.getDateFromString(activityData.getActivity_finished_date());
        int btnWidth = MyUtils.dayDiff(startDate,finishedDate);
        int widthOfMainBar = btnWidth*widthOfEachDay;

        RelativeLayout.LayoutParams buttonMainBarParams =
                new RelativeLayout.LayoutParams(widthOfMainBar, tableRowHeight);
        buttonMainBarParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        button.setLayoutParams(buttonMainBarParams);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            button.setBackgroundTintList(ColorStateList.valueOf(mainBarColor));
        }

        //On Long Click Listener of Button

        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(listener!= null){
                    listener.OnLongClickMainBar(activityData);
                }
                return true;
            }
        });

        return button;
    }

    private Button createProgressButton(final ActivityData activityData){
        Button button = new Button(context);
        Date startDate = MyUtils.getDateFromString(activityData.getActivity_start_date());
        Date finishedDate = MyUtils.getDateFromString(activityData.getActivity_finished_date());
        double work_done = activityData.getActivity_volume_of_work_done();
        double work = activityData.getActivity_volume_of_works();
        double ratio = work_done/work;
        int btnWidth = MyUtils.dayDiff(startDate,finishedDate);
        int widthOfMainBar = btnWidth*widthOfEachDay;
        int width_of_progress_bar = (int) (widthOfMainBar*ratio);

        RelativeLayout.LayoutParams buttonMainBarParams =
                new RelativeLayout.LayoutParams(width_of_progress_bar, tableRowHeight);
        buttonMainBarParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        button.setLayoutParams(buttonMainBarParams);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            button.setBackgroundTintList(ColorStateList.valueOf(progressBarColor));
        }

        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(listener!= null){
                    listener.onLongClickProgressBar(activityData);
                }
                return true;
            }
        });

        return button;
    }

    private Button createProgressTextView(ActivityData activityData){
        Button button = new Button(context);

        Date startDate = MyUtils.getDateFromString(activityData.getActivity_start_date());
        Date finishedDate = MyUtils.getDateFromString(activityData.getActivity_finished_date());
        double work_done = activityData.getActivity_volume_of_work_done();
        double work = activityData.getActivity_volume_of_works();

        double ratio = work_done/work;

        DecimalFormat df = new DecimalFormat("0.0");
        String progress = df.format(ratio*100);
        int btnWidth = MyUtils.dayDiff(startDate,finishedDate);
        int widthOfMainBar = btnWidth*widthOfEachDay-progress_text_width;
        int margin = (int) (widthOfMainBar*ratio);

        RelativeLayout.LayoutParams progress_text_param =
                new RelativeLayout.LayoutParams(progress_text_width, tableRowHeight);
        progress_text_param.addRule(RelativeLayout.ALIGN_PARENT_START);
        progress_text_param.setMarginStart(margin);

        button.setLayoutParams(progress_text_param);



        button.setText(progress+"%");
        button.setTextSize(progressTextTextSize);
        button.setTextColor(progressTextTextColor);
        button.setTypeface(progressTextTypeface);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            button.setBackgroundTintList(ColorStateList.valueOf(progressTextBackgroundColor));
        }


        return button;
    }

    private Button getTrackingBar(){
        Button trackingBar = new Button(context);
        Date todayDate = new Date();

        int dayDiff = MyUtils.dayDiff(firstDate,todayDate);
        int trackingBarMargin =getTrackingBarMargin();

        RelativeLayout.LayoutParams tracking_bar_params =
                new RelativeLayout.LayoutParams(tableRowHeight, tableRowHeight*dataSize);
        tracking_bar_params.addRule(RelativeLayout.ALIGN_PARENT_START);
        //Set the Margin
        tracking_bar_params.setMarginStart(trackingBarMargin);

        trackingBar.setLayoutParams(tracking_bar_params);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            trackingBar.setBackgroundTintList(ColorStateList.valueOf(trackingBarColor));
        }

        if(dayDiff<1){
            trackingBar.setVisibility(View.INVISIBLE);
        }

        trackingBar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(listener!= null){
                    listener.OnLongClickTrackinBar(getTrackingBarActivityList());
                }
                return true;
            }
        });

        return trackingBar;
    }

    public int getLeftMargin(Date startDate, Date firstDate){

        int dateDiff = MyUtils.dayDiff(startDate,firstDate);

        return (dateDiff-1)*widthOfEachDay;
    }

    private List<ActivityData> getTrackingBarActivityList(){
        List<ActivityData> returnList = new ArrayList<>();
        Date today = new Date();

        for(ActivityData x: dataList){
            Date startDate = MyUtils.getDateFromString(x.getActivity_start_date());
            Date finishedDate = MyUtils.getDateFromString(x.getActivity_finished_date());

            if(today.compareTo(startDate)>=0 && today.compareTo(finishedDate)<=0){
                returnList.add(x);
            }
        }

        return returnList;
    }

    private int getTrackingBarMargin(){
        Date today = new Date();
        int dayDiff = MyUtils.dayDiff(firstDate,today);

        int addForCentering = (widthOfEachDay-tableRowHeight)/2;

        int trackingBarMargin = (dayDiff-1)*widthOfEachDay+addForCentering;

        return trackingBarMargin;
    }



    public Typeface getTableDataTypeface() {
        return tableDataTypeface;
    }

    public void setTableDataTypeface(Typeface tableDataTypeface) {
        this.tableDataTypeface = tableDataTypeface;
    }

    public int getTableDataTextSize() {
        return tableDataTextSize;
    }

    public void setTableDataTextSize(int tableDataTextSize) {
        this.tableDataTextSize = tableDataTextSize;
    }

    public int getTableDataTextColor() {
        return tableDataTextColor;
    }

    public void setTableDataTextColor(int tableDataTextColor) {
        this.tableDataTextColor = tableDataTextColor;
    }

    public Typeface getTableHeaderTypeface() {
        return tableHeaderTypeface;
    }

    public void setTableHeaderTypeface(Typeface tableHeaderTypeface) {
        this.tableHeaderTypeface = tableHeaderTypeface;
    }

    public int getTableHeaderTextSize() {
        return tableHeaderTextSize;
    }

    public void setTableHeaderTextSize(int tableHeaderTextSize) {
        this.tableHeaderTextSize = tableHeaderTextSize;
    }

    public int getTableHeaderTextColor() {
        return tableHeaderTextColor;
    }

    public void setTableHeaderTextColor(int tableHeaderTextColor) {
        this.tableHeaderTextColor = tableHeaderTextColor;
    }

    public Typeface getTopTableHeaderTypeface() {
        return topTableHeaderTypeface;
    }

    public void setTopTableHeaderTypeface(Typeface topTableHeaderTypeface) {
        this.topTableHeaderTypeface = topTableHeaderTypeface;
    }

    public int getTopTableHeaderTextSize() {
        return topTableHeaderTextSize;
    }

    public void setTopTableHeaderTextSize(int topTableHeaderTextSize) {
        this.topTableHeaderTextSize = topTableHeaderTextSize;
    }

    public int getTopTableHeaderTextColor() {
        return topTableHeaderTextColor;
    }

    public void setTopTableHeaderTextColor(int topTableHeaderTextColor) {
        this.topTableHeaderTextColor = topTableHeaderTextColor;
    }

    public int getTableHeaderBackgroundColor() {
        return tableHeaderBackgroundColor;
    }

    public void setTableHeaderBackgroundColor(int tableHeaderBackgroundColor) {
        this.tableHeaderBackgroundColor = tableHeaderBackgroundColor;
    }

    public int getTableDataBackgroundColor() {
        return tableDataBackgroundColor;
    }

    public void setTableDataBackgroundColor(int tableDataBackgroundColor) {
        this.tableDataBackgroundColor = tableDataBackgroundColor;
    }

    public int getTopTableHeaderBackGroundColor() {
        return topTableHeaderBackGroundColor;
    }

    public void setTopTableHeaderBackGroundColor(int topTableHeaderBackGroundColor) {
        this.topTableHeaderBackGroundColor = topTableHeaderBackGroundColor;
    }

    public void setYearHeaderTypeFace(Typeface yearHeaderTypeFace) {
        this.yearHeaderTypeFace = yearHeaderTypeFace;
    }

    public void setYearHeaderTextSize(int yearHeaderTextSize) {
        this.yearHeaderTextSize = yearHeaderTextSize;
    }

    public void setYearHeaderTextColor(int yearHeaderTextColor) {
        this.yearHeaderTextColor = yearHeaderTextColor;
    }

    public void setYearHeaderBackgroundColor(int yearHeaderBackgroundColor) {
        this.yearHeaderBackgroundColor = yearHeaderBackgroundColor;
    }

    public void setMonthHeaderTypeFace(Typeface monthHeaderTypeFace) {
        this.monthHeaderTypeFace = monthHeaderTypeFace;
    }

    public void setMonthHeaderTextSize(int monthHeaderTextSize) {
        this.monthHeaderTextSize = monthHeaderTextSize;
    }

    public void setMonthHeaderTextColor(int monthHeaderTextColor) {
        this.monthHeaderTextColor = monthHeaderTextColor;
    }

    public void setMonthHeaderBackgroundColor(int monthHeaderBackgroundColor) {
        this.monthHeaderBackgroundColor = monthHeaderBackgroundColor;
    }

    public void setTableWidth(int tableWidth) {
        this.tableWidth = tableWidth;
    }

    public void setTableRowHeight(int tableRowHeight) {
        this.tableRowHeight = tableRowHeight;
    }

    public void setMainBarColor(int mainBarColor) {
        this.mainBarColor = mainBarColor;
    }

    public void setProgressBarColor(int progressBarColor) {
        this.progressBarColor = progressBarColor;
    }

    public void setProgress_text_width(int progress_text_width) {
        this.progress_text_width = progress_text_width;
    }

    public void setProgressTextTypeface(Typeface progressTextTypeface) {
        this.progressTextTypeface = progressTextTypeface;
    }

    public void setProgressTextTextSize(int progressTextTextSize) {
        this.progressTextTextSize = progressTextTextSize;
    }

    public void setProgressTextTextColor(int progressTextTextColor) {
        this.progressTextTextColor = progressTextTextColor;
    }

    public void setProgressTextBackgroundColor(int progressTextBackgroundColor) {
        this.progressTextBackgroundColor = progressTextBackgroundColor;
    }

    public void setTrackingBarColor(int trackingBarColor) {
        this.trackingBarColor = trackingBarColor;
    }



    public interface GanttChartLongPressListener{
        public void OnLongClickTrackinBar(List<ActivityData> activityDataList);
        public void OnLongClickMainBar(ActivityData data);
        public void onLongClickProgressBar(ActivityData data);
    }
}
