package com.mobdeve.s21.grp4.mco_taftbites;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;


public class ReviewRatingsReviewAdapter extends RecyclerView.Adapter<ReviewRatingsReviewAdapter.ReviewViewHolder> {

    private List<ReviewRatingsReview> reviewsList;
    private Context context;

    // Adapter constructor
    public ReviewRatingsReviewAdapter(List<ReviewRatingsReview> reviewsList, Context context) {
        this.reviewsList = reviewsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout for each review
        View view = LayoutInflater.from(context).inflate(R.layout.item_ratings_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        ReviewRatingsReview review = reviewsList.get(position);

        // Bind the review details
        holder.reviewerName.setText(review.getReviewerName());
        holder.reviewText.setText(review.getReviewText());
        holder.reviewDate.setText(review.getReviewDate());
        holder.reviewerRating.setRating(review.getRating());

        // Load the profile image using Picasso
        Picasso.get()
                .load(review.getProfileImageUrl())
                .placeholder(R.drawable.placeholder) // Placeholder while loading
                .error(R.drawable.error_image) // Error image if loading fails
                .into(holder.reviewerProfileImage);
    }


    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    // ViewHolder class for Review item layout
    public static class ReviewViewHolder extends RecyclerView.ViewHolder {

        ImageView reviewerProfileImage;
        TextView reviewerName, reviewText, reviewDate;
        RatingBar reviewerRating;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewerProfileImage = itemView.findViewById(R.id.reviewerProfileImage);
            reviewerName = itemView.findViewById(R.id.reviewerName);
            reviewText = itemView.findViewById(R.id.reviewText);
            reviewDate = itemView.findViewById(R.id.reviewDate);
            reviewerRating = itemView.findViewById(R.id.reviewerRating);
        }
    }
}
