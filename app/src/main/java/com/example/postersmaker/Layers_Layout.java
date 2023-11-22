package com.example.postersmaker;

import android.widget.Button;
import android.widget.TextView;

public class Layers_Layout {

    private Button lockLayerButton;
    private Button moveLayerButton;
    private Button editLayerButton;
    private TextView textView;

    public Button getLockLayerButton() {
        return lockLayerButton;
    }

    public void setLockLayerButton(Button lockLayerButton) {
        this.lockLayerButton = lockLayerButton;
    }

    public Button getMoveLayerButton() {
        return moveLayerButton;
    }

    public void setMoveLayerButton(Button moveLayerButton) {
        this.moveLayerButton = moveLayerButton;
    }

    public Button getEditLayerButton() {
        return editLayerButton;
    }

    public void setEditLayerButton(Button editLayerButton) {
        this.editLayerButton = editLayerButton;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public Layers_Layout(Button lockLayerButton, Button moveLayerButton, Button editLayerButton, TextView textView) {
        this.lockLayerButton = lockLayerButton;
        this.moveLayerButton = moveLayerButton;
        this.editLayerButton = editLayerButton;
        this.textView = textView;

    }
}