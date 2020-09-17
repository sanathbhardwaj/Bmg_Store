package com.digital.dhanbadbasket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digital.dhanbadbasket.R;
import com.digital.dhanbadbasket.Adapters.AllViewPageAdapter;
import com.digital.dhanbadbasket.Models.AllViewPageModel;
import com.digital.dhanbadbasket.Models.CartModel;
import com.digital.dhanbadbasket.Models.CategoryModel;
import com.digital.dhanbadbasket.Models.ProductHorizontalScrollGridModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    ImageButton backarrow;
    private ValueEventListener listener, listener1;
    DatabaseReference mRef;
    AllViewPageAdapter allViewPageAdapter;
    RecyclerView testing;
    private ShimmerFrameLayout mShimmerViewContainer;
    private RelativeLayout shimmer_rl, main_rl, no_internet_rl, cart;
    Button cart_btn;
    DatabaseReference mCartReference;
    String uid = FirebaseAuth.getInstance().getUid();
    TextView price, items;
    RelativeLayout search_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String category = sp.getString("category", "");

        mRef = FirebaseDatabase.getInstance().getReference("main_menu").child(category);
        mCartReference = FirebaseDatabase.getInstance().getReference("users");

        search_bar = findViewById(R.id.search_bar);

        search_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoryActivity.this, SearchActivity.class));
            }
        });

        backarrow = findViewById(R.id.backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final List<AllViewPageModel> allViewPageModelList = new ArrayList<>();
        allViewPageAdapter = new AllViewPageAdapter(allViewPageModelList);
        testing = findViewById(R.id.recycler);
        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(this);
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        testing.setLayoutManager(testingLayoutManager);
        testing.setAdapter(allViewPageAdapter);

        mShimmerViewContainer = findViewById(R.id.shimmer);
        shimmer_rl = findViewById(R.id.shimmer_rl);
        main_rl = findViewById(R.id.main_rl);
        no_internet_rl = findViewById(R.id.no_internet_rl);

        cart = findViewById(R.id.cart);
        cart_btn = findViewById(R.id.cart_btn);

        price = findViewById(R.id.price);
        items = findViewById(R.id.items);

        if (!isOnline()){
            if (mShimmerViewContainer.isShimmerVisible()){
                mShimmerViewContainer.stopShimmer();
                shimmer_rl.setVisibility(View.GONE);
            }
            no_internet_rl.setVisibility(View.VISIBLE);
        }
        else {
            no_internet_rl.setVisibility(View.GONE);
        }

        cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoryActivity.this, CartActivity.class));
            }
        });

        listener1 = new ValueEventListener() {
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
                    cart.setVisibility(View.GONE);
                    mCartReference.child(uid).child("cart").child("total_item_price").removeValue();
                    mCartReference.child(uid).child("cart").child("total_item_discounted_price").removeValue();
                    mCartReference.child(uid).child("cart").child("no_items").removeValue();
                }
                else {
                    cart.setVisibility(View.VISIBLE);
                    price.setText(String.valueOf("₹ "+totalDiscountedPrice));
                    items.setText(totalQty+" item(s)");
                    mCartReference.child(uid).child("cart").child("total_item_price").setValue(totalActualPrice.toString());
                    mCartReference.child(uid).child("cart").child("total_item_discounted_price").setValue(totalDiscountedPrice.toString());
                    mCartReference.child(uid).child("cart").child("no_items").setValue(totalQty.toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (mShimmerViewContainer.isShimmerVisible()){
                        mShimmerViewContainer.stopShimmer();
                        shimmer_rl.setVisibility(View.GONE);
                        no_internet_rl.setVisibility(View.GONE);
                        main_rl.setVisibility(View.VISIBLE);
                    }
                    long view_type = ds.child("view_type").getValue(long.class);
                    switch (String.valueOf(view_type)){
                        case "1":
                            allViewPageModelList.add(new AllViewPageModel(1, ds.child("banner").getValue().toString(), ds.child("background").getValue().toString()));
                            break;
                        case "3":
                        case "6":
                            List<ProductHorizontalScrollGridModel> productHorizontalScrollGridModelList = new ArrayList<>();
                            int category_count = 20;
                            for(int i = 1; i <= category_count; i++){
                                productHorizontalScrollGridModelList.add(new ProductHorizontalScrollGridModel(
                                         ds.child("horizontalScrollGridLayout").child(String.valueOf(i)).child("image").getValue().toString()
                                        ,ds.child("horizontalScrollGridLayout").child(String.valueOf(i)).child("name").getValue().toString()
                                        ,ds.child("horizontalScrollGridLayout").child(String.valueOf(i)).child("short_description").getValue().toString()
                                        ,ds.child("horizontalScrollGridLayout").child(String.valueOf(i)).child("weight").getValue().toString()
                                        ,Long.parseLong(ds.child("horizontalScrollGridLayout").child(String.valueOf(i)).child("price").getValue().toString())
                                        ,Long.parseLong(ds.child("horizontalScrollGridLayout").child(String.valueOf(i)).child("discounted_price").getValue().toString())
                                        ,ds.child("horizontalScrollGridLayout").child(String.valueOf(i)).child("key").getValue().toString()
                                ));
                            }
                            allViewPageModelList.add(new AllViewPageModel(Integer.parseInt(String.valueOf(view_type)), productHorizontalScrollGridModelList,"title"));
                            break;

                        case "5":
                            List<CategoryModel> categoryModelList = new ArrayList<>();
                        int category_product_count = 9;
                            for(int i = 1; i <= category_product_count; i++){
                                categoryModelList.add(new CategoryModel(ds.child(String.valueOf(i)).child("name").getValue().toString()
                                        ,ds.child(String.valueOf(i)).child("image").getValue().toString()
                                        ,ds.child(String.valueOf(i)).child("offer").getValue().toString()));
                            }
                            allViewPageModelList.add(new AllViewPageModel(5,"title", categoryModelList));
                            break;
                        default:
                            break;
                    }
                    allViewPageAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mRef.addListenerForSingleValueEvent(listener);
        mCartReference.child(uid).addValueEventListener(listener1);
    }
    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        testing.setAdapter(null);
        if (mCartReference.child(uid) != null && listener1 != null) {
            mCartReference.child(uid).removeEventListener(listener1);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmer();
    }
}