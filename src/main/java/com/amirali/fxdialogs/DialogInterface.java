package com.amirali.fxdialogs;

/**
 * @author Amir Ali
 */

public interface DialogInterface {

    /**
     * id of the positive button
     */
    int POSITIVE_BUTTON = 1;

    /**
     * id of the negative button
     */
    int NEGATIVE_BUTTON = -1;

    /**
     * id of the natural button
     */
    int NATURAL_BUTTON = 0;

    interface OnClickListener {
        /**
         * the method is called when button is clicked
         * @param which button id
         */
        void onClick(int which);
    }

    interface OnSingleChoiceSelectedListener {
        /**
         * the method is called when RadioButton selected
         * @param which index
         */
        void onItemSelected(int which);
    }

    interface OnMultiChoiceSelectedListener {
        /**
         * the method is called when CheckBox is checked or not
         * @param which index
         * @param isChecked CheckBox state whatever checked or not
         */
        void onItemSelected(int which, boolean isChecked);
    }
}
