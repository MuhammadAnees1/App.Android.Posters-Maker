package com.example.postersmaker;


import static com.example.postersmaker.MainActivity.textLayoutList2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Layers_Adapter extends RecyclerView.Adapter<Layers_Adapter.ViewHolder> {
    Context context;
    List<String> textList;
    final ItemTouchHelper itemTouchHelper;
    String text;

    TextLayout selectedTextLayout;

    int index;

    public Layers_Adapter(Context context, List<String> textList, RecyclerView recyclerView) {
        this.context = context;
        this.textList = (textList != null) ? textList : new ArrayList<>();

        // Set up the ItemTouchHelper


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
        text = textList.get(position);
        index = position;
        TextLayout iflocked = null;
        holder.txtTool.setText(text);
        for (int i = 0; i < textList.size(); i++) {
            for (TextLayout textLayout : textLayoutList2) {
                if (index == textLayoutList2.indexOf(textLayout)) {
                    iflocked = textLayout;
                }

                if(iflocked != null){



                    if(iflocked.getLocked()) {
                        holder.lockLayerButton.setBackgroundResource(R.drawable.baseline_lock_24);
                    }
                }}
        }


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
                }}


        });
    }

    @Override
    public int getItemCount() {
        return textList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTool;
        Button moveLayerButton, editLayerButton, lockLayerButton;

        ViewHolder(View itemView) {
            super(itemView);
            txtTool = itemView.findViewById(R.id.layerNameTextView);
            moveLayerButton = itemView.findViewById(R.id.MoveLayerButton);
            editLayerButton = itemView.findViewById(R.id.EditLayerButton);
            lockLayerButton = itemView.findViewById(R.id.LockLayerButton);
        }
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP | ItemTouchHelper.DOWN , 1) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            // Ensure the indices are within the bounds of the list
            if (fromPosition < toPosition) {
                // Move the item in the list
                Collections.swap(textList, fromPosition, toPosition);

                if (context instanceof MainActivity) {
                    Collections.swap(textLayoutList2, fromPosition, toPosition);
                    Collections.swap(((MainActivity) context).textLayoutList, fromPosition, toPosition);

                    // Swap the views inside the ViewGroup
                    TextHandlerClass.swapViewsInLayout(fromPosition, toPosition);
                }
                Collections.swap(TextHandlerClass.textLayoutList, fromPosition, toPosition);
                Collections.swap(TextHandlerClass.textList, fromPosition, toPosition);}
            else if (fromPosition > toPosition) {
                // Move the item in the list
                Collections.swap(textList, fromPosition, toPosition);

                if (context instanceof MainActivity) {
                    Collections.swap(textLayoutList2, fromPosition, toPosition);
                    Collections.swap(((MainActivity) context).textLayoutList, fromPosition, toPosition);

                    // Swap the views inside the ViewGroup
                    TextHandlerClass.swapViewsInLayout(fromPosition, toPosition);
                }
                Collections.swap(TextHandlerClass.textLayoutList, fromPosition, toPosition);
            }

            notifyItemMoved(fromPosition, toPosition);

            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            // Handle swipe if needed
        }
    };


}