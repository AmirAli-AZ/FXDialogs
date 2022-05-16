package com.amirali.fxdialogs;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
    private final ObjectProperty<Image> errorIconImageProperty = new SimpleObjectProperty<>(
            new Image(
                    Objects.requireNonNull(getClass().getResourceAsStream("icons/ic_error_64.png"))
            )
    );

    public ExceptionDialog(@NotNull Builder builder) {
        this.builder = builder;
        setupDialog();
    }

    private void setupDialog() {
        setTitle("Exception");

        var pane = getDialogPane();

        pane.setHeader(builder.defaultHeader);
        pane.setExpandableContent(builder.errorDetails);
        builder.errorDetails.requestFocus();
        if (builder.buttonTypes.isEmpty())
            pane.getButtonTypes().add(ButtonType.CLOSE);
        else
            pane.getButtonTypes().addAll(builder.buttonTypes);
        builder.errorIcon.imageProperty().bind(errorIconImageProperty);

        var scene = (Scene) pane.getScene();
        if (builder.styles.isEmpty())
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("themes/default-exception-dialog-theme.css")).toExternalForm());
        else
            scene.getStylesheets().addAll(builder.styles);
    }

    public ObjectProperty<Image> errorIconImageProperty() {
        return errorIconImageProperty;
    }

    public static class Builder {

        // UI components
        private final HBox defaultHeader = new HBox();
        private final Label defaultLabelMessage = new Label();
        private final ImageView errorIcon = new ImageView();
        private final TextArea errorDetails = new TextArea();

        private final List<ButtonType> buttonTypes = new ArrayList<>();
        private final List<String> styles = new ArrayList<>();

        public Builder() {
            errorIcon.setId("error-icon");
            defaultLabelMessage.setId("message");
            errorDetails.setId("error-details");

            errorIcon.setFitWidth(40);
            errorIcon.setFitHeight(40);
            errorDetails.setEditable(false);
            defaultLabelMessage.setWrapText(true);
            HBox.setHgrow(defaultLabelMessage, Priority.ALWAYS);
            defaultLabelMessage.setMaxWidth(Double.MAX_VALUE);
            defaultHeader.setAlignment(Pos.CENTER_LEFT);
            defaultHeader.getChildren().addAll(errorIcon, defaultLabelMessage);
        }

        public Builder setMessage(@NotNull String message) {
            defaultLabelMessage.setText(message);

            return this;
        }

        public Builder setException(@NotNull Throwable exception) {
            var sw = new StringWriter();
            var pw = new PrintWriter(sw);

            exception.printStackTrace(pw);

            errorDetails.setText(sw.toString());

            return this;
        }


        public Builder setErrorIcon(@NotNull Image image) {
            errorIcon.setImage(image);

            return this;
        }

        public Builder addButtons(@NotNull ButtonType... buttonTypes) {
            Collections.addAll(this.buttonTypes, buttonTypes);

            return this;
        }

        public Builder setStyles(String... styles) {
            Collections.addAll(this.styles, styles);

            return this;
        }

        public ExceptionDialog create() {
            return new ExceptionDialog(this);
        }
    }
}
