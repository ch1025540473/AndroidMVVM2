package com.mx.demo.viewmodel;

import android.os.AsyncTask;
import android.os.Bundle;

import com.mx.demo.BR;
import com.mx.demo.DemoModule;
import com.mx.demo.model.DemoUseCase;
import com.mx.demo.model.bean.ApiBean;
import com.mx.demo.view.factory.DemoItemViewFactory;
import com.mx.demo.viewmodel.viewbean.ColorItemViewBean;
import com.mx.demo.viewmodel.viewbean.ItemViewBean;
import com.mx.demo.viewmodel.viewbean.TextItemViewBean;
import com.mx.framework2.event.Events;
import com.mx.framework2.viewmodel.LifecycleViewModel;
import com.mx.framework2.weiget.PullToRefreshRecyclerViewDatabindingAdapters;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by chenbaocheng on 16/8/18.
 */
public class MainViewModel extends LifecycleViewModel {
    private LinkedList<ItemViewBean> items;
    private PullToRefreshRecyclerViewDatabindingAdapters.OnRefreshingCommand onRefreshingCommand;
    private PullToRefreshRecyclerViewDatabindingAdapters.OnLoadingCommand onLoadingCommand;
    boolean isRefreshing = true;
    boolean isLoading = true;

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
        notifyPropertyChanged(BR.model);
    }

    public boolean isRefreshing() {
        return isRefreshing;
    }

    public void setRefreshing(boolean refreshing) {
        isRefreshing = refreshing;
    }

    public PullToRefreshRecyclerViewDatabindingAdapters.OnLoadingCommand getOnLoadingCommand() {
        if (onLoadingCommand == null) {
            onLoadingCommand = new PullToRefreshRecyclerViewDatabindingAdapters.OnLoadingCommand() {
                @Override
                public void onLoading() {
                    Observable.create(new Observable.OnSubscribe<List<ApiBean>>() {
                        @Override
                        public void call(Subscriber<? super List<ApiBean>> subscriber) {

                            try {
                                Thread.sleep(2000);
                            } catch (Exception e) {

                            }

                            List<ApiBean> apiBeans = obtainUseCase(DemoUseCase.class).queryBeanList();
                            subscriber.onNext(apiBeans);
                        }
                    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<ApiBean>>() {
                        @Override
                        public void call(List<ApiBean> apiBeen) {
                            for (ApiBean apiBean : apiBeen) {
                                if (apiBean.type == 1) {
                                    ColorItemViewBean viewBean = new ColorItemViewBean();
                                    viewBean.setColor(apiBean.color);
                                    items.add(viewBean);
                                } else if (apiBean.type == 2) {
                                    TextItemViewBean viewBean = new TextItemViewBean();
                                    viewBean.setText(apiBean.content);
                                    viewBean.setUpperCase(apiBean.isTitle);
                                    items.add(viewBean);
                                }
                            }
                            setLoading(true);
                            notifyChange();
                        }
                    });
                }
            };
        }
        return onLoadingCommand;
    }

    public PullToRefreshRecyclerViewDatabindingAdapters.OnRefreshingCommand getOnRefreshingCommand() {
        if (onRefreshingCommand == null) {
            onRefreshingCommand = new PullToRefreshRecyclerViewDatabindingAdapters.OnRefreshingCommand() {
                @Override
                public void onRefreshing() {

                    Observable.create(new Observable.OnSubscribe<List<ApiBean>>() {
                        @Override
                        public void call(Subscriber<? super List<ApiBean>> subscriber) {

                            try {
                                Thread.sleep(2000);
                            } catch (Exception e) {

                            }

                            List<ApiBean> apiBeans = obtainUseCase(DemoUseCase.class).queryBeanList();
                            subscriber.onNext(apiBeans);
                        }
                    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<ApiBean>>() {
                        @Override
                        public void call(List<ApiBean> apiBeen) {
                            for (ApiBean apiBean : apiBeen) {
                                if (apiBean.type == 1) {
                                    ColorItemViewBean viewBean = new ColorItemViewBean();
                                    viewBean.setColor(apiBean.color);
                                    items.addFirst(viewBean);
                                } else if (apiBean.type == 2) {
                                    TextItemViewBean viewBean = new TextItemViewBean();
                                    viewBean.setText(apiBean.content);
                                    viewBean.setUpperCase(apiBean.isTitle);
                                    items.addFirst(viewBean);
                                }
                            }
                            setRefreshing(false);
                            setLoading(true);
                            notifyChange();

                        }
                    });


                }
            };
        }
        return onRefreshingCommand;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        // 翻译数据
        items = new LinkedList<>();
        // 业务bean -> main view model -> view beans -> recyclerview -> item view model -> item view bean

        notifyChange();
    }

    public List<ItemViewBean> getItems() {
        return items;
    }
}
