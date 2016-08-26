package com.mx.demo.viewmodel;

import android.util.Log;
import android.widget.Toast;

import com.mx.demo.event.RemoveTxtEvent;
import com.mx.demo.model.DemoUseCase;
import com.mx.demo.model.bean.ApiBean;
import com.mx.demo.viewmodel.viewbean.ChildColorItemViewBean;
import com.mx.demo.viewmodel.viewbean.ChildItemViewBean;
import com.mx.demo.viewmodel.viewbean.ChildListViewBean;
import com.mx.demo.viewmodel.viewbean.ChildTextItemViewBean;
import com.mx.demo.viewmodel.viewbean.ColorItemViewBean;
import com.mx.demo.viewmodel.viewbean.ItemViewBean;
import com.mx.demo.viewmodel.viewbean.TextItemViewBean;
import com.mx.framework2.viewmodel.LifecycleViewModel;
import com.mx.framework2.widget.OnLoadMoreCommand;
import com.mx.framework2.widget.OnPullDownCommand;
import com.mx.framework2.widget.OnStartRefreshingCommand;
import com.mx.framework2.widget.OnScrollCommand;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
                    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<List<ApiBean>>() {
                                @Override
                                public void call(List<ApiBean> apiBeen) {
                                    translate(apiBeen);
                                    updateItemViewId();
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
                    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<List<ApiBean>>() {
                                @Override
                                public void call(List<ApiBean> apiBeen) {
                                    items.clear();
                                    translate(apiBeen);
                                    updateItemViewId();
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


    private void translate(List<ApiBean> apiBeanList) {
        for (ApiBean apiBean : apiBeanList) {
            if (apiBean.type == 1) {
                ColorItemViewBean viewBean = new ColorItemViewBean();
                viewBean.setColor(apiBean.color);
                items.add(viewBean);
            } else if (apiBean.type == 2) {
                TextItemViewBean viewBean = new TextItemViewBean();
                viewBean.setText(apiBean.content);
                viewBean.setUpperCase(apiBean.isTitle);
                items.add(viewBean);
            } else if (apiBean.type == 3) {
                ChildListViewBean childListViewBean = new ChildListViewBean();
                List<ChildItemViewBean> list = new LinkedList<ChildItemViewBean>();
                for (int i = 0; i < 20; i++) {
                    if (i % 3 == 0) {
                        ChildColorItemViewBean childColorItemViewBean = new ChildColorItemViewBean();
                        list.add(childColorItemViewBean);
                    } else {
                        ChildTextItemViewBean childTextItemViewBean = new ChildTextItemViewBean();
                        childTextItemViewBean.setText("child item" + i);
                        list.add(childTextItemViewBean);
                    }
                }
                childListViewBean.setChildItemViewBeanList(list);
                items.add(childListViewBean);
            }
        }
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        // 翻译数据
        items = new LinkedList<>();
        notifyChange();
    }

    private void updateItemViewId() {
        int id = 0;
        for (ItemViewBean item : items) {
            item.setId(id);
            id++;
        }
    }

    public List<ItemViewBean> getItems() {
        return items;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveRemove(RemoveTxtEvent removeTxtEvent) {
        Log.d("PTR", "ClickCommand==> " + removeTxtEvent.getId());
        Toast.makeText(getContext(), "receiveRemove", Toast.LENGTH_SHORT).show();
        items.remove(removeTxtEvent.getId());
        updateItemViewId();
        notifyChange();
    }
}
