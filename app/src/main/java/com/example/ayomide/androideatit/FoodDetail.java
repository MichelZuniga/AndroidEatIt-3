package com.example.ayomide.androideatit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ayomide.androideatit.Database.Database;
import com.example.ayomide.androideatit.Model.Food;
import com.example.ayomide.androideatit.Model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodDetail extends AppCompatActivity {

    TextView food_name, food_price, food_description;
    ImageView food_image;
    ElegantNumberButton numberButton;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton fabCart;

    DatabaseReference foods;

    String foodId = "";

    Food currentFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_food_detail );

        //Firebase
        foods = FirebaseDatabase.getInstance().getReference("Food");

        //init view
        numberButton = findViewById(R.id.number_button);
        fabCart = findViewById(R.id.btn_cart);

        fabCart.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).addToCart( new Order(
                        foodId,
                        currentFood.getName(),
                        numberButton.getNumber(),
                        currentFood.getPrice(),
                        currentFood.getDiscount()
                ));

                Toast.makeText(FoodDetail.this, "added to cart", Toast.LENGTH_LONG).show();
            }
        });

        food_image = findViewById(R.id.img_food);
        food_name = findViewById(R.id.food_name);
        food_price = findViewById(R.id.food_price);
        food_description = findViewById(R.id.food_description);

        collapsingToolbarLayout = findViewById(R.id.collapsing);

        //Get Food Id from Intent
        if(getIntent() != null)
            foodId = getIntent().getStringExtra("FoodId");
        if(!foodId.isEmpty())
        {
            getFoodDetails(foodId);
        }
    }

    private void getFoodDetails(String foodId) {
        foods.child(foodId).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(Food.class);

                Picasso.with(getBaseContext()).load(currentFood.getImage()).into(food_image);//set Image

                food_name.setText(currentFood.getName());

                food_price.setText(currentFood.getPrice());

                food_description.setText(currentFood.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
