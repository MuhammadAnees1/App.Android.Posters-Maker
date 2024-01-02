package com.example.postersmaker;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.example.postersmaker.ImagePickerManager.imageLayoutList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.jgabrielfreitas.core.BlurImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CustomAdapter.OnItemSelected {
    private final CustomAdapter customAdapter = new CustomAdapter(this);
    RecyclerView LayerRecycleView;
    List<String> textList = new ArrayList<>();
    int idT, idI ;
    int Tid = 0;
    Layers_Adapter adapter = new Layers_Adapter(this, textList, LayerRecycleView);
    public final List<FrameLayout> textLayoutList = new ArrayList<>();
    public static List<TextLayout> textLayoutList2 = new ArrayList<>();
    public static List<ImageLayout> imageLayoutList2 = new ArrayList<>();

    List<CombinedItem> combinedItemList = new ArrayList<>();

    RelativeLayout borderLayout;
    static TranslateAnimation fadeIn;
    TranslateAnimation fadeOut;
    private final List<CustomAction> actions = new ArrayList<>();
    public static TextLayout selectedLayer;
    public static ImageLayout selectedLayer1;
    Boolean isLocked;
    TextView textView;
    Button deleteButton, rotateButton, resizeButton, saveButton, LayerButton;
    static HomeFragment homeFragment;
    private int currentActionIndex = -1;
    static BlurImageView imageView;
    public static ImageView  imageView2;
    public ImageView imgUndo,imgRedo;
    View previewImageView1;
    static FrameLayout container,frameLayout,parentLayout;
    public static void OpacityBackground(int progress) {
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

        LayerRecycleView = findViewById(R.id.LayerRecycleView);
        LayerRecycleView.setVisibility(View.GONE);
        LayerButton = findViewById(R.id.LayerButton);
        RecyclerView recyclerView = findViewById(R.id.rvConstraintTools);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        frameLayout = new FrameLayout(this);
        adapter = new Layers_Adapter(MainActivity.this, textList, LayerRecycleView);
        LayerRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
//        adapter.textList.addAll(TextHandlerClass.getTextList());

        adapter.notifyDataSetChanged();
        LayerRecycleView.setAdapter(adapter);
        imgUndo = findViewById(R.id.imgUndo);
        imgRedo = findViewById(R.id.imgRedo);
        parentLayout = findViewById(R.id.parentLayout);

        fadeIn = new TranslateAnimation(0, 0, 400, 0);
        fadeIn.setDuration(400);
        fadeOut = new TranslateAnimation(0, 0, 0, 400);
        fadeOut.setDuration(400);
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
//                combinedItemList.clear();
//                Layers_Adapter.combinedItemList.clear();
//                for (TextLayout textLayout : textLayoutList2) {
//                    CombinedItem combinedItem = new CombinedItem(textLayout.getTextView().getText().toString());
//                    combinedItemList.add(combinedItem);
//                }
//
//                for (ImageLayout imageLayout : imageLayoutList) {
//                    CombinedItem combinedItem = new CombinedItem(imageLayout);
//                    combinedItemList.add(combinedItem);
//                }
               Layers_Adapter.combinedItemList = combinedItemList;
            }
        });

    }
    @SuppressLint("ClickableViewAccessibility")
    TextLayout createTextLayout(String text, float x, float y) {
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


            for ( int i : CombinedItem.ids ){
                if(i == textLayout.getId()){
                    combinedItemList.remove(CombinedItem.ids.indexOf(i));

                    CombinedItem.ids.remove((Integer) i);
                    break;
                }

            }

            if (index != -1 && index < TextHandlerClass.textList.size()) {
                // Remove the TextLayout from your data structure
                TextHandlerClass.textList.remove(index);
                textLayoutList2.remove(index);
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
        idT = Tid + 1;
        Tid++;
        textLayout.setId(idT);
        CombinedItem.ids.add(idT);
        combinedItemList.add(new CombinedItem(textLayout));
            return textLayout;
    }
    public static void selectLayer(TextLayout textLayout) {
        unselectLayer(selectedLayer);
        if(selectedLayer1 != null) {


            unselectLayers(selectedLayer1);}


        if (!textLayout.getLocked()) {
            if (textLayout != null) {
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
                    if(HomeFragment.recyclerView != null){
                    HomeFragment.recyclerView.setVisibility(View.VISIBLE);}
                    // Set the background resource to indicate selection
                    layer.setBackgroundResource(R.drawable.border_style);
                }
                selectedLayer = textLayout;
            }
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
    public void updateBackgroundImage(String backgroundFileName) {
        imageView.setVisibility(View.INVISIBLE);
        try {
            InputStream inputStream = getAssets().open("Cover/" + backgroundFileName);
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            // Set the drawable as the background of the FrameLayout
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                parentLayout.setBackground(drawable);
            } else {
                parentLayout.setBackgroundDrawable(drawable);
            }
            parentLayout.setVisibility(View.VISIBLE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void updateBackgroundGallaryImage(String imagePath) {
        Glide.with(this)
                .load(imagePath)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        // Set the loaded image as the background of the FrameLayout
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            parentLayout.setBackground(resource);
                        } else {
                            // For older versions of Android
                            parentLayout.setBackgroundDrawable(resource);
                        }

                        // Ensure visibility
                        parentLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Handle any cleanup or additional operations when the image loading is cleared
                    }
                });
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
                TextHandlerClass.showTextDialog(this, textLayoutList, (ViewGroup) findViewById(android.R.id.content));
                break;
            case Photo:
                ImagePickerManager.openGallery(MainActivity.this);
                break;
            case FILTER:
                FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
                EffectFragment effectFragment = new EffectFragment();
                fragmentTransaction.replace(R.id.fragment_container, effectFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case EMOJI:
                openEmojiFragment();
                break;
            case Background:
                FragmentTransaction fragmentTransaction0 = this.getSupportFragmentManager().beginTransaction();
                BackGroundFragment backGroundFragment = new BackGroundFragment();
                fragmentTransaction0.replace(R.id.fragment_container, backGroundFragment);
                fragmentTransaction0.addToBackStack(null);
                fragmentTransaction0.commit();
                break;
            case Frames:
                FragmentTransaction fragmentTransaction1 = this.getSupportFragmentManager().beginTransaction();
                FrameFragment frameFragment = new FrameFragment();
                fragmentTransaction1.replace(R.id.fragment_container, frameFragment);
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();
                break;
        }
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
        TextLayout selectedTextLayout = findTextLayoutByText(index);

        // Call the selectLayer method in your MainActivity
        if (selectedTextLayout != null) {
            selectLayer(selectedTextLayout);
        }
    }
    public void onLockButtonClick(int index) {
        TextLayout selectedTextLayout = findTextLayoutByText(index);
        if (selectedTextLayout != null) {
            if (selectedTextLayout.getLocked()) {
                selectedTextLayout.setLocked(false);

            } else {
                selectedTextLayout.setLocked(true);
                if (selectedLayer == selectedTextLayout) {
                    unselectLayer(selectedTextLayout);
                }
            }
            adapter.selectedTextLayout = selectedTextLayout;
        }
    }
    public TextLayout findTextLayoutByText(int index) {

        for (TextLayout textLayout : textLayoutList2) {

            if (index == textLayoutList2.indexOf(textLayout)) {
                return textLayout;
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
    ImageLayout createImageLayout(Uri imageUri, String frameFileName, float x, float y) {
        ImageLayout imageLayout = new ImageLayout(frameLayout, borderLayout, deleteButton, rotateButton, resizeButton, saveButton, isLocked, null, imageView2,idI);
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
            Glide.with(this)
                    .load(imageUri)
                    .into(imageView2);
        } else if (frameFileName != null) {
            // Load the frame image


            Glide.with(this)
                    .load("file:///android_asset/Basic/" + frameFileName)
                    .into(imageView2);
        }
        imageLayout.setImageView(imageView2);
        Log.d(TAG, "createImageLayout: After setImageURI" + imageView2);
        imageView2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView2.setScaleType(ImageView.ScaleType.FIT_CENTER);

        deleteButton = ButtonCreator.createDeleteButton(this, 0.26f, 0.26f, -33, -29);
        imageLayout.setDeleteButton(deleteButton);

        imageLayout.getDeleteButton().setOnClickListener(v -> {


            for ( int i : CombinedItem.ids ){
                if(i == imageLayout.getId()){
                    combinedItemList.remove(CombinedItem.ids.indexOf(i));
                    CombinedItem.ids.remove((Integer) i);
                    break;
                }else{
                    Toast.makeText(this, "NF", Toast.LENGTH_SHORT).show();
                }
            }
            imageLayoutList.remove(imageLayout);
            imageLayoutList2.remove(imageLayout);
            Toast.makeText(this, "bnhj", Toast.LENGTH_SHORT).show();
            parentLayout.removeView(imageLayout.getFrameLayout());
            adapter.updateData(new ArrayList<>());
            adapter.notifyDataSetChanged();
            LayerRecycleView.setVisibility(View.GONE);

            if (container.getVisibility() == View.VISIBLE) {
                container.setVisibility(View.GONE);
                container.startAnimation(fadeOut);
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
        idI = Tid + 1;
        Tid++;
        imageLayout.setId(idI);
        CombinedItem.ids.add(idI);
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
                if (container.getVisibility() == View.GONE || container.getVisibility() == View.INVISIBLE) {
                    container.setVisibility(View.VISIBLE);
                    // Animation duration in milliseconds
//                    container.startAnimation(fadeIn);
                }
                if(HomeFragment.recyclerView.getVisibility() == View.VISIBLE){
                    HomeFragment.recyclerView.setVisibility(View.GONE);
                }
                HomeFragment.Image_control_button.setVisibility(View.VISIBLE);
                HomeFragment.Image_control_opacity.setVisibility(View.VISIBLE);
                // Set the background resource to indicate selection
                layer.setBackgroundResource(R.drawable.border_style);
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

                callSetDefaultState();
                // Set the background resource to indicate selection
                layer.setBackground(null);
            }
        }
    }
}