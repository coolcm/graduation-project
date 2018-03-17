package com.example.graduationproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.graduationproject.R;
import com.example.graduationproject.bean.CommentItemBean;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by csn on 2018/3/17.
 */

public class CommentItemAdapter extends RecyclerView.Adapter<CommentItemAdapter.ViewHolder> {
    private List<CommentItemBean> list;

    public CommentItemAdapter(List<CommentItemBean> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nameView.setText(list.get(position).getCommentatorName());
        holder.creditView.setText(String.valueOf(list.get(position).getCommentatorCredit()));
        holder.contentView.setText(list.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView commentatorImage;
        TextView nameView;
        TextView creditView;
        TextView contentView;

        public ViewHolder(View itemView) {
            super(itemView);
            commentatorImage = itemView.findViewById(R.id.commentator_image);
            nameView = itemView.findViewById(R.id.commentator_name);
            creditView = itemView.findViewById(R.id.commentator_credit);
            contentView = itemView.findViewById(R.id.comment_content);
        }
    }
}
