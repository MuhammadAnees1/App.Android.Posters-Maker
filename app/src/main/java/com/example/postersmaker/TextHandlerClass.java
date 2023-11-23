package com.example.postersmaker;

import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;
import java.util.ArrayList;
import java.util.List;

public class TextHandlerClass {

    static List<String> textList = new ArrayList<>();
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
        if (mainActivity.selectedLayer != null) {
            mainActivity.unselectLayer(mainActivity.selectedLayer);
        }
        TextLayout textLayout = mainActivity.createTextLayout(text, x, y);
        FrameLayout frameLayout = textLayout.getFrameLayout();
        textLayoutList.add(frameLayout);
        viewGroup.addView(frameLayout);

        mainActivity.addAction(new MainActivity.CustomAction(
                // Define the undo logic here
                () -> {
                    // Define how to undo the action
                    viewGroup.removeView(frameLayout);
                    textLayoutList.remove(frameLayout);
                },
                // Define the redo logic here
                () -> {
                    // Define how to redo the action
                    viewGroup.addView(frameLayout);
                    textLayoutList.add(frameLayout);

                }
        ));
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
                textView.setText(newText); // Update the existing TextView with the new text
                textView.requestLayout();
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
