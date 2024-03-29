package com.amirali.fxdialogs.notifications;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Amir Ali
 */

public class SimplePopupNotification extends PopupNotification {

    private final StringProperty titleProperty = new SimpleStringProperty(), messageProperty = new SimpleStringProperty();
    private final ObjectProperty<Image> closeImageProperty = new SimpleObjectProperty<>(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/amirali/fxdialogs/icons/round_close_black_24dp.png"))));
    private final VBox container = new VBox(5);
    private final ImageView icon = new ImageView();
    private final EventHandler<WindowEvent> shownEvent = windowEvent -> {
        var owner = getOwnerWindow();
        if (owner instanceof Stage ownerStage && !ownerStage.getIcons().isEmpty()) {
            icon.setManaged(true);
            icon.setImage(ownerStage.getIcons().get(0));
        }else {
            icon.setManaged(false);
        }
    };

    /**
     * initial SimplePopupNotification
     */
    public SimplePopupNotification() {
        super(null);

        createContent();
    }

    /**
     * initial SimplePopupNotification
     *
     * @param title   title
     * @param message message
     */
    public SimplePopupNotification(@NotNull String title, @NotNull String message) {
        super(null);

        setTitle(title);
        setMessage(message);

        createContent();
        addEventHandler(WindowEvent.WINDOW_SHOWN, shownEvent);
    }

    /**
     * initial SimplePopupNotification
     *
     * @param duration display duration
     * @param title    title
     * @param message  message
     */
    public SimplePopupNotification(@NotNull Duration duration, @NotNull String title, @NotNull String message) {
        super(duration, null);

        setTitle(title);
        setMessage(message);

        createContent();
        addEventHandler(WindowEvent.WINDOW_SHOWN, shownEvent);
    }

    /**
     * sets title of the notification
     *
     * @param title title
     */
    public void setTitle(@NotNull String title) {
        titleProperty.set(title);
    }

    /**
     * title of the notification
     *
     * @return String
     */
    public String getTitle() {
        return titleProperty.get();
    }

    /**
     * title of the notification as a property
     *
     * @return StringProperty
     */
    public StringProperty titleProperty() {
        return titleProperty;
    }

    /**
     * sets message of the notification
     *
     * @param message message
     */
    public void setMessage(@NotNull String message) {
        messageProperty.set(message);
    }

    /**
     * message of the notification
     *
     * @return String
     */
    public String getMessage() {
        return messageProperty.get();
    }

    /**
     * message of the notification as a property
     *
     * @return StringProperty
     */
    public StringProperty messageProperty() {
        return messageProperty;
    }

    /**
     * stylesheets of the root node
     *
     * @return ObservableList
     */
    public ObservableList<String> getStylesheets() {
        return container.getStylesheets();
    }

    /**
     * close image property of the close button
     *
     * @return ObjectProperty
     */
    public ObjectProperty<Image> closeImageProperty() {
        return closeImageProperty;
    }

    private void createContent() {
        var close = new Button();
        var closeImage = new ImageView();
        var title = new Label();
        var header = new HBox(3, icon, title, close);
        var message = new Label();

        container.getStyleClass().add("simple-notification");
        title.getStyleClass().add("title");
        message.getStyleClass().add("message");
        close.getStyleClass().add("close");

        container.setPrefSize(320, 150);

        icon.setFitWidth(30);
        icon.setFitHeight(30);
        title.textProperty().bind(titleProperty);
        HBox.setHgrow(title, Priority.ALWAYS);
        title.setMaxWidth(Double.MAX_VALUE);
        closeImage.setFitWidth(30);
        closeImage.setFitHeight(30);
        closeImage.imageProperty().bind(closeImageProperty);
        close.setGraphic(closeImage);
        close.setPrefSize(30, 30);
        close.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        close.setOnAction(event -> hide());

        message.setWrapText(true);
        message.textProperty().bind(messageProperty);
        message.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        container.getChildren().addAll(header, message);

        getScene().setRoot(container);
    }
}
