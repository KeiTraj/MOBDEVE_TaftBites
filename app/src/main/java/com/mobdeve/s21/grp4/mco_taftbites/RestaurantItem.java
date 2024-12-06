package com.mobdeve.s21.grp4.mco_taftbites;
// JASPER

public class RestaurantItem {
    private String id;
    private String name;
    private float rating;
    private String distance;
    private String cuisineType;
    private String imageUrl;

    public RestaurantItem() {
        // Default constructor required for calls to DataSnapshot.getValue()
    }

    public RestaurantItem(String id, String name, float rating, String distance, String cuisineType, String imageUrl) {
       this.id = id;
        this.name = name;
        this.rating = rating;
        this.distance = distance;
        this.cuisineType = cuisineType;
        this.imageUrl = imageUrl;
    }
    public String getId() { return id; }
    public String getName() { return name;}
    public void setName(String name) {this.name = name;}
    public float getRating() {return rating;}
    public void setRating(float rating) {this.rating = rating;}
    public String getDistance() {return distance;}
    public void setDistance(String distance) {this.distance = distance;}
    public String getCuisineType() {return cuisineType;}
    public void setCuisineType(String cuisineType) {this.cuisineType = cuisineType;}
    public String getImageUrl() {return imageUrl;}

    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}
}


