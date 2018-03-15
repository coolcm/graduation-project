package com.example.graduationproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.graduationproject.R;
import com.example.graduationproject.activity.ContentActivity;
import com.example.graduationproject.bean.ListItemBean;

import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by csn on 2018/3/14.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    private List<ListItemBean> list;
    private Context context;

    public MyRecyclerAdapter(List<ListItemBean> list, Context context){
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.userImageView.setImageResource(R.drawable.profile_photo_boy);
        holder.userIdView.setText("sjtucsn");
        holder.userCreditView.setText(String.valueOf(new Random().nextInt(100)));
        holder.textView.setText(list.get(position).getContent());
        holder.agreeView.setText(String.valueOf(new Random().nextInt(100)));
        holder.disagreeView.setText(String.valueOf(new Random().nextInt(100)));
        holder.commentView.setText(String.valueOf(new Random().nextInt(100)));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ContentActivity.class);
                intent.putExtra("content", list.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private CircleImageView userImageView;
        private TextView userIdView;
        private TextView userCreditView;
        private TextView textView;
        private TextView agreeView;
        private TextView disagreeView;
        private TextView commentView;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            userImageView = itemView.findViewById(R.id.user_image);
            userIdView = itemView.findViewById(R.id.user_id);
            userCreditView = itemView.findViewById(R.id.user_credit);
            textView = itemView.findViewById(R.id.text_view);
            agreeView = itemView.findViewById(R.id.num_of_agree);
            disagreeView = itemView.findViewById(R.id.num_of_disagreee);
            commentView = itemView.findViewById(R.id.num_of_comment);
        }
    }
}
