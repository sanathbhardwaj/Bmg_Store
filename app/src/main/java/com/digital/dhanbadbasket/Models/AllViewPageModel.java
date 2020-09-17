package com.digital.dhanbadbasket.Models;

import java.util.List;

public class AllViewPageModel {
    public static final int BANNER_SLIDER = 0;
    public static final int STRIP_AD_BANNER = 1;
    public static final int PRODUCT_HORIZONTAL_SCROLL_GRID = 3;
    public static final int CATEGORY_LAYOUT = 4;
    public static final int CATEGORY_PRODUCT_LAYOUT = 5;
    public static final int PRODUCT_HORIZONTAL_SCROLL_GRID_1 = 6;

    private int type;

    //////////////ProductHorizontalScrollGrid2
    private List<ProductHorizontalScrollGridModel> productHorizontalScrollGridModelList;
    String title;
    public List<ProductHorizontalScrollGridModel> getProductHorizontalScrollGridModelList() {
        return productHorizontalScrollGridModelList;
    }
    public void setProductHorizontalScrollGridModelList(List<ProductHorizontalScrollGridModel> productHorizontalScrollGridModelList) {
        this.productHorizontalScrollGridModelList = productHorizontalScrollGridModelList;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public AllViewPageModel(int type, List<ProductHorizontalScrollGridModel> productHorizontalScrollGridModelList, String title) {
        this.type = type;
        this.productHorizontalScrollGridModelList = productHorizontalScrollGridModelList;
        this.title = title;
    }
    //////////////ProductHorizontalScrollGrid2

    ////////////////BannerSlider
    private List<BannerSliderModel> bannerSliderModelList;
    public AllViewPageModel(int type, List<BannerSliderModel> bannerSliderModelList) {
        this.type = type;
        this.bannerSliderModelList = bannerSliderModelList;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public List<BannerSliderModel> getBannerSliderModelList() {
        return bannerSliderModelList;
    }
    public void setBannerSliderModelList(List<BannerSliderModel> bannerSliderModelList) {
        this.bannerSliderModelList = bannerSliderModelList;
    }
    ////////////////BannerSlider

    /////////////////// strip ad
    private String resource;
    private String backgroundColour;
    public AllViewPageModel(int type, String resource, String backgroundColour) {
        this.type = type;
        this.resource = resource;
        this.backgroundColour = backgroundColour;
    }
    public String getResource() {
        return resource;
    }
    public void setResource(String resource) {
        this.resource = resource;
    }
    public String getBackgroundColour() {
        return backgroundColour;
    }
    public void setBackgroundColour(String backgroundColour) {
        this.backgroundColour = backgroundColour;
    }
    /////////////////// strip ad

    ////////////// category layout
    private String top_title;
    private List<CategoryModel> categoryModelList;

    public AllViewPageModel(int type, String top_title, List<CategoryModel> categoryModelList) {
        this.type = type;
        this.top_title = top_title;
        this.categoryModelList = categoryModelList;
    }
    public String getTop_title() {
        return top_title;
    }
    public void setTop_title(String top_title) {
        this.top_title = top_title;
    }
    public List<CategoryModel> getCategoryModelList() {
        return categoryModelList;
    }
    public void setCategoryModelList(List<CategoryModel> categoryModelList) {
        this.categoryModelList = categoryModelList;
    }
    ////////////// category layout
}
