package com.digital.dhanbadbasket.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.digital.dhanbadbasket.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

public class ProductListAdapter extends RecyclerView.ViewHolder{
    View myView;

    public ProductListAdapter(@NonNull View itemView) {
        super(itemView);

        myView = itemView;

        itemView.setOnClickListener(view -> {
            mClickListener.OnItemClick(view, getAdapterPosition());
        });
        itemView.setOnLongClickListener(view -> {
            mClickListener.OnItemLongClick(view, getAdapterPosition());
            return true;
        });
    }

    public void setDetails(Context context, String product_name_, Long actual_price_, Long discounted_price_, String product_image_, String weight_, String short_description_){

        TextView product_name, actual_price, discounted_price, short_description, weight;
        ImageView product_image;

        product_name = myView.findViewById(R.id.product_name);
        actual_price = myView.findViewById(R.id.price);
        discounted_price = myView.findViewById(R.id.discounted_price);
        short_description = myView.findViewById(R.id.product_short_description);
        weight = myView.findViewById(R.id.product_quantity);
        product_image = myView.findViewById(R.id.product_image);

        product_name.setText(product_name_);
        short_description.setText(short_description_);
        weight.setText(weight_);
        Glide.with(myView.getContext()).load(product_image_).placeholder(R.mipmap.ic_launcher).into(product_image);
        if (actual_price_.equals(discounted_price_)){
            actual_price.setText("");
            discounted_price.setText("₹ "+discounted_price_);
        }
        else {
            actual_price.setText("₹ "+actual_price_);
            actual_price.setPaintFlags(actual_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            discounted_price.setText("₹ "+discounted_price_);
        }
    }
    private ProductListAdapter.ClickListener mClickListener;
    public interface ClickListener{
        void OnItemClick(View view, int position);
        void OnItemLongClick(View view, int position);
    }
}
