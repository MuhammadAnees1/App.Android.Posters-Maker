package com.example.postersmaker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import java.util.List;

public class categoryItemFragment extends Fragment {

    CategoryItemAdapter adapter;
    public categoryItemFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_category_item, container, false);

        // Retrieve category and items list from arguments
        Category category = getArguments().getParcelable("category");
        List<Item> items = getArguments().getParcelableArrayList("items");
        // Assuming you have your RecyclerView setup here
        ShimmerRecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(new CategoryItemAdapter(items));

        TextView categoryNameTextView = rootView.findViewById(R.id.category_name);
        categoryNameTextView.setText(category.getTitle());

        return rootView;
    }

}