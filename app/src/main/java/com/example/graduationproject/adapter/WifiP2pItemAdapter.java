package com.example.graduationproject.adapter;

import android.net.wifi.p2p.WifiP2pDevice;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.graduationproject.R;
import com.example.graduationproject.interfaces.OnDeviceClickListener;

import java.util.List;

/**
 * Created by csn on 2018/3/21
 */

public class WifiP2pItemAdapter extends RecyclerView.Adapter<WifiP2pItemAdapter.ViewHolder> { //评论内容适配器
    private List<WifiP2pDevice> list;
    private OnDeviceClickListener onDeviceClickListener;

    public WifiP2pItemAdapter(List<WifiP2pDevice> list) {
        this.list = list;
    }

    public void setOnDeviceClickListener(OnDeviceClickListener onDeviceClickListener) {
        this.onDeviceClickListener = onDeviceClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wifi_p2p, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.deviceNameView.setText(list.get(position).deviceName);
        holder.deviceAddressView.setText(list.get(position).deviceAddress);
        holder.deviceAddressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeviceClickListener.onDeviceClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView deviceNameView;
        TextView deviceAddressView;

        public ViewHolder(View itemView) {
            super(itemView);
            deviceNameView = itemView.findViewById(R.id.device_name);
            deviceAddressView = itemView.findViewById(R.id.device_address);
        }
    }
}
