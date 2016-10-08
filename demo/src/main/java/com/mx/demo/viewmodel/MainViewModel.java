package com.mx.demo.viewmodel;

import android.content.Intent;
import android.databinding.Bindable;
import android.os.Bundle;
import android.util.Log;

import com.mx.demo.BR;
import com.mx.demo.event.RemoveTxtEvent;
import com.mx.demo.event.UpdatedApiBeanEvent;
import com.mx.demo.model.DemoUseCase;
import com.mx.demo.model.bean.ApiBean;
import com.mx.demo.view.ui.SecondActivity;
import com.mx.demo.viewmodel.viewbean.ChildColorItemViewBean;
import com.mx.demo.viewmodel.viewbean.ChildItemViewBean;
import com.mx.demo.viewmodel.viewbean.ChildListViewBean;
import com.mx.demo.viewmodel.viewbean.ChildTextItemViewBean;
import com.mx.demo.viewmodel.viewbean.ColorItemViewBean;
import com.mx.demo.viewmodel.viewbean.ItemViewBean;
import com.mx.demo.viewmodel.viewbean.TextItemViewBean;
import com.mx.engine.utils.SubscriberResult;
import com.mx.framework2.view.ui.ActivityResultCallback;
import com.mx.framework2.viewmodel.LifecycleViewModel;
import com.mx.framework2.viewmodel.command.OnClickCommand;
import com.mx.framework2.viewmodel.command.OnLoadMoreCommand;
import com.mx.framework2.viewmodel.command.OnStartRefreshingCommand;
import com.mx.framework2.viewmodel.proxy.DialogProxy;
import com.mx.framework2.viewmodel.proxy.PTRRecyclerViewProxy;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by chenbaocheng on 16/8/18.
 */
public class MainViewModel extends LifecycleViewModel {

    private DialogProxy dialogProxy1;

    public void setPtrRecyclerViewProxy(PTRRecyclerViewProxy ptrRecyclerViewProxy) {
        this.ptrRecyclerViewProxy = ptrRecyclerViewProxy;
    }

    @Bindable
    public List<ItemViewBean> getItems() {
        return items;
    }


    private List<ItemViewBean> items = new LinkedList<>();
    private PTRRecyclerViewProxy
            ptrRecyclerViewProxy = new PTRRecyclerViewProxy();

    public PTRRecyclerViewProxy getPtrRecyclerViewProxy() {
        return ptrRecyclerViewProxy;
    }

    public MainViewModel() {
        ptrRecyclerViewProxy.setPtrMode(PTRRecyclerViewProxy.PTRMode.BOTH);
        ptrRecyclerViewProxy.setRefresh(false);
        ptrRecyclerViewProxy.setLoadMoreComplete(true);
        ptrRecyclerViewProxy.setOnLoadMoreCommand(new OnLoadMoreCommand() {
            @Override
            public void onLoadMore() {
                ptrRecyclerViewProxy.setLoadMoreComplete(false);
                obtainUseCase(DemoUseCase.class).loadMoreApiBeanFromNetwork(new SubscriberResult<List<ApiBean>>() {
                    @Override
                    public void onSuccess(List<ApiBean> apiBeanList) {
                        translateList(apiBeanList);
                        ptrRecyclerViewProxy.setLoadMoreComplete(true);
                        notifyPropertyChanged(BR.items);
                    }

                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                    }
                });
            }
        });
        ptrRecyclerViewProxy.setOnStartRefreshingCommand(new OnStartRefreshingCommand() {
            @Override
            public void onStartRefreshing() {
                obtainUseCase(DemoUseCase.class).loadMoreApiBeanFromNetwork(new SubscriberResult<List<ApiBean>>() {
                    @Override
                    public void onSuccess(List<ApiBean> apiBeanList) {
                        if (dialogProxy1 != null) {
                            //test dialog
                            dialogProxy1.show();
                        }
                        items.clear();
                        translateList(apiBeanList);
                        ptrRecyclerViewProxy.setLoadMoreComplete(true);
                        ptrRecyclerViewProxy.setRefresh(false);
                        notifyPropertyChanged(BR.items);
                    }

                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onFailure(Throwable throwable) {


                    }
                });

            }
        });

    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        System.out.println("ActivityResultCallback:onCreate=" + ">>>>" +
                MainViewModel.this);
    }

    private void translateList(List<ApiBean> apiBeanList) {
        for (ApiBean apiBean : apiBeanList) {
            if (apiBean.type == 1) {
                ColorItemViewBean viewBean = new ColorItemViewBean();
                viewBean.setColor(apiBean.color);
                viewBean.setId(apiBean.getId());
                items.add(viewBean);
            } else if (apiBean.type == 2) {
                TextItemViewBean viewBean = new TextItemViewBean();
                viewBean.setText(apiBean.content);
                viewBean.setUpperCase(apiBean.isTitle);
                viewBean.setId(apiBean.getId());
                items.add(viewBean);
            } else if (apiBean.type == 3) {
                ChildListViewBean childListViewBean = new ChildListViewBean();
                List<ChildItemViewBean> list = new LinkedList<ChildItemViewBean>();
                for (int i = 0; i < 20; i++) {
                    if (i % 3 == 0) {
                        ChildColorItemViewBean childColorItemViewBean = new
                                ChildColorItemViewBean();
                        list.add(childColorItemViewBean);
                    } else {
                        ChildTextItemViewBean childTextItemViewBean = new ChildTextItemViewBean();
                        childTextItemViewBean.setText("child item" + i);
                        list.add(childTextItemViewBean);
                    }
                }
                childListViewBean.setChildItemViewBeanList(list);
                childListViewBean.setId(apiBean.getId());
                items.add(childListViewBean);
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveUpdateItems(UpdatedApiBeanEvent updatedApiBeanEvent) {
        items.clear();
        translateList(obtainUseCase(DemoUseCase.class).queryBeanList());
        notifyPropertyChanged(BR.items);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveRemove(final RemoveTxtEvent removeTxtEvent) {
        Log.d("receiveRemove", "removeTxtEvent=" + removeTxtEvent.getActivityStarter());
        obtainUseCase(DemoUseCase.class).remove(removeTxtEvent.getId());
    }

    public void setDialogProxy(DialogProxy dialogProxy) {
        dialogProxy1 = dialogProxy;
    }

    public OnClickCommand getOnClickSecondActivity() {
        return new OnClickCommand() {
            @Override
            public void execute(int viewId) {
                System.out.println("ActivityResultCallback:onActivityResult execute=" + ">>>>" +
                        MainViewModel.this);
                startActivityForResult(new Intent(getContext(), SecondActivity.class), new
                        ActivityResultCallback() {
                            @Override
                            public void onActivityResult(int resultCode, Intent data) {
                                System.out.println("ActivityResultCallback:onActivityResult " +
                                        "result=" +
                                        resultCode + ">>>>" + MainViewModel.this);
                            }
                        });
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        System.out.println("MainActivity:onActivityResult request=" + requestCode);
    }
}
