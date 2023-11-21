package com.example.postersmaker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class Layers_Adapter extends RecyclerView.Adapter<Layers_Adapter.ViewHolder> {
    static List<String> textList = new ArrayList<>();
        private Context context;
        TextLayout textLayout;

        // Constructor to initialize the adapter with the data
        public Layers_Adapter(Context context, List<String> textList) {
            this.context = context;
            this.textList = textList;
        }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layers, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        textList = Collections.singletonList(textList.get(position));
        String text = textLayout.getTextView().getText().toString();
        holder.txtTool.setText(text);

        // Set up your buttons here, e.g., onClick listeners
        holder.moveLayerButton.setOnClickListener(v -> {
            // Handle move layer button click
        });

        holder.editLayerButton.setOnClickListener(v -> {
            // Handle edit layer button click
        });

        holder.lockLayerButton.setOnClickListener(v -> {
            // Handle lock layer button click
        });
    }

    @Override
    public int getItemCount() {
        return 0;
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
