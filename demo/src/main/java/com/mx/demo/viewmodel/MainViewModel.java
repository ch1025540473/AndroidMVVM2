package com.mx.demo.viewmodel;

import android.content.Intent;
import android.databinding.Bindable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mx.demo.BR;
import com.mx.demo.event.GotoPatchEvent;
import com.mx.demo.event.UpdatedApiBeanEvent;
import com.mx.demo.model.DemoUseCase;
import com.mx.demo.model.bean.ApiBean;
import com.mx.demo.view.ui.WebActivity;
import com.mx.demo.viewmodel.viewbean.ChildColorItemViewBean;
import com.mx.demo.viewmodel.viewbean.ChildItemViewBean;
import com.mx.demo.viewmodel.viewbean.ChildListViewBean;
import com.mx.demo.viewmodel.viewbean.ChildTextItemViewBean;
import com.mx.demo.viewmodel.viewbean.ColorItemViewBean;
import com.mx.demo.viewmodel.viewbean.ItemViewBean;
import com.mx.demo.viewmodel.viewbean.TextItemViewBean;
import com.mx.engine.utils.SubscriberResult;
import com.mx.framework2.viewmodel.LifecycleViewModel;
import com.mx.framework2.viewmodel.command.OnCheckedChangeCommand;
import com.mx.framework2.viewmodel.command.OnClickCommand;
import com.mx.framework2.viewmodel.command.OnLoadMoreCommand;
import com.mx.framework2.viewmodel.command.OnStartRefreshingCommand;
import com.mx.framework2.viewmodel.proxy.DialogProxy;
import com.mx.framework2.viewmodel.proxy.PTRRecyclerViewProxy;
import com.mx.router.RouteSubscribe;
import com.mx.router.Router;

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

    public Uri getUri() {
        return Uri.parse("demo/routeView");
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
            int count = 0;

            public void onLoadMore() {
                ptrRecyclerViewProxy.setLoadMoreComplete(false);
                obtainUseCase(DemoUseCase.class).loadMoreApiBeanFromNetwork(new SubscriberResult<List<ApiBean>>() {


                    @Override
                    public void onSuccess(List<ApiBean> apiBeanList) {
                        count++;
                        translateList(apiBeanList);
                        ptrRecyclerViewProxy.setLoadMoreComplete(true);
                        notifyPropertyChanged(BR.items);
                        if (count > 3) {
                            ptrRecyclerViewProxy.setPtrMode(PTRRecyclerViewProxy.PTRMode.TOP);
                        }

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
        Log.d("MainViewModel", "onCreate bundle>>" + bundle);

        Router.getDefault().registerReceiver(this);
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
                for (int i = 0; i < 3; i++) {
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

    @Override
    protected void onAttachedToView() {
        super.onAttachedToView();
        Log.d("MainViewModel", "onAttachedToView>>");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MainViewModel", "onStart>>");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("MainViewModel", "onRestart>>");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainViewModel", "onResume>>");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainViewModel", "onPause>>");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MainViewModel", "onStop>>");

        Router.getDefault().unregisterReceiver(this);
    }

    @Override
    protected void onDetachedFromView() {
        super.onDetachedFromView();
        Log.d("MainViewModel", "onDetachedFromView>>");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d("MainViewModel", "onWindowFocusChanged>>");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("viewModel", "test");
        super.onSaveInstanceState(outState);
        Log.d("MainViewModel", "onSaveInstanceState>>" + outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("MainViewModel", "onRestoreInstanceState>>" + savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean userVisibleHint) {
        super.setUserVisibleHint(userVisibleHint);
        Log.d("MainViewModel", "setUserVisibleHint>>");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveUpdateItems(UpdatedApiBeanEvent updatedApiBeanEvent) {
        items.clear();
        translateList(obtainUseCase(DemoUseCase.class).queryBeanList());
        notifyPropertyChanged(BR.items);

    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void receiveRemove(final RemoveTxtEvent removeTxtEvent) {
//        obtainUseCase(DemoUseCase.class).remove(removeTxtEvent.getId());
//    }

    @RouteSubscribe(uri = "demo/textRemoval")
    public void receiveRemove(Bundle bundle) {
        obtainUseCase(DemoUseCase.class).remove(bundle.getString("id"));
    }

    public void setDialogProxy(DialogProxy dialogProxy) {
        dialogProxy1 = dialogProxy;
    }

    public OnClickCommand getOnClickSecondActivity() {
        return new OnClickCommand() {
            @Override
            public void execute(int viewId) {
                //postEvent(new GotoAnotherEvent());
                Router.getDefault().broadcast("demo/anotherActivity", null);
            }
        };
    }

    public OnClickCommand getOnClickHotfixActivity() {
        return new OnClickCommand() {
            @Override
            public void execute(int viewId) {
                postEvent(new GotoPatchEvent());
            }
        };
    }

    public OnCheckedChangeCommand getCheckedChangeCommand() {
        return new OnCheckedChangeCommand() {
            @Override
            public void execute(int viewId, boolean isChecked) {
                Toast.makeText(getContext(), "Checked=" + isChecked, Toast.LENGTH_SHORT).show();
            }
        };
    }

    public OnClickCommand getOnClickWebActivity() {
        return new OnClickCommand() {
            @Override
            public void execute(int viewId) {
                startActivity(new Intent(getContext(), WebActivity.class));
            }
        };
    }
}
