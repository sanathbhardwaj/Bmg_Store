package com.digital.dhanbadbasket.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.digital.dhanbadbasket.Adapters.ProductListAdapter;
import com.digital.dhanbadbasket.Models.ProductListModel;
import com.digital.dhanbadbasket.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProductListFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String PAGE_TITLE = "no title";
    DatabaseReference mDatabaseReference, mCartRef;
    RecyclerView product_list;
    static String category;
    static String sub_category;
    static String position;
    String uid = FirebaseAuth.getInstance().getUid();
    ValueEventListener listener;
    ProgressBar progress_bar;
    RelativeLayout nothing_here_rl, main_rl;


    final DatabaseReference mCartReference = FirebaseDatabase.getInstance().getReference("users");

    public static ProductListFragment newInstance(int index, String pageTitle, String cat, String sub_cat, String pos){
        ProductListFragment fragment = new ProductListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        bundle.putString(PAGE_TITLE, pageTitle);
        fragment.setArguments(bundle);
        category = cat;
        sub_category = sub_cat;
        position = pos;
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        progress_bar = view.findViewById(R.id.progress_bar);
        nothing_here_rl = view.findViewById(R.id.nothing_here_rl);
        main_rl = view.findViewById(R.id.main_rl);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        product_list = view.findViewById(R.id.recycler);
        product_list.hasFixedSize();
        product_list.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("main_menu").child(category).child("2_categories").child(position).child("sub_categories").child(args.getString(PAGE_TITLE)).child("products");

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount()==0){
                    main_rl.setVisibility(View.GONE);
                    nothing_here_rl.setVisibility(View.VISIBLE);
                    progress_bar.setVisibility(View.GONE);
                }
                else {
                    main_rl.setVisibility(View.VISIBLE);
                    nothing_here_rl.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabaseReference.addValueEventListener(listener);


        mCartRef = FirebaseDatabase.getInstance().getReference("users").child(uid);
        FirebaseRecyclerOptions<ProductListModel> options =
                new FirebaseRecyclerOptions.Builder<ProductListModel>()
                        .setQuery(mDatabaseReference, ProductListModel.class)
                        .build();

        final FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ProductListModel, ProductListAdapter>(options) {
            @Override
            public ProductListAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_product_layout_b, parent, false);

                return new ProductListAdapter(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull final ProductListAdapter holder, final int position, @NonNull final ProductListModel model) {

                progress_bar.setVisibility(View.GONE);
                holder.setDetails(getContext(), model.getName(), model.getPrice(), model.getDiscounted_price(), model.getImage()
                        ,model.getWeight(), model.getShort_description());
                holder.itemView.setOnClickListener(view -> {
                    return;
                });
                holder.itemView.setOnLongClickListener(view -> false);

                final DatabaseReference gameRef = getRef(position);
                final String productID = gameRef.getKey();

                Button add = holder.itemView.findViewById(R.id.add);
                TextView reduce = holder.itemView.findViewById(R.id.reduce);
                TextView increase = holder.itemView.findViewById(R.id.increase);
                TextView quantity = holder.itemView.findViewById(R.id.quantity);
                LinearLayout add_sub = holder.itemView.findViewById(R.id.add_sub);
                final TextView name = holder.itemView.findViewById(R.id.product_name);
                final TextView discounted_price = holder.itemView.findViewById(R.id.discounted_price);
                final TextView actual_price = holder.itemView.findViewById(R.id.price);
                final TextView weight = holder.itemView.findViewById(R.id.product_quantity);

                final int[] counter = {0};
                final int[] discounted_price_product = {0};
                final int[] actual_price_product = {0};
                final int[] discounted_price_per_unit = {0};
                final int[] actual_price_per_unit = {0};

                String s1 = discounted_price.getText().toString();
                String s2 = actual_price.getText().toString();
                if (s2.equals("")){
                    s2=s1;
                }
                actual_price_per_unit[0] = Integer.parseInt(s2.replaceAll("[^0-9]", ""));
                discounted_price_per_unit[0] = Integer.parseInt(s1.replaceAll("[^0-9]", ""));

                add.setOnClickListener(view -> {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        if (FirebaseAuth.getInstance().getCurrentUser().isAnonymous()) {
                            Toast.makeText(getContext(), "Login required to add product.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            counter[0]++;
                            actual_price_product[0] = counter[0]*actual_price_per_unit[0];
                            discounted_price_product[0] = counter[0]*discounted_price_per_unit[0];
                            add.setVisibility(View.INVISIBLE);
                            add_sub.setVisibility(View.VISIBLE);
                            quantity.setText(String.valueOf(counter[0]));
                            mCartRef.child("cart").child("products").child(productID).child("Product_quantity").setValue(String.valueOf(counter[0]));
                            mCartRef.child("cart").child("products").child(productID).child("Product_image").setValue(model.getImage());
                            mCartRef.child("cart").child("products").child(productID).child("Product_weight").setValue(weight.getText().toString());
                            mCartRef.child("cart").child("products").child(productID).child("Product_discounted_price").setValue(String.valueOf(discounted_price_product[0]));
                            mCartRef.child("cart").child("products").child(productID).child("Product_discounted_unit_price").setValue(String.valueOf(discounted_price_per_unit[0]));
                            mCartRef.child("cart").child("products").child(productID).child("Product_name").setValue(name.getText().toString());
                            mCartRef.child("cart").child("products").child(productID).child("Product_price").setValue(String.valueOf(actual_price_product[0]));
                            mCartRef.child("cart").child("products").child(productID).child("Product_unit_price").setValue(String.valueOf(actual_price_per_unit[0]));
                        }
                    }
                });
                increase.setOnClickListener(view -> {
                    counter[0]++;
                    actual_price_product[0] = counter[0]*actual_price_per_unit[0];
                    discounted_price_product[0] = counter[0]*discounted_price_per_unit[0];
                    quantity.setText(String.valueOf(counter[0]));
                    mCartRef.child("cart").child("products").child(productID).child("Product_quantity").setValue(String.valueOf(counter[0]));
                    mCartRef.child("cart").child("products").child(productID).child("Product_image").setValue(model.getImage());
                    mCartRef.child("cart").child("products").child(productID).child("Product_weight").setValue(weight.getText().toString());
                    mCartRef.child("cart").child("products").child(productID).child("Product_discounted_price").setValue(String.valueOf(discounted_price_product[0]));
                    mCartRef.child("cart").child("products").child(productID).child("Product_discounted_unit_price").setValue(String.valueOf(discounted_price_per_unit[0]));
                    mCartRef.child("cart").child("products").child(productID).child("Product_name").setValue(name.getText().toString());
                    mCartRef.child("cart").child("products").child(productID).child("Product_price").setValue(String.valueOf(actual_price_product[0]));
                    mCartRef.child("cart").child("products").child(productID).child("Product_unit_price").setValue(String.valueOf(actual_price_per_unit[0]));
                });
                reduce.setOnClickListener(view -> {
                    if (counter[0] == 1) {
                        counter[0]--;
                        DatabaseReference clearCart = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        clearCart.child("cart").child("products").child(productID).removeValue();

                    }
                    else {
                        counter[0]--;
                        actual_price_product[0] = counter[0]*actual_price_per_unit[0];
                        discounted_price_product[0] = counter[0]*discounted_price_per_unit[0];
                        quantity.setText(String.valueOf(counter[0]));
                        mCartRef.child("cart").child("products").child(productID).child("Product_quantity").setValue(String.valueOf(counter[0]));
                        mCartRef.child("cart").child("products").child(productID).child("Product_image").setValue(model.getImage());
                        mCartRef.child("cart").child("products").child(productID).child("Product_weight").setValue(weight.getText().toString());
                        mCartRef.child("cart").child("products").child(productID).child("Product_discounted_price").setValue(String.valueOf(discounted_price_product[0]));
                        mCartRef.child("cart").child("products").child(productID).child("Product_discounted_unit_price").setValue(String.valueOf(discounted_price_per_unit[0]));
                        mCartRef.child("cart").child("products").child(productID).child("Product_name").setValue(name.getText().toString());
                        mCartRef.child("cart").child("products").child(productID).child("Product_price").setValue(String.valueOf(actual_price_product[0]));
                        mCartRef.child("cart").child("products").child(productID).child("Product_unit_price").setValue(String.valueOf(actual_price_per_unit[0]));
                    }
                });

                DatabaseReference mCart = FirebaseDatabase.getInstance().getReference("users");

                mCart.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("cart").child("products").child(productID).child("Product_quantity").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (!dataSnapshot.exists()){
                            counter[0] = 0;
                            add.setVisibility(View.VISIBLE);
                            add_sub.setVisibility(View.INVISIBLE);
                        }
                        else {
                            add.setVisibility(View.INVISIBLE);
                            add_sub.setVisibility(View.VISIBLE);
                            counter[0] = Integer.parseInt(dataSnapshot.getValue().toString());
                            actual_price_product[0] = counter[0]*actual_price_per_unit[0];
                            discounted_price_product[0] = counter[0]*discounted_price_per_unit[0];
                            quantity.setText(dataSnapshot.getValue().toString());
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        };
        firebaseRecyclerAdapter.startListening();
        product_list.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mCartReference.child(uid) != null && listener != null) {
            mCartReference.child(uid).removeEventListener(listener);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mCartReference.child(uid) != null && listener != null) {
            mCartReference.child(uid).removeEventListener(listener);
        }
        if (mDatabaseReference != null && listener != null) {
            mDatabaseReference.removeEventListener(listener);
        }
    }

}
