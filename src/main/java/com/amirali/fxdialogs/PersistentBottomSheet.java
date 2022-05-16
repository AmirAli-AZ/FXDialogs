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

import java.util.Objects;

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

    public PersistentBottomSheet(double spacing) {
        super(spacing);
    }

    public void hide(Duration duration) {
        var transition = new TranslateTransition(duration, this);
        transition.setToY(getHeight());
        transition.setOnFinished(event -> {
            showingProperty.set(false);
            performingShowHide = false;
        });
        transition.play();
        performingShowHide = true;
    }

    public void hide() {
        hide(Duration.seconds(1));
    }

    public void show(Duration duration) {
        var transition = new TranslateTransition(duration, this);
        transition.setFromY(getHeight());
        transition.setToY(0);
        transition.setOnFinished(event -> {
            showingProperty.set(true);
            performingShowHide = false;
        });
        transition.play();
        performingShowHide = true;
    }

    public void show() {
        show(Duration.seconds(1));
    }

    public boolean isShowing() {
        return showingProperty.get();
    }

    public void setShowing(boolean showing) {
        showingProperty.set(showing);
    }

    public BooleanProperty showingProperty() {
        return showingProperty;
    }

    public void addSupportResizing() {
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

                if (newHeight >= dragArea.getHeight() && newHeight < originalHeight)
                    setPrefHeight(newHeight);
            }
        });

        dragArea.getChildren().add(dragHandler);
        getChildren().add(0, dragArea);
    }

    public ObjectProperty<Image> dragHandlerImageProperty() {
        return dragHandlerImageProperty;
    }
}
