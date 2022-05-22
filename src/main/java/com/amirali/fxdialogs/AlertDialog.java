package com.amirali.fxdialogs;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Amir Ali
 */

public final class AlertDialog extends Stage {

    private final Builder builder;

    /**
     * @param builder AlertDialog builder
     */
    public AlertDialog(@NotNull Builder builder) {
        this.builder = builder;
        setupDialog();
    }

    private void setupDialog() {
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


        addEventHandler(WindowEvent.WINDOW_SHOWN, windowEvent -> {
            if (builder.soundPath != null) {
                var player = new AudioClip(builder.soundPath);
                player.play();
            }
        });
    }

    /**
     * sets title of the AlertDialog
     *
     * @param title title of the AlertDialog
     */
    public void setDialogTitle(@NotNull String title) {
        builder.dialogTitleProperty.set(title);
    }

    /**
     * @return title of the AlertDialog
     */
    public String getDialogTitle() {
        return builder.dialogTitleProperty.get();
    }

    /**
     * StringProperty of AlertDialog title
     *
     * @return StringProperty
     */
    public StringProperty dialogTitleProperty() {
        return builder.dialogTitleProperty;
    }

    /**
     * sets message of the AlertDialog
     *
     * @param message message of the AlertDialog
     */
    public void setDialogMessage(@NotNull String message) {
        builder.dialogMessageProperty.set(message);
    }

    /**
     * @return message of the AlertDialog
     */
    public String getDialogMessage() {
        return builder.dialogMessageProperty.get();
    }

    /**
     * StringProperty of AlertDialog title
     *
     * @return StringProperty
     */
    public StringProperty dialogMessageProperty() {
        return builder.dialogMessageProperty;
    }

    /**
     * sets the notification sound from Sounds and plays when stage is shown
     * <br>
     * <b>Note:</b> You need to add <b>javafx.media</b> dependency to use sounds
     *
     * @param sound default notification sounds
     */
    public void setSound(@NotNull Sounds sound) {
        builder.soundPath = Objects.requireNonNull(getClass().getResource("sounds/" + sound.getFileName())).toExternalForm();
    }

    /**
     * sets the notification sound from custom file path and plays when stage is shown
     * <br>
     * <b>Note:</b> You need to add <b>javafx.media</b> dependency to use sounds
     *
     * @param path custom file path
     */
    public void setSound(@NotNull String path) {
        builder.soundPath = Paths.get(path).toUri().toString();
    }

    /**
     * AlertDialog builder
     */
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
        private final StringProperty dialogTitleProperty = new SimpleStringProperty(), dialogMessageProperty = new SimpleStringProperty();

        /**
         * create initial layout
         */
        public Builder() {
            // init

            message.setId("message");
            message.setWrapText(true);
            message.setMaxWidth(Double.MAX_VALUE);
            message.textProperty().bind(dialogMessageProperty);

            title.setId("title");
            title.setWrapText(true);
            title.setMaxWidth(Double.MAX_VALUE);
            title.textProperty().bind(dialogTitleProperty);

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

            dialogTitleProperty.addListener((observableValue, oldValue, newValue) -> {
                if (!isTitleAdded) {
                    top.getChildren().add(0, this.title);

                    isTitleAdded = true;

                    if (dialog != null)
                        dialog.sizeToScene();
                }
            });

            dialogMessageProperty.addListener((observableValue, oldValue, newValue) -> {
                if (!isMessageAdded) {
                    top.getChildren().add(this.message);

                    isMessageAdded = true;

                    if (dialog != null)
                        dialog.sizeToScene();
                }
            });
        }

        /**
         * sets title of the AlertDialog
         *
         * @param title title of the AlertDialog
         * @return Builder
         */
        public Builder setDialogTitle(@NotNull String title) {
            dialogTitleProperty.set(title);

            return this;
        }

        /**
         * sets message of the AlertDialog
         *
         * @param message message of the AlertDialog
         * @return Builder
         */
        public Builder setDialogMessage(@NotNull String message) {
            dialogMessageProperty.set(message);

            return this;
        }

        /**
         * creates the positive button
         *
         * @param text     text of the button
         * @param listener button onClickListener,
         *                 when the button is clicked, the dialog closes
         * @return Builder
         */
        public Builder setPositiveButton(@NotNull String text, @NotNull DialogInterface.OnClickListener listener) {
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

        /**
         * creates the negative button
         *
         * @param text     text of the button
         * @param listener button onClickListener,
         *                 when the button is clicked, the dialog closes
         * @return Builder
         */
        public Builder setNegativeButton(@NotNull String text, @NotNull DialogInterface.OnClickListener listener) {
            negativeListeners.add(listener);

            negativeButton.setText(text);
            negativeButton.setOnAction(event -> {
                for (DialogInterface.OnClickListener clickListener : negativeListeners)
                    clickListener.onClick(DialogInterface.NEGATIVE_BUTTON);
                if (dialog != null)
                    dialog.close();
            });

            if (!isNegativeButtonAdded) {
                buttons.getButtons().add(negativeButton);
                isNegativeButtonAdded = true;
            }

            return this;
        }

        /**
         * creates the natural button on the left side of the button bar
         *
         * @param text     text of the button
         * @param listener button onClickListener,
         *                 when the button is clicked, the dialog closes
         * @return Builder
         */
        public Builder setNaturalButton(@NotNull String text, @NotNull DialogInterface.OnClickListener listener) {
            naturalListeners.add(listener);

            naturalButton.setText(text);
            naturalButton.setOnAction(event -> {
                for (DialogInterface.OnClickListener clickListener : naturalListeners)
                    clickListener.onClick(DialogInterface.NATURAL_BUTTON);
                if (dialog != null)
                    dialog.close();
            });

            if (!isNaturalButtonAdded) {
                buttons.getButtons().add(naturalButton);
                isNaturalButtonAdded = true;
            }

            return this;
        }

        /**
         * adds styles to style list and that list will be added to the scene
         *
         * @param styles dialog styles
         * @return Builder
         */
        public Builder addStyles(@NotNull String... styles) {
            Collections.addAll(this.styles, styles);

            return this;
        }

        /**
         * creates and adds RadioButton according to the items passed
         *
         * @param items         RadioButtons texts
         * @param selectedIndex default selected RadioButton
         * @param listener      selected listener
         * @return Builder
         */
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

        /**
         * creates and adds RadioButton according to the items passed without default selected item
         *
         * @param items    RadioButtons texts
         * @param listener selected listener
         * @return Builder
         */
        public Builder setSingleChoiceItems(@NotNull String[] items, @NotNull DialogInterface.OnSingleChoiceSelectedListener listener) {
            return setSingleChoiceItems(items, -1, listener);
        }

        /**
         * creates and adds CheckBoxes according to the items passed
         *
         * @param items           CheckBoxes texts
         * @param selectedIndexes default selected items
         * @param listener        selected listener
         * @return Builder
         */
        public Builder setMultiChoiceItems(@NotNull String[] items, @NotNull Integer[] selectedIndexes, @NotNull DialogInterface.OnMultiChoiceSelectedListener listener) {
            if (isRadioButtonsContainerAdded || isCustomNode)
                return this;

            if (isCheckBoxesContainerAdded)
                checkBoxesContainer.getChildren().clear();
            for (int i = 0; i < items.length; i++) {
                var checkBox = new CheckBox(items[i]);
                for (int j : selectedIndexes) {
                    if (i == j)
                        checkBox.setSelected(true);
                }
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

        /**
         * creates and adds CheckBoxes according to the items passed without default selected items
         *
         * @param items    CheckBoxes texts
         * @param listener selected listener
         * @return Builder
         */
        public Builder setMultiChoiceItems(@NotNull String[] items, @NotNull DialogInterface.OnMultiChoiceSelectedListener listener) {
            return setMultiChoiceItems(items, new Integer[]{}, listener);
        }

        /**
         * sets the custom node to the center of the dialog
         *
         * @param node custom node
         * @return Builder
         */
        public Builder setNode(@NotNull Node node) {
            container.setCenter(node);
            isCustomNode = true;

            return this;
        }

        /**
         * sets the notification sound from Sounds and plays when stage is shown
         * <br>
         * <b>Note:</b> You need to add <b>javafx.media</b> dependency to use sounds
         *
         * @param sound default notification sounds
         * @return Builder
         */
        public Builder setSound(@NotNull Sounds sound) {
            soundPath = Objects.requireNonNull(getClass().getResource("sounds/" + sound.getFileName())).toExternalForm();

            return this;
        }

        /**
         * sets the notification sound from custom file path and plays when stage is shown
         * <br>
         * <b>Note:</b> You need to add <b>javafx.media</b> dependency to use sounds
         *
         * @param path custom file path
         * @return Builder
         */
        public Builder setSound(@NotNull String path) {
            soundPath = Paths.get(path).toUri().toString();

            return this;
        }

        /**
         * creates AlertDialog
         *
         * @return AlertDialog
         */
        public AlertDialog create() {
            dialog = new AlertDialog(this);
            return dialog;
        }
    }
}
