package com.example.watch_hub.domain;

import java.io.Serializable;

public class ProductModel implements Serializable {

private String title;
private String price;
private String rating;
private String description;
private String image;
private  boolean show;

private String Category;

    public ProductModel(){
    }

    public ProductModel(String title, String price, String rating, String description, String image, boolean show, String category) {
        this.title = title;
        this.price = price;
        this.rating = rating;
        this.description = description;
        this.image = image;
        this.show = show;
        Category = category;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
}
