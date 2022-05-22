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

    private void calculatePosition(@NotNull NotificationPosition position, @NotNull Insets margin) {
        var visualBounds = Screen.getPrimary().getVisualBounds();

        switch (position) {
            case BOTTOM_RIGHT -> {
                setX(visualBounds.getMinX() + (visualBounds.getWidth() - getWidth()) - margin.getRight());
                setY(visualBounds.getMinY() + (visualBounds.getHeight() - getHeight()) - margin.getBottom());
            }

            case BOTTOM_LEFT -> {
                setX(margin.getLeft());
                setY(visualBounds.getMinY() + (visualBounds.getHeight() - getHeight()) - margin.getBottom());
            }

            case CENTER_BOTTOM -> {
                setX((visualBounds.getWidth() - getWidth()) / 2);
                setY(visualBounds.getMinY() + (visualBounds.getHeight() - getHeight()) - margin.getBottom());
            }

            case TOP_RIGHT -> {
                setX(visualBounds.getMinX() + (visualBounds.getWidth() - getWidth()) - margin.getRight());
                setY(margin.getTop());
            }

            case TOP_LEFT -> {
                setX(margin.getLeft());
                setY(margin.getTop());
            }

            case CENTER_TOP -> {
                setX((visualBounds.getWidth() - getWidth()) / 2);
                setY(margin.getTop());
            }
        }
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
}
