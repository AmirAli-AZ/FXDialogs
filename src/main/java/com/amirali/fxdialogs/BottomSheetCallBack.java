package com.amirali.fxdialogs;

public interface BottomSheetCallBack {
    void onState(PersistentBottomSheet bottomSheet, int state);
    void onSlide(PersistentBottomSheet bottomSheet, int percent);
}
