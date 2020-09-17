package com.digital.dhanbadbasket.Models;

public class CategoryModel {
    private String title;
    private String image;
    private String discount;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public CategoryModel(String title, String image, String discount) {
        this.title = title;
        this.image = image;
        this.discount = discount;
    }
}
