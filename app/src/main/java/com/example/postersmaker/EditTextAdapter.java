package com.example.postersmaker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
public class EditTextAdapter extends RecyclerView.Adapter<EditTextAdapter.ViewHolder> {
    List<ToolModels> mToolList = new ArrayList<>();
    OnItemSelected mOnItemSelected;
    public EditTextAdapter(OnItemSelected onItemSelected) {
        mOnItemSelected = onItemSelected;
        mToolList.add(new ToolModels("Controls", R.drawable.settings, ToolTypesForEditAdaptor.Control));
        mToolList.add(new ToolModels("Themes", R.drawable.baseline_style_24, ToolTypesForEditAdaptor.Themes));
        mToolList.add(new ToolModels("stroke", R.drawable.stroke, ToolTypesForEditAdaptor.stroke));
        mToolList.add(new ToolModels("Style", R.drawable.baseline_style_24, ToolTypesForEditAdaptor.Style));
        mToolList.add(new ToolModels("Size", R.drawable.text_size, ToolTypesForEditAdaptor.text_size));
        mToolList.add(new ToolModels("Fonts", R.drawable.baseline_text_fields_24, ToolTypesForEditAdaptor.Fonts));
        mToolList.add(new ToolModels("Colour", R.drawable.baseline_color_lens_24, ToolTypesForEditAdaptor.Colour));
        mToolList.add(new ToolModels("Background", R.drawable.background, ToolTypesForEditAdaptor.Background));
        mToolList.add(new ToolModels("Shadow", R.drawable.shadow, ToolTypesForEditAdaptor.Shadow));
    }
    public interface OnItemSelected {
        void onToolSelected(ToolTypesForEditAdaptor toolType);
    }
    static class ToolModels {
        String mToolName;
        int mToolIcon;
        ToolTypesForEditAdaptor mToolType;
        ToolModels(String toolName, int toolIcon, ToolTypesForEditAdaptor toolType) {
            mToolName = toolName;
            mToolIcon = toolIcon;
            mToolType = toolType;
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_editing_tools, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ToolModels item = mToolList.get(position);
        holder.txtTool.setText(item.mToolName);
        holder.imgToolIcon.setImageResource(item.mToolIcon);
    }
    @Override
    public int getItemCount() {
        return mToolList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgToolIcon;
        TextView txtTool;
        ViewHolder(View itemView) {
            super(itemView);
            imgToolIcon = itemView.findViewById(R.id.imgToolIcon);
            txtTool = itemView.findViewById(R.id.txtTool);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemSelected.onToolSelected(mToolList.get(getLayoutPosition()).mToolType);
                }
            });
        }
    }
}