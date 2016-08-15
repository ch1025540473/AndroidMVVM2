package com.mx.demo.model.bean;

/**
 * Created by chenbaocheng on 16/8/14.
 */
public class ApiBean {
    public int type = 0; // 1=color, 2=content

    public String color;

    public String content;
    public boolean isTitle;

    public ApiBean(int type, String color, String content, boolean isTitle){
        this.type = type;
        this.color = color;
        this.content = content;
        this.isTitle = isTitle;
    }

}
