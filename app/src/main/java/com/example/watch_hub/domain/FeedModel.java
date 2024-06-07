package com.example.watch_hub.domain;

public class FeedModel {

private String image;
private  String name;

private  boolean show;

    public FeedModel(){
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public FeedModel(String image, String name, boolean show) {
        this.image = image;
        this.name = name;
        this.show = show;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
