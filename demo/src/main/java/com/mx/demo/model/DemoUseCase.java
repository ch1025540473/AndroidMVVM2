package com.mx.demo.model;

import android.util.Log;
import android.widget.Toast;

import com.mx.demo.event.RemoveTxtEvent;
import com.mx.demo.event.UpdatedApiBeanEvent;
import com.mx.demo.model.bean.ApiBean;
import com.mx.engine.event.EventProxy;
import com.mx.engine.utils.SubscriberResult;
import com.mx.framework2.model.UseCase;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenbaocheng on 16/8/14.
 */
public class DemoUseCase extends UseCase {
    List<ApiBean> beans = new LinkedList<ApiBean>();
    Random random;

    public void refreshApiBeanFromNetwork(final SubscriberResult<List<ApiBean>> result) {

        Observable.create(new Observable.OnSubscribe<List<ApiBean>>() {
            @Override
            public void call(Subscriber<? super List<ApiBean>> subscriber) {
                try {
                    Thread.sleep((random.nextInt() >>> 1) % 3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mockRefreshNetworkApi();
                subscriber.onNext(beans);

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<ApiBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onNext(List<ApiBean> apiBeanList) {
                result.onSuccess(apiBeanList);
            }
        });
    }

    public void loadMoreApiBeanFromNetwork(final SubscriberResult<List<ApiBean>> result) {
        Observable.create(new Observable.OnSubscribe<List<ApiBean>>() {
            @Override
            public void call(Subscriber<? super List<ApiBean>> subscriber) {
                try {
                    Thread.sleep((random.nextInt() >>> 1) % 3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mockLoadMoreNetworkApi();
                subscriber.onNext(beans);

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<ApiBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onNext(List<ApiBean> apiBeanList) {
                result.onSuccess(apiBeanList);
            }
        });

    }

    public List<ApiBean> queryBeanList() {
        return beans;
    }

    public void remove(String id) {
        ListIterator<ApiBean> it = beans.listIterator();
        while (it.hasNext()) {
            if (it.next().getId().equals(id)) {
                it.remove();
            }
        }
        EventProxy.getDefault().post(new UpdatedApiBeanEvent());
    }

    private void mockRefreshNetworkApi() {
        beans.clear();
        beans.add(new ApiBean(getType(), getColor(), "my content", false));
        beans.add(new ApiBean(getType(), getColor(), "my content", false));
        beans.add(new ApiBean(getType(), getColor(), "my content", false));
        beans.add(new ApiBean(getType(), getColor(), "my content", false));
        beans.add(new ApiBean(getType(), getColor(), "my content", false));
        beans.add(new ApiBean(getType(), getColor(), "my content", false));
        beans.add(new ApiBean(getType(), getColor(), "my content", false));
        beans.add(new ApiBean(getType(), getColor(), "my content", false));
        beans.add(new ApiBean(getType(), getColor(), "my content", false));
        beans.add(new ApiBean(getType(), getColor(), "my content", false));
        beans.add(new ApiBean(getType(), getColor(), "my content", false));
        beans.add(new ApiBean(getType(), getColor(), "my content", false));
        beans.add(new ApiBean(getType(), getColor(), "my content", false));
        beans.add(new ApiBean(getType(), getColor(), "my content", false));
        beans.add(new ApiBean(getType(), getColor(), "my content", false));
        beans.add(new ApiBean(getType(), getColor(), "my content", false));
    }

    private void mockLoadMoreNetworkApi() {

        beans.add(new ApiBean(getType(), getColor(), "my content", false));
        beans.add(new ApiBean(getType(), getColor(), "my content", false));
        beans.add(new ApiBean(getType(), getColor(), "my content", false));
        beans.add(new ApiBean(getType(), getColor(), "my content", false));
        beans.add(new ApiBean(getType(), getColor(), "my content", false));
        beans.add(new ApiBean(getType(), getColor(), "my content", false));
        beans.add(new ApiBean(getType(), getColor(), "my content", false));
        beans.add(new ApiBean(getType(), getColor(), "my content", false));
        beans.add(new ApiBean(getType(), getColor(), "my content", false));
        beans.add(new ApiBean(getType(), getColor(), "my content", false));
        beans.add(new ApiBean(getType(), getColor(), "my content", false));
        beans.add(new ApiBean(getType(), getColor(), "my content", false));
        beans.add(new ApiBean(getType(), getColor(), "my content", false));
        beans.add(new ApiBean(getType(), getColor(), "my content", false));
        beans.add(new ApiBean(getType(), getColor(), "my content", false));
        beans.add(new ApiBean(getType(), getColor(), "my content", false));
    }

    @Override
    protected void onOpen() {
        EventProxy.getDefault().register(this);
        random = new Random();
    }

    private int getType() {
        return 3;
    }

    private String getColor() {
        return new String[]{"red", "yellow", "blue"}[(random.nextInt() >>> 1) % 3];
    }

    @Override
    protected void onClose() {
        beans.clear();
        EventProxy.getDefault().unregister(this);
    }

}
