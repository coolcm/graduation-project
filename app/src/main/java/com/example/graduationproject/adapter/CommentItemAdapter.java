package com.example.graduationproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.graduationproject.R;
import com.example.graduationproject.bean.CommentItemBean;
import com.example.graduationproject.bean.UserCreditBean;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by csn on 2018/3/17.
 */

public class CommentItemAdapter extends RecyclerView.Adapter<CommentItemAdapter.ViewHolder> { //评论内容适配器
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
        holder.creditView.setText(String.valueOf(DataSupport.where("userName = ?", list.get(position).getCommentatorName()).findFirst(UserCreditBean.class).getUserCredit()));
        holder.contentView.setText(list.get(position).getContent());
        holder.commentTimeView.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(list.get(position).getCommentTime()));
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
        TextView commentTimeView;

        public ViewHolder(View itemView) {
            super(itemView);
            commentatorImage = itemView.findViewById(R.id.commentator_image);
            nameView = itemView.findViewById(R.id.commentator_name);
            creditView = itemView.findViewById(R.id.commentator_credit);
            contentView = itemView.findViewById(R.id.comment_content);
            commentTimeView = itemView.findViewById(R.id.comment_time);
        }
    }
}
