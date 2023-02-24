package com.amirali.fxdialogs;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Amir Ali
 */

public final class SplashScreen extends Stage {

    private final Builder builder;
    private final ObjectProperty<Duration> durationProperty = new SimpleObjectProperty<>(Duration.ZERO) {
        @Override
        public void set(Duration duration) {
            super.set(duration);
            timeline = new Timeline(new KeyFrame(duration));
            timeline.setCycleCount(1);
            timeline.setOnFinished(event -> closeSplash());
            currentTimeProperty.bind(timeline.currentTimeProperty());
        }
    }, currentTimeProperty = new SimpleObjectProperty<>(Duration.ZERO);
    private Timeline timeline;

    /**
     * @param builder SplashScreen builder
     */
    public SplashScreen(@NotNull Builder builder) {
        this.builder = builder;
        setupSplashScreen();
    }

    private void setupSplashScreen() {
        setAlwaysOnTop(true);
        initStyle(StageStyle.TRANSPARENT);

        if (builder.scene != null) {
            setScene(builder.scene);
        } else if (builder.container != null) {
            var scene = new Scene(builder.container);
            setScene(scene);
        } else {
            throw new NullPointerException("container or scene cannot be null");
        }

        setDuration(builder.duration);

        addEventHandler(WindowEvent.WINDOW_SHOWN, windowEvent -> {
            if (timeline != null)
                timeline.play();

            if (builder.callBack != null)
                builder.callBack.onStarted();
        });

        addEventHandler(WindowEvent.WINDOW_HIDDEN, windowEvent -> {
            if (timeline != null && timeline.getStatus() == Animation.Status.RUNNING) {
                timeline.stop();
                closeSplash();
            }
        });
    }

    private void closeSplash() {
        builder.primaryStage.show();

        if (isShowing())
            close();

        if (builder.callBack != null)
            builder.callBack.onFinished();
    }

    /**
     * elapsed duration of the total duration
     *
     * @return Duration
     */
    public Duration getCurrentTime() {
        return currentTimeProperty.get();
    }

    /**
     * elapsed duration of the total duration as a read only property
     *
     * @return ReadOnlyObjectProperty
     */
    public ReadOnlyObjectProperty<Duration> currentTimeProperty() {
        return currentTimeProperty;
    }

    /**
     * sets duration of SplashScreen
     *
     * @param duration Duration
     */
    public void setDuration(@NotNull Duration duration) {
        durationProperty.set(duration);
    }

    /**
     * SplashScreen display duration
     *
     * @return Duration
     */
    public Duration getDuration() {
        return durationProperty.get();
    }


    /**
     * SplashScreen display duration as a property
     *
     * @return ObjectProperty
     */
    public ObjectProperty<Duration> durationProperty() {
        return durationProperty;
    }

    /**
     * SplashScreen builder
     */
    public static class Builder {

        private final Duration duration;
        private Parent container;
        private SplashScreenCallBack callBack;
        private Scene scene;
        private final Stage primaryStage;

        /**
         * initial SplashScreen
         *
         * @param duration     duration of the SplashScreen
         * @param primaryStage primary stage
         * @throws IllegalStateException Cannot show the SplashScreen once primaryStage has been set visible
         */
        public Builder(@NotNull Duration duration, @NotNull Stage primaryStage) {
            if (primaryStage.isShowing())
                throw new IllegalStateException("Cannot show the SplashScreen once primaryStage has been set visible");

            this.duration = duration;
            this.primaryStage = primaryStage;
        }

        /**
         * sets container layout of the SplashScreen
         *
         * @param container Parent
         * @return Builder
         */
        public Builder setLayout(@NotNull Parent container) {
            this.container = container;

            return this;
        }

        /**
         * sets scene of the SplashScreen
         *
         * @param scene SplashScreen scene
         * @return Builder
         */
        public Builder setScene(@NotNull Scene scene) {
            this.scene = scene;

            return this;
        }

        /**
         * sets callBack for SplashScreen events
         *
         * @param callBack SplashScreenCallBack
         * @return Builder
         */
        public Builder setCallBack(@NotNull SplashScreenCallBack callBack) {
            this.callBack = callBack;

            return this;
        }

        /**
         * creates SplashScreen
         *
         * @return SplashScreenWindow
         */
        public SplashScreen create() {
            return new SplashScreen(this);
        }
    }
}
