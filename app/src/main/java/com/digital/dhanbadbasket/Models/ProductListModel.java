package com.digital.dhanbadbasket.Models;

public class ProductListModel {
    String image;
    String name;
    String short_description;
    String weight;
    Long price, discounted_price;

    public ProductListModel() {
    }

    public ProductListModel(String image, String name, String short_description, String weight, Long price, Long discounted_price) {
        this.image = image;
        this.name = name;
        this.short_description = short_description;
        this.weight = weight;
        this.price = price;
        this.discounted_price = discounted_price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getDiscounted_price() {
        return discounted_price;
    }

    public void setDiscounted_price(Long discounted_price) {
        this.discounted_price = discounted_price;
    }
}
