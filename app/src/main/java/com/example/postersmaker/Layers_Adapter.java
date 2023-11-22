package com.example.postersmaker;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public class Layers_Adapter extends RecyclerView.Adapter<Layers_Adapter.ViewHolder> {
    Context context;
    List<String> textList;
    String text;

    public boolean isLocked = false;
    public Layers_Adapter(Context context, List<String> textList) {
        this.context = context;
        this.textList = (textList != null) ? textList : new ArrayList<>();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layers, parent, false);
        return new ViewHolder(view);
    }
    public void updateData(List<String> newTextList) {
        textList = newTextList;

    }
    @Override

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        text = textList.get(position);
        holder.txtTool.setText(text);

        // Set up your buttons here, e.g., onClick listeners
        holder.moveLayerButton.setOnClickListener(v -> {
        });

        holder.editLayerButton.setOnClickListener(v -> {
            // Handle edit layer button
            String clickedText = textList.get(holder.getAdapterPosition());

            if (context instanceof MainActivity) {

                ((MainActivity) context).onEditButtonClick(clickedText);
            }

        });

        holder.lockLayerButton.setOnClickListener(v -> {
            String clickedText = textList.get(holder.getAdapterPosition());

            if (!isLocked) {
                isLocked = true;
                holder.lockLayerButton.setBackgroundResource(R.drawable.baseline_lock_24);
            }
            else {
                isLocked = false;
                holder.lockLayerButton.setBackgroundResource(R.drawable.baseline_lock_open_24);
            }



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
}
