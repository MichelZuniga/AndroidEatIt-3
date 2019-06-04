package com.example.ayomide.androideatit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.ayomide.androideatit.Common.Common;
import com.example.ayomide.androideatit.Interface.ItemClickListener;
import com.example.ayomide.androideatit.Model.Request;
import com.example.ayomide.androideatit.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    DatabaseReference requests;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_order_status );

        //init firebase
        requests = FirebaseDatabase.getInstance().getReference("Requests");

        recyclerView = findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //If we start order activity from home Activity
        //we will not put any extra, so we just load order by phone from Common
        if(getIntent() == null)
            loadOrders(Common.currentUser.getPhone());
        else
            loadOrders(getIntent().getStringExtra("userPhone"));
    }

    private void loadOrders(String phone) {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests.orderByChild("phone").equalTo(phone)
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {
            viewHolder.tvOrderId.setText(adapter.getRef(position).getKey());
            viewHolder.tvOrderStatus.setText(Common.convertCodeToStatus(model.getStatus()));
            viewHolder.tvOrderPhone.setText(model.getPhone());
            viewHolder.tvOrderAddress.setText(model.getAddress());

            viewHolder.setItemClickListener( new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    //...
                }
            } );
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }



}
