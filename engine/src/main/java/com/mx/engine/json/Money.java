package com.mx.engine.json;

import java.util.Locale;

/**
 * Created by chenbaocheng on 16/4/21.
 */
public class Money {
    private long value = 0;

    private long yuan;
    private long jiao;
    private long fen;

    private double valueInYuan;
    private double valueInJiao;
    private double valueInFen;



    public Money(){}

    public Money(long value){
        this();
        setValue(value);
    }

    public void setValue(long value){
        this.value = value;

        yuan = value / 100;
        jiao = (value / 10 ) % 10;
        fen = value % 10;

        valueInYuan = value / 100d;
        valueInJiao = value / 10d;
        valueInFen = value;
    }

    public long getValue() {
        return value;
    }

    public long getYuan() {
        return yuan;
    }

    public long getJiao() {
        return jiao;
    }

    public long getFen() {
        return fen;
    }

    public double getValueInYuan(){
        return valueInYuan;
    }

    public double getValueInJiao(){
        return valueInJiao;
    }

    public double getValueInFen(){
        return valueInFen;
    }

    public String getYuanFormat(int precision){
        return format(getValueInYuan(), precision);
    }

    public String getJiaoFormat(int precision){
        return format(getValueInJiao(), precision);
    }

    public String getFenFormat(int precision){
        return format(getValueInFen(), precision);
    }

    public static String format(double value, int precision){
        if(precision < 0){
            precision = 0;
        }
        return String.format(Locale.getDefault(), "%." + precision +"f", value).intern();
    }
}
