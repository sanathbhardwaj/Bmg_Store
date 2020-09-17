package com.digital.dhanbadbasket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digital.dhanbadbasket.Adapters.CartAdapter;
import com.digital.dhanbadbasket.Models.CartModel;
import com.digital.dhanbadbasket.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {

    ImageButton backarrow;
    Button home;
    String uid = FirebaseAuth.getInstance().getUid();
    RecyclerView cart_list;
    DatabaseReference categoryRef;
    ValueEventListener listener;
    RelativeLayout empty, main, address, totol;
    Button pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        backarrow = findViewById(R.id.backarrow);
        home = findViewById(R.id.home);

        cart_list = findViewById(R.id.cart);
        cart_list.hasFixedSize();
        cart_list.setLayoutManager(new LinearLayoutManager(this));

        empty = findViewById(R.id.empty);
        main = findViewById(R.id.main);
        pay = findViewById(R.id.pay);
        address = findViewById(R.id.jk);
        totol = findViewById(R.id.total_rl);

        categoryRef = FirebaseDatabase.getInstance().getReference("users");

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this ,PaymentChooseActivity.class));
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer totalActualPrice = 0, totalDiscountedPrice = 0, totalQty = 0;
                for( DataSnapshot ds :dataSnapshot.child("cart").child("products").getChildren()) {
                    CartModel cart_class = ds.getValue(CartModel.class);
                    if (cart_class.getProduct_price()!=null && cart_class.getProduct_quantity()!=null){
                        Integer actual_cost = Integer.parseInt(cart_class.getProduct_price());
                        Integer discounted_cost = Integer.parseInt(cart_class.getProduct_discounted_price());
                        Integer qty = Integer.parseInt(cart_class.getProduct_quantity());
                        totalActualPrice = totalActualPrice+actual_cost;
                        totalDiscountedPrice = totalDiscountedPrice+discounted_cost;
                        totalQty = totalQty+qty;
                    }
                }
                if (totalActualPrice==0 || totalDiscountedPrice==0){
                    main.setVisibility(View.GONE);
                    address.setVisibility(View.GONE);
                    totol.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                    categoryRef.child(uid).child("cart").child("total_item_price").removeValue();
                    categoryRef.child(uid).child("cart").child("total_item_discounted_price").removeValue();
                    categoryRef.child(uid).child("cart").child("no_items").removeValue();
                }
                else {
                    categoryRef.child(uid).child("cart").child("total_item_price").setValue(totalActualPrice.toString());
                    categoryRef.child(uid).child("cart").child("total_item_discounted_price").setValue(totalDiscountedPrice.toString());
                    categoryRef.child(uid).child("cart").child("no_items").setValue(totalQty.toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        categoryRef.child(uid).addValueEventListener(listener);


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<CartModel> options =
                new FirebaseRecyclerOptions.Builder<CartModel>()
                        .setQuery(categoryRef.child(uid).child("cart").child("products"), CartModel.class)
                        .build();
        final FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CartModel, CartAdapter>(options) {
            @Override
            public CartAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_cart_element, parent, false);

                return new CartAdapter(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final CartAdapter holder, final int position, @NonNull final CartModel model) {

                final DatabaseReference gameRef = getRef(position);
                final String productID = gameRef.getKey();

                holder.setDetails(getApplicationContext(), model.getProduct_image(), model.getProduct_name(), model.getProduct_discounted_price()
                        ,model.getProduct_quantity(), model.getProduct_weight());

                final int[] counter = {0};
                final int[] actual_product_price = {0};
                final int[] actual_unit_product_price = {0};
                final int[] discounted_product_price = {0};
                final int[] discounted_unit_product_price = {0};

                if (model.getProduct_discounted_price() != null && model.getProduct_quantity() != null && model.getProduct_discounted_unit_price() != null) {
                    counter[0] = Integer.parseInt(model.getProduct_quantity());

                    actual_unit_product_price[0] = Integer.parseInt(model.getProduct_unit_price());
                    actual_product_price[0] = Integer.parseInt(model.getProduct_price());

                    discounted_unit_product_price[0] = Integer.parseInt(model.getProduct_discounted_unit_price());
                    discounted_product_price[0] = Integer.parseInt(model.getProduct_discounted_price());

                }

                TextView increase = holder.itemView.findViewById(R.id.increase);
                TextView reduce = holder.itemView.findViewById(R.id.reduce);
                TextView quantity = holder.itemView.findViewById(R.id.quantity);
                TextView discounted_price = holder.itemView.findViewById(R.id.discounted_price);

                increase.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        counter[0]++;
                        actual_product_price[0] = counter[0]*actual_unit_product_price[0];
                        discounted_product_price[0] = counter[0]*discounted_unit_product_price[0];
                        quantity.setText(String.valueOf(counter[0]));
                        discounted_price.setText("₹ "+discounted_product_price[0]);
                        categoryRef.child(uid).child("cart").child("products").child(productID).child("Product_quantity").setValue(String.valueOf(counter[0]));
                        categoryRef.child(uid).child("cart").child("products").child(productID).child("Product_discounted_price").setValue(String.valueOf(discounted_product_price[0]));
                        categoryRef.child(uid).child("cart").child("products").child(productID).child("Product_discounted_unit_price").setValue(String.valueOf(discounted_unit_product_price[0]));
                        categoryRef.child(uid).child("cart").child("products").child(productID).child("Product_price").setValue(String.valueOf(actual_product_price[0]));
                        categoryRef.child(uid).child("cart").child("products").child(productID).child("Product_unit_price").setValue(String.valueOf(actual_unit_product_price[0]));
                    }
                });

                reduce.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (counter[0] == 1) {
                            counter[0]--;
                            DatabaseReference clearCart = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            clearCart.child("cart").child("products").child(productID).removeValue();

                        }
                        else {
                            counter[0]--;
                            actual_product_price[0] = counter[0]*actual_unit_product_price[0];
                            discounted_product_price[0] = counter[0]*discounted_unit_product_price[0];
                            quantity.setText(String.valueOf(counter[0]));
                            discounted_price.setText("₹ "+discounted_product_price[0]);
                            categoryRef.child(uid).child("cart").child("products").child(productID).child("Product_quantity").setValue(String.valueOf(counter[0]));
                            categoryRef.child(uid).child("cart").child("products").child(productID).child("Product_discounted_price").setValue(String.valueOf(discounted_product_price[0]));
                            categoryRef.child(uid).child("cart").child("products").child(productID).child("Product_discounted_unit_price").setValue(String.valueOf(discounted_unit_product_price[0]));
                            categoryRef.child(uid).child("cart").child("products").child(productID).child("Product_price").setValue(String.valueOf(actual_product_price[0]));
                            categoryRef.child(uid).child("cart").child("products").child(productID).child("Product_unit_price").setValue(String.valueOf(actual_unit_product_price[0]));
                        }
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        return false;
                    }
                });
            }
        };
        firebaseRecyclerAdapter.startListening();
        cart_list.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (categoryRef.child(uid) != null && listener != null) {
            categoryRef.child(uid).removeEventListener(listener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (categoryRef.child(uid) != null && listener != null) {
            categoryRef.child(uid).removeEventListener(listener);
        }
    }
}

