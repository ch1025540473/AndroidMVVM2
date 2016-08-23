package com.mx.demo.viewmodel;

import android.util.Log;

import com.mx.demo.model.DemoUseCase;
import com.mx.demo.model.bean.ApiBean;
import com.mx.demo.viewmodel.viewbean.ColorItemViewBean;
import com.mx.demo.viewmodel.viewbean.ItemViewBean;
import com.mx.demo.viewmodel.viewbean.TextItemViewBean;
import com.mx.framework2.viewmodel.LifecycleViewModel;
import com.mx.framework2.widget.OnLoadMoreCommand;
import com.mx.framework2.widget.OnPullDownCommand;
import com.mx.framework2.widget.OnStartRefreshingCommand;
import com.mx.framework2.widget.OnScrollCommand;

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
    private OnStartRefreshingCommand onStartRefreshingCommand;
    private OnLoadMoreCommand onLoadMoreCommand;
    private OnScrollCommand onScrollCommand;
    private OnPullDownCommand onPullDownCommand;
    boolean isRefreshing = true;
    boolean isLoadMoreComplete = true;
    boolean isLoadMoreEnabled = false;

    public boolean isLoadMoreEnabled() {
        return isLoadMoreEnabled;
    }

    public void setLoadMoreEnabled(boolean loadMoreEnabled) {
        isLoadMoreEnabled = loadMoreEnabled;
    }

    public boolean isLoadMoreComplete() {
        return isLoadMoreComplete;
    }

    public void setLoadMoreComplete(boolean isLoadingComplated) {
        this.isLoadMoreComplete = isLoadingComplated;
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

    public OnPullDownCommand getOnPullDownCommand() {
        if (null == onPullDownCommand) {
            onPullDownCommand = new OnPullDownCommand() {
                @Override
                public void pullToRefresh() {
                    Log.d("PTR", "pullToRefresh");
                }

                @Override
                public void releaseToRefresh() {
                    Log.d("PTR", "releaseToRefresh");
                }

                @Override
                public void onPull(float scaleOfLayout) {
                    Log.d("PTR", "onPull>> scaleOfLayout=" + scaleOfLayout);
                }

                @Override
                public void refreshing() {
                    Log.d("PTR", "refreshing");
                }

                @Override
                public void reset() {
                    Log.d("PTR", "reset");
                }
            };
        }
        return onPullDownCommand;
    }

    public OnLoadMoreCommand getOnLoadMoreCommand() {
        if (onLoadMoreCommand == null) {
            onLoadMoreCommand = new OnLoadMoreCommand() {
                @Override
                public void onLoadMore() {
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
                            setLoadMoreComplete(true);
                            notifyChange();
                        }
                    });
                }
            };
        }
        return onLoadMoreCommand;
    }

    public OnStartRefreshingCommand getOnStartRefreshingCommand() {
        if (onStartRefreshingCommand == null) {
            onStartRefreshingCommand = new OnStartRefreshingCommand() {
                @Override
                public void onStartRefreshing() {

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
                            setLoadMoreEnabled(true);
                            setLoadMoreComplete(true);
                            notifyChange();
                        }
                    });


                }
            };
        }
        return onStartRefreshingCommand;
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
