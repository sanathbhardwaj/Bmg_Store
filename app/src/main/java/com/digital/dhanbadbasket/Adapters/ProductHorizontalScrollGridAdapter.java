package com.digital.dhanbadbasket.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.dhanbadbasket.Models.CartModel;
import com.digital.dhanbadbasket.Models.ProductHorizontalScrollGridModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.digital.dhanbadbasket.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ProductHorizontalScrollGridAdapter extends RecyclerView.Adapter<ProductHorizontalScrollGridAdapter.ViewHolder> {

    private List<ProductHorizontalScrollGridModel> productHorizontalScrollGridModelList;
    String uid = FirebaseAuth.getInstance().getUid();
    ValueEventListener listener, listner1;
    DatabaseReference mCartReference;


    public ProductHorizontalScrollGridAdapter(List<ProductHorizontalScrollGridModel> productHorizontalScrollGridModelList) {
        this.productHorizontalScrollGridModelList = productHorizontalScrollGridModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_product_layout_a, parent, false);
        return new ProductHorizontalScrollGridAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final String  image = productHorizontalScrollGridModelList.get(position).getImage();
        final String name = productHorizontalScrollGridModelList.get(position).getName();
        final String discounted_price = productHorizontalScrollGridModelList.get(position).getDiscounted_price().toString();
        final String actual_price = productHorizontalScrollGridModelList.get(position).getPrice().toString();
        final String weight = productHorizontalScrollGridModelList.get(position).getWeight();
        final String productID = productHorizontalScrollGridModelList.get(position).getKey();

        holder.setDetails(image, name, discounted_price, weight);
        final DatabaseReference mCartRef, mCart;

        mCartRef = FirebaseDatabase.getInstance().getReference("users").child(uid).child("cart");


        holder.discounted_price_per_unit[holder.getAdapterPosition()] = Integer.parseInt(discounted_price.replaceAll("[^0-9]", ""));
        holder.actual_price_per_unit[holder.getAdapterPosition()] = Integer.parseInt(actual_price.replaceAll("[^0-9]", ""));

        if (holder.counter[holder.getAdapterPosition()]>0){
            holder.btn_add.setVisibility(View.INVISIBLE);
            holder.add_sub.setVisibility(View.VISIBLE);
            holder.quantity.setText(String.valueOf(holder.counter[holder.getAdapterPosition()]));
        }
        else {
            holder.btn_add.setVisibility(View.VISIBLE);
            holder.add_sub.setVisibility(View.INVISIBLE);
        }

        holder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.counter[holder.getAdapterPosition()]++;
                holder.discounted_price[holder.getAdapterPosition()] = holder.counter[holder.getAdapterPosition()]*holder.discounted_price_per_unit[holder.getAdapterPosition()];
                holder.actual_price[holder.getAdapterPosition()] = holder.counter[holder.getAdapterPosition()]*holder.actual_price_per_unit[holder.getAdapterPosition()];
                holder.btn_add.setVisibility(View.INVISIBLE);
                holder.add_sub.setVisibility(View.VISIBLE);
                holder.quantity.setText(String.valueOf(holder.counter[holder.getAdapterPosition()]));
                mCartRef.child("products").child(productID).child("Product_quantity").setValue(String.valueOf(holder.counter[holder.getAdapterPosition()]));
                mCartRef.child("products").child(productID).child("Product_name").setValue(name);
                mCartRef.child("products").child(productID).child("Product_image").setValue(image);
                mCartRef.child("products").child(productID).child("Product_weight").setValue(weight);
                mCartRef.child("products").child(productID).child("Product_discounted_unit_price").setValue(discounted_price);
                mCartRef.child("products").child(productID).child("Product_unit_price").setValue(actual_price);
                mCartRef.child("products").child(productID).child("Product_discounted_price").setValue(String.valueOf(holder.discounted_price[holder.getAdapterPosition()]));
                mCartRef.child("products").child(productID).child("Product_price").setValue(String.valueOf(holder.actual_price[holder.getAdapterPosition()]));


            }
        });

        holder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.counter[holder.getAdapterPosition()]++;
                holder.discounted_price[holder.getAdapterPosition()] = holder.counter[holder.getAdapterPosition()]*holder.discounted_price_per_unit[holder.getAdapterPosition()];
                holder.actual_price[holder.getAdapterPosition()] = holder.counter[holder.getAdapterPosition()]*holder.actual_price_per_unit[holder.getAdapterPosition()];
                holder.quantity.setText(String.valueOf(holder.counter[holder.getAdapterPosition()]));
                mCartRef.child("products").child(productID).child("Product_quantity").setValue(String.valueOf(holder.counter[holder.getAdapterPosition()]));
                mCartRef.child("products").child(productID).child("Product_name").setValue(name);
                mCartRef.child("products").child(productID).child("Product_image").setValue(image);
                mCartRef.child("products").child(productID).child("Product_weight").setValue(weight);
                mCartRef.child("products").child(productID).child("Product_discounted_unit_price").setValue(discounted_price);
                mCartRef.child("products").child(productID).child("Product_unit_price").setValue(actual_price);
                mCartRef.child("products").child(productID).child("Product_discounted_price").setValue(String.valueOf(holder.discounted_price[holder.getAdapterPosition()]));
                mCartRef.child("products").child(productID).child("Product_price").setValue(String.valueOf(holder.actual_price[holder.getAdapterPosition()]));
            }
        });
        holder.reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.counter[holder.getAdapterPosition()]--;
                holder.discounted_price[holder.getAdapterPosition()] = holder.counter[holder.getAdapterPosition()]*holder.discounted_price_per_unit[holder.getAdapterPosition()];
                holder.actual_price[holder.getAdapterPosition()] = holder.counter[holder.getAdapterPosition()]*holder.actual_price_per_unit[holder.getAdapterPosition()];
                if (holder.counter[holder.getAdapterPosition()]==0){
                    holder.add_sub.setVisibility(View.INVISIBLE);
                    holder.btn_add.setVisibility(View.VISIBLE);
                    mCartRef.child("products").child(productID).removeValue();
                    return;
                }
                else {
                    holder.quantity.setText(String.valueOf(holder.counter[holder.getAdapterPosition()]));
                }
                mCartRef.child("products").child(productID).child("Product_quantity").setValue(String.valueOf(holder.counter[holder.getAdapterPosition()]));
                mCartRef.child("products").child(productID).child("Product_name").setValue(name);
                mCartRef.child("products").child(productID).child("Product_image").setValue(image);
                mCartRef.child("products").child(productID).child("Product_weight").setValue(weight);
                mCartRef.child("products").child(productID).child("Product_discounted_unit_price").setValue(discounted_price);
                mCartRef.child("products").child(productID).child("Product_unit_price").setValue(actual_price);
                mCartRef.child("products").child(productID).child("Product_discounted_price").setValue(String.valueOf(holder.discounted_price[holder.getAdapterPosition()]));
                mCartRef.child("products").child(productID).child("Product_price").setValue(String.valueOf(holder.actual_price[holder.getAdapterPosition()]));

            }
        });
        mCart = FirebaseDatabase.getInstance().getReference("users");

        listner1 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    holder.counter[holder.getAdapterPosition()] = 0;
                    holder.btn_add.setVisibility(View.VISIBLE);
                    holder.add_sub.setVisibility(View.INVISIBLE);
                }
                else {
                    holder.btn_add.setVisibility(View.INVISIBLE);
                    holder.add_sub.setVisibility(View.VISIBLE);
                    holder.counter[holder.getAdapterPosition()] = Integer.parseInt(dataSnapshot.getValue().toString());
                    holder.actual_price[holder.getAdapterPosition()] = holder.counter[holder.getAdapterPosition()]*holder.actual_price_per_unit[holder.getAdapterPosition()];
                    holder.discounted_price[holder.getAdapterPosition()] = holder.counter[holder.getAdapterPosition()]*holder.discounted_price_per_unit[holder.getAdapterPosition()];
                    holder.quantity.setText(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mCart.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("cart").child("products").child(productID).child("Product_quantity").addListenerForSingleValueEvent(listner1);
    }
    @Override
    public int getItemCount() {
        return productHorizontalScrollGridModelList.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView product_image;
        TextView product_name, product_quantity, product_price, increase, reduce, quantity;
        Button btn_add;
        LinearLayout add_sub;
        final int [] counter = new int [productHorizontalScrollGridModelList.size()];
        final int[] discounted_price_per_unit = new int[productHorizontalScrollGridModelList.size()];
        final int[] discounted_price = new int[productHorizontalScrollGridModelList.size()];

        final int[] actual_price_per_unit = new int[productHorizontalScrollGridModelList.size()];
        final int[] actual_price = new int[productHorizontalScrollGridModelList.size()];

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            product_image = itemView.findViewById(R.id.product_image);
            product_name = itemView.findViewById(R.id.product_name);
            product_quantity = itemView.findViewById(R.id.product_quantity);
            product_price = itemView.findViewById(R.id.discounted_price);
            btn_add = itemView.findViewById(R.id.add);
            add_sub = itemView.findViewById(R.id.add_sub);

            increase = itemView.findViewById(R.id.increase);
            reduce = itemView.findViewById(R.id.reduce);
            quantity = itemView.findViewById(R.id.quantity);

            mCartReference = FirebaseDatabase.getInstance().getReference("users");
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
                        mCartReference.child(uid).child("cart").child("total_item_price").removeValue();
                        mCartReference.child(uid).child("cart").child("total_item_discounted_price").removeValue();
                        mCartReference.child(uid).child("cart").child("no_items").removeValue();
                    }
                    else {
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
        }
        private void setDetails(String image, String name, String price, String weight){
            Glide.with(itemView.getContext()).load(image).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(product_image);
            product_name.setText(name);
            product_quantity.setText(weight);
            product_price.setText("₹ "+price);
        }
    }
    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (mCartReference.child(uid) != null && listener != null) {
            mCartReference.child(uid).removeEventListener(listener);
        }

//        Toast.makeText(recyclerView.getContext(), "Adapter closed", Toast.LENGTH_SHORT).show();
    }
}
