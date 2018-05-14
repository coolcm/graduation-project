package com.example.graduationproject.component;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.graduationproject.R;
import com.example.graduationproject.adapter.PhotoItemAdapter;
import com.example.graduationproject.bean.UserPhotoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csn on 2018/5/14. 选择头像对话框类
 */

public class PhotoDialogFragment extends DialogFragment {

    CallBack callBack;

    public interface CallBack {
        void onSelect(int resourceId);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.fragment_photo, null);
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            final List<UserPhotoBean> list = new ArrayList<>();
            for (int i = 1; i <= 12; i++) {
                list.add(new UserPhotoBean(getResources().getIdentifier("head_" + i, "drawable", getActivity().getPackageName())));
            }
            PhotoItemAdapter myAdapter = new PhotoItemAdapter(list);
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
            recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    outRect.top = 3;
                    outRect.bottom = 3;
                    outRect.left = 3;
                    outRect.right = 3;
                }
            });
            recyclerView.setAdapter(myAdapter);
            builder.setView(view).setTitle("请选择一个头像").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    for (UserPhotoBean userPhoto: list) {
                        if (userPhoto.getVisibility() == View.VISIBLE) {
                            callBack.onSelect(userPhoto.getResourceId());
                            return;
                        }
                    }
                    Toast.makeText(getActivity(), "请选择一个头像", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CallBack) {
            callBack = (CallBack) context;
        }
    }
}
