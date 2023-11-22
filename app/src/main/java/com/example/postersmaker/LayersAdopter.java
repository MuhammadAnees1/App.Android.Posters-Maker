package com.example.postersmaker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LayersAdopter extends RecyclerView.Adapter<LayersAdopter.LayerViewHolder> {
    private final Context context;
    private final List<Layers_Layout> layersLayoutList;

    public LayersAdopter(Context context, List<Layers_Layout> layersLayoutList) {
        this.context = context;
        this.layersLayoutList = layersLayoutList;
    }

    @Override
    public LayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each list item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layers, parent, false);
        return new LayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LayerViewHolder holder, int position) {
        // Bind data to the views in each list item
        Layers_Layout layersLayout = layersLayoutList.get(position);
        holder.layerNameTextView.setText("Anees");
        holder.lockLayerButton = layersLayout.getLockLayerButton();
        holder.moveLayerButton = layersLayout.getMoveLayerButton();
        holder.editLayerButton = layersLayout.getEditLayerButton();
        // You may need to set other properties here based on your Layers_Layout object
    }

    @Override
    public int getItemCount() {
        return layersLayoutList.size();
    }

    public static class LayerViewHolder extends RecyclerView.ViewHolder {
        TextView layerNameTextView;
        Button moveLayerButton, lockLayerButton, editLayerButton;

        public LayerViewHolder(View itemView) {
            super(itemView);
            moveLayerButton = itemView.findViewById(R.id.MoveLayerButton);
            layerNameTextView = itemView.findViewById(R.id.layerNameTextView);
            lockLayerButton = itemView.findViewById(R.id.LockLayerButton);
            editLayerButton = itemView.findViewById(R.id.EditLayerButton);
        }
    }
}
