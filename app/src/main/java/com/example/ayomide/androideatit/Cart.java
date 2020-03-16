package com.example.ayomide.androideatit;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ayomide.androideatit.Common.Common;
import com.example.ayomide.androideatit.Database.Database;
import com.example.ayomide.androideatit.Model.MyResponse;
import com.example.ayomide.androideatit.Model.Notification;
import com.example.ayomide.androideatit.Model.Order;
import com.example.ayomide.androideatit.Model.Request;
import com.example.ayomide.androideatit.Model.Sender;
import com.example.ayomide.androideatit.Model.Token;
import com.example.ayomide.androideatit.Remote.APIService;
import com.example.ayomide.androideatit.ViewHolder.CartAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    TextView price_total;
    Button btnPlaceOrder;

    DatabaseReference table_user, requests;

    List<Order> cart = new ArrayList<>();

    CartAdapter adapter;

    APIService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_cart );

        //init Service
        mService = Common.getFCMService();

        //init Firebase
        requests = FirebaseDatabase.getInstance().getReference("Requests");

        table_user = FirebaseDatabase.getInstance().getReference();

        //init
        recyclerView = findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        price_total = findViewById(R.id.total);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);

        btnPlaceOrder.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cart.size() > 0)
                    showAlertDialog();
                else
                    Toast.makeText(Cart.this, "Your cart is empty", Toast.LENGTH_SHORT).show();
            }
        });

        loadListFood();
    }


    private void showAlertDialog()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("One more step!");
        alertDialog.setMessage("Enter your home address: ");

        LayoutInflater inflater = this.getLayoutInflater();
        View order_address_comment = inflater.inflate( R.layout.order_address_comment , null);

        final MaterialEditText edtAddress = order_address_comment.findViewById( R.id.etAddress );
        final MaterialEditText edtComment = order_address_comment.findViewById( R.id.etComment );

        alertDialog.setView(order_address_comment);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //create new Request
                Request request = new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        edtAddress.getText().toString(),
                        edtComment.getText().toString(),
                        price_total.getText().toString(),
                        "0", //for satus
                        cart
                );

                //Submit to Firebase
                //Use System.CurrentMilli to key
                String order_number = String.valueOf( System.currentTimeMillis() );
                requests.child(order_number)
                        .setValue(request);
                
                SendOrderNotification(order_number);

                //Delete the cart
                new Database(getBaseContext()).cleanCart();

                Toast.makeText(Cart.this, "Thank you, Order placed", Toast.LENGTH_LONG).show();
                //finish();
            }
        });

        alertDialog.setNegativeButton( "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    //function to send message to server app when order is placed
    private void SendOrderNotification(final String order_number) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");

        final Query data = tokens.orderByChild( "serverToken" ).equalTo( true ); //get all node with serverToken is true

        data.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {
                    Token serverToken = postSnapshot.getValue(Token.class);

                    //Create raw payload to send
                    Notification notification = new Notification( "CHOW", "You have a new order:"+order_number );
                    Sender content = new Sender(serverToken.getToken(),notification);
                    mService.sendNotification( content )
                            .enqueue( new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

                                    //only run when you get result
                                    if (response.code() == 200) {
                                        if (response.body().success == 1) {
                                            Toast.makeText( Cart.this, "Thank you, Order placed", Toast.LENGTH_SHORT ).show();
                                            finish();
                                        } else {
                                            Toast.makeText( Cart.this, "Failed !!!", Toast.LENGTH_LONG ).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                    Log.e( "ERROR", t.getMessage() );
                                }
                            } );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void loadListFood()
    {
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart, this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        //calculate total price
        int total = 0;
        for(Order order:cart)
            total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
        Locale locale = new Locale("en", "NG");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        price_total.setText(fmt.format(total));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if(item.getTitle().equals(Common.DELETE))
            deleteCart(item.getOrder());
        return super.onContextItemSelected( item );
    }

    private void deleteCart(int order) {
        //this will remove item at List<Order> by position
        cart.remove(order);
        //After that, al old data will be deleted from SQLite
        new Database(this).cleanCart();
        //Add final new data from List<Order> will be updated to SQLite
        for(Order item: cart)
            new Database(this).addToCart(item);
        //Refresh
        loadListFood();
    }
}