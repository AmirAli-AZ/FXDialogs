package com.amirali.fxdialogs;

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

public final class TimePickerDialog extends Stage {

    public TimePickerDialog(@NotNull Builder builder) {
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

    public static class Builder {

        // UI components
        private final HBox container = new HBox(10);
        private final Label hoursLabel = new Label("0"), minutesLabel = new Label("0");
        private final ToggleButton amButton = new ToggleButton("AM"), pmButton = new ToggleButton("PM");
        private final ToggleGroup toggleGroup = new ToggleGroup();

        private int hours, minutes;
        private final List<String> styles = new ArrayList<>();
        private boolean init = true;

        public Builder(@NotNull DialogInterface.OnTimeSetListener listener) {
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

            var hoursArrowUp = new ImageView(
                    new Image(
                            Objects.requireNonNull(getClass().getResourceAsStream("icons/round_keyboard_arrow_up_black_24dp.png"))
                    )
            );
            var hoursArrowDown = new ImageView(
                    new Image(
                            Objects.requireNonNull(getClass().getResourceAsStream("icons/round_keyboard_arrow_down_black_24dp.png"))
                    )
            );
            var minutesArrowUp = new ImageView(
                    new Image(
                            Objects.requireNonNull(getClass().getResourceAsStream("icons/round_keyboard_arrow_up_black_24dp.png"))
                    )
            );
            var minutesArrowDown = new ImageView(
                    new Image(
                            Objects.requireNonNull(getClass().getResourceAsStream("icons/round_keyboard_arrow_down_black_24dp.png"))
                    )
            );

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

            var am_pm = Calendar.getInstance().get(Calendar.AM_PM);
            toggleGroup.selectToggle(am_pm == Calendar.AM ? amButton : pmButton);

            hours = Calendar.getInstance().get(Calendar.HOUR);
            minutes = Calendar.getInstance().get(Calendar.MINUTE);
            hoursLabel.setId("hours");
            hoursLabel.setText(String.valueOf(hours));
            minutesLabel.setId("minutes");
            minutesLabel.setText(String.valueOf(minutes));

            hoursArrowUp.setOnMouseClicked(mouseEvent -> {
                hoursLabel.setText(String.valueOf(hours == 12 ? hours : ++hours));
                listener.onTimeSet(getResult());
            });

            hoursArrowDown.setOnMouseClicked(mouseEvent -> {
                hoursLabel.setText(String.valueOf(hours == 0 ? hours : --hours));
                listener.onTimeSet(getResult());
            });

            minutesArrowUp.setOnMouseClicked(mouseEvent -> {
                minutesLabel.setText(String.valueOf(minutes == 59 ? minutes : ++minutes));
                listener.onTimeSet(getResult());
            });

            minutesArrowDown.setOnMouseClicked(mouseEvent -> {
                minutesLabel.setText(String.valueOf(minutes == 0 ? minutes : --minutes));
                listener.onTimeSet(getResult());
            });

            toggleGroup.selectedToggleProperty().addListener((observableValue, oldToggle, newToggle) -> {
                if (!init)
                    listener.onTimeSet(getResult());
            });

            init = false;
        }

        public Builder initTime(Time time) {
            init = true;
            hours = time.hours();
            minutes = time.minutes();

            hoursLabel.setText(String.valueOf(hours));
            minutesLabel.setText(String.valueOf(minutes));

            toggleGroup.selectToggle(time.am_pm() == Time.AM_PM.AM ? amButton : pmButton);
            init = false;

            return this;
        }

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

        public TimePickerDialog create() {
            return new TimePickerDialog(this);
        }
    }
}
