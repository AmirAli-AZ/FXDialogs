package com.amirali.fxdialogs;

/**
 * @author Amir Ali
 */

public interface BottomSheetCallBack {
    /**
     * the method is called when bottom sheet state changes
     * @param bottomSheet bottom sheet object
     * @param state bottom sheet state
     */
    void onState(PersistentBottomSheet bottomSheet, int state);

    /**
     * the method is called when bottom sheet dragged and resized
     * @param bottomSheet bottom sheet object
     * @param percent percentage resized
     */
    void onResized(PersistentBottomSheet bottomSheet, int percent);
}
