package com.example.postersmaker;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.example.postersmaker.MainActivity.combinedItemList;
import static com.example.postersmaker.MainActivity.homeFragment;
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
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

public class TextHandlerClass {
    static List<FrameLayout> textLayoutList = new ArrayList<>();
    static List<String> textList = new ArrayList<>();

    public static void swapViewsInLayout(int fromIndex, int toIndex) {
        if (fromIndex >= 0 && fromIndex < MainActivity.combinedItemList.size() &&
                toIndex >= 0 && toIndex < MainActivity.combinedItemList.size()) {
            int a= 0;
            int b= 0;
            CombinedItem fromLayout = MainActivity.findLayoutByIndex(fromIndex);
            CombinedItem toLayout = MainActivity.findLayoutByIndex(toIndex);
            FrameLayout fromFrameLayout = null;
            assert fromLayout != null;
            if (fromLayout.getImageLayout() != null) {
                fromFrameLayout = fromLayout.getImageLayout().getFrameLayout();
            }
            else if (fromLayout.getTextlayout2() != null) {
                fromFrameLayout = fromLayout.getTextlayout2().getFrameLayout();
            }
            FrameLayout toFrameLayout = null;
            assert toLayout != null;
            if (toLayout.getImageLayout() != null) {
                toFrameLayout = toLayout.getImageLayout().getFrameLayout();
            }
            else if (toLayout.getTextlayout2() != null) {
                toFrameLayout = toLayout.getTextlayout2().getFrameLayout();
            }

            if (fromFrameLayout != null && toFrameLayout != null) {
                ViewGroup fromParent = (ViewGroup) fromFrameLayout.getParent();
                ViewGroup toParent = (ViewGroup) toFrameLayout.getParent();

                if (fromParent != null && toParent != null) {
                    // Remove the views from their current parents
                    for (int i = 0; i < fromParent.getChildCount(); i++) {
                        if(fromParent.getChildAt(i) == fromFrameLayout){
                            fromParent.removeView(fromParent.getChildAt(i));
                            a = i;

                            break;
                        }
                    }
                    for (int i = 0; i < toParent.getChildCount(); i++) {
                        if(toParent.getChildAt(i) == toFrameLayout){
                            toParent.removeView(toParent.getChildAt(i));
                            b = i;
                            break;}
                    }

                    toParent.addView(fromFrameLayout, b );

                    fromParent.addView(toFrameLayout, a);
                }
                Toast.makeText(homeFragment.getActivity(), "to" + (combinedItemList.indexOf(toLayout)+1) + a  + "from" + (combinedItemList.indexOf(fromLayout)+1) + b, Toast.LENGTH_SHORT).show();

            }
        }

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
                addTextToImage(context, textLayoutList, viewGroup, text, 400, 400);
                // Add the text to the list
                textList.add(text);
                TextHandlerClass.textLayoutList = textLayoutList;
                if (context instanceof MainActivity) {
                    MainActivity mainActivity = (MainActivity) context;
                    mainActivity.container.setVisibility(View.VISIBLE);
                    mainActivity.adapter.updateData(new ArrayList<>());
                    mainActivity.adapter.textList.addAll(TextHandlerClass.getTextList());
                    mainActivity.adapter.notifyDataSetChanged();
                    mainActivity.LayerRecycleView.setAdapter(mainActivity.adapter);
                    FragmentTransaction fragmentTransaction = mainActivity.getSupportFragmentManager().beginTransaction();
                    HomeFragment homeFragment = new HomeFragment();
                    fragmentTransaction.replace(R.id.fragment_container, homeFragment);
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

        TextLayout textLayout = mainActivity.createTextLayout(text, x, y);
        FrameLayout frameLayout = textLayout.getFrameLayout();

        // Check if the frameLayout already has a parent
        if (frameLayout.getParent() == null) {
            // Add the frameLayout to the viewGroup
            viewGroup.addView(frameLayout);
            textLayoutList.add(frameLayout);

            // Add an action for undo and redo
            mainActivity.addAction(new MainActivity.CustomAction(
                    () -> {
                        // Undo logic: remove the frameLayout from the viewGroup
                        viewGroup.removeView(frameLayout);
                        textLayoutList.remove(frameLayout);
                    },
                    () -> {
                        // Redo logic: add the frameLayout back to the viewGroup
                        viewGroup.addView(frameLayout);
                        textLayoutList.add(frameLayout);
                    }
            ));
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

                int index = -1;

                for (int i = 0; i < textLayoutList2.size(); i++) {
                    TextLayout textLayout = textLayoutList2.get(i);
                    if (textLayout.getTextView() == textView) {
                        index = i;
                        break;
                    }
                }

                if (index != -1) {
                    textList.set(index, newText);
                }

                textView.setText(newText); // Update the existing TextView with the new text
                textView.requestLayout();
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