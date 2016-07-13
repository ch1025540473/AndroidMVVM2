package com.mx.engine.json;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by chenbaocheng on 16/4/21.
 */
public class Time {
    private Calendar calendar;

    public Time(){
        calendar = Calendar.getInstance();
    }

    public Time(long milliseconds){
        this();
        setMilliseconds(milliseconds);
    }

    protected Calendar getCalendar(){
        return calendar;
    }

    public void setMilliseconds(long value){
        calendar.setTimeInMillis(value);
    }

    public long getMilliseconds(){
        return calendar.getTimeInMillis();
    }

    public String toString(String format){
        return new SimpleDateFormat(format, Locale.getDefault()).format(calendar.getTime());
    }
}
