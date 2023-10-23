package com.example.postersmaker;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.util.List;

public class TextHandlerClass {

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
                addTextToImage(context, textLayoutList, viewGroup, text, 100, 100); // Default position
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
        FrameLayout textLayout = mainActivity.createTextLayout(text, x, y);
        textLayoutList.add(textLayout);
        viewGroup.addView(textLayout);

        mainActivity.addAction(new MainActivity.CustomAction(
                // Define the undo logic here
                () -> {
                    // Define how to undo the action
                    viewGroup.removeView(textLayout);
                    textLayoutList.remove(textLayout);
                },
                // Define the redo logic here
                () -> {
                    // Define how to redo the action
                    viewGroup.addView(textLayout);
                    textLayoutList.add(textLayout);
                }
        ));

    }

}
