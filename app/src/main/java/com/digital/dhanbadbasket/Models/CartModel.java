package com.digital.dhanbadbasket.Models;

public class CartModel {
    String Product_discounted_price;
    String Product_discounted_unit_price;
    String Product_name;
    String Product_price;
    String Product_quantity;
    String Product_unit_price;
    String Product_weight;
    String Product_image;

    public CartModel() {
    }

    public CartModel(String product_discounted_price, String product_discounted_unit_price, String product_name, String product_price, String product_quantity, String product_unit_price, String product_weight, String product_image) {
        Product_discounted_price = product_discounted_price;
        Product_discounted_unit_price = product_discounted_unit_price;
        Product_name = product_name;
        Product_price = product_price;
        Product_quantity = product_quantity;
        Product_unit_price = product_unit_price;
        Product_weight = product_weight;
        Product_image = product_image;
    }

    public String getProduct_discounted_price() {
        return Product_discounted_price;
    }

    public void setProduct_discounted_price(String product_discounted_price) {
        Product_discounted_price = product_discounted_price;
    }

    public String getProduct_discounted_unit_price() {
        return Product_discounted_unit_price;
    }

    public void setProduct_discounted_unit_price(String product_discounted_unit_price) {
        Product_discounted_unit_price = product_discounted_unit_price;
    }

    public String getProduct_name() {
        return Product_name;
    }

    public void setProduct_name(String product_name) {
        Product_name = product_name;
    }

    public String getProduct_price() {
        return Product_price;
    }

    public void setProduct_price(String product_price) {
        Product_price = product_price;
    }

    public String getProduct_quantity() {
        return Product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        Product_quantity = product_quantity;
    }

    public String getProduct_unit_price() {
        return Product_unit_price;
    }

    public void setProduct_unit_price(String product_unit_price) {
        Product_unit_price = product_unit_price;
    }

    public String getProduct_weight() {
        return Product_weight;
    }

    public void setProduct_weight(String product_weight) {
        Product_weight = product_weight;
    }

    public String getProduct_image() {
        return Product_image;
    }

    public void setProduct_image(String product_image) {
        Product_image = product_image;
    }
}
