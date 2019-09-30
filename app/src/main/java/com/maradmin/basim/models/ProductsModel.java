package com.maradmin.basim.models;

public class ProductsModel {
    private String key ;
    private String image ;
    private String arabic_name ;
    private String english_name ;
    private String price ;
    private String unit ;


    public ProductsModel() {
    }


    public ProductsModel(String key, String image, String arabic_name, String english_name, String price, String unit) {
        this.key = key;
        this.image = image;
        this.arabic_name = arabic_name;
        this.english_name = english_name;
        this.price = price;
        this.unit = unit;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getArabic_name() {
        return arabic_name;
    }

    public void setArabic_name(String arabic_name) {
        this.arabic_name = arabic_name;
    }

    public String getEnglish_name() {
        return english_name;
    }

    public void setEnglish_name(String english_name) {
        this.english_name = english_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
