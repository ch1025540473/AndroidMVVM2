package com.mx.framework2.view.factory;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;

import com.mx.engine.utils.ObjectUtils;
import com.mx.framework2.view.DataBindingFactory;
import com.mx.framework2.viewmodel.AbsItemViewModel;
import com.mx.framework2.viewmodel.RecyclerItemViewModel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Created by chenbaocheng on 16/8/11.
 */
public abstract class ItemViewFactory<ItemType> {
    private Context context;
    private Map<Class<?>, Queue<AbsItemViewModel>> viewModelCache = new HashMap<>();

    public ItemViewFactory(){
    }

    @SuppressWarnings("unchecked")
    public final AbsItemViewModel<ItemType> getViewModel(ItemType item){
        Class<?> type = getViewModelType(item);
        AbsItemViewModel<ItemType> vm = (AbsItemViewModel<ItemType>)ObjectUtils.newInstance(type); //TODO use factory here
        cacheViewModel(vm);

        return vm;
    }

    protected abstract Class<? extends AbsItemViewModel> getViewModelType(ItemType item);

    protected abstract ViewDataBinding createViewDataBinding(AbsItemViewModel<ItemType> viewModel);

    public final ViewDataBinding getViewDataBinding(AbsItemViewModel<ItemType> viewModel){
        return createViewDataBinding(viewModel);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext(){
        return this.context;
    }

    protected final LayoutInflater getInflater(){
        return LayoutInflater.from(context);
    }

    protected final <T extends ViewDataBinding> T inflate(@LayoutRes int layoutId){
        return DataBindingFactory.inflate(context,layoutId);
    }

    protected final void cacheViewModel(AbsItemViewModel vm){
        Queue<AbsItemViewModel> queue = viewModelCache.get(vm.getClass());
        if(queue == null){
            queue = new LinkedList<>();
            viewModelCache.put(vm.getClass(), queue);
        }
        queue.add(vm);
    }

    public final AbsItemViewModel obtainViewModel(Class<?> viewModelType){
        if(viewModelCache.containsKey(viewModelType)){
            return viewModelCache.get(viewModelType).poll();
        }
        return null;
    }
}
