package com.amirali.fxdialogs;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class ExceptionDialog extends Dialog<ButtonType> {

    private final Builder builder;

    public ExceptionDialog(@NotNull Builder builder) {
        this.builder = builder;
        setupDialog();
    }

    private void setupDialog() {
        var pane = getDialogPane();

        pane.setHeader(builder.defaultHeader);
        pane.setExpandableContent(builder.errorDetails);
        builder.errorDetails.requestFocus();
        if (builder.buttonTypes.isEmpty())
            pane.getButtonTypes().add(ButtonType.CLOSE);
        else
            pane.getButtonTypes().addAll(builder.buttonTypes);

        var scene = (Scene) pane.getScene();
        if (builder.styles.isEmpty())
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("themes/default-exception-dialog-theme.css")).toExternalForm());
        else
            scene.getStylesheets().addAll(builder.styles);
    }

    public ObjectProperty<Image> errorIconImageProperty() {
        return builder.errorIconImageProperty;
    }

    public void setDialogMessage(@NotNull String message) {
        builder.dialogMessageProperty.set(message);
    }

    public String getDialogMessage() {
        return builder.dialogMessageProperty.get();
    }

    public StringProperty dialogMessageProperty() {
        return builder.dialogMessageProperty;
    }

    public void setException(@NotNull Throwable exception) {
        builder.exceptionProperty.set(exception);
    }

    public Throwable getException() {
        return builder.exceptionProperty.get();
    }

    public ObjectProperty<Throwable> exceptionProperty() {
        return builder.exceptionProperty;
    }


    public static class Builder {

        // UI components
        private final HBox defaultHeader = new HBox();
        private final TextArea errorDetails = new TextArea();

        private final List<ButtonType> buttonTypes = new ArrayList<>();
        private final List<String> styles = new ArrayList<>();
        private final ObjectProperty<Image> errorIconImageProperty = new SimpleObjectProperty<>(
                new Image(
                        Objects.requireNonNull(getClass().getResourceAsStream("icons/ic_error_64.png"))
                )
        );
        private final ObjectProperty<Throwable> exceptionProperty = new SimpleObjectProperty<>();
        private final StringProperty dialogMessageProperty = new SimpleStringProperty();

        public Builder() {
            ImageView errorIcon = new ImageView();
            errorIcon.setId("error-icon");
            errorIcon.setFitWidth(40);
            errorIcon.setFitHeight(40);
            errorIcon.imageProperty().bind(errorIconImageProperty);

            Label defaultLabelMessage = new Label();
            defaultLabelMessage.setId("message");
            defaultLabelMessage.setWrapText(true);
            HBox.setHgrow(defaultLabelMessage, Priority.ALWAYS);
            defaultLabelMessage.setMaxWidth(Double.MAX_VALUE);
            defaultLabelMessage.textProperty().bind(dialogMessageProperty);

            errorDetails.setId("error-details");
            errorDetails.setEditable(false);

            defaultHeader.setAlignment(Pos.CENTER_LEFT);
            defaultHeader.getChildren().addAll(errorIcon, defaultLabelMessage);

            exceptionProperty.addListener((observableValue, oldValue, newValue) -> {
                var sw = new StringWriter();
                var pw = new PrintWriter(sw);

                newValue.printStackTrace(pw);

                errorDetails.setText(sw.toString());
            });
        }

        public Builder setDialogMessage(@NotNull String message) {
            dialogMessageProperty.set(message);

            return this;
        }

        public Builder setException(@NotNull Throwable exception) {
            exceptionProperty.set(exception);

            return this;
        }

        public Builder addButtons(@NotNull ButtonType... buttonTypes) {
            Collections.addAll(this.buttonTypes, buttonTypes);

            return this;
        }

        public Builder setStyles(@NotNull String... styles) {
            Collections.addAll(this.styles, styles);

            return this;
        }

        public ExceptionDialog create() {
            return new ExceptionDialog(this);
        }
    }
}
