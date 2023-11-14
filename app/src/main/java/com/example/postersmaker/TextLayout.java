package com.example.postersmaker;

import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class TextLayout {


        public FrameLayout frameLayout;
        private  Button deleteButton;
        private  Button rotateButton;
        private  Button resizeButton;
        private  Button saveButton;
        private TextView textView;

        public TextLayout(FrameLayout frameLayout, Button deleteButton, Button rotateButton, Button resizeButton, Button saveButton, TextView textView) {
            this.frameLayout = frameLayout;
            this.deleteButton = deleteButton;
            this.rotateButton = rotateButton;
            this.resizeButton = resizeButton;
            this.saveButton = saveButton;
            this.textView = textView;
        }

        // Getter methods for the views
        public FrameLayout getFrameLayout() {
            return frameLayout;
        }

        public  Button getDeleteButton() {
            return deleteButton;
        }


    public void setDeleteButton(Button deleteButton) {
        this.deleteButton = deleteButton;
    }

    public void setRotateButton(Button rotateButton) {
        this.rotateButton = rotateButton;
    }

    public void setResizeButton(Button resizeButton) {
        this.resizeButton = resizeButton;
    }

    public Button getRotateButton() {
            return rotateButton;
        }

        public Button getResizeButton() {
            return resizeButton;
        }


    public void setFrameLayout(FrameLayout frameLayout) {
        this.frameLayout = frameLayout;
    }

    public Button getSaveButton() {
            return saveButton;
        }

    public float getX() {
        return frameLayout.getX();
    }

    public float getY() {


        return frameLayout.getY();
    }

    // Methods to set X and Y positions
    public void setX(float x) {
        frameLayout.setX(x);
    }

    public void setY(float y) {
        frameLayout.setY(y);
    }

        public TextView getTextView() {
            return textView;
        }
        public void setTextView(TextView textView) {
            this.textView = textView;
        }
        public void setSaveButton(Button saveButton) {
            this.saveButton = saveButton;
        }
    }


