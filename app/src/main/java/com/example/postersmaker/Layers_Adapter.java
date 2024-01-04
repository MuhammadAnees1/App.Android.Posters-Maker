package com.example.postersmaker;


import static com.example.postersmaker.MainActivity.imageUri1;
import static com.example.postersmaker.MainActivity.imageView;
import static com.example.postersmaker.MainActivity.selectedLayer;
import static com.example.postersmaker.MainActivity.textLayoutList2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Layers_Adapter extends RecyclerView.Adapter<Layers_Adapter.ViewHolder> {
    Context context;
    static List<CombinedItem> combinedItemList;
    List<String> textList;
    final ItemTouchHelper itemTouchHelper;
    String text;

    CombinedItem selectedItem;
    int index;
    public Layers_Adapter(Context context, List<String> textList, RecyclerView recyclerView) {
        this.context = context;
        combinedItemList = (combinedItemList != null) ? combinedItemList : new ArrayList<>();

        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layers, parent, false);
        return new ViewHolder(view);
    }
    public void updateData(List<String> newTextList) {
        textList = newTextList;
        notifyDataSetChanged();
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CombinedItem combinedItem = combinedItemList.get(position);

        TextLayout textLayout2 = combinedItem.getTextlayout2();
        ImageLayout imageLayout = combinedItem.getImageLayout();
        Uri imageUri = imageLayout != null ? imageLayout.getImageUri() : null;

        CombinedItem iflocked = null;


        for (int i = 0; i < MainActivity.combinedItemList.size(); i++) {
            for (CombinedItem selectedItem2 : MainActivity.combinedItemList) {
                if (index == MainActivity.combinedItemList.indexOf(selectedItem2)) {
                    iflocked = combinedItem;
                }
                if(iflocked != null){
                    if(iflocked.getImageLayout() != null){
                        if(iflocked.getImageLayout().getLocked()) {
                            holder.lockLayerButton.setBackgroundResource(R.drawable.baseline_lock_24);
                        }
                    }
                    if (iflocked.getTextlayout2() != null) {
                        if(iflocked.getTextlayout2().getLocked()) {
                            holder.lockLayerButton.setBackgroundResource(R.drawable.baseline_lock_24);
                        }}
                }
            }
        }

        // Now, based on the type of the item, set the appropriate data in the ViewHolder
        if (textLayout2 != null) {
            // It's a text item
            String text = textLayout2.getTextView().getText().toString();
            holder.txtTool.setText(text);
            holder.txtTool.setVisibility(View.VISIBLE);
            holder.imageView3.setVisibility(View.GONE);
        } else if (imageLayout != null) {
            if(imageUri!= null) {
                holder.imageView3.setImageURI(imageLayout.getImageUri());
            }
            else{
                Glide.with(context)
                        .load(imageUri1)
                        .into(holder.imageView3);
            }
            holder.imageView3.setImageURI(imageLayout.getImageUri());
            holder.imageView3.setVisibility(View.VISIBLE);
            holder.txtTool.setVisibility(View.GONE);
        }


        holder.moveLayerButton. setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                itemTouchHelper.startDrag(holder);
            }
            return true;
        });

        holder.editLayerButton.setOnClickListener(v -> {
            // Handle edit layer button
            index = holder.getAdapterPosition();
            if (context instanceof MainActivity) {

                ((MainActivity) context).onEditButtonClick(index);
            }
        });

        holder.lockLayerButton.setOnClickListener(v -> {

            index = holder.getAdapterPosition();


            if (context instanceof MainActivity) {

                ((MainActivity) context).onLockButtonClick(index);

                if(selectedItem != null){
                    if (selectedItem.getImageLayout()!= null) {
                        if (!selectedItem.getImageLayout().getLocked()) {
                            holder.lockLayerButton.setBackgroundResource(R.drawable.baseline_lock_open_24);
                        } else {
                            holder.lockLayerButton.setBackgroundResource(R.drawable.baseline_lock_24);
                        }
                    }

                    else if (selectedItem.getTextlayout2() != null) {
                        if (!selectedItem.getTextlayout2().getLocked()) {
                            holder.lockLayerButton.setBackgroundResource(R.drawable.baseline_lock_open_24);
                        } else {
                            holder.lockLayerButton.setBackgroundResource(R.drawable.baseline_lock_24);
                        }
                    }


                }}
        });
    }

    @Override
    public int getItemCount() {
        return combinedItemList.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTool;
        Button moveLayerButton, editLayerButton, lockLayerButton;
        ImageView imageView3;

        ViewHolder(View itemView) {
            super(itemView);
            txtTool = itemView.findViewById(R.id.layerNameTextView);
            moveLayerButton = itemView.findViewById(R.id.MoveLayerButton);
            editLayerButton = itemView.findViewById(R.id.EditLayerButton);
            lockLayerButton = itemView.findViewById(R.id.LockLayerButton);
            imageView3 = itemView.findViewById(R.id.layerNameImageView);  // Add this line
        }
    }
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP | ItemTouchHelper.DOWN , 1) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            // Ensure the indices are within the bounds of the list
            if (!MainActivity.combinedItemList.isEmpty() && fromPosition < MainActivity.combinedItemList.size() && toPosition < MainActivity.combinedItemList.size()) {
                // Move the item in the list
                Collections.swap(MainActivity.combinedItemList, fromPosition, toPosition);
                if (context instanceof MainActivity) {
                    if (!MainActivity.combinedItemList.isEmpty() && fromPosition <MainActivity.combinedItemList.size() && toPosition <MainActivity.combinedItemList.size()) {
                        Collections.swap(MainActivity. combinedItemList, fromPosition, toPosition);
                    }
                    // Swap the views inside the ViewGroup
                    TextHandlerClass.swapViewsInLayout(fromPosition, toPosition);
                }
                notifyItemMoved(fromPosition, toPosition);
            }
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            // Handle swipe if needed
        }
    };
}
