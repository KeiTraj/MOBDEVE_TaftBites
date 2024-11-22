package com.mobdeve.s21.grp4.mco_taftbites;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private List<MenuItem> menuItemsList;
    private Context context;

    public MenuAdapter(List<MenuItem> menuItemsList, Context context) {
        this.menuItemsList = menuItemsList;
        this.context = context;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItem menuItem = menuItemsList.get(position);

        // Set menu item name
        holder.menuItemName.setText(menuItem.getName());

        // Load the image from the URL using Picasso
        Picasso.get()
                .load(menuItem.getImageUrl())
                .placeholder(R.drawable.placeholder) // Placeholder image
                .error(R.drawable.error_image) // Fallback image in case of error
                .into(holder.menuItemImage);
    }

    @Override
    public int getItemCount() {
        return menuItemsList.size();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {

        ImageView menuItemImage;
        TextView menuItemName;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            menuItemImage = itemView.findViewById(R.id.menuItemImage);
            menuItemName = itemView.findViewById(R.id.menuItemName);
        }
    }
}
