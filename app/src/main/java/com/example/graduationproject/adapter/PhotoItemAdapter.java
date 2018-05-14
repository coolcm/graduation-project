package com.example.graduationproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.graduationproject.R;
import com.example.graduationproject.bean.UserPhotoBean;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by csn on 2018/5/14. 头像列表适配器
 */

public class PhotoItemAdapter extends RecyclerView.Adapter<PhotoItemAdapter.ViewHolder> {
    private List<UserPhotoBean> list;

    public PhotoItemAdapter(List<UserPhotoBean> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.userPhoto.setImageResource(list.get(position).getResourceId());
        holder.checkedPhoto.setVisibility(list.get(position).getVisibility());
        holder.userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (UserPhotoBean userPhoto: list) {
                    if (list.indexOf(userPhoto) == holder.getAdapterPosition()) {
                        userPhoto.setVisibility(View.VISIBLE); //设置check图标只显示在一张图片上
                    } else {
                        userPhoto.setVisibility(View.INVISIBLE);
                    }
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userPhoto;
        CircleImageView checkedPhoto;

        ViewHolder(View itemView) {
            super(itemView);
            userPhoto = itemView.findViewById(R.id.user_photo);
            checkedPhoto = itemView.findViewById(R.id.checked);
        }
    }
}
