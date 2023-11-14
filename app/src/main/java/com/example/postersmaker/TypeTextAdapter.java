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

public class TypeTextAdapter extends RecyclerView.Adapter<TypeTextAdapter.ViewHolder> {
    List<ToolModels> mToolList = new ArrayList<>();
    onToolSelecteds mOnItemSelected;
    public TypeTextAdapter(onToolSelecteds onItemSelected) {
        mOnItemSelected = onItemSelected;
        mToolList.add(new ToolModels("formatLeft", R.drawable.baseline_format_align_left_24, ToolTypesForTypeTextAdaptor.formatLeft));
        mToolList.add(new ToolModels("Center", R.drawable.baseline_format_align_center_24, ToolTypesForTypeTextAdaptor.formatCenter));
        mToolList.add(new ToolModels("Right", R.drawable.baseline_format_align_right_24, ToolTypesForTypeTextAdaptor.formatRight));
        mToolList.add(new ToolModels("Bold", R.drawable.baseline_format_bold_24, ToolTypesForTypeTextAdaptor.formatBold));
        mToolList.add(new ToolModels("Underline", R.drawable.baseline_format_underlined_24, ToolTypesForTypeTextAdaptor.FormatUnderlined));
        mToolList.add(new ToolModels("Italic", R.drawable.baseline_format_italic_24, ToolTypesForTypeTextAdaptor.formatItalic));
        mToolList.add(new ToolModels("Format", R.drawable.text_formatting, ToolTypesForTypeTextAdaptor.Format));
    }

    public interface onToolSelecteds {
        void onToolSelected(ToolTypesForTypeTextAdaptor toolType);
    }
    static class ToolModels {
        String mToolName;
        int mToolIcon;
        ToolTypesForTypeTextAdaptor mToolType;
        ToolModels(String toolName, int toolIcon, ToolTypesForTypeTextAdaptor toolType) {
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