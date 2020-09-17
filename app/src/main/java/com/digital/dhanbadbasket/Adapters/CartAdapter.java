package com.digital.dhanbadbasket.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.digital.dhanbadbasket.R;

public class CartAdapter extends RecyclerView.ViewHolder{
    View myView;

    public CartAdapter(@NonNull View itemView) {
        super(itemView);

        myView = itemView;

        itemView.setOnClickListener(view -> mClickListener.OnItemClick(view, getAdapterPosition()));
        itemView.setOnLongClickListener(view -> {
            mClickListener.OnItemLongClick(view, getAdapterPosition());
            return true;
        });
    }

    public void setDetails(Context context, String product_image_, String product_name_, String product_price_, String product_quantity_, String product_weight_){

        TextView product_name, product_price, product_quantity, product_weight;
        ImageView product_image;

        product_name = myView.findViewById(R.id.product_name);
        product_price = myView.findViewById(R.id.discounted_price);
        product_quantity = myView.findViewById(R.id.quantity);
        product_weight = myView.findViewById(R.id.product_quantity);
        product_image = myView.findViewById(R.id.product_image);

        product_name.setText(product_name_);
        product_price.setText("₹ "+product_price_);
        product_quantity.setText(product_quantity_);
        product_weight.setText(product_weight_);
        Glide.with(myView.getContext()).load(product_image_).placeholder(R.mipmap.ic_launcher).into(product_image);
    }
    private CartAdapter.ClickListener mClickListener;
    public interface ClickListener{
        void OnItemClick(View view, int position);
        void OnItemLongClick(View view, int position);
    }
}
