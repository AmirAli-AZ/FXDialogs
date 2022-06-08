package com.amirali.fxdialogs.notifications;

import com.amirali.fxdialogs.Sounds;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.media.AudioClip;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author Amir Ali
 */

public class PopupNotification extends Popup {

    private NotificationPosition position = NotificationPosition.BOTTOM_RIGHT;
    private String soundPath;
    private Timeline timeline;
    private final ObjectProperty<Insets> marginProperty = new SimpleObjectProperty<>(new Insets(0));
    private final ObjectProperty<Duration> durationProperty = new SimpleObjectProperty<>(Duration.ZERO) {
        @Override
        public void set(Duration duration) {
            super.set(duration);
            timeline = new Timeline(new KeyFrame(duration));
            timeline.setCycleCount(1);
            timeline.setOnFinished(event -> {
                if (isShowing())
                    hide();
            });
            currentTimeProperty.bind(timeline.currentTimeProperty());
        }
    }, currentTimeProperty = new SimpleObjectProperty<>(Duration.ZERO);
    private final StringProperty idProperty = new SimpleStringProperty();
    private final EventHandler<WindowEvent> shownEvent = windowEvent -> {

        if (soundPath != null) {
            var player = new AudioClip(soundPath);
            player.play();
        }
        applyPosition(position, marginProperty.get());
        marginProperty.addListener((observableValue, oldValue, newValue) -> applyPosition(position, newValue));

        if (timeline != null)
            timeline.play();

        try {
            NotificationInfoLogger.save(new NotificationInfo(position, getHeight(), idProperty.get()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }, hiddenEvent = windowEvent -> {
        if (timeline != null && currentTimeProperty.get().lessThan(durationProperty.get()))
            timeline.stop();
        try {
            NotificationInfoLogger.removeIf(new NotificationInfo(position, getHeight(), idProperty.get()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    };

    /**
     * initial notification
     *
     * @param nodes content of the notification
     */
    public PopupNotification(Node... nodes) {
        getContent().addAll(nodes);
        addEventHandler(WindowEvent.WINDOW_SHOWN, shownEvent);
        addEventHandler(WindowEvent.WINDOW_HIDDEN, hiddenEvent);
    }

    /**
     * initial notification
     *
     * @param nodes content of the notification
     * @param duration display duration
     */
    public PopupNotification(@NotNull Duration duration, Node... nodes) {
        getContent().addAll(nodes);
        durationProperty.set(duration);
        addEventHandler(WindowEvent.WINDOW_SHOWN, shownEvent);
        addEventHandler(WindowEvent.WINDOW_HIDDEN, hiddenEvent);
    }

    private void applyPosition(@NotNull NotificationPosition position, @NotNull Insets margin) {
        var visualBounds = Screen.getPrimary().getVisualBounds();
        var notificationsHeight = 0.0;
        try {
            var notificationsInfo = NotificationInfoLogger.getNotificationsInfo();
            if (!notificationsInfo.isEmpty())
                notificationsHeight = notificationsInfo.stream().filter(action -> !action.getId().equals(idProperty.get()) && action.getPosition() == position).mapToDouble(NotificationInfo::getHeight).sum();
        } catch (IOException e) {
            e.printStackTrace();
        }

        switch (position) {
            case BOTTOM_RIGHT -> {
                setX(visualBounds.getMinX() + (visualBounds.getWidth() - getWidth()) - margin.getRight());
                setY((visualBounds.getMinY() + (visualBounds.getHeight() - getHeight()) - margin.getBottom()) - notificationsHeight);
            }

            case BOTTOM_LEFT -> {
                setX(margin.getLeft());
                setY((visualBounds.getMinY() + (visualBounds.getHeight() - getHeight()) - margin.getBottom()) - notificationsHeight);
            }

            case CENTER_BOTTOM -> {
                setX((visualBounds.getWidth() - getWidth()) / 2);
                setY((visualBounds.getMinY() + (visualBounds.getHeight() - getHeight()) - margin.getBottom()) - notificationsHeight);
            }

            case TOP_RIGHT -> {
                setX(visualBounds.getMinX() + (visualBounds.getWidth() - getWidth()) - margin.getRight());
                setY(margin.getTop() + notificationsHeight);
            }

            case TOP_LEFT -> {
                setX(margin.getLeft());
                setY(margin.getTop() + notificationsHeight);
            }

            case CENTER_TOP -> {
                setX((visualBounds.getWidth() - getWidth()) / 2);
                setY(margin.getTop() + notificationsHeight);
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
        soundPath = Objects.requireNonNull(getClass().getResource("/com/amirali/fxdialogs/sounds/" + sound.getFileName())).toExternalForm();
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

    /**
     * sets the notification display duration
     *
     * @param duration display duration
     */
    public void setDuration(@NotNull Duration duration) {
        durationProperty.set(duration);
    }

    /**
     * notification display duration
     *
     * @return Duration
     */
    public Duration getDuration() {
        return durationProperty.get();
    }

    /**
     * notification display duration as a property
     *
     * @return ObjectProperty
     */
    public ObjectProperty<Duration> durationProperty() {
        return durationProperty;
    }

    /**
     * elapsed duration of the total duration
     * <br>
     * returns zero value if timer isn't started yet
     *
     * @return Duration
     */
    public Duration getCurrentTime() {
        return currentTimeProperty.get();
    }

    /**
     * elapsed duration of the total duration as a read only property
     * <br>
     * returns zero value if timer isn't started yet
     *
     * @return ReadOnlyObjectProperty
     */
    public ReadOnlyObjectProperty<Duration> currentTimeProperty() {
        return currentTimeProperty;
    }

    /**
     * sets id of the notification
     *
     * @param id id of the notification
     */
    public void setId(@NotNull String id) {
        idProperty.set(id);
    }

    /**
     * id of the notification
     *
     * @return String
     */
    public String getId() {
        return idProperty.get();
    }

    /**
     * id of the notification as a property
     *
     * @return StringProperty
     */
    public StringProperty idProperty() {
        return idProperty;
    }
}
