package com.example.postersmaker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dhaval2404.colorpicker.ColorPickerDialog;
import com.github.dhaval2404.colorpicker.listener.ColorListener;
import com.github.dhaval2404.colorpicker.model.ColorShape;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ColorPickerFragment extends Fragment {
    static Drawable circleDrawable;
    private static List<GradientDrawable> colorDrawables = new ArrayList<>();

    private List<Integer> colors;
    private List<ShadeItem> shades;
    private ColorAdapter colorAdapter;
    private OnColorSelectedListener colorSelectedListener;

    private ShadeAdapter shadeAdapter;
    public interface OnColorSelectedListener {
        void onColorSelected(int color);
    }
    public static ColorPickerFragment newInstance(List<Integer> colors, OnColorSelectedListener listener) {
        ColorPickerFragment fragment = new ColorPickerFragment();
        fragment.colorSelectedListener = listener;
        Bundle args = new Bundle();
        args.putIntegerArrayList("colors", new ArrayList<>(colors));
        fragment.setArguments(args);
        return fragment;
    }



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            colors = getArguments().getIntegerArrayList("colors");
        }
        // Initialize your color and shade data here
        initializeColors();
        initializeShades();
        colorAdapter = new ColorAdapter(colors, getContext());
        shadeAdapter = new ShadeAdapter(shades, getContext());
    }

    private void initializeColors() {

    }

    private void initializeShades() {
        shades = new ArrayList<>();
        for (int color : colors) {

            shades.add(new ShadeItem(color));
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_color_picker, container, false);

        RecyclerView colorRecyclerView = view.findViewById(R.id.colorRecycleView);
        RecyclerView shadeRecyclerView = view.findViewById(R.id.shadeRecycleView);


        ImageView colorPickerButton = view.findViewById(R.id.colorPickerButton);
        colorPickerButton.setOnClickListener(v -> {
                new ColorPickerDialog
                        .Builder(getContext())
                        .setTitle("Pick Theme")
                        .setColorShape(ColorShape.SQAURE)
                        .setDefaultColor(R.color.black)
                        .setColorListener(new ColorListener() {
                            @Override
                            public void onColorSelected(int color, @NotNull String colorHex) {
                               MainActivity activity = (MainActivity) requireActivity();


                                   Track.list.add(new Track(MainActivity.selectedLayer.getId(), true, MainActivity.selectedLayer.getTextView().getCurrentTextColor()));
                                   Track.list2.clear();

                                activity.selectedLayer.getTextView().setTextColor(color);
                            }
                        })
                        .show();
                });
        LinearLayoutManager colorLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager shadeLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        colorRecyclerView.setLayoutManager(colorLayoutManager);
        shadeRecyclerView.setLayoutManager(shadeLayoutManager);

        colorRecyclerView.setAdapter(colorAdapter);
        shadeRecyclerView.setAdapter(shadeAdapter);
        shadeRecyclerView.setVisibility(View.GONE);

        colorAdapter.setOnItemClickListener(position -> {
            // Handle color item click
            int selectedColor = colors.get(position);
            MainActivity activity = (MainActivity) requireActivity();
            if(MainActivity.selectedLayer != null){
            if(!Track.list.isEmpty()){
                if(Track.list.get(Track.list.size()-1).getTid() == MainActivity.selectedLayer.getId() && Track.list.get(Track.list.size() - 1).isIstextcolor() && Track.list.get(Track.list.size() - 1).getTextColor() == selectedColor){
                    Track.list.remove(Track.list.size() - 1);
                }
            }
            Track.list.add(new Track(MainActivity.selectedLayer.getId(), true, MainActivity.selectedLayer.getTextView().getCurrentTextColor()));
            Track.list2.clear();

            activity.selectedLayer.getTextView().setTextColor(selectedColor);
            shadeRecyclerView.setVisibility(View.VISIBLE);
            updateShades(selectedColor);
            }
        });
        shadeAdapter.setOnShadeItemClickListener(shadeColor -> {
            // Handle shade item click
            if (colorSelectedListener != null) {
                colorSelectedListener.onColorSelected(shadeColor);
            }
        });

        return view;
    }

    private void updateShades(int selectedColor) {
        initializeShades(selectedColor);
        shadeAdapter.updateData(shades);
    }
    private void initializeShades(int selectedColor) {
        shades = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            int alpha = (int) (255 * (i * 0.1f));
            int shadedColor = Color.argb(alpha, Color.red(selectedColor), Color.green(selectedColor), Color.blue(selectedColor));
            shades.add(new ShadeItem(shadedColor));
        }
    }


    private static class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {

        private List<Integer> colors;
        private Context context;
        private static OnItemClickListener onItemClickListener;
        private OnShadeItemClickListener onShadeItemClickListener;

        ColorAdapter(List<Integer> colors, Context context) {
            this.colors = colors;
            this.context = context;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public interface OnShadeItemClickListener {
            void onShadeItemClick(int shadeColor);
        }
        @NonNull
        @Override
        public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_color, parent, false);
            return new ColorViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
            int color = colors.get(position);
            holder.setColor(color);

            holder.itemView.setOnClickListener(v -> {
                // Handle color item click
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            });
        }


        @Override
        public int getItemCount() {
            return colors.size();
        }

        static class ColorViewHolder extends RecyclerView.ViewHolder {
            View colorView;

            ColorViewHolder(@NonNull View itemView) {
                super(itemView);
                colorView = itemView.findViewById(R.id.colorView);

                // Set the initial color
                int defaultColor = Color.parseColor("#FF0000"); // Replace with your default color
                circleDrawable = createCircleDrawable(defaultColor);
                colorView.setBackground(circleDrawable);

                colorView.setOnClickListener(v -> {
                    // Handle color item click
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getAdapterPosition());
                    }
                });
            }
            void setColor(int color) {
                // Update the color dynamically
                circleDrawable = createCircleDrawable(color);
                colorView.setBackground(circleDrawable);
            }

            private Drawable createCircleDrawable(int color) {
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setShape(GradientDrawable.OVAL);
                gradientDrawable.setColor(color);
                return gradientDrawable;
            }

        }
    }

    private static class ShadeAdapter extends RecyclerView.Adapter<ShadeAdapter.ShadeViewHolder> {
        private static List<GradientDrawable> shadeDrawables = new ArrayList<>();

        private static List<ShadeItem> shades;
        private final Context context;
        private OnShadeItemClickListener onShadeItemClickListener;
        public interface OnShadeItemClickListener {
            void onShadeItemClick(int position);
        }

        public void setOnShadeItemClickListener(OnShadeItemClickListener listener) {
            this.onShadeItemClickListener = listener;
        }
        ShadeAdapter(List<ShadeItem> shades, Context context) {
            this.shades = shades;
            this.context = context;
        }

        void updateData(List<ShadeItem> shades) {
            this.shades = shades;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ShadeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_shade, parent, false);
            return new ShadeViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull ShadeViewHolder holder, int position) {
            ShadeItem shadeItem = shades.get(position);
            holder.setColor(shadeItem.getShadeColor());

            holder.itemView.setOnClickListener(v -> {
                if (onShadeItemClickListener != null) {
                    onShadeItemClickListener.onShadeItemClick(shadeItem.getShadeColor());
                }
            });
        }

        @Override
        public int getItemCount() {
            return shades.size();
        }

        static class ShadeViewHolder extends RecyclerView.ViewHolder {
            View shadeView;

            ShadeViewHolder(@NonNull View itemView) {
                super(itemView);
                shadeView = itemView.findViewById(R.id.shadeView);

                // Set the initial color
                int defaultColor = Color.parseColor("#FF0000"); // Replace with your default color
                circleDrawable = createCircleDrawable(defaultColor);
                shadeView.setBackground(circleDrawable);


            }

            void setColor(int color) {
                // Update the color dynamically
                circleDrawable = createCircleDrawable(color);
                shadeView.setBackground(circleDrawable);
            }

            private Drawable createCircleDrawable(int color) {
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setShape(GradientDrawable.OVAL);
                gradientDrawable.setColor(color);
                shadeDrawables.add(gradientDrawable);
                return gradientDrawable;
            }

        }

    }

    // Define your ShadeItem class as needed
    public class ShadeItem {
        private int color;

        ShadeItem(int color) {
            this.color = color;
        }

        public int getColor() {
            return color;
        }
        public int getShadeColor() {
            // You can return the original color or modify it based on your requirements
            return color;
        }

    }

    // Define an interface for item click
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}
