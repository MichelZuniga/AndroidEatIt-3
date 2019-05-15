package com.example.ayomide.androideatit.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.ayomide.androideatit.Interface.ItemClickListener;
import com.example.ayomide.androideatit.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView tvOrderId, tvOrderStatus, tvOrderPhone, tvOrderAddress;

    private ItemClickListener itemClickListener;

    public OrderViewHolder(@NonNull View itemView) {
        super( itemView );

        tvOrderId = itemView.findViewById(R.id.order_id);
        tvOrderStatus = itemView.findViewById(R.id.order_status);
        tvOrderPhone = itemView.findViewById(R.id.order_phone);
        tvOrderAddress = itemView.findViewById(R.id.order_address);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
