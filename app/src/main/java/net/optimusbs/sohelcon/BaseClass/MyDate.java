package net.optimusbs.sohelcon.BaseClass;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Sohel on 10/9/2016.
 */

public class MyDate implements Serializable {

    private Date startDateofMonth;
    private Date endDateofMonth;

    private String[] months= {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

    public MyDate() {
    }

    public MyDate(Date startDateofMonth, Date endDateofMonth) {
        this.startDateofMonth = startDateofMonth;
        this.endDateofMonth = endDateofMonth;
    }

    public Date getStartDateofMonth() {
        return startDateofMonth;
    }

    public void setStartDateofMonth(Date startDateofMonth) {
        this.startDateofMonth = startDateofMonth;
    }

    public Date getEndDateofMonth() {
        return endDateofMonth;
    }

    public void setEndDateofMonth(Date endDateofMonth) {
        this.endDateofMonth = endDateofMonth;
    }

    public int getMonth(){
        int month =-1;
        if(startDateofMonth!= null && endDateofMonth != null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDateofMonth);

            month = calendar.get(Calendar.MONTH)+1;
        }

        return month;
    }

    public String getMonthYear(){
        String month_year = null;

        if(startDateofMonth!= null && endDateofMonth != null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDateofMonth);

            int month = calendar.get(Calendar.MONTH)+1;

            String monthName = months[month-1];

            int year = calendar.get(Calendar.YEAR);

            month_year = monthName+"-"+year;
        }

        return month_year;

    }
}
