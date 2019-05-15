package com.example.ayomide.androideatit.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.ayomide.androideatit.Cart;
import com.example.ayomide.androideatit.Interface.ItemClickListener;
import com.example.ayomide.androideatit.Model.Order;
import com.example.ayomide.androideatit.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView tvCartName, tvCartPrice;
    public ImageView cartCount;

    private ItemClickListener itemClickListener;

    public void setTvCartName(TextView tvCartName) {
        this.tvCartName = tvCartName;
    }

    public CartViewHolder(@NonNull View itemView) {
        super( itemView );

        tvCartName = itemView.findViewById(R.id.cart_item_name);
        tvCartPrice = itemView.findViewById(R.id.cart_item_price);
        cartCount = itemView.findViewById(R.id.cart_item_count);
    }

    @Override
    public void onClick(View view) {

    }
}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<Order> listData = new ArrayList<>();
    private Context context;

    public CartAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_layout, viewGroup,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i) {

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(""+listData.get(i).getQuantity(), Color.RED);
        cartViewHolder.cartCount.setImageDrawable(drawable);

        Locale locale = new Locale("en", "NG");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        int price = (Integer.parseInt(listData.get(i).getPrice()))*(Integer.parseInt(listData.get(i).getQuantity()));
        cartViewHolder.tvCartPrice.setText(fmt.format(price));

        cartViewHolder.tvCartName.setText(listData.get(i).getProductName());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
