package com.mobdeve.s21.grp4.mco_taftbites;
// // RESTAURANT DETAILS PAGE - under "what people say"

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> reviewsList;
    private Context context;

    // Constructor to initialize context and reviews list
    public ReviewAdapter(List<Review> reviewsList, Context context) {
        this.reviewsList = reviewsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    // Binding data to the views
    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewsList.get(position);
        holder.reviewText.setText(review.getText());
        holder.reviewerName.setText("• " + review.getReviewerName());
        holder.reviewRating.setText(String.valueOf(review.getRating()));
    }

    // Return the number of reviews in the list
    @Override
    public int getItemCount() {
        return Math.min(reviewsList.size(), 5);  // Limit to 5 reviews
    }

    // ViewHolder class for holding the review item views
    public static class ReviewViewHolder extends RecyclerView.ViewHolder {

        TextView reviewText;
        TextView reviewerName;
        TextView reviewRating;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewText = itemView.findViewById(R.id.reviewText);
            reviewerName = itemView.findViewById(R.id.reviewerName);
            reviewRating = itemView.findViewById(R.id.reviewRating);
        }
    }


}
