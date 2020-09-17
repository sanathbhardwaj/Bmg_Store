package com.digital.dhanbadbasket.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.digital.dhanbadbasket.Models.BannerSliderModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.digital.dhanbadbasket.R;

import java.util.List;

public class BannerSliderAdapter extends PagerAdapter {
    List<BannerSliderModel> bannerSliderModels;
    public BannerSliderAdapter(List<BannerSliderModel> bannerSliderModels) {
        this.bannerSliderModels = bannerSliderModels;
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.home_screen_slider, container, false);
        ImageView banner = view.findViewById(R.id.banner_slider);
        Glide.with(container.getContext()).load(bannerSliderModels.get(position).getBanner()).apply(new RequestOptions().placeholder(R.drawable.placeholder_banner)).into(banner);
        container.addView(view, 0);
        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
    @Override
    public int getCount() {
        return bannerSliderModels.size();
    }
}
