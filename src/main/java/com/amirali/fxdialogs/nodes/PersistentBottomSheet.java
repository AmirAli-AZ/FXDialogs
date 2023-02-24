package com.amirali.fxdialogs.nodes;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

/**
 * @author Amir Ali
 */

public class PersistentBottomSheet extends VBox {

    private TranslateTransition transition;

    private final BooleanProperty showingProperty = new SimpleBooleanProperty(true) {
        @Override
        public void set(boolean b) {
            super.set(b);
            if (transition != null && transition.getStatus() == Animation.Status.RUNNING) {
                super.set(!b);
                return;
            }
            transition = new TranslateTransition(getDuration(), PersistentBottomSheet.this);
            transition.setInterpolator(Interpolator.EASE_BOTH);
            transition.setOnFinished(event -> {
                if (callBack != null)
                    callBack.onState(PersistentBottomSheet.this, b ? SHOWN : HIDDEN);
            });
            if (b) {
                transition.setFromY(getHeight());
                transition.setToY(0);
            }else {
                transition.setToY(getHeight());
            }
            transition.play();
        }
    };
    private final ObjectProperty<Duration> durationProperty = new SimpleObjectProperty<>(Duration.seconds(1));
    private double previousHeight;
    private BottomSheetCallBack callBack;

    /**
     * when bottom sheet is collapsed
     */
    public static final int COLLAPSING = 604;
    /**
     * when bottom sheet is expanded
     */
    public static final int EXPANDING = 688;
    /**
     * when bottom sheet is dragged by mouse
     */
    public static final int DRAGGED = 85;
    /**
     * when bottom sheet is hidden
     */
    public static final int HIDDEN = 744;
    /**
     * when bottom sheet is shown
     */
    public static final int SHOWN = 451;

    /**
     * default constructor
     */
    public PersistentBottomSheet() {
        super();
        getStyleClass().add("persistent-bottomSheet");
    }

    /**
     * @param nodes nodes of PersistentBottomSheet
     */
    public PersistentBottomSheet(Node... nodes) {
        super(nodes);
        getStyleClass().add("persistent-bottomSheet");
    }

    /**
     * @param spacing vbox spacing between nodes
     */
    public PersistentBottomSheet(double spacing) {
        super(spacing);
        getStyleClass().add("persistent-bottomSheet");
    }

    /**
     * @param spacing vbox spacing between nodes
     * @param nodes nodes of PersistentBottomSheet
     */
    public PersistentBottomSheet(double spacing, Node... nodes) {
        super(spacing, nodes);
        getStyleClass().add("persistent-bottomSheet");
    }

    /**
     * hides the bottom sheet with y-axis transition animation
     */
    public void hide() {
        setShowing(false);
    }

    /**
     * show the bottom sheet with y-axis transition animation
     */
    public void show() {
        setShowing(true);
    }

    /**
     * showing state of bottom sheet
     * @return boolean
     */
    public boolean isShowing() {
        return showingProperty.get();
    }

    /**
     * set showing state to hide or show the bottom sheet
     * @param showing showing state
     */
    public void setShowing(boolean showing) {
        showingProperty.set(showing);
    }

    /**
     * showing state property
     * @return BooleanProperty
     */
    public BooleanProperty showingProperty() {
        return showingProperty;
    }

    /**
     * adds resizing support when mouse is dragged
     */
    public void addResizingSupport() {
        setOnMouseDragged(mouseEvent -> {
            if (mouseEvent.getButton() != MouseButton.PRIMARY)
                return;

            var newHeight = getHeight() - mouseEvent.getY();
            if (newHeight < 0)
                return;
            setPrefHeight(newHeight);

            if (callBack != null) {
                // return state
                callBack.onState(this, DRAGGED);

                if (newHeight > previousHeight)
                    callBack.onState(this, EXPANDING);
                if (newHeight < previousHeight)
                    callBack.onState(this, COLLAPSING);

                callBack.onResized(this, newHeight);
            }

            previousHeight = newHeight;
        });
    }

    /**
     * sets the default duration of hide and show animations
     * @param duration default duration of hide and show animations
     */
    public void setDuration(@NotNull Duration duration) {
        durationProperty.set(duration);
    }

    /**
     * default show and hide animations duration
     * @return Duration
     */
    public Duration getDuration() {
        return durationProperty.get();
    }

    /**
     * default show and hide animations duration as a property
     *
     * @return ObjectProperty
     */
    public ObjectProperty<Duration> durationProperty() {
        return durationProperty;
    }

    /**
     * sets a callBack for bottom sheet states
     * @param callBack bottom sheet states callBack
     */
    public void setCallBack(@NotNull BottomSheetCallBack callBack) {
        this.callBack = callBack;
    }

    /**
     * bottom sheet states callBack
     * @return BottomSheetCallBack
     */
    public BottomSheetCallBack getCallBack() {
        return callBack;
    }
}
