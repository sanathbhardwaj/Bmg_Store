package com.digital.dhanbadbasket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.digital.dhanbadbasket.Adapters.SectionPagerAdapter;
import com.digital.dhanbadbasket.Models.CartModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class ProductsListActivity extends AppCompatActivity {

    DatabaseReference mRef, mCart;
    ImageButton backarrow;
    String uid = FirebaseAuth.getInstance().getUid();
    DatabaseReference mCartReference;
    private ValueEventListener listener;
    Button cart_btn;
    TextView price, items;
    RelativeLayout cart, shimmer_rl, main_rl;
    private ShimmerFrameLayout mShimmerViewContainer;
    RelativeLayout search_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);
        final List<String> titles = new ArrayList<>();

//        category = getIntent().getStringExtra("category_name");
//        sub_category = getIntent().getStringExtra("sub_category");
//        position = getIntent().getStringExtra("position");
//        previous_category = getIntent().getStringExtra("previous_category");

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String category = sp.getString("category", "");
        String sub_category = sp.getString("sub_category", "");
        String position = sp.getString("position", "");
        backarrow = findViewById(R.id.backarrow);
        mShimmerViewContainer = findViewById(R.id.shimmer);
        shimmer_rl = findViewById(R.id.shimmer_rl);
        main_rl = findViewById(R.id.main_rl);

        mCart = FirebaseDatabase.getInstance().getReference("users");
        mCartReference = FirebaseDatabase.getInstance().getReference("users");

        cart = findViewById(R.id.cart);
        cart_btn = findViewById(R.id.cart_btn);

        price = findViewById(R.id.price);
        items = findViewById(R.id.items);

        search_bar = findViewById(R.id.search_bar);

        search_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductsListActivity.this, SearchActivity.class));
            }
        });

        cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductsListActivity.this, CartActivity.class));
            }
        });

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        mCartReference.child(uid).addValueEventListener(listener);

        if (sub_category.equals("")){
            mRef = FirebaseDatabase.getInstance().getReference("main_menu").child(category).child("sub_categories");
        }
        else {
            mRef = FirebaseDatabase.getInstance().getReference("main_menu").child(category).child("2_categories").child(position).child("sub_categories");
        }

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (mShimmerViewContainer.isShimmerVisible()){
                    mShimmerViewContainer.stopShimmer();
                    shimmer_rl.setVisibility(View.GONE);
//                    no_internet_rl.setVisibility(View.GONE);
                    main_rl.setVisibility(View.VISIBLE);
                }

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    titles.add(ds.child("name").getValue().toString());
                }
                SectionPagerAdapter sectionsPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager(),BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, titles, category, sub_category, position);
                ViewPager viewPager = findViewById(R.id.view_pager);
                viewPager.setAdapter(sectionsPagerAdapter);
                TabLayout tabs = findViewById(R.id.tabs);
                tabs.setupWithViewPager(viewPager);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}