package com.example.postersmaker;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FontAdapter extends RecyclerView.Adapter<FontAdapter.FontViewHolder> {

    private List<Integer> fontList;
    private Context context;
    private FontClickListener listener;

    public FontAdapter(Context context, List<Integer> fontList, FontClickListener listener) {
        this.context = context;
        this.fontList = fontList;
        this.listener = listener;
    }

    @Override
    public FontViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_font, parent, false);
        return new FontViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FontViewHolder holder, int position) {
        final Integer fontResourceId = fontList.get(position);

        // Load the typeface from the resource ID
        Typeface typeface = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            typeface = context.getResources().getFont(fontResourceId);
        }

        // Set the typeface to the TextView
        holder.textView.setTypeface(typeface);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the font resource ID to the listener
                listener.onFontClick(fontResourceId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fontList.size();
    }

    public static class FontViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public FontViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.fontTextView);
        }
    }

    public interface FontClickListener {
        void onFontClick(Integer fontResourceId);


    }
}
