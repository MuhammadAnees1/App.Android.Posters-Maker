package com.example.postersmaker;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class FontAdapter extends RecyclerView.Adapter<FontAdapter.FontViewHolder> {

    private List<Font> fontList;
    private FontClickListener listener;

    public FontAdapter(Context context, List<Font> fontList, FontClickListener listener) {
        this.fontList = fontList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FontViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_font, parent, false);
        return new FontViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FontViewHolder holder, int position) {
        final Font font = fontList.get(position);

        // Set font name
        holder.textView.setText(font.getName());
        Typeface typeface = Typeface.createFromFile(new File(font.getFilePath()));
        holder.textView.setTypeface(typeface);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the font to the listener
                listener.onFontClick(font);
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
        void onFontClick(Font font);
    }
}




