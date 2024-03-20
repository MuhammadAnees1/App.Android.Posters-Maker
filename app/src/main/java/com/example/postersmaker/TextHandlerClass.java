package com.example.postersmaker;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.example.postersmaker.MainActivity.combinedItemList;
import static com.example.postersmaker.MainActivity.container;
import static com.example.postersmaker.MainActivity.textLayoutList2;

import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextHandlerClass {
    static List<FrameLayout> textLayoutList = new ArrayList<>();
    static List<String> textList = new ArrayList<>();

    public static void swapViewsInLayout(int fromIndex, int toIndex) {
        if (isValidIndex(fromIndex) && isValidIndex(toIndex)) {
            CombinedItem fromLayout = MainActivity.findLayoutByIndex(fromIndex);
            CombinedItem toLayout = MainActivity.findLayoutByIndex(toIndex);

            if (fromLayout != null && toLayout != null) {
                FrameLayout fromFrameLayout = getFrameLayoutFromLayout(fromLayout);
                FrameLayout toFrameLayout = getFrameLayoutFromLayout(toLayout);

                if (fromFrameLayout != null && toFrameLayout != null) {
                    // Swap the items in the data structure
                    Collections.swap(combinedItemList, fromIndex, toIndex);
                    MainActivity mainActivity = new MainActivity();
                    // Notify the adapter about the change
                    mainActivity.adapter.notifyItemMoved(fromIndex, toIndex);

                    // Swap the FrameLayouts in the layout
                    swapFrameLayouts(fromFrameLayout, toFrameLayout);
                }
            }
        }
    }

    private static void swapFrameLayouts(FrameLayout fromFrameLayout, FrameLayout toFrameLayout) {
        ViewGroup fromParent = (ViewGroup) fromFrameLayout.getParent();
        ViewGroup toParent = (ViewGroup) toFrameLayout.getParent();

        if (fromParent != null && toParent != null) {
            // Get the indices of the FrameLayouts
            int fromIndex = fromParent.indexOfChild(fromFrameLayout);
            int toIndex = toParent.indexOfChild(toFrameLayout);

            // Swap the layout parameters to update positions
            fromFrameLayout.setLayoutParams(toFrameLayout.getLayoutParams());
            toFrameLayout.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            // Swap the FrameLayouts in the parent
            fromParent.removeView(fromFrameLayout);
            toParent.addView(fromFrameLayout, toIndex);

            toParent.removeView(toFrameLayout);
            fromParent.addView(toFrameLayout, fromIndex);
        }
    }

    private static boolean isValidIndex(int index) {
        return index >= 0 && index < MainActivity.combinedItemList.size();
    }

    private static FrameLayout getFrameLayoutFromLayout(CombinedItem layout) {
        if (layout.getImageLayout() != null) {
            return layout.getImageLayout().getFrameLayout();
        } else if (layout.getTextlayout2() != null) {
            return layout.getTextlayout2().getFrameLayout();
        }
        return null;
    }


    public static void showTextDialog(Context context, List<FrameLayout> textLayoutList, ViewGroup viewGroup) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Enter Text");
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = input.getText().toString();
                text =  text + " ";
                addTextToImage(context, textLayoutList, viewGroup, text, 400, 400);
                // Add the text to the list
                textList.add(text);
                TextHandlerClass.textLayoutList = textLayoutList;
                if (context instanceof MainActivity) {
                    MainActivity mainActivity = (MainActivity) context;
                    MainActivity.container.setVisibility(View.VISIBLE);
                    mainActivity.adapter.updateData(new ArrayList<>());
                    mainActivity.adapter.textList.addAll(TextHandlerClass.getTextList());
                    mainActivity.adapter.notifyDataSetChanged();
                    mainActivity.LayerRecycleView.setAdapter(mainActivity.adapter);
                    FragmentTransaction fragmentTransaction = mainActivity.getSupportFragmentManager().beginTransaction();
                    HomeFragment1 homeFragment1 = new HomeFragment1();
                    fragmentTransaction.replace(R.id.fragment_container, homeFragment1);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    public static void addTextToImage(Context context, List<FrameLayout> textLayoutList, ViewGroup viewGroup, String text, float x, float y) {
        MainActivity mainActivity = (MainActivity) context;

        // Unselect the old layer if there is one
        if (MainActivity.selectedLayer != null) {
            MainActivity.unselectLayer(MainActivity.selectedLayer);
        }

        TextLayout textLayout = mainActivity.createTextLayout(text, x, y,false);
        textLayoutList2.add(textLayout);
        FrameLayout frameLayout = textLayout.getFrameLayout();

        // Check if the frameLayout already has a parent
        if (frameLayout.getParent() == null) {
            // Add the frameLayout to the viewGroup
            viewGroup.addView(frameLayout);
            textLayoutList.add(frameLayout);


        } else {
            Log.e(TAG, "addTextToImage: FrameLayout already has a parent");
        }
    }

    // Retrieve the array of texts
    public static List<String> getTextList() {
        return textList;
    }

    public static void edittextDialog(Context context, TextView textView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Text");
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(textView.getText().toString());
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newText = input.getText().toString();
                newText =  newText + " ";

                int index = -1;

                for (int i = 0; i < textLayoutList2.size(); i++) {
                    TextLayout textLayout = textLayoutList2.get(i);
                    if (textLayout.getTextView() == textView) {
                        Track.list.add(new Track(true,textLayout.getId(),textView.getText().toString(),index));
                        Track.list2.clear();

                        index = i;
                        break;
                    }
                }

                if (index != -1) {
                    textList.set(index, newText);
                }

                textView.setText(newText); // Update the existing TextView with the new text
                textView.requestLayout();
                container.setVisibility(View.VISIBLE);
                if (context instanceof MainActivity) {
                    MainActivity mainActivity = (MainActivity) context;

                    mainActivity.adapter.updateData(new ArrayList<>());
                    mainActivity.adapter.textList.addAll(TextHandlerClass.getTextList());
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}