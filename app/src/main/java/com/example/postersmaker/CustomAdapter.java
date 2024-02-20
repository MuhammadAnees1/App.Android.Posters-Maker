package com.example.postersmaker;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    List<ToolModel> mToolList = new ArrayList<>();
    OnItemSelected mOnItemSelected;
    public CustomAdapter(OnItemSelected onItemSelected) {
        mOnItemSelected = onItemSelected;
        mToolList.add(new ToolModel("Photo", R.drawable.gallery_icon, ToolTypeForCustomAdaptor.Photo));
        mToolList.add(new ToolModel("Text", R.drawable.baseline_text_fields_24, ToolTypeForCustomAdaptor.TEXT));
        mToolList.add(new ToolModel("Emoji", R.drawable.ic_insert_emoticon, ToolTypeForCustomAdaptor.EMOJI));
        mToolList.add(new ToolModel("Frames", R.drawable.baseline_filter_frames_24, ToolTypeForCustomAdaptor.Frames));
        mToolList.add(new ToolModel("Background", R.drawable.background, ToolTypeForCustomAdaptor.Background));
        mToolList.add(new ToolModel("Brush", R.drawable.baseline_brush_24, ToolTypeForCustomAdaptor.BRUSH));
    }
    public interface OnItemSelected {
        void onToolSelected(ToolTypeForCustomAdaptor toolType);
    }
    static class ToolModel {
        String mToolName;
        int mToolIcon;
        ToolTypeForCustomAdaptor mToolType;
        ToolModel(String toolName, int toolIcon, ToolTypeForCustomAdaptor toolType) {
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
        ToolModel item = mToolList.get(position);
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
            itemView.setOnClickListener(v -> mOnItemSelected.onToolSelected(mToolList.get(getLayoutPosition()).mToolType));
        }
    }
}