package com.touchreno.myapplication.models;

import java.io.Serializable;

public class AllAccModel implements Serializable {
    String name, img_url, type, description,ar,name2;
    String price;

    public AllAccModel() {
    }

    public AllAccModel(String name,String name2,String img_url, String type, String description, String price,String ar) {
        this.name = name;
        this.name2=name2;
        this.img_url = img_url;
        this.type = type;
        this.description = description;
        this.price = price;
        this.ar=ar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAr(){return ar;}
    public void setAr(String ar) {
        this.ar = ar;
    }


    public String getName2() {
        return name2;
    }
}
