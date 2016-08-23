package com.mx.demo.viewmodel;

import android.util.Log;

import com.mx.demo.model.DemoUseCase;
import com.mx.demo.model.bean.ApiBean;
import com.mx.demo.viewmodel.viewbean.ColorItemViewBean;
import com.mx.demo.viewmodel.viewbean.ItemViewBean;
import com.mx.demo.viewmodel.viewbean.TextItemViewBean;
import com.mx.framework2.viewmodel.LifecycleViewModel;
import com.mx.framework2.weiget.OnLoadingCommand;
import com.mx.framework2.weiget.OnPullToRefreshCommand;
import com.mx.framework2.weiget.OnRefreshingCommand;
import com.mx.framework2.weiget.OnScrollCommand;
import com.mx.framework2.weiget.PullToRefreshRecyclerViewDataBindingAdapters;

import java.util.LinkedList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by chenbaocheng on 16/8/18.
 */
public class MainViewModel extends LifecycleViewModel {
    private LinkedList<ItemViewBean> items;
    private OnRefreshingCommand onRefreshingCommand;
    private OnLoadingCommand onLoadingCommand;
    private OnScrollCommand onScrollCommand;
    private OnPullToRefreshCommand onPullToRefreshCommand;
    boolean isRefreshing = true;
    boolean isLoadingCompleted = true;
    boolean isLoadingEnable = false;

    public boolean isLoadingEnable() {
        return isLoadingEnable;
    }

    public void setLoadingEnable(boolean loadingEnable) {
        isLoadingEnable = loadingEnable;
    }

    public boolean isLoadingCompleted() {
        return isLoadingCompleted;
    }

    public void setLoadingCompleted(boolean isLoadingComplated) {
        this.isLoadingCompleted = isLoadingComplated;
    }

    public boolean isRefreshing() {
        return isRefreshing;
    }

    public void setRefreshing(boolean refreshing) {
        isRefreshing = refreshing;
    }

    public OnScrollCommand getOnScrollCommand() {
        if (null == onScrollCommand) {
            onScrollCommand = new OnScrollCommand() {
                @Override
                public void onScrollStateChanged(int id, int state) {
                    Log.d("PTR", "onScrollStateChanged id=" + id + " state=" + state);
                }

                @Override
                public void onScrolled(int id, int v, int h, int dx, int dy) {
                    Log.d("PTR", "onScrolled id=" + id + " v=" + v + " h=" + h + " dx=" + dx + " dy=" + dy);
                }
            };
        }
        return onScrollCommand;
    }

    public OnPullToRefreshCommand getOnPullToRefreshCommand() {
        if (null == onPullToRefreshCommand) {
            onPullToRefreshCommand = new OnPullToRefreshCommand() {
                @Override
                public void onMove(boolean isPullToRefresh, float scaleOfLayout) {
                    Log.d("PTR", "onMove isPullToRefresh=" + isPullToRefresh + " scaleOfLayout=" + scaleOfLayout);
                }

                @Override
                public void onRefreshing() {
                    Log.d("PTR", "onMove onRefreshing");
                }

                @Override
                public void onReset() {
                    Log.d("PTR", "onMove onReset");
                }
            };
        }
        return onPullToRefreshCommand;
    }

    public OnLoadingCommand getOnLoadingCommand() {
        if (onLoadingCommand == null) {
            onLoadingCommand = new OnLoadingCommand() {
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
                            setLoadingCompleted(true);
                            notifyChange();
                        }
                    });
                }
            };
        }
        return onLoadingCommand;
    }

    public OnRefreshingCommand getOnRefreshingCommand() {
        if (onRefreshingCommand == null) {
            onRefreshingCommand = new OnRefreshingCommand() {
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
                            items.clear();
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
                            setLoadingEnable(true);
                            setLoadingCompleted(true);
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
