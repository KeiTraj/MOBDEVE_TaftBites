package com.mobdeve.s21.grp4.mco_taftbites;

public class MenuItem {
    private String name;
    private String imageUrl; // Change this to String for URL

    // Constructor
    public MenuItem(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
