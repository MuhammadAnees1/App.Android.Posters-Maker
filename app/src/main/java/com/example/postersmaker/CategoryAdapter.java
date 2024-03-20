package com.example.postersmaker;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import java.util.List;

interface CategoryClickListener {
    void onSeeAllClick(Category category);
}

class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<Category> categories;
    private CategoryClickListener categoryClickListener;
    private Context context;

    CategoryAdapter(List<Category> categories, CategoryClickListener listener, Context context) {
        this.categories = categories;
        this.categoryClickListener = listener;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_layout_recycleview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.titleTextView.setText(category.title);
        holder.innerRecyclerView.setLayoutManager(new LinearLayoutManager(holder.innerRecyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        holder.innerRecyclerView.setAdapter(new CategoryItemAdapter(category.items));
        holder.innerRecyclerView.showShimmerAdapter();


        // Handle click on "seeAll_category" button
        holder.seeAll_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryClickListener != null) {
                    categoryClickListener.onSeeAllClick(category);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, seeAll_category;
        ShimmerRecyclerView innerRecyclerView;

        ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.category_title);
            innerRecyclerView = itemView.findViewById(R.id.inner_recycler_view);
            seeAll_category = itemView.findViewById(R.id.seeAll_category);
        }
    }
}