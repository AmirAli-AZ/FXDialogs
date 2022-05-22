package com.amirali.fxdialogs;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.media.AudioClip;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author Amir Ali
 */

public class PopupNotification extends Popup {

    private NotificationPosition position = NotificationPosition.BOTTOM_RIGHT;
    private final Location location = new Location();
    private final ObjectProperty<Insets> marginProperty = new SimpleObjectProperty<>(new Insets(0));
    private String soundPath;

    /**
     * initial notification
     *
     * @param nodes content of the notification
     */
    public PopupNotification(Node... nodes) {
        getContent().addAll(nodes);

        addEventHandler(WindowEvent.WINDOW_SHOWN, windowEvent -> {
            if (soundPath != null) {
                var player = new AudioClip(soundPath);
                player.play();
            }
            calculatePosition(position, marginProperty.get());
            marginProperty.addListener((observableValue, oldValue, newValue) -> calculatePosition(position, newValue));
        });
    }

    private void calculatePosition(@NotNull NotificationPosition position, @NotNull Insets padding) {
        var visualBounds = Screen.getPrimary().getVisualBounds();

        switch (position) {
            case BOTTOM_RIGHT -> {
                location.x = visualBounds.getMinX() + (visualBounds.getWidth() - getWidth()) - padding.getRight();
                location.y = visualBounds.getMinY() + (visualBounds.getHeight() - getHeight()) - padding.getBottom();
            }

            case BOTTOM_LEFT -> {
                location.x = padding.getLeft();
                location.y = visualBounds.getMinY() + (visualBounds.getHeight() - getHeight()) - padding.getBottom();
            }

            case CENTER_BOTTOM -> {
                location.x = (visualBounds.getWidth() - getWidth()) / 2;
                location.y = visualBounds.getMinY() + (visualBounds.getHeight() - getHeight()) - padding.getBottom();
            }

            case TOP_RIGHT -> {
                location.x = visualBounds.getMinX() + (visualBounds.getWidth() - getWidth()) - padding.getRight();
                location.y = padding.getTop();
            }

            case TOP_LEFT -> {
                location.x = padding.getLeft();
                location.y = padding.getTop();
            }

            case CENTER_TOP -> {
                location.x = (visualBounds.getWidth() - getWidth()) / 2;
                location.y = padding.getTop();
            }
        }

        setX(location.x);
        setY(location.y);
    }

    /**
     * sets position of the PopupNotification on the screen
     *
     * @param position NotificationPosition
     */
    public void setPosition(@NotNull NotificationPosition position) {
        this.position = position;
    }

    /**
     * sets margin of the notification
     *
     * @param margin Insets
     */
    public void setMargin(@NotNull Insets margin) {
        marginProperty.set(margin);
    }

    /**
     * gets margin of the notification
     *
     * @return Insets
     */
    public Insets getMargin() {
        return marginProperty.get();
    }

    /**
     * margin property
     *
     * @return ObjectProperty
     */
    public ObjectProperty<Insets> marginProperty() {
        return marginProperty;
    }

    /**
     * sets the notification sound from Sounds and plays when notification is shown
     * <br>
     * <b>Note:</b> You need to add <b>javafx.media</b> dependency to use sounds
     *
     * @param sound default notification sounds
     */
    public void setSound(@NotNull Sounds sound) {
        soundPath = Objects.requireNonNull(getClass().getResource("sounds/" + sound.getFileName())).toExternalForm();
    }

    /**
     * sets the notification sound from custom file path and plays when notification is shown
     * <br>
     * <b>Note:</b> You need to add <b>javafx.media</b> dependency to use sounds
     *
     * @param path custom file path
     */
    public void setSound(@NotNull String path) {
        soundPath = Paths.get(path).toUri().toString();
    }

    private static class Location {
        public double x, y;
    }
}
