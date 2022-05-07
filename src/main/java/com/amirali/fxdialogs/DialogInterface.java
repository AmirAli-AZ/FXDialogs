package com.amirali.fxdialogs;

public interface DialogInterface {

    int POSITIVE_BUTTON = 1;

    int NEGATIVE_BUTTON = -1;

    int NATURAL_BUTTON = 0;

    interface OnClickListener {
        void onClick(int which);
    }

    interface OnSingleChoiceSelectedListener {
        void onItemSelected(int which);
    }

    interface OnMultiChoiceSelectedListener {
        void onItemSelected(int which, boolean isChecked);
    }
}
