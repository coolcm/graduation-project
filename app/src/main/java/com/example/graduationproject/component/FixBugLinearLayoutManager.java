package com.example.graduationproject.component;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by csn on 2018/4/9.
 * 解决华为5.1手机上的RecyclerView遇到Inconsistency detected崩溃的bug
 */

public class FixBugLinearLayoutManager extends LinearLayoutManager {
    public FixBugLinearLayoutManager(Context context) {
        super(context);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e("IndexOutOfBounds", "Inconsistency detected" );
        }
    }
}
