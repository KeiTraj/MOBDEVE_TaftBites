package com.mobdeve.s21.grp4.mco_taftbites;

// REVIEW MODEL SPECIFIC FOR THE ReviewRatingsReview PAGE

public class ReviewRatingsReview {
    private String reviewerName;
    private String reviewText;
    private String reviewDate;
    private String profileImageUrl; // URL for the profile image
    private float rating;

    // Constructor with profile image URL
    public ReviewRatingsReview(String reviewerName, String reviewText, String reviewDate, String profileImageUrl, float rating) {
        this.reviewerName = reviewerName;
        this.reviewText = reviewText;
        this.reviewDate = reviewDate;
        this.profileImageUrl = profileImageUrl;
        this.rating = rating;
    }

    // Constructor without profile image URL
    public ReviewRatingsReview(String reviewerName, String reviewText, String reviewDate, float rating) {
        this(reviewerName, reviewText, reviewDate, null, rating); // Pass null for profileImageUrl
    }

    // Getters
    public String getReviewerName() {return reviewerName;}

    public String getReviewText() {return reviewText;}

    public String getReviewDate() {return reviewDate;}

    public String getProfileImageUrl() {return profileImageUrl;}

    public float getRating() {return rating;}
}
