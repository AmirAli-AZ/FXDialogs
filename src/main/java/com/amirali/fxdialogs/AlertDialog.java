package com.amirali.fxdialogs;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class AlertDialog extends Stage {

    public AlertDialog(@NotNull Builder builder) {
        setupDialog(builder);
    }

    private void setupDialog(@NotNull Builder builder) {
        var scene = new Scene(builder.container);
        if (builder.styles.isEmpty()) {
            scene.getStylesheets().add(
                    Objects.requireNonNull(
                            getClass().getResource("themes/default-alert-dialog-theme.css")
                    ).toExternalForm()
            );
        } else {
            scene.getStylesheets().addAll(builder.styles);
        }
        setScene(scene);
        setTitle(builder.title.getText());

        // play audio when stage is shown

        if (builder.soundPath != null) {
            addEventHandler(WindowEvent.WINDOW_SHOWN, windowEvent -> {
                var player = new AudioClip(builder.soundPath);
                player.play();
            });
        }
    }

    public static class Builder {

        // UI components
        private final Label title = new Label();
        private final Label message = new Label();
        private final Button positiveButton = new Button();
        private final Button negativeButton = new Button();
        private final Button naturalButton = new Button();
        private final ButtonBar buttons = new ButtonBar();
        private final BorderPane container = new BorderPane();
        private final VBox center = new VBox(3);
        private final VBox top = new VBox(5);
        private final ToggleGroup toggleGroup = new ToggleGroup();
        private final VBox radioButtonsContainer = new VBox(3);
        private final VBox checkBoxesContainer = new VBox(3);

        private boolean isPositiveButtonAdded,
                isNegativeButtonAdded,
                isMessageAdded,
                isTitleAdded,
                isRadioButtonsContainerAdded,
                isCheckBoxesContainerAdded,
                isNaturalButtonAdded,
                isCustomNode;
        private final List<DialogInterface.OnClickListener> positiveListeners = new ArrayList<>(),
                negativeListeners = new ArrayList<>(),
                naturalListeners = new ArrayList<>();
        private final List<String> styles = new ArrayList<>();
        private String soundPath;

        private AlertDialog dialog;

        public Builder() {
            // init

            message.setId("message");
            message.setWrapText(true);
            title.setId("title");
            positiveButton.setId("positive-button");
            positiveButton.setDefaultButton(true);
            negativeButton.setId("negative-button");
            naturalButton.setId("natural-button");
            ButtonBar.setButtonData(naturalButton, ButtonBar.ButtonData.LEFT);
            buttons.setPadding(new Insets(10));
            radioButtonsContainer.setPadding(new Insets(10));
            checkBoxesContainer.setPadding(new Insets(10));
            center.setPrefWidth(400);
            center.setPadding(new Insets(10));
            top.setPadding(new Insets(10));
            top.setPrefWidth(400);

            container.setTop(top);
            container.setCenter(center);
            container.setBottom(buttons);
        }

        public Builder setTitle(String title) {
            this.title.setText(title);
            if (!isTitleAdded) {
                top.getChildren().add(0, this.title);

                isTitleAdded = true;
            }
            return this;
        }

        public Builder setMessage(String message) {
            this.message.setText(message);
            if (!isMessageAdded) {
                top.getChildren().add(this.message);

                isMessageAdded = true;
            }
            return this;
        }

        public Builder setPositiveButton(String text, @NotNull DialogInterface.OnClickListener listener) {
            positiveListeners.add(listener);

            positiveButton.setText(text);
            positiveButton.setOnAction(event -> {
                for (DialogInterface.OnClickListener clickListener : positiveListeners)
                    clickListener.onClick(DialogInterface.POSITIVE_BUTTON);
                dialog.close();
            });

            if (!isPositiveButtonAdded) {
                buttons.getButtons().add(positiveButton);
                isPositiveButtonAdded = true;
            }

            return this;
        }

        public Builder setNegativeButton(String text, @NotNull DialogInterface.OnClickListener listener) {
            negativeListeners.add(listener);

            negativeButton.setText(text);
            negativeButton.setOnAction(event -> {
                for (DialogInterface.OnClickListener clickListener : negativeListeners)
                    clickListener.onClick(DialogInterface.NEGATIVE_BUTTON);
                dialog.close();
            });

            if (!isNegativeButtonAdded) {
                buttons.getButtons().add(negativeButton);
                isNegativeButtonAdded = true;
            }

            return this;
        }

        public Builder setNaturalButton(String text, @NotNull DialogInterface.OnClickListener listener) {
            naturalListeners.add(listener);

            naturalButton.setText(text);
            naturalButton.setOnAction(event -> {
                for (DialogInterface.OnClickListener clickListener : naturalListeners)
                    clickListener.onClick(DialogInterface.NATURAL_BUTTON);
                dialog.close();
            });

            if (!isNaturalButtonAdded) {
                buttons.getButtons().add(naturalButton);
                isNaturalButtonAdded = true;
            }

            return this;
        }

        public Builder addStyles(@NotNull String... styles) {
            Collections.addAll(this.styles, styles);

            return this;
        }

        public Builder setSingleChoiceItems(@NotNull String[] items, int selectedIndex, @NotNull DialogInterface.OnSingleChoiceSelectedListener listener) {
            if (isCheckBoxesContainerAdded || isCustomNode)
                return this;

            if (isRadioButtonsContainerAdded)
                radioButtonsContainer.getChildren().clear();
            for (int i = 0; i < items.length; i++) {
                var radioButton = new RadioButton(items[i]);
                radioButton.setToggleGroup(toggleGroup);
                radioButton.setSelected(selectedIndex != -1 && i == selectedIndex);

                radioButtonsContainer.getChildren().add(radioButton);
            }
            if (!isRadioButtonsContainerAdded) {
                center.getChildren().add(0, radioButtonsContainer);
                isRadioButtonsContainerAdded = true;
            }
            toggleGroup.selectedToggleProperty().addListener((observableValue, oldToggle, newToggle) -> {
                var index = radioButtonsContainer.getChildren().indexOf(newToggle);
                listener.onItemSelected(index);
            });

            return this;
        }

        public Builder setSingleChoiceItems(@NotNull String[] items, @NotNull DialogInterface.OnSingleChoiceSelectedListener listener) {
            return setSingleChoiceItems(items, -1, listener);
        }

        public Builder setMultiChoiceItems(@NotNull String[] items, @NotNull Integer[] selectedIndexes, @NotNull DialogInterface.OnMultiChoiceSelectedListener listener) {
            if (isRadioButtonsContainerAdded || isCustomNode)
                return this;

            if (isCheckBoxesContainerAdded)
                checkBoxesContainer.getChildren().clear();
            for (int i = 0; i < items.length; i++) {
                var checkBox = new CheckBox(items[i]);
                checkBox.setSelected(selectedIndexes.length > i && i == selectedIndexes[i]);
                int finalI = i;
                checkBox.selectedProperty().addListener((observableValue, oldValue, newValue) -> listener.onItemSelected(finalI, newValue));

                checkBoxesContainer.getChildren().add(checkBox);
            }
            if (!isCheckBoxesContainerAdded) {
                center.getChildren().add(0, checkBoxesContainer);
                isCheckBoxesContainerAdded = true;
            }

            return this;
        }

        public Builder setMultiChoiceItems(@NotNull String[] items, @NotNull DialogInterface.OnMultiChoiceSelectedListener listener) {
            return setMultiChoiceItems(items, new Integer[]{}, listener);
        }

        public Builder setNode(@NotNull Node node) {
            container.setCenter(node);
            isCustomNode = true;

            return this;
        }

        public Builder setSound(@NotNull Sounds sound) {
            soundPath = Objects.requireNonNull(getClass().getResource("sounds/" + sound.getPath())).toExternalForm();

            return this;
        }

        public Builder setSound(@NotNull String path) {
            soundPath = path;

            return this;
        }

        public AlertDialog create() {
            dialog = new AlertDialog(this);
            return dialog;
        }
    }
}
