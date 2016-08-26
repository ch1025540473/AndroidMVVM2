package com.mx.demo.event;

import com.mx.demo.viewmodel.viewbean.ItemViewBean;
import com.mx.engine.event.BroadcastEvent;

/**
 * Created by liuyuxuan on 16/8/26.
 */
public class RemoveTxtEvent extends BroadcastEvent {
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
