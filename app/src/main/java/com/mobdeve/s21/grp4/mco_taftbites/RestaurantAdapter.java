package com.mobdeve.s21.grp4.mco_taftbites;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {

    private List<RestaurantItem> restaurantList;
    private Context context;

    public RestaurantAdapter(Context context, List<RestaurantItem> restaurantList) {
        this.context = context;
        this.restaurantList = restaurantList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.restaurants, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RestaurantItem restaurant = restaurantList.get(position);

        // Set restaurant details
        holder.name.setText(restaurant.getName());
        holder.ratingBar.setRating(restaurant.getRating());
        holder.distance.setText(restaurant.getDistance());
        holder.cuisineType.setText(restaurant.getCuisineType());

        // Load restaurant image using Picasso
        Picasso.get()
                .load(restaurant.getImageUrl()) // Image URL from Firestore
                .placeholder(R.drawable.placeholder) // Placeholder image while loading
                .error(R.drawable.error_image) // Image in case of error
                .into(holder.imageView);

        // Set OnClickListener for the "View" button
        holder.viewButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), RestaurantDetailsActivity.class);

            // Pass the restaurant ID to the details page
            intent.putExtra("restaurantId", restaurant.getId()); // Assuming `getId()` gives the Firestore document ID

            v.getContext().startActivity(intent);
        });
    }


    @Override
    public int getItemCount() { return restaurantList.size();}

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, distance, cuisineType;
        RatingBar ratingBar;
        ImageView imageView;
        Button viewButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.menuItemName);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            distance = itemView.findViewById(R.id.kilometersAway);
            cuisineType = itemView.findViewById(R.id.cuisineType);
            imageView = itemView.findViewById(R.id.menuItemImage);
            viewButton = itemView.findViewById(R.id.viewButton); // Button ID from restaurants.xml
        }
    }

    public void updateList(List<RestaurantItem> updatedList) {
        this.restaurantList.clear();
        this.restaurantList.addAll(updatedList);
        notifyDataSetChanged();
    }

}
