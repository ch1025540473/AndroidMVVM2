package com.mx.demo.model;

import com.mx.demo.model.bean.ApiBean;
import com.mx.framework2.model.UseCase;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by chenbaocheng on 16/8/14.
 */
public class DemoUseCase extends UseCase{
    public List<ApiBean> queryBeanList(){
        List<ApiBean> beans = new LinkedList<>();

        beans.add(new ApiBean(1, "red", null, false));
        beans.add(new ApiBean(2, "red", "my content", false));
        beans.add(new ApiBean(1, "yellow", null, false));
        beans.add(new ApiBean(1, "blue", null, false));
        beans.add(new ApiBean(2, "yellow", "my text2", true));
        beans.add(new ApiBean(1, "red", null, false));
        beans.add(new ApiBean(2, "blue", "my text3", false));
        beans.add(new ApiBean(1, "yellow", null, false));
        beans.add(new ApiBean(2, "blue", "my text4", true));
        beans.add(new ApiBean(1, "blue", null, false));

        return beans;
    }

    @Override
    protected void onOpen() {

    }

    @Override
    protected void onClose() {

    }
}
