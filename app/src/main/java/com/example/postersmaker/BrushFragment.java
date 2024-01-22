package com.example.postersmaker;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.github.dhaval2404.colorpicker.ColorPickerDialog;
import com.github.dhaval2404.colorpicker.listener.ColorListener;
import com.github.dhaval2404.colorpicker.model.ColorShape;


public class BrushFragment extends Fragment {

  SeekBar size;
  Bitmap brushBitmap;
  ImageView brush, eraser,save,color;

    public BrushFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



       View view = inflater.inflate(R.layout.fragment_brush, container, false);

       size = view.findViewById(R.id.brushSize);
       brush = view.findViewById(R.id.brush);
       eraser = view.findViewById(R.id.eraser);
       save = view.findViewById(R.id.save);
       color = view.findViewById(R.id.colorPick);
        size.setProgress(DrawPaint.size);
        size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                 progress = seekBar.getProgress();
                DrawPaint.size = (int) (progress/2.5);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
       brush.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               DrawPaint.brushEnable = !DrawPaint.brushEnable;
               if(MainActivity.brushSelected){
                   MainActivity.brushSelected = false;
                   brush.setBackground(null);
               }
               else{MainActivity.brushSelected = true;

                   brush.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.brush_bg, null));
               }
           }
       });
       eraser.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               MainActivity.drawPaintView.eraseDrawing();
               DrawPaint.bitmap1 = null;
               MainActivity.brushSelected = false;
           }
       });
       save.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (DrawPaint.bitmap1!= null){
                   brushBitmap = DrawPaint.bitmap1;

                   MainActivity mainActivity = (MainActivity) getActivity();
                   assert mainActivity != null;
                   if(MainActivity.selectedLayer != null)
                   {MainActivity.unselectLayer(MainActivity.selectedLayer);}
                   else if(MainActivity.selectedLayer1 != null){
                           MainActivity.unselectLayers(MainActivity.selectedLayer1);
                   }

                   MainActivity.brushSelected = false;
                   mainActivity.createImageLayout(brushBitmap,null, null, 0, 0);
                   MainActivity.drawPaintView.eraseDrawing();
                   DrawPaint.bitmap1 = null;
                   DrawPaint.brushEnable = false;
                   mainActivity.defaultContainer();
               }
           }
       });
        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ColorPickerDialog
                        .Builder(getContext())
                        .setTitle("Pick Theme")
                        .setColorShape(ColorShape.SQAURE)
                        .setDefaultColor(R.color.black)
                        .setColorListener(new ColorListener() {
                            @Override
                            public void onColorSelected(int color, @NonNull String colorHex) {
                                DrawPaint.color = color;
                            }
                        })
                        .show();
            }
        });

       return view;
    }
}