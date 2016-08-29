package com.mx.demo.event;

import com.mx.demo.viewmodel.viewbean.ItemViewBean;
import com.mx.engine.event.BroadcastEvent;

/**
 * Created by liuyuxuan on 16/8/26.
 */
public class RemoveTxtEvent extends BroadcastEvent {
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
