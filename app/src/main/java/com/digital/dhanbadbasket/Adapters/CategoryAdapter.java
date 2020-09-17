package com.digital.dhanbadbasket.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.dhanbadbasket.CategoryActivity;
import com.digital.dhanbadbasket.DoctorsActivity;
import com.digital.dhanbadbasket.Models.CategoryModel;
import com.digital.dhanbadbasket.PickupAndDropActivity;
import com.digital.dhanbadbasket.ProductsListActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.digital.dhanbadbasket.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    private List<CategoryModel> categoryModelList;
    private Context mContext;

    public CategoryAdapter(List<CategoryModel> categoryModelList, Context context) {
        this.categoryModelList = categoryModelList;
        this.mContext = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_category_layout, parent, false);
        return new CategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String  resource = categoryModelList.get(position).getImage();
        String discount = categoryModelList.get(position).getDiscount();
        String title = categoryModelList.get(position).getTitle();

        holder.setImage(resource);
        holder.setDiscount(discount);
        holder.setTitle(title);

    }
    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title_, discount_;
        RelativeLayout category_rl;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.category_image);
            title_ = itemView.findViewById(R.id.category_title);
            discount_ = itemView.findViewById(R.id.category_discount);
            category_rl = itemView.findViewById(R.id.category_rl);

            String activity = mContext.getClass().getSimpleName();


            category_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String a = "Groceries & Essentials";
                    String b = "Fruits & Vegetables";
                    String c = "Kitchen Utensils";
                    String d = "Pickup & Drop Anything";
                    String e = "Meat, Fish & Eggs";
                    String f = "Pet Supplies";
                    String g = "Doctors in the City";
                    String z = title_.getText().toString();

                    if (activity.equals("MainActivity") && (z.equals(a))){
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("category",title_.getText().toString());
                        editor.putString("position",String.valueOf(ViewHolder.this.getAdapterPosition() + 1));
                        editor.commit();
                        itemView.getContext().startActivity(new
                                Intent(itemView.getContext(),
                                CategoryActivity.class));
                    }
                    else if (activity.equals("MainActivity") && z.equals(d)){
                        itemView.getContext().startActivity(new Intent(
                                itemView.getContext(), PickupAndDropActivity.class
                        ));
                    }
                    else if (activity.equals("MainActivity") && z.equals(g)){
                        itemView.getContext().startActivity(new Intent(
                                itemView.getContext(), DoctorsActivity.class
                        ));
                    }
                    else if (activity.equals("CategoryActivity")){
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("sub_category",title_.getText().toString());
                        editor.putString("position",String.valueOf(ViewHolder.this.getAdapterPosition() + 1));
                        editor.commit();
                        itemView.getContext().startActivity(new
                                Intent(itemView.getContext(),
                                ProductsListActivity.class));
                    }
                    else if (activity.equals("MainActivity") && (z.equals(e)||z.equals(f) || z.equals(b))){
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("sub_category","");
                        editor.putString("category",title_.getText().toString());
                        editor.putString("position",String.valueOf(ViewHolder.this.getAdapterPosition() + 1));
                        editor.commit();
                        itemView.getContext().startActivity(new
                                Intent(itemView.getContext(),
                                ProductsListActivity.class));
                    }
                    else {
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("sub_category",title_.getText().toString());
                        editor.putString("position",String.valueOf(ViewHolder.this.getAdapterPosition() + 1));
                        editor.commit();
                        itemView.getContext().startActivity(new
                                Intent(itemView.getContext(),
                                ProductsListActivity.class));
                    }

                }
            });
        }
        private void setImage(String resource){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(imageView);
        }
        private void setDiscount(String discount){
            if (discount.equals("0")){
                discount_.setVisibility(View.INVISIBLE);
            }
            else {
                discount_.setText(discount+"% off");
            }
        }
        private void setTitle(String title){
            title_.setText(title);
        }
    }
}
