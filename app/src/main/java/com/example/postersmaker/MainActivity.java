package com.example.postersmaker;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.ContextCompat.checkSelfPermission;

import static com.example.postersmaker.HomeFragment.recyclerView;
import static com.example.postersmaker.ImagePickerManager.imageLayoutList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
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
import com.example.loadJsonDataFromFile;
import com.jgabrielfreitas.core.BlurImageView;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CustomAdapter.OnItemSelected {
    private final CustomAdapter customAdapter = new CustomAdapter(this);
    RecyclerView LayerRecycleView;
    List<String> textList = new ArrayList<>();
    int idT, idI ;
    public static Uri imageUri1;
    ImageView savImgButton;
    int Tid = 0;
    Layers_Adapter adapter = new Layers_Adapter(this, textList, LayerRecycleView);
    public final List<FrameLayout> textLayoutList = new ArrayList<>();
    public static List<TextLayout> textLayoutList2 = new ArrayList<>();
    public static List<ImageLayout> imageLayoutList2 = new ArrayList<>();

    static List<CombinedItem> combinedItemList = new ArrayList<>();

    RelativeLayout borderLayout;
    static TranslateAnimation fadeIn;
    TranslateAnimation fadeOut;
    private final List<CustomAction> actions = new ArrayList<>();
    public static TextLayout selectedLayer;
    public static ImageLayout selectedLayer1;
    Boolean isLocked;
    boolean isframe ;
    static Bitmap originalBitmap1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 1;
    static String CurrentImg = null ;

    Bitmap imgBitmap;
    TextView textView;
    Button deleteButton,deleteButton2, rotateButton, resizeButton, saveButton, LayerButton;
    static HomeFragment homeFragment;
    private int currentActionIndex = -1;
    static BlurImageView imageView;
    public static ImageView  imageView2 ,imageView4;
    public ImageView imgUndo,imgRedo;
    View previewImageView1;
    static FrameLayout container,container2,frameLayout,parentLayout;

    public  void OpacityBackground(int progress) {
        imageView.setVisibility(View.VISIBLE);
        float opacity = progress / 100f;

        imageView.setAlpha(opacity);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (BlurImageView) findViewById(R.id.previewImageView);
        previewImageView1 = findViewById(R.id.previewImageView1);
        container = findViewById(R.id.fragment_container);
        container2 = findViewById(R.id.fragment_container3);
        LayerRecycleView = findViewById(R.id.LayerRecycleView);
        LayerRecycleView.setVisibility(View.GONE);
        LayerButton = findViewById(R.id.LayerButton);
        savImgButton = findViewById(R.id.imgSave);
        imageView4 = findViewById(R.id.imgClose);
        RecyclerView recyclerView = findViewById(R.id.rvConstraintTools);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        frameLayout = new FrameLayout(this);
        adapter = new Layers_Adapter(MainActivity.this, textList, LayerRecycleView);
        LayerRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
//        adapter.textList.addAll(TextHandlerClass.getTextList());
        parentLayout = findViewById(R.id.parentLayout);

        adapter.notifyDataSetChanged();
        LayerRecycleView.setAdapter(adapter);
        imgUndo = findViewById(R.id.imgUndo);
        imgRedo = findViewById(R.id.imgRedo);

        fadeIn = new TranslateAnimation(0, 0, 400, 0);
        fadeIn.setDuration(400);
        fadeOut = new TranslateAnimation(0, 0, 0, 400);
        fadeOut.setDuration(400);

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an instance of loadJsonDataFromFile and call the method
                loadJsonDataFromFile jsonLoader = new loadJsonDataFromFile(MainActivity.this);
                jsonLoader.loadJsonData();
                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        savImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) v.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                        ActivityCompat.requestPermissions((Activity) v.getContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions((Activity) v.getContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                    }
                } else {
                    // Permission has already been granted, you can perform the storage operation
                    performStorageOperation();
                }

            }

        });

        parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unselectLayer(selectedLayer);
                unselectLayers(selectedLayer1);
                selectedLayer = null;
                selectedLayer1 = null;
                LayerRecycleView.setVisibility(View.GONE);
                callSetDefaultState();
                if (container.getVisibility() == View.VISIBLE) {
                    container.setVisibility(View.GONE);
//                    container.startAnimation(fadeOut);
                }
                if (container2.getVisibility() == View.VISIBLE) {
                    container2.setVisibility(View.GONE);
                }
            }
        });
        imgUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undo();
            }
        });
        imgRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redo();
            }
        });
        LayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (LayerRecycleView.getVisibility() == View.VISIBLE) {
                    LayerRecycleView.setVisibility(View.GONE);
                } else {
                    LayerRecycleView.setVisibility(View.VISIBLE);
                }

                Layers_Adapter.combinedItemList = combinedItemList;
            }
        });

    }
    @SuppressLint("ClickableViewAccessibility")
    public TextLayout createTextLayout(String text, float x, float y) {
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
        textLayoutList2.add(textLayout);

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
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        borderLayout.setMinimumHeight(textView.getHeight() + 20);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setTypeface(null, Typeface.NORMAL);
        textView.setMaxWidth(imageView.getWidth() - 40);
        frameLayout.setMinimumHeight(textView.getHeight() + 20);


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
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unselectLayer(selectedLayer);
                unselectLayers(selectedLayer1);
                selectedLayer = null;
                selectedLayer1 = null;
                LayerRecycleView.setVisibility(View.GONE);
                callSetDefaultState();
                if (container.getVisibility() == View.VISIBLE) {
                    container.setVisibility(View.GONE);
                    container.startAnimation(fadeOut);
                }
                if(container2.getVisibility() == View.VISIBLE) {
                    container2.setVisibility(View.GONE);
                }
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
                            lastX = event.getRawX();
                            lastY = event.getRawY();
                            unselectLayer(selectedLayer);
                            unselectLayers(selectedLayer1);
                            selectLayer(textLayout);
                            return true;

                        case MotionEvent.ACTION_MOVE:
                            if (textLayout.getLocked() != null) {
                                if (!textLayout.getLocked()) {
                                    float newX = event.getRawX();
                                    float newY = event.getRawY();
                                    float dX = newX - lastX;
                                    float dY = newY - lastY;
                                    // Update the position of the frameLayout based on the drag movement
                                    textLayout.getFrameLayout().setX(textLayout.getFrameLayout().getX() + dX);
                                    textLayout.getFrameLayout().setY(textLayout.getFrameLayout().getY() + dY);
                                    lastX = newX;
                                    lastY = newY;
                                    break;
                                }
                            }
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
                            lastX = event.getRawX();
                            lastY = event.getRawY();
                            selectLayer(textLayout);

                            return true;
                        case MotionEvent.ACTION_MOVE:
                            if(textLayout.getLocked() != null) {
                                if (!textLayout.getLocked()) {
                                    float newX = event.getRawX();
                                    float newY = event.getRawY();
                                    float dX = newX - lastX;
                                    float dY = newY - lastY;

                                    textLayout.getFrameLayout().setX(textLayout.getFrameLayout().getX() + dX);
                                    textLayout.getFrameLayout().setY(textLayout.getFrameLayout().getY() + dY);

                                    lastX = newX;
                                    lastY = newY;
                                    break;
                                }
                            }}
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
        idT = Tid + 1;
        Tid++;
        textLayout.setId(Tid);
        CombinedItem.ids.add(Tid);
        combinedItemList.add(new CombinedItem(textLayout));
        Toast.makeText(this, ""+Tid, Toast.LENGTH_SHORT).show();

        return textLayout;
    }
    public static void selectLayer(TextLayout textLayout) {
        unselectLayer(selectedLayer);
        if(selectedLayer1 != null) {


            unselectLayers(selectedLayer1);}



        if (textLayout != null) {
            if(textLayout.getLocked() != null ) {
                if(!textLayout.getLocked()) {

                    container2.setVisibility(View.GONE);
                    container.setVisibility(View.VISIBLE);
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
                            container.setVisibility(View.VISIBLE);
//                        HomeFragment.recyclerView.setVisibility(View.VISIBLE);
                            // Animation duration in milliseconds
                            container.startAnimation(fadeIn);
                        }
                        if(HomeFragment.Image_control_opacity != null && HomeFragment.Image_control_button != null) {
                            if(HomeFragment.Image_control_opacity.getVisibility() == View.VISIBLE ) {
                                HomeFragment.Image_control_opacity.setVisibility(View.GONE);
                                HomeFragment.Image_control_button.setVisibility(View.GONE);
                            }
                        }
                        if(recyclerView != null){
                            recyclerView.setVisibility(View.VISIBLE);}
                        // Set the background resource to indicate selection
                        layer.setBackgroundResource(R.drawable.border_style);
                    }
                    selectedLayer = textLayout;
                }}
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
                // Set the background resource to indicate selection
                layer.setBackground(null);
            }
        }
    }
    void addAction(CustomAction action) {
        if (currentActionIndex < actions.size() - 1) {
            actions.subList(currentActionIndex + 1, actions.size()).clear();
        }
        actions.add(action);
        currentActionIndex = actions.size() - 1;
    }
    private void undo() {
        if (currentActionIndex >= 0) {
            CustomAction action = actions.get(currentActionIndex);
            action.undo();
            currentActionIndex--;
        }
    }
    private void redo() {
        if (currentActionIndex < actions.size() - 1) {
            currentActionIndex++;
            CustomAction action = actions.get(currentActionIndex);
            action.redo();
        }
    }
    public  void updateBackgroundImage(String backgroundFileName) {
        try {
            InputStream inputStream = getAssets().open("Cover/" + backgroundFileName);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Set the bitmap as the image of the ImageView
            imageView.setImageBitmap(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void updateBackgroundGallaryImage(String imagePath) {
        Glide.with(this)
                .load(imagePath)
                .into(imageView);

//                .into(new CustomTarget<Drawable>() {
//                    @Override
//                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                        // Set the loaded image as the background of the FrameLayout
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                            parentLayout.setBackground(resource);
//                        } else {
//                            // For older versions of Android
//                            parentLayout.setBackgroundDrawable(resource);
//                        }
//
//                        // Ensure visibility
//                        parentLayout.setVisibility(View.VISIBLE);
//                    }
//
//                    @Override
//                    public void onLoadCleared(@Nullable Drawable placeholder) {
//                        // Handle any cleanup or additional operations when the image loading is cleared
//                    }
//                });
    }

    public void applyEffectOnBackgroundImage(String backgroundFileName) {
        Glide.with(this)
                .load("file:///android_asset/effect/" + backgroundFileName)
                .into(imageView);
    }

    static class CustomAction {
        private final Runnable undoAction;
        private final Runnable redoAction;

        public CustomAction(Runnable undoAction, Runnable redoAction) {
            this.undoAction = undoAction;
            this.redoAction = redoAction;
        }

        public void undo() {
            undoAction.run();
        }

        public void redo() {
            redoAction.run();
        }
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
            case FILTER:
                defaultContainer();
                FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
                EffectFragment effectFragment = new EffectFragment();
                fragmentTransaction.replace(R.id.fragment_container, effectFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case EMOJI:
                defaultContainer();
                openEmojiFragment();
                break;
            case Background:
                defaultContainer();
                FragmentTransaction fragmentTransaction0 = this.getSupportFragmentManager().beginTransaction();
                BackGroundFragment backGroundFragment = new BackGroundFragment();
                fragmentTransaction0.replace(R.id.fragment_container, backGroundFragment);
                fragmentTransaction0.addToBackStack(null);
                fragmentTransaction0.commit();
                if(CurrentImg != null){
                    updateBackgroundImage(CurrentImg);
                    imageView.setImageBitmap(originalBitmap1);}
                break;
            case Frames:
                defaultContainer();
                container2.setVisibility(View.VISIBLE);
                FragmentTransaction fragmentTransaction1 = this.getSupportFragmentManager().beginTransaction();
                FrameFragment frameFragment = new FrameFragment();
                fragmentTransaction1.replace(R.id.fragment_container3, frameFragment);
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();
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
        container.setVisibility(View.GONE);
        container2.setVisibility(View.GONE);
    }
    private void openEmojiFragment() {
        EmojiFragment emojiFragment = new EmojiFragment();
        emojiFragment.setEmojiListener(new EmojiFragment.EmojiListener() {
            @Override
            public void onEmojiClick(String emojiUnicode) {
                // Handle the clicked emoji, if needed
                createTextLayout(emojiUnicode, 300, 300);
                textList.add(emojiUnicode);
                TextHandlerClass.textList.add(emojiUnicode);
                container.setVisibility(View.VISIBLE);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                HomeFragment homeFragment = new HomeFragment();
                fragmentTransaction.replace(R.id.fragment_container, homeFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
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
    public void setHomeFragment(HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
    }
    public int pxTodp(int px) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, getResources().getDisplayMetrics());
    }
    public static void callSetDefaultState() {
        if (homeFragment != null) {
            homeFragment.setDefaultStateFromExternal();
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    public ImageLayout createImageLayout(Uri imageUri, String frameFileName, float x, float y) {
        ImageLayout imageLayout = new ImageLayout(frameLayout, borderLayout, deleteButton2, rotateButton, resizeButton, saveButton, isLocked, null, imageView2,idI,isframe );
        imageLayout.setFrameLayout(frameLayout);
        imageLayout.setLocked(false);
        frameLayout.setTag(imageLayout);
        selectedLayer1 = imageLayout;
        imageLayoutList2.add(imageLayout);

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
            // Load the main image
            imageLayout.isFrame = false;
            Glide.with(this)
                    .load(imageUri)
                    .into(imageView2);
        } else if (frameFileName != null) {
            // Load the frame image
            imageLayout.isFrame = true;

            Glide.with(this)
                    .load("file:///android_asset/Basic/" + frameFileName)
                    .into(imageView2);
            imageUri1 = Uri.parse(("file:///android_asset/Basic/" + frameFileName));
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

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unselectLayers(selectedLayer1);
                unselectLayer(selectedLayer);
                selectedLayer1 = null;

                LayerRecycleView.setVisibility(View.GONE);
                callSetDefaultState();
                if (container.getVisibility() == View.VISIBLE) {
                    container.setVisibility(View.GONE);
//                    container.startAnimation(fadeOut);
                }
                if(container2.getVisibility() == View.VISIBLE){
                    container2.setVisibility(View.GONE);
                }
            }
        });



        imageLayout.getFrameLayout().setOnTouchListener(new View.OnTouchListener() {
            private float lastX, lastY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = event.getRawX();
                        lastY = event.getRawY();
                        unselectLayers(selectedLayer1);
                        unselectLayer(selectedLayer);
                        selectLayers(imageLayout);

                        return true;
                    case MotionEvent.ACTION_MOVE:
                        if (!imageLayout.getLocked()) {
                            float newX = event.getRawX();
                            float newY = event.getRawY();
                            float dX = newX - lastX;
                            float dY = newY - lastY;
                            imageLayout.getFrameLayout().setX(imageLayout.getFrameLayout().getX() + dX);
                            imageLayout.getFrameLayout().setY(imageLayout.getFrameLayout().getY() + dY);
                            lastX = newX;
                            lastY = newY;
                            break;
                        }
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

        parentLayout.addView(imageLayout.getFrameLayout());
        Tid++;
        idI = Tid;
        imageLayout.setId(Tid);
        CombinedItem.ids.add(Tid);
        combinedItemList.add(new CombinedItem(imageLayout));

        return imageLayout;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (fragment instanceof BackGroundFragment) {
            BackGroundFragment backgroundFragment = (BackGroundFragment) fragment;
            backgroundFragment.handleActivityResult(requestCode, resultCode, data);
        }
        // Forward the result to ImagePickerManager for handling
        ImagePickerManager.handleActivityResult(this, requestCode, resultCode, data);
    }

    public static void selectLayers(ImageLayout imageLayout) {
        unselectLayers(selectedLayer1);

        if(selectedLayer1 != null){


            unselectLayer(selectedLayer);}
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
                        if (container.getVisibility() == View.GONE || container.getVisibility() == View.INVISIBLE) {
                            container.setVisibility(View.VISIBLE);
                            container2.setVisibility(View.GONE);

//                    container.startAnimation(fadeIn);
                        }}
                    else {
                        if(container2.getVisibility()== View.GONE || container2.getVisibility() == View.INVISIBLE){
                            container2.setVisibility(View.VISIBLE);
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
                    if (HomeFragment.Image_control_button != null && HomeFragment.Image_control_opacity != null) {
                        HomeFragment.Image_control_button.setVisibility(View.VISIBLE);
                        HomeFragment.Image_control_opacity.setVisibility(View.VISIBLE);
                    }
                    // Set the background resource to indicate selection
                    layer.setBackgroundResource(R.drawable.border_style);
                }
            }
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
                container2.setVisibility(View.GONE);
                callSetDefaultState();
                // Set the background resource to indicate selection
                layer.setBackground(null);
            }
        }
    }
    public Bitmap getBitmapFromView(FrameLayout view) {
        // Check if the view has been laid out
        if (view.getWidth() == 0 || view.getHeight() == 0) {
            // The view hasn't been laid out yet, return null
            return null;
        }

        // Create a Bitmap with the same dimensions as the view
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);

        // Create a Canvas using the Bitmap
        Canvas canvas = new Canvas(bitmap);

        // Draw the view's visible content onto the Canvas
        view.draw(canvas);

        return bitmap;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted, yay! Do the
                    // storage-related task you need to do.
                    performStorageOperation();
                } else {
                    Toast.makeText(this, "Permission required to save image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void performStorageOperation() {
        try {

            JSONFileManager.saveJSONFile(combinedItemList, getApplicationContext());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        if(selectedLayer != null){
            unselectLayer(selectedLayer);}
        if(selectedLayer1 != null){
            unselectLayers(selectedLayer1);}

        imgBitmap = getBitmapFromView(parentLayout);
        ImageSaver.saveAsImage(MainActivity.this, imgBitmap);
    }
}
