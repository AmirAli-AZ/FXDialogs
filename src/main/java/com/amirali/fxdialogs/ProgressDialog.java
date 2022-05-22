package com.amirali.fxdialogs;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Amir Ali
 */

public final class ProgressDialog extends Stage {

    private final Builder builder;

    /**
     * @param builder ProgressDialog builder
     */
    public ProgressDialog(@NotNull Builder builder) {
        this.builder = builder;
        setupDialog();
    }

    private void setupDialog() {
        var scene = new Scene(builder.container);
        if (builder.styles.isEmpty()) {
            scene.getStylesheets().add(
                    Objects.requireNonNull(
                            getClass().getResource("themes/default-progress-dialog-theme.css")
                    ).toExternalForm()
            );
        }else {
            scene.getStylesheets().addAll(builder.styles);
        }
        setScene(scene);
    }

    /**
     * sets progress of the ProgressDialog
     * @param value new progress value
     */
    public void setProgress(double value) {
        builder.progressProperty.set(value);
    }

    /**
     * progress value of the ProgressDialog
     * @return double
     */
    public double getProgress() {
        return builder.progressProperty.get();
    }

    /**
     * DoubleProperty of the ProgressDialog progress
     * @return DoubleProperty
     */
    public DoubleProperty getProgressProperty() {
        return builder.progressProperty;
    }

    /**
     * sets title of the dialog
     * @param title title of the ProgressDialog
     */
    public void setDialogTitle(@NotNull String title) {
        builder.dialogTitleProperty.set(title);
    }

    /**
     * title of the ProgressDialog
     * @return String
     */
    public String getDialogTitle() {
        return builder.dialogTitleProperty.get();
    }

    /**
     * StringProperty of the ProgressDialog title
     * @return StringProperty
     */
    public StringProperty dialogTitleProperty() {
        return builder.dialogTitleProperty;
    }

    /**
     * sets message of the ProgressDialog
     * @param message message of the ProgressDialog
     */
    public void setDialogMessage(@NotNull String message) {
        builder.dialogMessageProperty.set(message);
    }

    /**
     * message of the ProgressDialog
     * @return String
     */
    public String getDialogMessage() {
        return builder.dialogMessageProperty.get();
    }

    /**
     * StringProperty of the ProgressDialog message
     * @return StringProperty
     */
    public StringProperty dialogMessageProperty() {
        return builder.dialogMessageProperty;
    }

    /**
     * returns progress type of the ProgressDialog that can be Bar or Indicator
     * @return ProgressBarType
     */
    public ProgressBarType getProgressType() {
        return builder.type;
    }

    /**
     * ProgressDialog builder
     */
    public static class Builder {

        // UI components
        private final BorderPane container = new BorderPane();
        private final Label title = new Label();
        private final Label message = new Label();
        private final HBox center = new HBox(3);
        private final VBox top = new VBox(5);
        private final ProgressBar progressBar = new ProgressBar();
        private final ProgressIndicator progressIndicator = new ProgressIndicator();

        private boolean isProgressAdded, isTitleAdded, isMessageAdded;
        // default progress type is Bar
        private ProgressBarType type = ProgressBarType.Bar;
        private final List<String> styles = new ArrayList<>();
        private final StringProperty dialogTitleProperty = new SimpleStringProperty(), dialogMessageProperty = new SimpleStringProperty();
        private final DoubleProperty progressProperty = new SimpleDoubleProperty();
        private ProgressDialog progressDialog;

        /**
         * creates initial layout
         */
        public Builder() {
            // init

            title.setId("title");
            title.setWrapText(true);
            title.setMaxWidth(Double.MAX_VALUE);
            title.textProperty().bind(dialogTitleProperty);
            message.setId("message");
            message.setWrapText(true);
            message.setMaxWidth(Double.MAX_VALUE);
            message.textProperty().bind(dialogMessageProperty);
            center.setPadding(new Insets(5));
            center.setAlignment(Pos.CENTER);
            top.setPadding(new Insets(10));
            progressBar.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(progressBar, Priority.ALWAYS);
            progressBar.progressProperty().bind(progressProperty);
            progressIndicator.progressProperty().bind(progressProperty);
            container.setCenter(center);
            container.setTop(top);

            dialogTitleProperty.addListener((observableValue, oldValue, newValue) -> {
                if (!isTitleAdded) {
                    top.getChildren().add(0, title);
                    isTitleAdded = true;

                    if (progressDialog != null)
                        progressDialog.sizeToScene();
                }
            });

            dialogMessageProperty.addListener((observableValue, oldValue, newValue) -> {
                if (!isMessageAdded) {
                    top.getChildren().add(message);
                    isMessageAdded = true;

                    if (progressDialog != null)
                        progressDialog.sizeToScene();
                }
            });
        }

        /**
         * sets progress type of the ProgressDialog that can be Bar or Indicator
         * @param type progress type
         * @return Builder
         */
        public Builder setProgressType(@NotNull ProgressBarType type) {
            if (!isProgressAdded) {
                this.type = type;

                if (type == ProgressBarType.Bar) {
                    progressBar.setId("progressBar");
                    center.getChildren().add(progressBar);
                }else {
                    progressIndicator.setId("progressIndicator");
                    center.getChildren().add(progressIndicator);
                }
                isProgressAdded = true;
            }

            return this;
        }

        /**
         * sets progress of the ProgressDialog
         * @param value new progress value
         * @return Builder
         */
        public Builder setProgress(double value) {
            progressProperty.set(value);

            return this;
        }

        /**
         * sets title of the ProgressDialog
         * @param title title of the ProgressDialog
         * @return Builder
         */
        public Builder setDialogTitle(@NotNull String title) {
            dialogTitleProperty.set(title);

            return this;
        }

        /**
         * sets message of the ProgressDialog
         * @param message message of the ProgressDialog
         * @return Builder
         */
        public Builder setDialogMessage(@NotNull String message) {
            dialogMessageProperty.set(message);

            return this;
        }

        /**
         * adds styles to style list and that list will be added to the scene
         * @param styles dialog styles
         * @return Builder
         */
        public Builder setStyles(String... styles) {
            Collections.addAll(this.styles, styles);

            return this;
        }

        /**
         * creates ProgressDialog
         * @return ProgressDialog
         */
        public ProgressDialog create() {
            progressDialog = new ProgressDialog(this);
            return progressDialog;
        }
    }

    /**
     * progress types that can be Bar or Indicator
     */
    public enum ProgressBarType {
        Indicator,Bar
    }
}
