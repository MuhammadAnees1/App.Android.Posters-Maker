package com.example.postersmaker;

public class CustomActionRedondo {
        private final Runnable undoAction;
        private final Runnable redoAction;
        public CustomActionRedondo(Runnable undoAction, Runnable redoAction) {
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

