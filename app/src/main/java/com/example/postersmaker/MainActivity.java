package com.example.postersmaker;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.example.postersmaker.HomeFragment1.recyclerView;
import static com.example.postersmaker.ImagePickerManager.imageLayoutList;
import static com.example.postersmaker.MainActivity.ScaleListener.MAX_SCALE_FACTOR;
import static com.example.postersmaker.MainActivity.ScaleListener.lastScaleFactor;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jgabrielfreitas.core.BlurImageView;

import org.json.JSONException;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CustomAdapter.OnItemSelected {
    private final CustomAdapter customAdapter = new CustomAdapter(this);
    static RecyclerView LayerRecycleView;
    List<String> textList = new ArrayList<>();
    int idT, idI ;
    int parent2width;
    static ImageView currentimage = null;
    float x = 0, y = 0;
    static int widthsize =0,heightsize =0;
    JSONReader jsonReader;
    static Bitmap drawnBitmap = null;
 static boolean brushSelected=false, backimg;

   public static Uri imageUri1;
    static DrawPaint drawPaintView ;
    static Bitmap currenBit;
    private boolean isBrushMode = true;
    ;

    int Tid = 0;
     Layers_Adapter adapter = new Layers_Adapter(this, LayerRecycleView);
    public final List<FrameLayout> textLayoutList = new ArrayList<>();
    public static List<TextLayout> textLayoutList2 = new ArrayList<>();
    public static List<ImageLayout> imageLayoutList2 = new ArrayList<>();

    static List<CombinedItem> combinedItemList = new ArrayList<>();

    RelativeLayout borderLayout;
    static TranslateAnimation fadeIn ,centerAnimation;
    TranslateAnimation fadeOut;
    public static TextLayout selectedLayer;
    public static ImageLayout selectedLayer1;
        LinearLayout Optionssave;
    Boolean isLocked;
    boolean isframe ;
    static Bitmap originalBitmap1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 1;
    static String CurrentImg = null ;
    TextView textView, LayerButton, reset,imgUndo,imgRedo,savImgButton;
    Button deleteButton,deleteButton2, rotateButton, resizeButton, saveButton, zoomIn,zoomOut;
    static HomeFragment1 homeFragment1;
    private int currentActionIndex = -1;
    static BlurImageView imageView, filterView;
    public static ImageView  imageView2, zoomUp,zoomDown,zoomLeft,zoomRight;
    private Handler handler = new Handler();
    Bitmap brushBitmap;
    View previewImageView1;
    private ScaleGestureDetector scaleGestureDetector;
    static LinearLayout LayoutControls;

    private float scaleFactor = 1.0f, maxX, maxY;
    static FrameLayout container,container2, filtercontainer,parentLayout2,bgcontainer,brushContainer,frameLayout,parentLayout,frameContainer;

    public  void OpacityBackground(int progress) {
        imageView.setVisibility(View.VISIBLE);
        float opacity = progress / 100f;

            imageView.setAlpha(opacity);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (BlurImageView) findViewById(R.id.previewImageView);

        filterView = (BlurImageView) findViewById(R.id.filterImageView);
        previewImageView1 = findViewById(R.id.previewImageView1);
        LayoutControls = findViewById(R.id.LayoutControls);

        container = findViewById(R.id.fragment_container);
        container2 = findViewById(R.id.fragment_container3);
        LayerRecycleView = findViewById(R.id.LayerRecycleView);
        LayerRecycleView.setVisibility(View.GONE);
        LayerButton = findViewById(R.id.LayerButton);
        savImgButton = findViewById(R.id.imgSave);
        drawPaintView = findViewById(R.id.drawPaintView);
        zoomIn = findViewById(R.id.zoomIn);
        zoomOut = findViewById(R.id.zoomOut);
        zoomRight = findViewById(R.id.zoomRightButton);
        zoomLeft = findViewById(R.id.zoomLeftButton);
        zoomUp = findViewById(R.id.zoomUpButton);
        zoomDown = findViewById(R.id.zoomDownButton);
        RecyclerView recyclerView = findViewById(R.id.rvConstraintTools);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        frameLayout = new FrameLayout(this);
        adapter = new Layers_Adapter(MainActivity.this, LayerRecycleView);
        LayerRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
//        adapter.textList.addAll(TextHandlerClass.getTextList());
        parentLayout = findViewById(R.id.parentLayout);
        parentLayout2 = findViewById(R.id.parentLayout2);
        reset = findViewById(R.id.reset);
        adapter.notifyDataSetChanged();
        LayerRecycleView.setAdapter(adapter);
        imgUndo = findViewById(R.id.imgUndo);
        imgRedo = findViewById(R.id.imgRedo);
        brushContainer = findViewById(R.id.fragment_container4);
        drawPaintView = findViewById(R.id.drawPaintView);
        frameContainer=findViewById(R.id.fragment_container5);
        bgcontainer = findViewById(R.id.fragment_container6);
        filtercontainer = findViewById(R.id.filter_container);
        Optionssave = findViewById(R.id.Options_save);
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        parent2width = displayMetrics.widthPixels;

       calculate();


//        JSONReader.readJSONFile(this);

        fadeIn = new TranslateAnimation(0, 0, 400, 0);
        fadeIn.setDuration(400);
        fadeOut = new TranslateAnimation(0, 0, 0, 400);
        fadeOut.setDuration(400);
        savImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Optionssave.setVisibility(View.VISIBLE);
                Save_Fragment save_fragment = new Save_Fragment();
                FragmentTransaction saveTransaction = getSupportFragmentManager().beginTransaction();
                saveTransaction.replace(R.id.Options_save, save_fragment);
                saveTransaction.addToBackStack(null);
               saveTransaction.commit();


                if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) v.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                        ActivityCompat.requestPermissions((Activity) v.getContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions((Activity) v.getContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                    }
                    }

            }

        });
        parentLayout.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                if (event.getPointerCount() == 1) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // ... (your existing code)
                            x = event.getRawX();
                            y = event.getRawY();
                            maxX = (parentLayout.getWidth() * lastScaleFactor - parentLayout.getWidth()) / 2;
                            maxY = (parentLayout.getHeight() * lastScaleFactor - parentLayout.getHeight()) / 2;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            float x1 = event.getRawX();
                            float y2 = event.getRawY();

                            float dX = x1 - x;
                            float dY = y2 - y;

                            // Limit the movement based on the screen boundaries
                            float newTranslationX = parentLayout.getTranslationX() + dX;
                            float newTranslationY = parentLayout.getTranslationY() + dY;

                            newTranslationX = Math.max(-maxX, Math.min(newTranslationX, maxX));
                            newTranslationY = Math.max(-maxY, Math.min(newTranslationY, maxY));

                            parentLayout.setTranslationX(newTranslationX);
                            parentLayout.setTranslationY(newTranslationY);

                            x = x1;
                            y = y2;
                            break;
                        case MotionEvent.ACTION_UP:
                            // After scaling ends, check boundaries and adjust the layout position
                            float maxX = (parentLayout.getWidth() * lastScaleFactor - parentLayout.getWidth()) / 2;
                            float maxY = (parentLayout.getHeight() * lastScaleFactor - parentLayout.getHeight()) / 2;

                             newTranslationX = parentLayout.getTranslationX();
                             newTranslationY = parentLayout.getTranslationY();

                            // Limit the translation based on the screen boundaries
                            newTranslationX = Math.max(-maxX, Math.min(newTranslationX, maxX));
                            newTranslationY = Math.max(-maxY, Math.min(newTranslationY, maxY));

                            // Animate the layout to the adjusted position
                            parentLayout.animate()
                                    .translationX(newTranslationX)
                                    .translationY(newTranslationY)
                                    .setDuration(200) // Adjust the duration as needed
                                    .start();                            break;
                    }
                }
                else{
                    scaleGestureDetector.onTouchEvent(event);
                }

                return true;
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int childCount = parentLayout.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View childView = parentLayout.getChildAt(i);

                    // Check if the child view is not an instance of ImageView
                    if (childView != imageView && childView != filterView && childView != previewImageView1) {
                        parentLayout.removeView(childView);
                    }
                }
                imageView.setImageBitmap(null);
                filterView.setImageBitmap(null);
              combinedItemList.clear();
              imageLayoutList.clear();
              textLayoutList.clear();
              textLayoutList2.clear();
              imageLayoutList2.clear();
              textList.clear();
              TextHandlerClass.textList.clear();
              Track.list.clear();
              Track.list2.clear();
              LayerRecycleView.setVisibility(View.GONE);
              defaultContainer();
                JSONReader.readJSONFile(MainActivity.this);

            }
        });
//        zoomIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                float newScale = parentLayout.getScaleX() *1.01f;
//                newScale = Math.max(1.0f, Math.min(newScale, MAX_SCALE_FACTOR));
//                updateLastScaleFactor(newScale);
//                parentLayout.setScaleX(newScale);
//                parentLayout.setScaleY(newScale);
//            }
//        });
//
//        zoomOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                float newScale = parentLayout.getScaleX() / 1.01f;
//                newScale = Math.max(1.0f, Math.min(newScale, MAX_SCALE_FACTOR));
//                updateLastScaleFactor(newScale);
//                parentLayout.setScaleX(newScale);
//                parentLayout.setScaleY(newScale);;
//            }
//        });
//        zoomRight.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    moveContinuesly("Right");
//                }
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    handler.removeCallbacksAndMessages(null);
//                }
//                return true;
//            }
//        });
//
//        zoomLeft.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    moveContinuesly("Left");
//                }
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    handler.removeCallbacksAndMessages(null);
//                }
//                return true;
//            }
//        });
//
//        zoomUp.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    moveContinuesly("Up");
//                }
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    handler.removeCallbacksAndMessages(null);
//                }
//                return true;
//            }
//        });
//
//        zoomDown.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    moveContinuesly("Down");
//                }
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    handler.removeCallbacksAndMessages(null);
//                }
//                return true;
//            }
//        });


        imgUndo.setOnClickListener(v -> undo_redo.undo(getApplicationContext()));
        imgRedo.setOnClickListener(v -> undo_redo.redo(getApplicationContext()));

        LayerButton.setOnClickListener(v -> {

            if (LayerRecycleView.getVisibility() == View.VISIBLE) {
                LayerRecycleView.setVisibility(View.GONE);
            } else {
                LayerRecycleView.setVisibility(View.VISIBLE);
            }

           Layers_Adapter.combinedItemList = combinedItemList;
        });

    }

    @SuppressLint("ClickableViewAccessibility")
    TextLayout createTextLayout(String text, float x, float y,boolean emoji) {
        frameLayout = new FrameLayout(this);
        // Create a FrameLayout to hold the TextView and the button
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        frameLayout.setBackgroundResource(R.drawable.border_style);

        frameLayout.setMinimumWidth(20);
        TextLayout textLayout = new TextLayout(frameLayout, borderLayout, deleteButton, rotateButton, resizeButton, saveButton, textView, isLocked, idT);
        textLayout.setFrameLayout(frameLayout);
        textLayout.setLocked(false);
        frameLayout.setTag(textLayout);
        selectedLayer = textLayout;
        if(emoji){
            textLayout.setIsemoji(true);
        }
        borderLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams borderLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        borderLayout.setLayoutParams(borderLayoutParams);
        int layoutMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 19, getResources().getDisplayMetrics());
        borderLayoutParams.setMargins(layoutMargin, layoutMargin, layoutMargin, layoutMargin);
        borderLayout.setGravity(Gravity.CENTER);

        textLayout.setBorderLayout(borderLayout);
        fadeIn = new TranslateAnimation(0, 0, 400, 0);
        fadeIn.setDuration(400);
        fadeOut = new TranslateAnimation(0, 0, 0, 400);
        fadeOut.setDuration(400);


//        borderLayout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                float x = event.getX();
//                float y = event.getY();
//
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_MOVE:
//
//                        borderLayout.setX(x - borderLayout.getWidth() / 2);
//                        borderLayout.setY(y - borderLayout.getHeight() / 2);
//                        break;}
//                return false;
//            }
//        });
        textView = new TextView(this);
        textView.setText(text);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        textLayout.setMaxSize(textView.getTextSize());
        borderLayout.setMinimumHeight(textView.getHeight() + 20);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setTypeface(null, Typeface.NORMAL);
        textView.setMaxWidth(imageView.getWidth() - 40);
        frameLayout.setMinimumHeight(textView.getHeight() + 10);
        LayerRecycleView.setVisibility(View.GONE);
        idT = Tid + 1;
        Tid++;
        textLayout.setId(Tid);
        CombinedItem.ids.add(Tid);
        combinedItemList.add(new CombinedItem(textLayout));

        deleteButton = ButtonCreator.createDeleteButton(this, 0.26f, 0.26f, -33, -29);
        textLayout.setDeleteButton(deleteButton);

        textLayout.getDeleteButton().setOnClickListener(v -> {
            ViewGroup viewGroup = findViewById(android.R.id.content);

            // Remove the TextLayout's frameLayout from the view hierarchy
            viewGroup.removeView(textLayout.getFrameLayout());

            // Remove the frameLayout from your lists
            textLayoutList.remove(textLayout.getFrameLayout());
            parentLayout.removeView(textLayout.getFrameLayout());

            // Find the index of the TextLayout in textLayoutList2
            int index = textLayoutList2.indexOf(textLayout);
            textLayoutList2.remove(textLayout);

            for (CombinedItem c : combinedItemList) {
                if (c.getTextlayout2() != null && c.getTextlayout2() == textLayout) {
                    combinedItemList.remove(c);
                    break;
                }
            }


            if (index != -1 && index < TextHandlerClass.textList.size()) {
                // Remove the TextLayout from your data structure
                TextHandlerClass.textList.remove(index);
            }

            // Update your adapter or UI as needed
            adapter.updateData(new ArrayList<>());
            adapter.textList.addAll(TextHandlerClass.getTextList());

            selectedLayer = null;
            LayerRecycleView.setVisibility(View.GONE);

            if (container.getVisibility() == View.VISIBLE) {
                container.setVisibility(View.GONE);
                container.startAnimation(fadeOut);
            }
            if(container2.getVisibility() == View.VISIBLE) {
                container2.setVisibility(View.GONE);
            }
        });


        rotateButton = ButtonCreator.createRotateButton(this, 0.26f, 0.26f, -33, -29);
        textLayout.setRotateButton(rotateButton);
        textLayout.getRotateButton().setOnTouchListener(new RotateTouchListener(textLayout));

        // Add the TextView to the border layout
        borderLayout.addView(textView);
        // Create a button to resize the text
        resizeButton = ButtonCreator.createResizeButton(this, 0.26f, 0.26f, -31, -29);
        textLayout.setResizeButton(resizeButton);

        ResizeTouchListener textResizeTouchListener = new ResizeTouchListener(textLayout);
        resizeButton.setOnTouchListener(textResizeTouchListener);

        saveButton = ButtonCreator.createSaveButton(this, 0.282f, 0.282f, -30, -18);
        textLayout.setSaveButton(saveButton);

        if (selectedLayer != null && textLayout.getFrameLayout() != null) {
            textLayout.getSaveButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    unselectLayer(selectedLayer);
                    unselectLayers(selectedLayer1);
                    selectedLayer = null;
                    callSetDefaultState();
                    if (container.getVisibility() == View.VISIBLE) {

                        container.setVisibility(View.GONE);
                        container.startAnimation(fadeOut);
                    }
                    if (container2.getVisibility() == View.VISIBLE) {
                        container2.setVisibility(View.GONE);
                    }
                }
            });
        }
        filterView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unselectLayer(selectedLayer);
                unselectLayers(selectedLayer1);
                selectedLayer = null;
                selectedLayer1 = null;
                LayerRecycleView.setVisibility(View.GONE);
                callSetDefaultState();
                defaultContainer();
                if (container.getVisibility() == View.VISIBLE) {
                    container.setVisibility(View.GONE);
                    container.startAnimation(fadeOut);
                }
                if(container2.getVisibility() == View.VISIBLE) {
                    container2.setVisibility(View.GONE);
                }
                scaleGestureDetector.onTouchEvent(event);

                return false;
            }


        });
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unselectLayer(selectedLayer);
                unselectLayers(selectedLayer1);
                selectedLayer = null;
                selectedLayer1 = null;
                LayerRecycleView.setVisibility(View.GONE);
                callSetDefaultState();
                defaultContainer();
                if (container.getVisibility() == View.VISIBLE) {
                    container.setVisibility(View.GONE);
                    container.startAnimation(fadeOut);
                }
                if(container2.getVisibility() == View.VISIBLE) {
                    container2.setVisibility(View.GONE);
                }
                scaleGestureDetector.onTouchEvent(event);


                return false;
            }


        });
        if (selectedLayer != null && textLayout.getFrameLayout() != null) {
            textLayout.setTextView(textView);
            textLayout.getTextView().setOnTouchListener(new View.OnTouchListener() {
                private float lastX, lastY;
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            unselectLayer(selectedLayer);
                            unselectLayers(selectedLayer1);
                            selectLayer(textLayout);
                            lastX = event.getRawX();
                            lastY = event.getRawY();
                            Track.list.add(new Track(textLayout.getId(),textLayout.getFrameLayout().getX(), textLayout.getFrameLayout().getY(), true));

                            return true;

                        case MotionEvent.ACTION_MOVE:
                            if(selectedLayer == textLayout ){
                            if (textLayout.getLocked() != null) {
                                if (!textLayout.getLocked()&& !brushSelected) {
                                    float newX = event.getRawX();
                                    float newY = event.getRawY();
                                    float dX = newX - lastX;
                                    float dY = newY - lastY;
                                    // Update the position of the frameLayout based on the drag movement
                                    textLayout.getFrameLayout().setX(textLayout.getFrameLayout().getX() + dX);
                                    textLayout.getFrameLayout().setY(textLayout.getFrameLayout().getY() + dY);
                                    lastX = newX;
                                    lastY = newY;}

                                }
                            }
                            break;
                    }
                    return true;
                }
            });
            textLayout.getFrameLayout().setOnTouchListener(new View.OnTouchListener() {
                private float lastX, lastY;
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            unselectLayer(selectedLayer);
                            unselectLayers(selectedLayer1);
                            selectLayer(textLayout);
                            lastX = event.getRawX();
                            lastY = event.getRawY();
                            Track.list.add(new Track(textLayout.getId(),textLayout.getFrameLayout().getX(), textLayout.getFrameLayout().getY(),true));
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            if(selectedLayer == textLayout){
                            if(textLayout.getLocked() != null) {
                                if (!textLayout.getLocked()&&!brushSelected) {
                                    float newX = event.getRawX();
                                    float newY = event.getRawY();
                                    float dX = newX - lastX;
                                    float dY = newY - lastY;

                                    textLayout.getFrameLayout().setX(textLayout.getFrameLayout().getX() + dX);
                                    textLayout.getFrameLayout().setY(textLayout.getFrameLayout().getY() + dY);

                                    lastX = newX;
                                    lastY = newY;
                                    }

                                }
                            }
                            break;
                    }
                    return true;
                }
            });
        }
        textLayout.getBorderLayout().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        unselectLayer(selectedLayer);
                        unselectLayers(selectedLayer1);
                        selectLayer(textLayout);

                }
                return false;
            }
        });
        textLayout.getFrameLayout().addView(textLayout.getBorderLayout());
        textLayout.getFrameLayout().addView(textLayout.getResizeButton());
        textLayout.getFrameLayout().addView(textLayout.getDeleteButton());
        textLayout.getFrameLayout().addView(textLayout.getRotateButton());
        textLayout.getFrameLayout().addView(textLayout.getSaveButton());
        textLayout.getFrameLayout().setX(x);
        textLayout.getFrameLayout().setY(y);
        parentLayout.addView(textLayout.getFrameLayout());


        ScaleAnimation expandAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        expandAnimation.setDuration(600);
        textLayout.getFrameLayout().setAnimation(expandAnimation);
        textLayout.getBorderLayout().setAnimation(expandAnimation);
        textLayout.getTextView().setAnimation(expandAnimation);

        return textLayout;
    }
    public static void selectLayer(TextLayout textLayout) {
        unselectLayer(selectedLayer);
        LayoutControls.setVisibility(View.GONE);

        if(selectedLayer1 != null) {


            unselectLayers(selectedLayer1);}

        if (!brushSelected){

            if (textLayout != null) {
                if(textLayout.getLocked() != null ) {
                    if(!textLayout.getLocked()) {

                        container2.setVisibility(View.GONE);
                        container.setVisibility(View.GONE);
                        if(!textLayout.isemoji){
                            container.setVisibility(View.VISIBLE);}

                        FrameLayout layer = textLayout.getFrameLayout();
                        if (layer != null) {
                            Button resizeButton = textLayout.getResizeButton();
                            Button deleteButton = textLayout.getDeleteButton();
                            Button rotateButton = textLayout.getRotateButton();
                            Button saveButton = textLayout.getSaveButton();

                            // Make the associated buttons and features visible
                            if (resizeButton != null && deleteButton != null && rotateButton != null && saveButton != null) {
                                resizeButton.setVisibility(View.VISIBLE);
                                deleteButton.setVisibility(View.VISIBLE);
                                rotateButton.setVisibility(View.VISIBLE);
                                saveButton.setVisibility(View.VISIBLE);
                            }
                            if (container.getVisibility() == View.GONE || container.getVisibility() == View.INVISIBLE) {
                                if (!textLayout.isemoji) {
                                    container.setVisibility(View.VISIBLE);
                                    container.startAnimation(fadeIn);}
                            }
                            if(HomeFragment1.imageButtons != null) {
                                HomeFragment1.imageButtons.setVisibility(View.GONE);
                            }

                            if(recyclerView != null){
                                recyclerView.setVisibility(View.VISIBLE);}
                            // Set the background resource to indicate selection
                            layer.setBackgroundResource(R.drawable.border_style);
                        }
                        selectedLayer = textLayout;
                    }}}
        }
    }
    public static void unselectLayer(TextLayout textLayout) {
        if (textLayout != null) {
            FrameLayout layer = textLayout.getFrameLayout();
            if (layer != null) {
                Button resizeButton = textLayout.getResizeButton();
                Button deleteButton = textLayout.getDeleteButton();
                Button rotateButton = textLayout.getRotateButton();
                Button saveButton = textLayout.getSaveButton();
                if (resizeButton != null && deleteButton != null && rotateButton != null && saveButton != null) {
                    resizeButton.setVisibility(View.INVISIBLE);
                    deleteButton.setVisibility(View.INVISIBLE);
                    rotateButton.setVisibility(View.INVISIBLE);
                    saveButton.setVisibility(View.INVISIBLE);
                }
                callSetDefaultState();

                layer.setBackground(null);
            }
        }
        MainActivity.selectedLayer = null;
    }
    public  void updateBackgroundImage(String backgroundFileName) {
        try {
            // Assuming backgroundFileName contains the file name of the background image
            File backgroundFile = new File(backgroundFileName);
            if (backgroundFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(backgroundFile.getAbsolutePath());
                imageView.setImageBitmap(bitmap);
            } else {
                Toast.makeText(this, "No file", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateBackgroundGallaryImage(Uri imagePath) {
        if (imagePath != null) {
            Glide.with(this)
                    .load(imagePath)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            // Handle loading failure if needed
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                            // Access the Bitmap from the Drawable resource
                            currenBit = ((BitmapDrawable) resource).getBitmap();
                            Toast.makeText(MainActivity.this, "taken", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    })
                    .into(imageView);


        }
    }

    public void applyEffectOnBackgroundImage(String backgroundFileName) {
        filterView.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load("file://" + backgroundFileName)
                .into(filterView);
    }


    static double getAngle(double x, double y, float pivotX, float pivotY) {
        double rad = Math.atan2(y - pivotY, x - pivotX) + Math.PI;
        return (rad * 180 / Math.PI + 180) % 360;
    }
    private boolean isViewInBounds(View view, int x, int y) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int viewX = location[0];
        int viewY = location[1];
        int viewWidth = view.getWidth();
        int viewHeight = view.getHeight();
        return (x >= viewX && x <= (viewX + viewWidth)) && (y >= viewY && y <= (viewY + viewHeight));
    }
    @Override
    public void onToolSelected(ToolTypeForCustomAdaptor toolType) {
        switch (toolType) {
            case TEXT:
                defaultContainer();
                TextHandlerClass.showTextDialog(this, textLayoutList, (ViewGroup) findViewById(android.R.id.content));
                break;
            case Photo:
                defaultContainer();
                ImagePickerManager.openGallery(MainActivity.this);
                break;


            case EMOJI:
                defaultContainer();
                openEmojiFragment();
                break;
            case Background:
                defaultContainer();
                bgcontainer.setVisibility(View.VISIBLE);
                FragmentTransaction fragmentTransaction0 = this.getSupportFragmentManager().beginTransaction();
                BackGroundFragment backGroundFragment = new BackGroundFragment();
                fragmentTransaction0.replace(R.id.fragment_container6, backGroundFragment);
                fragmentTransaction0.addToBackStack(null);
                fragmentTransaction0.commit();
                break;
            case Frames:
                defaultContainer();
                frameContainer.setVisibility(View.VISIBLE);
                FragmentTransaction fragmentTransaction1 = this.getSupportFragmentManager().beginTransaction();
                FrameFragment frameFragment = new FrameFragment();
                fragmentTransaction1.replace(R.id.fragment_container5, frameFragment);
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();



                break;
                case BRUSH:
                    if(selectedLayer != null){

                        unselectLayer(selectedLayer);
                    }
                    if(selectedLayer1 != null){
                        unselectLayers(selectedLayer1);
                    }
                    defaultContainer();
                    if(brushContainer.getVisibility() == View.GONE || brushContainer.getVisibility() == View.INVISIBLE){


                    brushContainer.setVisibility(View.VISIBLE);
                    FragmentTransaction brushtransactionn = this.getSupportFragmentManager().beginTransaction();
                    BrushFragment brushFragment = new BrushFragment();
                    brushtransactionn.replace(R.id.fragment_container4, brushFragment);
                    brushtransactionn.addToBackStack(null);
                    brushtransactionn.commit();

}
                    break;
            default:
                defaultContainer();

                break;
        }
    }

 void defaultContainer(){

    if(selectedLayer != null) {
        unselectLayer(selectedLayer);
    }
    else if(selectedLayer1 != null) {
        unselectLayers(selectedLayer1);
    }
    brushSelected = false;
     DrawPaint.brushEnable = false;
     drawPaintView.eraseDrawing();
     Optionssave.setVisibility(View.GONE);
     DrawPaint.bitmap1 = null;
     filtercontainer.setVisibility(View.GONE);
     frameContainer.setVisibility(View.GONE);
    container.setVisibility(View.GONE);
    bgcontainer.setVisibility(View.GONE);
    container2.setVisibility(View.GONE);
     brushContainer.setVisibility(View.GONE);



 }
    private void openEmojiFragment() {
        EmojiFragment emojiFragment = new EmojiFragment();
        emojiFragment.setEmojiListener(new EmojiFragment.EmojiListener() {
            @Override
            public void onEmojiClick(String emojiUnicode) {
                // Handle the clicked emoji, if needed
                createTextLayout(emojiUnicode, 300, 300,true);
                textList.add(emojiUnicode);
                TextHandlerClass.textList.add(emojiUnicode);

            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        emojiFragment.show(fragmentManager, emojiFragment.getTag());

    }
    public void onEditButtonClick(int index) {
        // Find the TextLayout in textLayoutList based on the text
        CombinedItem selectedLayout = findLayoutByIndex(index);
        if(selectedLayout.getImageLayout() != null) {
            selectLayers(selectedLayout.getImageLayout());
        }
        else if(selectedLayout.getTextlayout2() != null) {
            selectLayer(selectedLayout.getTextlayout2());
        }
    }
    public void onLockButtonClick(int index) {

        CombinedItem selectedLayout = findLayoutByIndex(index);
        if(selectedLayout.getImageLayout() != null) {
            if(selectedLayout.getImageLayout().getLocked()) {
                selectedLayout.getImageLayout().setLocked(false);
            }
            else {
                selectedLayout.getImageLayout().setLocked(true);
                if(selectedLayer1 != null){
                unselectLayers(selectedLayer1);}
            }
        }
        else if(selectedLayout.getTextlayout2() != null) {
            if(selectedLayout.getTextlayout2().getLocked()) {
                selectedLayout.getTextlayout2().setLocked(false);
            }
            else {
                selectedLayout.getTextlayout2().setLocked(true);
                if(selectedLayer != null) {
                unselectLayer(selectedLayer);}
            }
        }
        adapter.selectedItem = selectedLayout;
    }
    public static CombinedItem findLayoutByIndex(int index) {

        for (CombinedItem layout : combinedItemList) {

            if (index == combinedItemList.indexOf(layout)) {
                return layout;
            }
        }


        return null;
    }
    public void setHomeFragment(HomeFragment1 homeFragment1) {
        this.homeFragment1 = homeFragment1;
    }
    public int pxTodp(int px) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, getResources().getDisplayMetrics());
    }
    public static void callSetDefaultState() {
        if (homeFragment1 != null) {
            homeFragment1.setDefaultStateFromExternal();
            homeFragment1.editTextAdapter.clearSelection();
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    ImageLayout createImageLayout(Bitmap brushBitmap, Uri imageUri, String frameFilePath, float x, float y) {
        ImageLayout imageLayout = new ImageLayout(frameLayout, borderLayout, deleteButton2, rotateButton, resizeButton, saveButton, isLocked, null, imageView2,idI,isframe );
        imageLayout.setFrameLayout(frameLayout);
        imageLayout.setLocked(false);
        frameLayout.setTag(imageLayout);
        selectedLayer1 = imageLayout;
        imageLayoutList2.add(imageLayout);
        handler = new Handler();
        frameLayout = new FrameLayout(this);
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        frameLayout.setBackgroundResource(R.drawable.border_style);
        imageLayout.setFrameLayout(frameLayout);
        frameLayout.setMinimumWidth(20);
        Log.d(TAG, "createImageLayout: " + imageUri);

        RelativeLayout borderLayouts = new RelativeLayout(this);
        RelativeLayout.LayoutParams borderLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int layoutMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
        borderLayoutParams.setMargins(layoutMargin, layoutMargin, layoutMargin, layoutMargin);
        borderLayouts.setLayoutParams(borderLayoutParams);
        imageLayout.setBorderLayout(borderLayouts);
        borderLayouts.setGravity(Gravity.CENTER);

        imageView2 = new ImageView(this);
        if (imageUri != null) {
            imageLayout.isFrame = false;
            Glide.with(this)
                    .load(imageUri)
                    .into(imageView2);

            imageLayout.setBitmap(BitmapFactory.decodeFile(imageUri.getPath()));
        } else if (frameFilePath != null) {
            Glide.with(this)
                    .load("file://" + frameFilePath)
                    .into(imageView2);

            // Set other properties or perform additional actions related to the frame
            imageUri1 = Uri.parse(("file://" + frameFilePath));
            imageLayout.setBitmap(BitmapFactory.decodeFile(frameFilePath));

            // Make other necessary changes or updates based on the frame image click event
            // For example:
            frameContainer.setVisibility(View.VISIBLE);
        }
        else if(brushBitmap != null){
            imageLayout.isFrame = false;
            imageView2.setImageBitmap(brushBitmap);
            imageLayout.setImgBitmap(brushBitmap);
            imageLayout.setBitmap(brushBitmap);
        }
        imageLayout.setImageView(imageView2);
        imageView2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView2.setScaleType(ImageView.ScaleType.FIT_CENTER);

        deleteButton2 = ButtonCreator.createDeleteButton(this, 0.26f, 0.26f, -33, -29);
        imageLayout.setDeleteButton(deleteButton2);

        imageLayout.getDeleteButton().setOnClickListener(v -> {

            for (CombinedItem c : combinedItemList) {
                if (c.getImageLayout() != null && c.getImageLayout() == imageLayout) {
                    combinedItemList.remove(c);
                    break;
                }
            }

//            for ( int i=0 ; i<CombinedItem.ids.size() ; i++) {
//                int td = CombinedItem.ids.get(i);
//                if(td == imageLayout.getId()){
//                    combinedItemList.remove(CombinedItem.ids.indexOf(td));
//                    CombinedItem.ids.remove((Integer) td);
//                    break;
//                }
//            }
            imageLayoutList.remove(imageLayout);
            imageLayoutList2.remove(imageLayout);
            parentLayout.removeView(imageLayout.getFrameLayout());
            adapter.updateData(new ArrayList<>());
            adapter.notifyDataSetChanged();
            LayerRecycleView.setVisibility(View.GONE);

            if (container.getVisibility() == View.VISIBLE) {
                container.setVisibility(View.GONE);
                container.startAnimation(fadeOut);
            }
            if(container2.getVisibility() == View.VISIBLE){
                container2.setVisibility(View.GONE);
                container2.startAnimation(fadeOut);
            }
            if (selectedLayer1 == imageLayout) {
                unselectLayers(selectedLayer1);

            }

        });

        resizeButton = ButtonCreator.createResizeButton(this, 0.26f, 0.26f, -31, -29);
        imageLayout.setResizeButton(resizeButton);
        ResizeTouchListener imageResizeTouchListener = new ResizeTouchListener(imageLayout);
        resizeButton.setOnTouchListener(imageResizeTouchListener);

        rotateButton = ButtonCreator.createRotateButton(this, 0.26f, 0.26f, -33, -29);
        imageLayout.setRotateButton(rotateButton);
        imageLayout.getRotateButton().setOnTouchListener(new RotateTouchListener(imageLayout));

        saveButton = ButtonCreator.createSaveButton(this, 0.282f, 0.282f, -30, -18);
        imageLayout.setSaveButton(saveButton);

        imageLayout.getBorderLayout().setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                unselectLayers(selectedLayer1);
                unselectLayer(selectedLayer);
                selectLayers(imageLayout);
            }
            return false;
        });




        imageLayout.getFrameLayout().setOnTouchListener(new View.OnTouchListener() {
            private float lastX, lastY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        defaultContainer();
                        unselectLayers(selectedLayer1);
                        unselectLayer(selectedLayer);
                        selectLayers(imageLayout);
                        container2.setVisibility(View.VISIBLE);

                        if(imageLayout.getImgBitmap() !=null && homeFragment1 !=null){
                            homeFragment1.Image_filter.setVisibility(View.GONE);
                        }
                        lastX = event.getRawX();
                        lastY = event.getRawY();
                        Track.list.add(new Track(imageLayout.getId(),imageLayout.getFrameLayout().getX(),imageLayout.getFrameLayout().getY(),true));



                        return true;
                    case MotionEvent.ACTION_MOVE:
                        if(selectedLayer1 ==imageLayout){
                        if (!imageLayout.getLocked() && !brushSelected) {
                            float newX = event.getRawX();
                            float newY = event.getRawY();
                            float dX = newX - lastX;
                            float dY = newY - lastY;
                            imageLayout.getFrameLayout().setX(imageLayout.getFrameLayout().getX() + dX);
                            imageLayout.getFrameLayout().setY(imageLayout.getFrameLayout().getY() + dY);
                            lastX = newX;
                            lastY = newY;
                            }

                        }
                        break;
                }
                return true;
            }
        });

        borderLayouts.addView(imageLayout.getImageView());
        imageLayout.getFrameLayout().addView(imageLayout.getBorderLayout());
        imageLayout.getFrameLayout().addView(imageLayout.getResizeButton());
        imageLayout.getFrameLayout().addView(imageLayout.getDeleteButton());
        imageLayout.getFrameLayout().addView(imageLayout.getRotateButton());
        imageLayout.getFrameLayout().addView(imageLayout.getSaveButton());
        imageLayout.getFrameLayout().setX(x);
        imageLayout.getFrameLayout().setY(y);

        if (selectedLayer1 != null && imageLayout.getFrameLayout() != null) {
            imageLayout.getSaveButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    unselectLayers(selectedLayer1);
                    unselectLayer(selectedLayer);
                    selectedLayer1 = null;
                    callSetDefaultState();
                }
            });
        }
        LayerRecycleView.setVisibility(View.GONE);

        ScaleAnimation expandAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        expandAnimation.setDuration(600);
        imageLayout.getFrameLayout().startAnimation(expandAnimation);
        parentLayout.addView(imageLayout.getFrameLayout());
        Tid++;
        idI = Tid;
        imageLayout.setId(Tid);
        CombinedItem.ids.add(Tid);
        combinedItemList.add(new CombinedItem(imageLayout));
        selectLayers(imageLayout);
        return imageLayout;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(backimg){
            Uri imageUri = data.getData();
            updateBackgroundGallaryImage(imageUri);
            backimg =false;
        }
        else {
        ImagePickerManager.handleActivityResult(this, requestCode, resultCode, data);
    }}

    public static void selectLayers(ImageLayout imageLayout) {
        unselectLayers(selectedLayer1);
        LayoutControls.setVisibility(View.GONE);


        if(selectedLayer1 != null){


            unselectLayer(selectedLayer);}
        if(!brushSelected){
            selectedLayer1 = imageLayout;
            if(!imageLayout.getLocked()){
                if (imageLayout != null ) {
                    FrameLayout layer = imageLayout.getFrameLayout();
                    if (layer != null) {
                        Button resizeButton = imageLayout.getResizeButton();
                        Button deleteButton = imageLayout.getDeleteButton();
                        Button rotateButton = imageLayout.getRotateButton();
                        Button saveButton = imageLayout.getSaveButton();

                        // Make the associated buttons and features visible
                        if (resizeButton != null && deleteButton != null && rotateButton != null && saveButton != null) {
                            resizeButton.setVisibility(View.VISIBLE);
                            deleteButton.setVisibility(View.VISIBLE);
                            rotateButton.setVisibility(View.VISIBLE);
                            saveButton.setVisibility(View.VISIBLE);
                        }

                        if(!imageLayout.isFrame){
                            if (recyclerView != null) {
                                recyclerView.setVisibility(View.GONE);
                                }
                            if(HomeFragment1.imageButtons != null) {
                            HomeFragment1.imageButtons.setVisibility(View.GONE);
                            }

                            container.setVisibility(View.VISIBLE);


                        }
                        else {
                            if(container2.getVisibility()== View.GONE || container2.getVisibility() == View.INVISIBLE){
                                frameContainer.setVisibility(View.VISIBLE);
                                container.setVisibility(View.GONE);
                                FrameFragment frameFragment = new FrameFragment();
                                if(frameFragment.seekBar != null) {

                                    frameFragment.seekBar.setProgress((int) selectedLayer1.getImageView().getAlpha());
                                }
                            }
                        }
                        if (recyclerView != null) {
                            if (recyclerView.getVisibility() == View.VISIBLE) {
                                recyclerView.setVisibility(View.GONE);
                            }
                        }
                        if (HomeFragment1.imageButtons != null) {
                            HomeFragment1.imageButtons.setVisibility(View.VISIBLE);
                            HomeFragment1.Image_filter.setVisibility(View.VISIBLE);
                        }
                        // Set the background resource to indicate selection
                        layer.setBackgroundResource(R.drawable.border_style);
                    }
                }}
        }
    }
    public static void unselectLayers(ImageLayout selectedLayer1) {
        if (selectedLayer1 != null) {

            FrameLayout layer = selectedLayer1.getFrameLayout();
            if (layer != null) {
                Button resizeButton = selectedLayer1.getResizeButton();
                Button deleteButton = selectedLayer1.getDeleteButton();
                Button rotateButton = selectedLayer1.getRotateButton();
                Button saveButton = selectedLayer1.getSaveButton();
                if (resizeButton != null && deleteButton != null && rotateButton != null && saveButton != null) {
                    resizeButton.setVisibility(View.INVISIBLE);
                    deleteButton.setVisibility(View.INVISIBLE);
                    rotateButton.setVisibility(View.INVISIBLE);
                    saveButton.setVisibility(View.INVISIBLE);
                }
                frameContainer.setVisibility(View.GONE);
                container.setVisibility(View.GONE);
                bgcontainer.setVisibility(View.GONE);
                container2.setVisibility(View.GONE);
                brushContainer.setVisibility(View.GONE);
                callSetDefaultState();
                // Set the background resource to indicate selection
                layer.setBackground(null);
            }
        }
        MainActivity.selectedLayer1 = null;
    }

    public Bitmap getBitmapFromView(FrameLayout view) {
        // Check if the view has been laid out
        if (view.getWidth() == 0 || view.getHeight() == 0) {
            // The view hasn't been laid out yet, return null
            return null;
        }else{

        // Create a Bitmap with the same dimensions as the view
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);

        // Create a Canvas using the Bitmap
        Canvas canvas = new Canvas(bitmap);

        // Draw the view's visible content onto the Canvas
        view.draw(canvas);
            return bitmap;
}

    }
    static float convertPixelsToSP(float px, Context context) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px / scaledDensity;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted, yay! Do the
                    performStorageOperation(400);
                } else {
                    // Permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }

     void performStorageOperation(int newWidth) {
        try {

            JSONFileManager.saveJSONFile(combinedItemList, getApplicationContext());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        if (selectedLayer != null) {
            unselectLayer(selectedLayer);
        }
        if (selectedLayer1 != null) {
            unselectLayers(selectedLayer1);
        }
        defaultContainer();
        if(parentLayout != null) {
        Bitmap bitmap = getBitmapFromView(parentLayout);

        int width = parentLayout.getWidth();
        int height = parentLayout.getHeight();
        float ratio = (float) width / height;
        int newHeight = (int) (newWidth / ratio);
        ImageSaver.saveAsImage(MainActivity.this, bitmap, newWidth, newHeight);
    }

    }
    public void calculate(){
          if(heightsize>widthsize){
                parentLayout2.getLayoutParams().width=(parent2width/heightsize)*widthsize;
                parentLayout.getLayoutParams().width=(parent2width/heightsize)*widthsize;
                imageView.getLayoutParams().width=(parent2width/heightsize)*widthsize;
              drawPaintView.getLayoutParams().width=(parent2width/heightsize)*widthsize;
                filterView.getLayoutParams().width=(parent2width/heightsize)*widthsize;
          }
          else{
                parentLayout2.getLayoutParams().height=(parent2width/widthsize)*heightsize;
                parentLayout.getLayoutParams().height=(parent2width/widthsize)*heightsize;
                imageView.getLayoutParams().height=(parent2width/widthsize)*heightsize;
                drawPaintView.getLayoutParams().height=(parent2width/widthsize)*heightsize;
            filterView.getLayoutParams().height=(parent2width/widthsize)*heightsize;
          }


}
    private void  moveContinuesly(String direction){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               if(direction.equals("Left")){
                   float newTranslationX = parentLayout.getTranslationX()-1.50f;
                   updateTranslation(newTranslationX, parentLayout.getTranslationY());
                   handler.postDelayed(this, 10);

               }else if(direction.equals("Right")){
                   float newTranslationX = parentLayout.getTranslationX()+1.50f;

                   updateTranslation(newTranslationX, parentLayout.getTranslationY());
                   handler.postDelayed(this, 10);

               }else if(direction.equals("Up")){
                   float newTranslationY = parentLayout.getTranslationY()-1.50f;
                   updateTranslation(parentLayout.getTranslationX(), newTranslationY);
                   handler.postDelayed(this, 10);

               }else if(direction.equals("Down")){
                   float newTranslationY = parentLayout.getTranslationY()+1.50f;
                   updateTranslation(parentLayout.getTranslationX(), newTranslationY);}
                handler.postDelayed(this, 10);

            }
        }, 50);
    }
    private void updateTranslation(float newTranslationX, float newTranslationY) {

      float maxX = lastScaleFactor/2;
      Toast.makeText(this, (parentLayout.getX()-maxX)+" "+ parentLayout2.getX() , Toast.LENGTH_SHORT).show();
      if(parentLayout.getX()-maxX<parentLayout2.getX()){
          parentLayout.setX(newTranslationX);
      }
    }

    static class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
            static final float MAX_SCALE_FACTOR = 1.5f;
            private float currentScale = 1.0f;

            static float lastScaleFactor = 1.0f;
            private float focusX, focusY; // Store the focus point

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                float scaleFactor = detector.getScaleFactor();
//                LayoutControls.setVisibility(View.VISIBLE);

                // Add a sensitivity threshold to avoid small unintended scale changes
                float sensitivityThreshold = 0.01f; // Adjust this value as needed

                // Check if the scale factor change exceeds the sensitivity threshold
                if (Math.abs(scaleFactor - 1.0f) > sensitivityThreshold) {
                    currentScale *= scaleFactor;

                    // Get the focus point
                    focusX = detector.getFocusX();
                    focusY = detector.getFocusY();
                    currentScale = Math.max(1f, Math.min(currentScale, MAX_SCALE_FACTOR));

                    animateScale(currentScale, focusX, focusY);
                }

                return true;
            }


            private void animateScale(float targetScale, float focusX, float focusY) {
                ValueAnimator animator = ValueAnimator.ofFloat(currentScale, targetScale);
                animator.addUpdateListener(animation -> {
                    float animatedValue = (float) animation.getAnimatedValue();

                    // Scale the parentLayout around the focus point
                    parentLayout.setPivotX(focusX);
                    parentLayout.setPivotY(focusY);
                    parentLayout.setScaleX(animatedValue);
                    parentLayout.setScaleY(animatedValue);
                    updateLastScaleFactor(animatedValue);
                });
                animator.start();
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                // Initialize the focus point when scaling begins
                focusX = detector.getFocusX();
                focusY = detector.getFocusY();
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                // After scaling ends, check boundaries and adjust the layout position
                float maxX = (parentLayout.getWidth() * lastScaleFactor - parentLayout.getWidth()) / 2;
                float maxY = (parentLayout.getHeight() * lastScaleFactor - parentLayout.getHeight()) / 2;

                float newTranslationX = parentLayout.getTranslationX();
                float newTranslationY = parentLayout.getTranslationY();

                // Limit the translation based on the screen boundaries
                newTranslationX = Math.max(-maxX, Math.min(newTranslationX, maxX));
                newTranslationY = Math.max(-maxY, Math.min(newTranslationY, maxY));

                // Animate the layout to the adjusted position
                parentLayout.animate()
                        .translationX(newTranslationX)
                        .translationY(newTranslationY)
                        .setDuration(200) // Adjust the duration as needed
                        .start();
            }


        public static void updateLastScaleFactor(float scaleFactor) {
            lastScaleFactor = scaleFactor;
        }
        }

}