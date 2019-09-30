package com.maradmin.basim.models;

public class MainMenuModel {
    private String key ;
    private String image ;
    private String title ;


    public MainMenuModel() {
    }

    public MainMenuModel(String key, String image, String title) {
        this.key = key;
        this.image = image;
        this.title = title;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
