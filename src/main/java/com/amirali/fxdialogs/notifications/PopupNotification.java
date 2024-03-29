package com.amirali.fxdialogs.notifications;

import com.amirali.fxdialogs.Sounds;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.PopupControl;
import javafx.scene.media.AudioClip;
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

public class PopupNotification extends PopupControl {

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
    private final StringProperty notificationIdProperty = new SimpleStringProperty();
    private final EventHandler<WindowEvent> shownEvent = windowEvent -> {
        if (soundPath != null) {
            var player = new AudioClip(soundPath);
            player.play();
        }
        applyPosition();
        marginProperty.addListener((observableValue, oldValue, newValue) -> applyPosition());

        if (timeline != null)
            timeline.play();

        try {
            NotificationInfoLogger.save(new NotificationInfo(position, getNotificationId(), getHeight()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }, hiddenEvent = windowEvent -> {
        if (timeline != null && currentTimeProperty.get().lessThan(durationProperty.get()))
            timeline.stop();
        try {
            NotificationInfoLogger.removeIf(new NotificationInfo(position, getNotificationId(), getHeight()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    };

    /**
     * initial notification
     *
     * @param root root of the notification
     */
    public PopupNotification(Parent root) {
        if (root != null)
            getScene().setRoot(root);
        setAutoHide(true);
        addEventHandler(WindowEvent.WINDOW_SHOWN, shownEvent);
        addEventHandler(WindowEvent.WINDOW_HIDDEN, hiddenEvent);
    }

    /**
     * initial notification
     *
     * @param root root of the notification
     * @param duration display duration
     */
    public PopupNotification(@NotNull Duration duration, Parent root) {
        if (root != null)
            getScene().setRoot(root);
        setDuration(duration);
        setAutoHide(true);
        addEventHandler(WindowEvent.WINDOW_SHOWN, shownEvent);
        addEventHandler(WindowEvent.WINDOW_HIDDEN, hiddenEvent);
    }

    private void applyPosition() {
        var delta = calculatePosition();
        setX(delta.x());
        setY(delta.y());
    }

    /**
     * calculates notification position
     *
     * @return Delta
     */
    public Delta calculatePosition() {
        var visualBounds = Screen.getPrimary().getVisualBounds();
        var notificationsHeight = 0.0;
        try {
            var notificationsInfo = NotificationInfoLogger.getNotificationsInfo();
            if (!notificationsInfo.isEmpty())
                notificationsHeight = notificationsInfo.stream().filter(
                        action -> !action.id().equals(getNotificationId()) && action.position() == getPosition()
                ).mapToDouble(NotificationInfo::height).sum();
        } catch (IOException e) {
            e.printStackTrace();
        }
        var margin = getMargin();

        return switch (position) {
            case BOTTOM_RIGHT -> new Delta(
                    visualBounds.getMinX() + (visualBounds.getWidth() - getWidth()) - margin.getRight(),
                    (visualBounds.getMinY() + (visualBounds.getHeight() - getHeight()) - margin.getBottom()) - notificationsHeight
            );

            case BOTTOM_LEFT -> new Delta(
                    margin.getLeft(),
                    (visualBounds.getMinY() + (visualBounds.getHeight() - getHeight()) - margin.getBottom()) - notificationsHeight
            );

            case CENTER_BOTTOM -> new Delta(
                (visualBounds.getWidth() - getWidth()) / 2,
                (visualBounds.getMinY() + (visualBounds.getHeight() - getHeight()) - margin.getBottom()) - notificationsHeight
            );

            case TOP_RIGHT -> new Delta(
                visualBounds.getMinX() + (visualBounds.getWidth() - getWidth()) - margin.getRight(),
                margin.getTop() + notificationsHeight
            );

            case TOP_LEFT -> new Delta(
                margin.getLeft(),
                margin.getTop() + notificationsHeight
            );

            case CENTER_TOP -> new Delta(
                (visualBounds.getWidth() - getWidth()) / 2,
                margin.getTop() + notificationsHeight
            );
        };
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
     * returns current position of PopupNotification on the screen
     *
     * @return NotificationPosition
     */
    public NotificationPosition getPosition() {
        return position;
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
    public void setNotificationId(@NotNull String id) {
        notificationIdProperty.set(id);
    }

    /**
     * id of the notification
     *
     * @return String
     */
    public String getNotificationId() {
        return notificationIdProperty.get();
    }

    /**
     * id of the notification as a property
     *
     * @return StringProperty
     */
    public StringProperty notificationIdProperty() {
        return notificationIdProperty;
    }
}
