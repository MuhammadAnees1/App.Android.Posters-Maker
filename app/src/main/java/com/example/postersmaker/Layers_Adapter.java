package com.example.postersmaker;


import static com.example.postersmaker.MainActivity.imageView;
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
    List<String> textList;
    static List<CombinedItem> combinedItemList;

    final ItemTouchHelper itemTouchHelper;
    String text;
    TextLayout selectedTextLayout;
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

        String text = combinedItem.getText();
        ImageLayout imageLayout = combinedItem.getImageLayout();
        Uri imageUri = imageLayout != null ? imageLayout.getImageUri() : null;
        Toast.makeText(context, "" + imageUri, Toast.LENGTH_SHORT).show();
        Log.d("TAG", "onBindViewHolder: " + imageUri);

        // Now, based on the type of the item, set the appropriate data in the ViewHolder
        if (text != null) {
            // It's a text item
            holder.txtTool.setText(text);
            holder.txtTool.setVisibility(View.VISIBLE);
        } else if (imageLayout != null) {
            holder.imageView3.setImageURI(imageUri);
            holder.imageView3.setVisibility(View.VISIBLE);
        }


        //        text = textList.get(position);
//        index = position;
//        TextLayout iflocked = null;
//        holder.txtTool.setText(text);
//        for (int i = 0; i < textList.size(); i++) {
//            for (TextLayout textLayout : textLayoutList2) {
//                if (index == textLayoutList2.indexOf(textLayout)) {
//                    iflocked = textLayout;
//                }
//                if(iflocked != null){
//                    if(iflocked.getLocked()) {
//                        holder.lockLayerButton.setBackgroundResource(R.drawable.baseline_lock_24);
//                    }
//                }
//            }
//        }
        // Set up your buttons here, e.g., onClick listeners
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
                if(selectedTextLayout != null){
                    if (!selectedTextLayout.getLocked()) {
                        holder.lockLayerButton.setBackgroundResource(R.drawable.baseline_lock_open_24);
                    } else {
                        holder.lockLayerButton.setBackgroundResource(R.drawable.baseline_lock_24);
                    }
                }
            }
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
            if (!textList.isEmpty() && fromPosition < textList.size() && toPosition < textList.size()) {
                // Move the item in the list
                Collections.swap(textList, fromPosition, toPosition);
                if (context instanceof MainActivity) {
                    if (!textLayoutList2.isEmpty() && fromPosition < textLayoutList2.size() && toPosition < textLayoutList2.size()) {
                        Collections.swap(textLayoutList2, fromPosition, toPosition);
                    }
                    if (!((MainActivity) context).textLayoutList.isEmpty() && fromPosition < ((MainActivity) context).textLayoutList.size() && toPosition < ((MainActivity) context).textLayoutList.size()) {
                        Collections.swap(((MainActivity) context).textLayoutList, fromPosition, toPosition);
                    }
                    // Swap the views inside the ViewGroup
                    TextHandlerClass.swapViewsInLayout(fromPosition, toPosition);
                }
                if (!TextHandlerClass.textLayoutList.isEmpty() && fromPosition < TextHandlerClass.textLayoutList.size() && toPosition < TextHandlerClass.textLayoutList.size()
                        && !TextHandlerClass.textList.isEmpty() && fromPosition < TextHandlerClass.textList.size() && toPosition < TextHandlerClass.textList.size()) {
                    Collections.swap(TextHandlerClass.textLayoutList, fromPosition, toPosition);
                    Collections.swap(TextHandlerClass.textList, fromPosition, toPosition);
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