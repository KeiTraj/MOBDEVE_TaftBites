package com.mobdeve.s21.grp4.mco_taftbites;
// RESTAURANT DETAILS PAGE

public class Review {
    private String reviewerName;
    private String text;
    private int profileImageResId = -1;
    private float rating;
    private String reviewDate;

    // Constructor for vertical reviews with profile image
    public Review(String reviewerName, String text,String reviewDate, int profileImageResId, float rating) {
        this.reviewerName = reviewerName;
        this.text = text;
        this.reviewDate = reviewDate;
        this.profileImageResId = profileImageResId;
        this.rating = rating;
    }

    // Constructor for horizontal reviews without profile image
    public Review(String reviewerName, String text, float rating) {
        this.reviewerName = reviewerName;
        this.text = text;
        this.rating = rating;
    }

    // Getters
    public String getReviewerName() {
        return reviewerName;
    }

    public String getText() {
        return text;
    }

    public int getProfileImageResId() {
        return profileImageResId;
    }

    public float getRating() {
        return rating;
    }

    public String getReviewDate() {
        return reviewDate;
    }
}
