package com.widget.ptr.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.widget.ptr.builder.PtrMode;
import com.widget.ptr.listener.LoadMoreRecyclerListener;
import com.widget.ptr.listener.OnBothRefreshListener;
import com.widget.ptr.listener.OnLoadMoreListener;
import com.widget.ptr.listener.OnPullDownListener;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class PtrRecyclerView extends PtrRefreshLayout {

    private Context context;
    private RecyclerView recyclerView;
    private PtrFrameLayout.LayoutParams params;
    private LoadMoreRecyclerListener onScrollListener;
    private PtrMode mode;
    private float downY;

    private int touchSlop;

    public PtrRecyclerView(Context context) {
        super(context);
        this.context = context;
        init(null);
    }

    public PtrRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init(attrs);
    }

    public PtrRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    /**
     * RecyclerView实例工厂
     * @param context
     * @return
     */
    protected RecyclerView buildRecyclerView(Context context, AttributeSet attrs){
        return new RecyclerView(context, attrs);
    }

    private void init(AttributeSet attrs) {
        recyclerView = buildRecyclerView(context, attrs);
        recyclerView.setOverScrollMode(OVER_SCROLL_NEVER);
        params = new PtrFrameLayout.LayoutParams(
                PtrFrameLayout.LayoutParams.MATCH_PARENT, PtrFrameLayout.LayoutParams.MATCH_PARENT);
        recyclerView.setLayoutParams(params);
        recyclerView.getItemAnimator().setChangeDuration(0);
        addView(recyclerView);
        setResistance(1.7f);
        setRatioOfHeaderHeightToRefresh(1.2f);
        setDurationToClose(200);
        setDurationToCloseHeader(1000);
        setKeepHeaderWhenRefresh(true);
        setPullToRefresh(false);
        //ViewPager滑动冲突
        disableWhenHorizontalMove(true);

        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }


    public void setAdapter(@NonNull RecyclerView.Adapter adapter) {
        if (null == adapter) {
            throw new NullPointerException("adapter cannot be null");
        }
        recyclerView.setAdapter(adapter);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
    }

    public void setItemAnimator(RecyclerView.ItemAnimator itemAnimator) {
        if (null == itemAnimator) {
            return;
        }
        recyclerView.setItemAnimator(itemAnimator);
    }

    public void setMode(PtrMode mode) {
        this.mode = mode;
        if (PtrMode.NONE == mode || PtrMode.BOTTOM == mode) {

            setEnabled(false);
        } else {
            setEnabled(true);
        }

        if (null != onScrollListener) {
            onScrollListener.setMode(mode);
        }
    }

    public void addOnScrollListener(RecyclerView.OnScrollListener listener) {
        if (null == listener) {
            return;
        }
        if (listener instanceof LoadMoreRecyclerListener) {
            onScrollListener = (LoadMoreRecyclerListener) listener;
            recyclerView.addOnScrollListener(onScrollListener);
        } else {
            recyclerView.addOnScrollListener(listener);
        }
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor) {
        if (null == decor) {
            return;
        }
        recyclerView.addItemDecoration(decor);
    }

    public void scrollToPosition(int position) {
        recyclerView.scrollToPosition(position);
    }


    public void setOnBothRefreshListener(final OnBothRefreshListener listener) {
        if (PtrMode.NONE == mode || null == listener) {
            return;
        }

        if (PtrMode.BOTH == mode || PtrMode.TOP == mode) {
            //当前允许下拉刷新

            setPtrHandler(new PtrHandler() {
                @Override
                public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                }

                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    listener.onPullDown();
                }
            });
        }

        if (PtrMode.BOTH == mode || PtrMode.BOTTOM == mode) {
            if (null != onScrollListener) {
                onScrollListener.setOnBothRefreshListener(listener);
            }
        }
    }

    public void setLoadMoreEnabled(boolean enabled) {
        if (onScrollListener != null) {
            onScrollListener.setLoadingMoreEnabled(enabled);
        }

    }


    public void setOnPullDownListener(final OnPullDownListener listener) {

        if (PtrMode.NONE == mode || null == listener) {
            return;
        }

        if (PtrMode.BOTH == mode || PtrMode.TOP == mode) {
            //当前允许下拉刷新
            setPtrHandler(new PtrHandler() {

                @Override
                public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                }

                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    listener.onPullDown();
                }
            });
        }

    }

    public void setOnLoadMoreListener(final OnLoadMoreListener listener) {
        if (PtrMode.NONE == mode || null == listener) {
            return;
        }

        if (PtrMode.BOTH == mode || PtrMode.BOTTOM == mode) {
            if (null != onScrollListener) {
                onScrollListener.setOnLoadMoreListener(listener);
            }
        }
    }

    public void setFooterView(PtrLoadingMoreFooterView ptrLoadingMoreFooterView) {
        if (null == ptrLoadingMoreFooterView) {
            return;
        }
        if (onScrollListener == null) {
            return;
        }
        onScrollListener.setFooterLoadingLayout(ptrLoadingMoreFooterView);
    }


    public RecyclerView real() {
        return recyclerView;
    }

    public void onRefreshCompleted() {
        if (PtrMode.BOTH == mode || PtrMode.TOP == mode) {
            refreshComplete();
        }
        if (PtrMode.BOTH == mode || PtrMode.BOTTOM == mode) {
            if (null != onScrollListener) {
                onScrollListener.onRefreshComplete();
            }
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {

        if (!isEnabled()) {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downY = e.getY();
                    break;
                case MotionEvent.ACTION_MOVE:

                    break;
                case MotionEvent.ACTION_UP:
                    float currentY = e.getY();
                    if ((currentY - downY) > 0) {
                        //手指向下
                        onScrollListener.isLoadingMoreEnabled = false;
                    } else {
                        //手指向上
                        onScrollListener.isLoadingMoreEnabled = true;
                    }
                    break;
            }
        }
        return super.dispatchTouchEvent(e);
    }

    private int lastX = 0;
    private int lastY = 0;

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        final int x = (int) ev.getX();
//        final int y = (int) ev.getY();
//
//        boolean intercept = false;
//
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                intercept = false;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (y > lastX && y > touchSlop && )
//                int xOffSet = Math.abs(x - lastX);
//                int yOffSet = Math.abs(y - lastY);
//                if (yOffSet > touchSlop) {
//                    intercept = true;
//                }
//                break;
//        }
//
//        lastX = x;
//        lastY = y;
//
//
//        return intercept ? intercept : super.onInterceptTouchEvent(ev);
//    }
}
