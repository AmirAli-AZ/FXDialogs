package com.amirali.fxdialogs;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Amir Ali
 */

public final class TimePickerDialog extends Stage {

    private final Builder builder;
    private final ObjectProperty<Image> arrowUpImageProperty = new SimpleObjectProperty<>(
            new Image(
                    Objects.requireNonNull(getClass().getResourceAsStream("icons/round_keyboard_arrow_up_black_24dp.png"))
            )
    );
    private final ObjectProperty<Image> arrowDownImageProperty = new SimpleObjectProperty<>(
            new Image(
                    Objects.requireNonNull(getClass().getResourceAsStream("icons/round_keyboard_arrow_down_black_24dp.png"))
            )
    );

    /**
     * @param builder TimePickerDialog builder
     */
    public TimePickerDialog(@NotNull Builder builder) {
        this.builder = builder;
        setupDialog();
    }

    private void setupDialog() {
        builder.bindArrowUpImageProperty(arrowUpImageProperty);
        builder.bindArrowDownImageProperty(arrowDownImageProperty);

        var scene = new Scene(builder.container);
        if (builder.styles.isEmpty()) {
            scene.getStylesheets().add(
                    Objects.requireNonNull(
                            getClass().getResource("themes/default-timepicker-dialog-theme.css")
                    ).toExternalForm()
            );
        }else {
            scene.getStylesheets().addAll(builder.styles);
        }
        setScene(scene);
    }

    /**
     * @return arrow up image property
     */
    public ObjectProperty<Image> arrowUpImageProperty() {
        return arrowUpImageProperty;
    }

    /**
     * @return arrow down image property
     */
    public ObjectProperty<Image> arrowDownImageProperty() {
        return arrowDownImageProperty;
    }

    /**
     * sets time of the TimePickerDialog
     * @param time new time of the TimePickerDialog
     */
    public void setTime(@NotNull Time time) {
        builder.timeProperty.set(time);
    }

    /**
     * @return time of the TimePickerDialog
     */
    public Time getTime() {
        return builder.timeProperty.get();
    }


    /**
     * @return time object property
     */
    public ObjectProperty<Time> timeProperty() {
        return builder.timeProperty;
    }

    /**
     * TimePickerDialog builder
     */
    public static class Builder {

        // UI components
        private final HBox container = new HBox(10);
        private final Label hoursLabel = new Label("0"), minutesLabel = new Label("0");
        private final ToggleButton amButton = new ToggleButton("AM"), pmButton = new ToggleButton("PM");
        private final ToggleGroup toggleGroup = new ToggleGroup();
        private final ImageView hoursArrowUp = new ImageView(),
                hoursArrowDown = new ImageView(),
                minutesArrowUp = new ImageView(),
                minutesArrowDown = new ImageView();

        private int hours, minutes;
        private final List<String> styles = new ArrayList<>();
        private boolean init = true;
        private final ObjectProperty<Time> timeProperty = new SimpleObjectProperty<>();

        /**
         * creates initial layout
         */
        public Builder() {
            // init

            var vBox1 = new VBox(5);
            var vBox2 = new VBox(5);
            var vBox3 = new VBox(5);

            vBox1.setPadding(new Insets(10));
            vBox2.setPadding(new Insets(10));
            vBox3.setPadding(new Insets(10));
            vBox1.setAlignment(Pos.CENTER);
            vBox2.setAlignment(Pos.CENTER);
            vBox3.setAlignment(Pos.CENTER);

            hoursArrowUp.setId("hours-arrow-up");
            hoursArrowDown.setId("hours-arrow-down");
            minutesArrowUp.setId("minutes-arrow-up");
            minutesArrowDown.setId("minutes-arrow-down");

            hoursArrowUp.setPickOnBounds(true);
            hoursArrowDown.setPickOnBounds(true);
            minutesArrowUp.setPickOnBounds(true);
            minutesArrowDown.setPickOnBounds(true);

            setWidthAndHeight(hoursArrowUp);
            setWidthAndHeight(hoursArrowDown);
            setWidthAndHeight(minutesArrowUp);
            setWidthAndHeight(minutesArrowDown);

            vBox1.getChildren().addAll(hoursArrowUp, hoursLabel, hoursArrowDown);
            vBox2.getChildren().addAll(minutesArrowUp, minutesLabel, minutesArrowDown);

            amButton.setId("am-button");
            amButton.setToggleGroup(toggleGroup);
            pmButton.setId("pm-button");
            pmButton.setToggleGroup(toggleGroup);
            vBox3.getChildren().addAll(amButton, pmButton);
            container.setAlignment(Pos.CENTER);

            var colon = new Label(":");
            colon.setId("colon");
            container.getChildren().addAll(vBox1, colon, vBox2, vBox3);

            var currentTime = Time.getCurrentTime();
            timeProperty.set(currentTime);

            toggleGroup.selectToggle(currentTime.am_pm() == Time.AM_PM.AM ? amButton : pmButton);
            hours = currentTime.hours();
            minutes = currentTime.minutes();
            hoursLabel.setId("hours");
            hoursLabel.setText(String.valueOf(hours));
            minutesLabel.setId("minutes");
            minutesLabel.setText(String.valueOf(minutes));

            hoursArrowUp.setOnMouseClicked(mouseEvent -> {
                hoursLabel.setText(String.valueOf(hours == 12 ? hours : ++hours));
                timeProperty.set(getResult());
            });

            hoursArrowDown.setOnMouseClicked(mouseEvent -> {
                hoursLabel.setText(String.valueOf(hours == 0 ? hours : --hours));
                timeProperty.set(getResult());
            });

            minutesArrowUp.setOnMouseClicked(mouseEvent -> {
                minutesLabel.setText(String.valueOf(minutes == 59 ? minutes : ++minutes));
                timeProperty.set(getResult());
            });

            minutesArrowDown.setOnMouseClicked(mouseEvent -> {
                minutesLabel.setText(String.valueOf(minutes == 0 ? minutes : --minutes));
                timeProperty.set(getResult());
            });

            toggleGroup.selectedToggleProperty().addListener((observableValue, oldToggle, newToggle) -> {
                if (!init)
                    timeProperty.set(getResult());
            });

            timeProperty.addListener((observableValue, oldValue, newValue) -> {
                init = true;
                hours = newValue.hours();
                minutes = newValue.minutes();

                hoursLabel.setText(String.valueOf(hours));
                minutesLabel.setText(String.valueOf(minutes));

                toggleGroup.selectToggle(newValue.am_pm() == Time.AM_PM.AM ? amButton : pmButton);
                init = false;
            });

            init = false;
        }

        /**
         * sets time of the TimePickerDialog
         * @param time new time of the TimePickerDialog
         * @return Builder
         */
        public Builder setTime(@NotNull Time time) {
            timeProperty.set(time);

            return this;
        }

        /**
         * adds styles to style list and that list will be added to the scene
         * @param styles dialog styles
         * @return Builder
         */
        public Builder setStyles(@NotNull String... styles) {
            Collections.addAll(this.styles, styles);

            return this;
        }

        private void setWidthAndHeight(ImageView imageView) {
            imageView.setFitWidth(40);
            imageView.setFitHeight(40);
        }

        private Time getResult() {
            return new Time(
                    hours,
                    minutes,
                    toggleGroup.getSelectedToggle() == amButton ? Time.AM_PM.AM : Time.AM_PM.PM
            );
        }

        private void bindArrowUpImageProperty(ObjectProperty<Image> imageProperty) {
            hoursArrowUp.imageProperty().bind(imageProperty);
            minutesArrowUp.imageProperty().bind(imageProperty);
        }

        private void bindArrowDownImageProperty(ObjectProperty<Image> imageProperty) {
            hoursArrowDown.imageProperty().bind(imageProperty);
            minutesArrowDown.imageProperty().bind(imageProperty);
        }

        /**
         * creates TimePickerDialog
         * @return TimePickerDialog
         */
        public TimePickerDialog create() {
            return new TimePickerDialog(this);
        }
    }
}
