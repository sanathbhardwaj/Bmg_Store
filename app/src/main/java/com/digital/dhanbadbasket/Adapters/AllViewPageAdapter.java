package com.digital.dhanbadbasket.Adapters;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.digital.dhanbadbasket.Models.BannerSliderModel;
import com.digital.dhanbadbasket.Models.CategoryModel;
import com.digital.dhanbadbasket.Models.AllViewPageModel;
import com.digital.dhanbadbasket.Models.ProductHorizontalScrollGridModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.digital.dhanbadbasket.ProductsActivity;
import com.digital.dhanbadbasket.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AllViewPageAdapter extends RecyclerView.Adapter{

    private List<AllViewPageModel> allViewPageModelList;
    private TextView Title, view_all;
    private RecyclerView gridView;

    public AllViewPageAdapter(List<AllViewPageModel> allViewPageModelList) {
        this.allViewPageModelList = allViewPageModelList;
    }

    @Override
    public int getItemViewType(int position) {
        switch(allViewPageModelList.get(position).getType()){
            case 0:
                return AllViewPageModel.BANNER_SLIDER;
            case 1:
                return AllViewPageModel.STRIP_AD_BANNER;
            case 4:
                return AllViewPageModel.CATEGORY_LAYOUT;
            case 3:
                return AllViewPageModel.PRODUCT_HORIZONTAL_SCROLL_GRID;
            case 5:
                return AllViewPageModel.CATEGORY_PRODUCT_LAYOUT;
            case 6:
                return AllViewPageModel.PRODUCT_HORIZONTAL_SCROLL_GRID_1;
            default:
                return -1;
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case AllViewPageModel.BANNER_SLIDER:
                View bannerSliderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_slider_layout, parent, false);
                return new BannerSliderViewHolder(bannerSliderView);
            case AllViewPageModel.STRIP_AD_BANNER:
                View stripAdView = LayoutInflater.from(parent.getContext()).inflate(R.layout.strip_ad_layout,parent, false);
                return new StripBannerViewHolder(stripAdView);
            case AllViewPageModel.PRODUCT_HORIZONTAL_SCROLL_GRID:
            case AllViewPageModel.PRODUCT_HORIZONTAL_SCROLL_GRID_1:
                View horizontalScrollGridView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_grid_layout, parent,false);
                return new HorizontalScrollGridViewHolder(horizontalScrollGridView);
            case AllViewPageModel.CATEGORY_LAYOUT:
            case AllViewPageModel.CATEGORY_PRODUCT_LAYOUT:
                View categoryView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_grid_layout, parent,false);
                return new CategoryViewHolder(categoryView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (allViewPageModelList.get(position).getType()) {
            case AllViewPageModel.BANNER_SLIDER:
                List<BannerSliderModel> sliderModelList = allViewPageModelList.get(position).getBannerSliderModelList();
                ((BannerSliderViewHolder) holder).setBannerSliderViewPager(sliderModelList);
                break;
            case AllViewPageModel.STRIP_AD_BANNER:
                String resource = allViewPageModelList.get(position).getResource();
                String color = allViewPageModelList.get(position).getBackgroundColour();
                ((StripBannerViewHolder)holder).setStripAd(resource, color);
                break;
            case AllViewPageModel.CATEGORY_LAYOUT:
                String categoryLayoutTitle = "Instant delivery in the city";
                List<CategoryModel> categoryModelList = allViewPageModelList.get(position).getCategoryModelList();
                ((CategoryViewHolder)holder).setCategoryLayout(categoryModelList, categoryLayoutTitle);
                break;
            case AllViewPageModel.CATEGORY_PRODUCT_LAYOUT:
                String categoryProductLayoutTitle = "Shop by category";
                List<CategoryModel> categoryProductModelList = allViewPageModelList.get(position).getCategoryModelList();
                ((CategoryViewHolder)holder).setProductCategoryLayout(categoryProductModelList, categoryProductLayoutTitle);
                break;
            case AllViewPageModel.PRODUCT_HORIZONTAL_SCROLL_GRID:
                String horizontalScrollGridTitle = "Top Selling";
                List<ProductHorizontalScrollGridModel> productHorizontalScrollGridModelList = allViewPageModelList.get(position).getProductHorizontalScrollGridModelList();
                ((HorizontalScrollGridViewHolder)holder).setHorizontalScrollGrid(productHorizontalScrollGridModelList, horizontalScrollGridTitle);
                break;
            case AllViewPageModel.PRODUCT_HORIZONTAL_SCROLL_GRID_1:
                String horizontalScrollGridTitle1 = "Frequently bought";
                List<ProductHorizontalScrollGridModel> productHorizontalScrollGridModelList1 = allViewPageModelList.get(position).getProductHorizontalScrollGridModelList();
                ((HorizontalScrollGridViewHolder)holder).setHorizontalScrollGridSingle(productHorizontalScrollGridModelList1, horizontalScrollGridTitle1);
                break;

            default:
                return;
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        gridView.setAdapter(null);
    }

    @Override
    public int getItemCount() {
        return allViewPageModelList.size();
    }
    public class BannerSliderViewHolder extends RecyclerView.ViewHolder{

        private ViewPager bannerSliderViewPager;
        private int currentPage;
        private Timer timer;
        final private long DELAY_TIME = 3000;
        final private long PERIOD_TIME = 3000;
        private List<BannerSliderModel> arrangedList;
        public BannerSliderViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerSliderViewPager = itemView.findViewById(R.id.banner_slider_view_pager);
        }
        private void setBannerSliderViewPager(final List<BannerSliderModel> bannerSliderModelList){
            currentPage = 2;
            if (timer != null){
                timer.cancel();
            }
            arrangedList = new ArrayList<>();
            for(int x = 0; x <bannerSliderModelList.size(); x++){
                arrangedList.add(x, bannerSliderModelList.get(x));
            }
            arrangedList.add(0, bannerSliderModelList.get(bannerSliderModelList.size()-2));
            arrangedList.add(1, bannerSliderModelList.get(bannerSliderModelList.size()-1));
            arrangedList.add(bannerSliderModelList.get(0));
            arrangedList.add(bannerSliderModelList.get(1));


            BannerSliderAdapter bannerSliderAdapter = new BannerSliderAdapter(arrangedList);
            bannerSliderViewPager.setAdapter(bannerSliderAdapter);
            bannerSliderViewPager.setClipToPadding(false);
            bannerSliderViewPager.setPageMargin(20);

            bannerSliderViewPager.setCurrentItem(currentPage);
            ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }
                @Override
                public void onPageSelected(int position) {
                    currentPage = position;
                }
                @Override
                public void onPageScrollStateChanged(int state) {
                    if(state == ViewPager.SCROLL_STATE_IDLE){
                        pageLooper(arrangedList);
                    }
                }
            };
            bannerSliderViewPager.addOnPageChangeListener(onPageChangeListener);
            startBannerSlideShow(arrangedList);

            bannerSliderViewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    BannerSliderViewHolder.this.pageLooper(arrangedList);
                    BannerSliderViewHolder.this.stopBannerSlideShow();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        BannerSliderViewHolder.this.startBannerSlideShow(arrangedList);
                    }
                    return false;
                }
            });
        }
        private void pageLooper(List<BannerSliderModel> bannerSliderModelList){
            if (currentPage == bannerSliderModelList.size() - 2){
                currentPage = 2;
                bannerSliderViewPager.setCurrentItem(currentPage, false);
            }
            if (currentPage == 1){
                currentPage = bannerSliderModelList.size()-3;
                bannerSliderViewPager.setCurrentItem(currentPage, false);
            }
        }
        private void startBannerSlideShow(final List<BannerSliderModel> bannerSliderModelList){
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (currentPage >= bannerSliderModelList.size()) {
                        currentPage = 1;
                    }
                    bannerSliderViewPager.setCurrentItem(currentPage++, true);
                }
            };
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(runnable);
                }
            },DELAY_TIME, PERIOD_TIME);
        }
        private void stopBannerSlideShow(){
            timer.cancel();
        }

    }
    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        private TextView categoryTitle;
        private RecyclerView gridView;
        private TextView view_all;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.grid_category_layout_title);
            gridView = itemView.findViewById(R.id.grid_category_layout);
            view_all = itemView.findViewById(R.id.view_all);
        }
        private void setCategoryLayout(List<CategoryModel> categoryModelList, String title){
            view_all.setVisibility(View.GONE);
            categoryTitle.setText(title);
            CategoryAdapter categoryAdapter = new CategoryAdapter(categoryModelList, itemView.getContext());
            gridView.hasFixedSize();
            gridView.setLayoutManager(new GridLayoutManager(itemView.getContext(),4, LinearLayoutManager.VERTICAL,false));
            gridView.setAdapter(categoryAdapter);
            categoryAdapter.notifyDataSetChanged();
        }
        private void setProductCategoryLayout(List<CategoryModel> categoryModelList, String title){
            categoryTitle.setText(title);
            view_all.setVisibility(View.GONE);
            CategoryAdapter categoryAdapter = new CategoryAdapter(categoryModelList, itemView.getContext());
            gridView.hasFixedSize();
            gridView.setLayoutManager(new GridLayoutManager(itemView.getContext(),3));
            gridView.setAdapter(categoryAdapter);
            categoryAdapter.notifyDataSetChanged();
        }
    }
    public class StripBannerViewHolder extends RecyclerView.ViewHolder{

        private ImageView strAdImg;
        private CardView strAdLayout;

        public StripBannerViewHolder(@NonNull View itemView) {
            super(itemView);

            strAdImg = itemView.findViewById(R.id.strip_ad_img);
            strAdLayout = itemView.findViewById(R.id.strip_ad_container);
        }
        private void setStripAd(String resource, String color){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.placeholder_banner_2)).into(strAdImg);
            strAdLayout.setBackgroundColor(Color.parseColor(color));
//            strAdLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(itemView.getContext(), "Clicked"+getLayoutPosition(), Toast.LENGTH_SHORT).show();
//
//                    if (getLayoutPosition()==2){
//                        itemView.getContext().startActivity(new Intent(itemView.getContext(), MedicineActivity.class));
//                    }
//                    else if(getLayoutPosition()==4){
//                        itemView.getContext().startActivity(new Intent(itemView.getContext(), TaskMasterActivity.class));
//                    }
//                }
//            });
        }
    }
    public class HorizontalScrollGridViewHolder extends RecyclerView.ViewHolder{
        public HorizontalScrollGridViewHolder(@NonNull View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.grid_category_layout_title);
            view_all = itemView.findViewById(R.id.view_all);
            gridView = itemView.findViewById(R.id.grid_category_layout);
        }
        private void setHorizontalScrollGrid(List<ProductHorizontalScrollGridModel> horizontalScrollGridModelList, String title){
            Title.setText(title);
            view_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemView.getContext().startActivity(new Intent(itemView.getContext(), ProductsActivity.class));
                }
            });
            ProductHorizontalScrollGridAdapter productHorizontalScrollGridAdapter = new ProductHorizontalScrollGridAdapter(horizontalScrollGridModelList);
            gridView.hasFixedSize();
            gridView.setLayoutManager(new GridLayoutManager(itemView.getContext(), 2, GridLayoutManager.HORIZONTAL, false));
            gridView.setAdapter(productHorizontalScrollGridAdapter);
            productHorizontalScrollGridAdapter.notifyDataSetChanged();
        }
        private void setHorizontalScrollGridSingle(List<ProductHorizontalScrollGridModel> horizontalScrollGridModelList, String title){
            Title.setText(title);
            view_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemView.getContext().startActivity(new Intent(itemView.getContext(), ProductsActivity.class));
                }
            });
            ProductHorizontalScrollGridAdapter productHorizontalScrollGridAdapter = new ProductHorizontalScrollGridAdapter(horizontalScrollGridModelList);
            gridView.hasFixedSize();
            gridView.setLayoutManager(new GridLayoutManager(itemView.getContext(), 1, GridLayoutManager.HORIZONTAL, false));
            gridView.setAdapter(productHorizontalScrollGridAdapter);
            productHorizontalScrollGridAdapter.notifyDataSetChanged();
        }
    }
    public class SingleHorizontalScrollGridViewHolder extends RecyclerView.ViewHolder{
        public SingleHorizontalScrollGridViewHolder(@NonNull View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.grid_category_layout_title);
            gridView = itemView.findViewById(R.id.grid_category_layout);
        }
        private void setHorizontalScrollGridSingle(List<ProductHorizontalScrollGridModel> horizontalScrollGridModelList, String title){
            Title.setText(title);
            ProductHorizontalScrollGridAdapter productHorizontalScrollGridAdapter = new ProductHorizontalScrollGridAdapter(horizontalScrollGridModelList);
            gridView.hasFixedSize();
            gridView.setLayoutManager(new GridLayoutManager(itemView.getContext(), 1, GridLayoutManager.HORIZONTAL, false));
            gridView.setAdapter(productHorizontalScrollGridAdapter);
            productHorizontalScrollGridAdapter.notifyDataSetChanged();
        }
    }
}
