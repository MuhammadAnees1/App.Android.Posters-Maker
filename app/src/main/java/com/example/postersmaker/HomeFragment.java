package com.example.postersmaker;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements CategoryClickListener {
    ImageCarousel carousel;
    List<CarouselItem> carouselItems = new ArrayList<>();
    ShimmerRecyclerView outerRecyclerView;
    CategoryAdapter categoryAdapter;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        carousel = view.findViewById(R.id.carousel);

        List<String> imageUrls = getImageUrlsOrResources();

        for (String imageUrl : imageUrls) {
            CarouselItem item = new CarouselItem(imageUrl);
            carouselItems.add(item);
        }
        carousel.addData(carouselItems);

        outerRecyclerView = view.findViewById(R.id.categoryList);

        outerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Prepare your data
        List<Category> categories = prepareCategoriesData();

        CategoryAdapter adapter = new CategoryAdapter(categories, this, getContext());
//        outerRecyclerView.setAdapter(adapter);
        outerRecyclerView.showShimmerAdapter();
        return view;
    }

    private List<Category> prepareCategoriesData() {
        List<Category> categories = new ArrayList<>();

        // Category 1
//        List<Item> category1Items = new ArrayList<>();
//        category1Items.add(new Item("Item 1", "Description 1"));
//        category1Items.add(new Item("Item 2", "Description 2"));
//        category1Items.add(new Item("Item 3", "Description 3"));
//        category1Items.add(new Item("Item 4", "Description 4"));
//        category1Items.add(new Item("Item 5", "Description 5"));
//
//        Category category1 = new Category("Category 1", category1Items);
//        categories.add(category1);
//
//        // Category 2
//        List<Item> category2Items = new ArrayList<>();
//        category2Items.add(new Item("Item 3", "Description 3"));
//        category2Items.add(new Item("Item 4", "Description 4"));
//        Category category2 = new Category("Category 2", category2Items);
//        categories.add(category2);
//
//        // Category 3
//        List<Item> category3Items = new ArrayList<>();
//        category3Items.add(new Item("Item 3", "Description 3"));
//        category3Items.add(new Item("Item 4", "Description 4"));
//        Category category3 = new Category("Category 3", category3Items);
//        categories.add(category3);
//
//        // Category 4
//        List<Item> category4Items = new ArrayList<>();
//        category4Items.add(new Item("Item 3", "Description 3"));
//        category4Items.add(new Item("Item 4", "Description 4"));
//        Category category4 = new Category("Category 4", category4Items);
//        categories.add(category4);

        return categories;
    }

    private List<String> getImageUrlsOrResources() {
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("https://img.freepik.com/free-vector/gift-coupon-with-ribbon-offer_24877-55663.jpg?t=st=1708668895~exp=1708669495~hmac=de3ab2b61827884c92054f11341533167899708ccd7f5b8ae7a37c84215b2661");
        imageUrls.add("https://img.freepik.com/free-vector/hand-drawn-flat-halloween-background_23-2149062624.jpg?w=1060&t=st=1708668968~exp=1708669568~hmac=8eb1e9696bcd10e030a0cfd113e2d0bcd53d5590adfcd0b2a59b49fc0819dcce");
        imageUrls.add("https://img.freepik.com/free-photo/card-with-red-ribbons-bows_1232-1776.jpg?w=1380&t=st=1708669063~exp=1708669663~hmac=2397fc5c34c3b19e360f258e39cbfd0cca23b4a4eddbcd4692d671a98af9eb01");
        return imageUrls;
    }
    @Override
    public void onSeeAllClick(Category category) {
        categoryItemFragment fragment = new categoryItemFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("category", category);
        bundle.putParcelableArrayList("items", new ArrayList<>(category.items));
        fragment.setArguments(bundle);
        Log.d(TAG, "onSeeAllClick: dataaaa"+bundle);
        // Use FragmentTransaction to replace the current fragment with the new one
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }
}
