package com.cpsdbd.sohelcon.Utility;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;


import com.cpsdbd.sohelcon.BaseClass.ActivityData;
import com.cpsdbd.sohelcon.BaseClass.FinanceData;
import com.cpsdbd.sohelcon.BaseClass.MonthlyChartData;
import com.cpsdbd.sohelcon.BaseClass.MyDate;
import com.cpsdbd.sohelcon.BaseClass.StoreData;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sohel on 9/21/2016.
 */
public class MyUtils {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    public static boolean isThisDateValid(String dateToValidate, String dateFromat){

        if(dateToValidate == null){
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
        sdf.setLenient(false);

        try {

            //if not valid, it will throw ParseException
            Date date = sdf.parse(dateToValidate);
            System.out.println(date);

        } catch (ParseException e) {

            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static void showDialogAndSetTime( Context context, final EditText editText) {
        final Calendar c = Calendar.getInstance();

        String date = editText.getText().toString().trim();


        if(!date.equals("")){
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            try{
                c.setTime(formatter.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                int actualMonth = monthOfYear+1;
                StringBuffer sb = new StringBuffer();
                String day="";
                String month="";
                if(String.valueOf(dayOfMonth).length()==1){
                    day="0"+dayOfMonth;
                }else{
                    day=String.valueOf(dayOfMonth);
                }
                if(String.valueOf(actualMonth).length()==1){
                    month="0"+(monthOfYear+1);
                }else{
                    month=String.valueOf(monthOfYear+1);
                }
                sb.append(day)
                        .append("-")
                        .append(month)
                        .append("-")
                        .append(year);

                editText.setText(sb.toString());
            }
        },mYear,mMonth,mDay);
        datePickerDialog.show();
        datePickerDialog.setTitle("Pick a Date");
    }


    public static void showDialogAndSetTime( Context context, final TextView textView) {

        final Calendar c = Calendar.getInstance();


        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                int actualMonth = monthOfYear+1;
                StringBuffer stringBuffer = new StringBuffer();
                String day="";
                String month="";
                if(String.valueOf(dayOfMonth).length()==1){
                    day="0"+dayOfMonth;
                }else{
                    day=String.valueOf(dayOfMonth);
                }
                if(String.valueOf(actualMonth).length()==1){
                    month="0"+(monthOfYear+1);
                }else{
                    month=String.valueOf(monthOfYear+1);
                }
                stringBuffer.append(day)
                        .append("-")
                        .append(month)
                        .append("-")
                        .append(year);

                textView.setText(stringBuffer.toString());
            }
        },mYear,mMonth,mDay);
        datePickerDialog.show();
        datePickerDialog.setTitle("Pick a Date");

    }

    public static void animate(Context context,RecyclerView.ViewHolder holder, boolean scrollingState){

        AnimatorSet animatorSet = new AnimatorSet();

        int width = getScreenWidth(context);

        if(holder.getAdapterPosition()%2==0){
            width=-width;
        }


        ObjectAnimator translateY = ObjectAnimator.ofFloat(holder.itemView,"translationY",scrollingState==true?-200:200,0);
        ObjectAnimator translateX = ObjectAnimator.ofFloat(holder.itemView,"translationX",width,0);
        translateY.setDuration(500);
        translateX.setDuration(1500);
        animatorSet.playTogether(translateX,translateY);

        animatorSet.start();


    }

    public static void testAnimation(RecyclerView.ViewHolder holder){

        AnimatorSet animatorSet = new AnimatorSet();
        int initial_position=holder.getAdapterPosition()*56;

        ObjectAnimator translateY = ObjectAnimator.ofFloat(holder.itemView,"translationY",-initial_position,0);
        //ObjectAnimator translateX = ObjectAnimator.ofFloat(holder.itemView,"translationX",width,0);
        translateY.setDuration(1500);
       // translateX.setDuration(1500);
        animatorSet.playTogether(translateY);

        animatorSet.start();


    }




    public static List<FinanceData> accendingByDate(List<FinanceData> rawList){
        Collections.sort(rawList, new Comparator<FinanceData>() {
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
        return rawList;
    }

    public static Date getDateFromString(String dateStr){
        Date date = null;

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        try {
            date = formatter.parse(dateStr);

        }catch (ParseException e){
            e.printStackTrace();
        }

        return date;
    }

    private static int getMonthFromDate(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        return c.get(Calendar.MONTH)+1;

    }

    public static int getMonthFromString(String dateStr){
        Date date = getDateFromString(dateStr);

        return getMonthFromDate(date);
    }

    public static Date maxDate(List<Date> dateList){
        Collections.sort(dateList);

        Date date = dateList.get(dateList.size()-1);

        return date;
    }

    public static Date minDate(List<Date> dateList){
        Collections.sort(dateList);

        Date date = dateList.get(0);

        return date;
    }

    public static List<Date> getDateListFromFinanceData(List<FinanceData> financeDataList){
        List<Date> dateList = new ArrayList<>();

        for (FinanceData x: financeDataList){
            Date date = getDateFromString(x.getFinance_transaction_date());
            dateList.add(date);
        }

        return dateList;
    }

    public static List<Date> getDateListFromStoreData(List<StoreData> storeDataList){
        List<Date> dateList = new ArrayList<>();

        for (StoreData x: storeDataList){
            Date date = getDateFromString(x.getStore_rec_con_date());
            dateList.add(date);
        }

        return dateList;
    }

    public static int monthDifference(Date startDate, Date endDate){
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        cal1.setTime(startDate);
        cal2.setTime(endDate);



        int yearDiff = cal2.get(Calendar.YEAR)-cal1.get(Calendar.YEAR);




        int monthDiff = yearDiff*12+cal2.get(Calendar.MONTH)-cal1.get(Calendar.MONTH);

        return monthDiff;
    }

    public static int dayDiff(Date startDate,Date endDate){
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        cal1.setTime(startDate);
        cal2.setTime(endDate);

        long timediffinSecond = cal2.getTimeInMillis()-cal1.getTimeInMillis();
        int dayDiff = (int) (timediffinSecond/(1000*60*60*24));

        return dayDiff+1;
    }


    public static List<MyDate> getMydateList(Date startDate, int monthDiff){

        int startDateMonth = getMonthFromDate(startDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        int startYear = calendar.get(Calendar.YEAR);
        int startMonth = calendar.get(Calendar.MONTH)+1;

        List<MyDate> myDateList = new ArrayList<>();

        for(int i=0;i<=monthDiff;i++){
            int month = startMonth+i;

            int newMonth = -1;
            int newYear = -1;


            if(month>12){
                newMonth = month % 12;
                newYear = startYear+ (int) month/12;
            }else{
                newMonth = month;
                newYear = startYear;
            }

            Date strtDate = getDateFromString("01"+"-"+newMonth+"-"+newYear);

            Date endDate = getEndDate(strtDate);

            MyDate myDate = new MyDate(strtDate,endDate);

            myDateList.add(myDate);


        }

        return myDateList;

    }


    public static List<StoreData> getDailyConsumptionGraphData(Date startDate,int dayDiff,List<StoreData> storeDataList){
        List<StoreData> returnList = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DATE,-1);

        for(int i=0;i<dayDiff;i++){
            calendar.add(Calendar.DATE,1);
            Date targetDate = calendar.getTime();
            String dateStr = dateToString(targetDate);
            double quantity = getQuantityFromADate(dateStr,storeDataList);

            StoreData storeData = new StoreData();
            storeData.setStore_rec_con_date(dateStr);
            storeData.setStore_quantity(quantity);

            returnList.add(storeData);

        }

        return returnList;
    }

    private static Date getEndDate(Date startDate){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        int dateInt =calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH)+1;
        int year = calendar.get(Calendar.YEAR);

        String date = dateInt+"-"+month+"-"+year;

        Date endDate = getDateFromString(date);

        return endDate;
    }


    public static List<MonthlyChartData> getMonthLiChartData(List<MyDate> myDateList, List<FinanceData> financeDataList){
        List<MonthlyChartData> monthlyChartDataList = new ArrayList<>();

        for(MyDate x : myDateList){

            double amount = 0;

            for (FinanceData y : financeDataList){
                String date = y.getFinance_transaction_date();
                Date thisDate = getDateFromString(date);

                if(y.getFinance_status()==1){

                    if(thisDate.compareTo(x.getStartDateofMonth())>=0 && thisDate.compareTo(x.getEndDateofMonth())<=0){
                        amount= amount+y.getFinance_amount();
                    }

                }


            }

            MonthlyChartData monthlyChartData = new MonthlyChartData(x.getMonthYear(),amount);

            monthlyChartDataList.add(monthlyChartData);
        }

        return monthlyChartDataList;
    }


    public static String dateToString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(date);
    }

    private static double getQuantityFromADate(String date,List<StoreData> storeDataList){
        double quantity =0;

        for (StoreData x: storeDataList){
            if(x.getStore_rec_con_date().equals(date)){
                quantity=quantity+x.getStore_quantity();
            }
        }
        return quantity;
    }

    public static Map<String,String> getFirstDateAndLastDateMap(List<ActivityData> dataList){
        Map<String,String> returnMap = new HashMap<>();

        List<Date> startDateList = new ArrayList<>();
        List<Date> finishedDateList = new ArrayList<>();
        //Date firstDate = null;

        for (ActivityData x:dataList){
            String stringStartDate = x.getActivity_start_date();
            String stringFinishedDate = x.getActivity_finished_date();
            Date startDate = getDateFromString(stringStartDate);
            Date finishedDate = getDateFromString(stringFinishedDate);


            startDateList.add(startDate);
            finishedDateList.add(finishedDate);

        }

        Collections.sort(startDateList);
        Collections.sort(finishedDateList);

        // Log.d("TestCollection",String.valueOf(finishedDateList));

        String firstDateStr = dateToString(startDateList.get(0));
        String lastDateStr = dateToString(finishedDateList.get(finishedDateList.size()-1));

        returnMap.put("firstDate",firstDateStr);
        returnMap.put("lastDate",lastDateStr);

        return returnMap;
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static String getYear(Date date){
        String dateString = null;

        DateFormat formatter = new SimpleDateFormat("yyyy");

        dateString = formatter.format(date);

        return dateString;
    }

    public static Date incrementDate(int dayIncrement,Date currentDate){

        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE,dayIncrement);

        Date bal = c.getTime();

        return bal;

    }

    public static String getDateAndMonth(Date date){
        String dateString = null;

        DateFormat formatter = new SimpleDateFormat("dd-MMM");

        dateString = formatter.format(date);

        return dateString;
    }

    public static int getScreenWidth(Context context){

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        //int height = metrics.heightPixels;

        return width;
    }

    public static int convertPixelsToDp(int px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int dp = (int) (px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }


    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth,
                                                     int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }


    public static List<String> getUniqueParticular(List<FinanceData> financeDataList){
        List<String> mainList = new ArrayList<>();
        HashSet<String> hashSet = new HashSet<>();

        for(FinanceData x: financeDataList){
            hashSet.add(x.getFinance_particular());
        }

        mainList.addAll(hashSet);

        return mainList;
    }

    public static List<String> getParticularList(List<FinanceData> financeDataList){
        List<String> mainList = new ArrayList<>();
        for(FinanceData x: financeDataList){
            mainList.add(x.getFinance_particular());
        }
        return mainList;
    }






}
