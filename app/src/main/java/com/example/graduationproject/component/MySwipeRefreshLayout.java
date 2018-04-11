package com.example.graduationproject.component;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by csn on 2018/4/11.
 */

public class MySwipeRefreshLayout extends SwipeRefreshLayout {

    private NestedScrollView nestedScrollView;
    public interface OnLoadMoreListener {
        void onLoad();
    }
    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLoadingMore = false;

    public MySwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (nestedScrollView == null) {
            if (getChildCount() > 0) {
                if (getChildAt(0) instanceof NestedScrollView) {
                    nestedScrollView = (NestedScrollView) getChildAt(0);
                    nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                        @Override
                        public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                                Log.e(TAG, "BOTTOM SCROLL");
                                if (!isLoadingMore) {
                                    isLoadingMore = true;
                                    mOnLoadMoreListener.onLoad();
                                }
                            }
                        }
                    });
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    public void setLoadingMore(boolean isLoadingMore) {
        this.isLoadingMore = isLoadingMore;
    }
}

