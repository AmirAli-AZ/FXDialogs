package com.amirali.fxdialogs;

import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Amir Ali
 */

public class PersistentBottomSheet extends VBox {

    private final BooleanProperty showingProperty = new SimpleBooleanProperty(true);
    private final ObjectProperty<Image> dragHandlerImageProperty = new SimpleObjectProperty<>(
            new Image(
                    Objects.requireNonNull(getClass().getResourceAsStream("icons/round_horizontal_rule_black_24dp.png"))
            )
    );
    private boolean performingShowHide;
    private boolean firstMouseDrag;
    private double originalHeight;
    private Duration duration = Duration.seconds(1);
    private BottomSheetCallBack callBack;

    /**
     * different states for bottom sheet behaviour
     */
    public static final int COLLAPSED = 604;
    public static final int EXPANDED = 688;
    public static final int DRAGGED = 85;
    public static final int HIDDEN = 744;
    public static final int SHOWN = 451;

    /**
     * sets a listener of the showing property
     */
    public PersistentBottomSheet() {
        showingProperty.addListener((observableValue, oldValue, newValue) -> {
            if (!performingShowHide) {
                if (newValue)
                    show();
                else
                    hide();
            }
        });
    }

    /**
     * sets a listener of the showing property
     * @param spacing vbox spacing between nodes
     */
    public PersistentBottomSheet(double spacing) {
        super(spacing);
        showingProperty.addListener((observableValue, oldValue, newValue) -> {
            if (!performingShowHide) {
                if (newValue)
                    show();
                else
                    hide();
            }
        });
    }

    /**
     * hide the bottom sheet with y-axis transition animation
     * @param duration duration of hide animation
     */
    public void hide(@NotNull Duration duration) {
        var transition = new TranslateTransition(duration, this);
        transition.setToY(getHeight());
        transition.setOnFinished(event -> {
            showingProperty.set(false);
            if (callBack != null)
                callBack.onState(this, HIDDEN);

            performingShowHide = false;
        });
        transition.play();
        performingShowHide = true;
    }

    /**
     * hide the bottom sheet with y-axis transition animation and given duration in setDuration(Duration duration)
     */
    public void hide() {
        hide(duration);
    }

    /**
     * show the bottom sheet with y-axis transition animation
     * @param duration duration of show animation
     */
    public void show(Duration duration) {
        var transition = new TranslateTransition(duration, this);
        transition.setFromY(getHeight());
        transition.setToY(0);
        transition.setOnFinished(event -> {
            showingProperty.set(true);
            if (callBack != null)
                callBack.onState(this, SHOWN);

            performingShowHide = false;
        });
        transition.play();
        performingShowHide = true;
    }

    /**
     * show the bottom sheet with y-axis transition animation and given duration in setDuration(Duration duration)
     */
    public void show() {
        show(duration);
    }

    /**
     * @return showing state of bottom sheet
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
     * @return showing state property
     */
    public BooleanProperty showingProperty() {
        return showingProperty;
    }

    /**
     * adds a stack pane as drag area and an imageview to handle drag event at the top of bottom sheet
     * when dragHandler dragged, it resizes the bottom sheet and pass the states to callBack,
     * the minimum resize also depends on the height of drag area and maximum resize depends on the original height of bottom sheet
     */
    public void addSupportResizing() {
        // DO NOT change min height
        setMinHeight(USE_PREF_SIZE);

        var dragArea = new StackPane();
        var dragHandler = new ImageView();

        dragHandler.setId("dragHandler");
        dragHandler.setPickOnBounds(true);
        dragHandler.setFitHeight(30);
        dragHandler.setFitWidth(30);
        dragHandler.imageProperty().bind(dragHandlerImageProperty);

        dragHandler.setOnMouseDragged(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (!firstMouseDrag) {
                    firstMouseDrag = true;
                    originalHeight = getHeight();
                }

                var newHeight = getHeight() - mouseEvent.getY();

                if (newHeight >= dragArea.getHeight() && newHeight <= originalHeight) {
                    setPrefHeight(newHeight);

                    if (callBack != null) {
                        // return state
                        callBack.onState(this, DRAGGED);

                        if (newHeight == originalHeight)
                            callBack.onState(this, EXPANDED);
                        if (newHeight == dragArea.getHeight())
                            callBack.onState(this, COLLAPSED);

                        // return slide percent

                        callBack.onResized(this, (int) ((newHeight - dragArea.getHeight()) / (originalHeight - dragArea.getHeight()) * 100));
                    }
                }
            }
        });

        dragArea.getChildren().add(dragHandler);
        getChildren().add(0, dragArea);
    }

    /**
     * @return image property of dragHandler
     */
    public ObjectProperty<Image> dragHandlerImageProperty() {
        return dragHandlerImageProperty;
    }

    /**
     * sets the default duration of hide and show animations
     * @param duration default duration of hide and show animations
     */
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    /**
     * @return default show and hide animations duration
     */
    public Duration getDuration() {
        return duration;
    }

    /**
     * sets a callBack for bottom sheet states
     * @param callBack bottom sheet states callBack
     */
    public void setCallBack(BottomSheetCallBack callBack) {
        this.callBack = callBack;
    }

    /**
     * @return bottom sheet states callBack
     */
    public BottomSheetCallBack getCallBack() {
        return callBack;
    }
}
