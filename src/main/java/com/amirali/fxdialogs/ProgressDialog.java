package com.amirali.fxdialogs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class ProgressDialog extends Stage {

    private final Builder builder;

    public ProgressDialog(@NotNull Builder builder) {
        this.builder = builder;
        setupDialog();
    }

    private void setupDialog() {
        if (builder.isTitleAdded)
            setTitle(builder.title.getText());
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

    public void setProgress(double value) {
        if (builder.isProgressAdded) {
            if (builder.type == ProgressBarType.Bar)
                builder.progressBar.setProgress(value);
            else
                builder.progressIndicator.setProgress(value);
        }
    }

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

        public Builder() {
            // init

            title.setId("title");
            title.setWrapText(true);
            title.setMaxWidth(Double.MAX_VALUE);
            message.setId("message");
            message.setWrapText(true);
            message.setMaxWidth(Double.MAX_VALUE);
            center.setPadding(new Insets(5));
            center.setAlignment(Pos.CENTER);
            top.setPadding(new Insets(10));
            progressBar.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(progressBar, Priority.ALWAYS);
            container.setPrefWidth(200);
            container.setCenter(center);
            container.setTop(top);
        }

        public Builder setProgressBar(@NotNull ProgressBarType type) {
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

        public Builder setProgress(double value) {
            if (isProgressAdded) {
                if (type == ProgressBarType.Bar)
                    progressBar.setProgress(value);
                else
                    progressIndicator.setProgress(value);
            }

            return this;
        }

        public Builder setTitle(@NotNull String title) {
            if (!isTitleAdded) {
                top.getChildren().add(0, this.title);
                isTitleAdded = true;
            }
            this.title.setText(title);

            return this;
        }

        public Builder setMessage(@NotNull String message) {
            if (!isMessageAdded) {
                top.getChildren().add(this.message);
                isMessageAdded = true;
            }
            this.message.setText(message);

            return this;
        }

        public Builder setStyles(@NotNull String... styles) {
            Collections.addAll(this.styles, styles);

            return this;
        }

        public ProgressDialog create() {
            return new ProgressDialog(this);
        }
    }

    public enum ProgressBarType {
        Indicator,Bar
    }
}
