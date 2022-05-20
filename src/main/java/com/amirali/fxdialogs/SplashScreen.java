package com.amirali.fxdialogs;

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

    /**
     * @param builder SplashScreen builder
     */
    public SplashScreen(@NotNull Builder builder) {
        this.builder = builder;
        setupSplashScreen();
    }

    private void setupSplashScreen() {
        setAlwaysOnTop(true);
        initStyle(StageStyle.UNDECORATED);

        if (builder.scene != null) {
            setScene(builder.scene);
        } else if (builder.container != null) {
            var scene = new Scene(builder.container);
            if (!builder.styles.isEmpty())
                scene.getStylesheets().addAll(builder.styles);
            setScene(scene);
        } else {
            throw new NullPointerException("container or scene cannot be null");
        }

        addEventHandler(WindowEvent.WINDOW_SHOWN, windowEvent -> {
            builder.timeline.play();
            if (builder.callBack != null)
                builder.callBack.onStarted();
        });
    }

    /**
     * elapsed time of the total time
     *
     * @return Duration
     */
    public Duration getCurrentTime() {
        return builder.timeline.getCurrentTime();
    }

    /**
     * elapsed time of the total time as a read only property
     *
     * @return ReadOnlyObjectProperty
     */
    public ReadOnlyObjectProperty<Duration> currentTimeProperty() {
        return builder.timeline.currentTimeProperty();
    }

    /**
     * total time
     *
     * @return Duration
     */
    public Duration getTotalTime() {
        return builder.totalTimeProperty.get();
    }


    /**
     * total time as a read only property
     *
     * @return ReadOnlyObjectProperty
     */
    public ReadOnlyObjectProperty<Duration> totalTimeProperty() {
        return builder.totalTimeProperty;
    }

    /**
     * SplashScreen builder
     */
    public static class Builder {

        private final List<String> styles = new ArrayList<>();
        private final Timeline timeline;
        private final ObjectProperty<Duration> totalTimeProperty;
        private Parent container;
        private SplashScreenCallBack callBack;
        private Scene scene;

        private SplashScreen splashScreen;

        /**
         * initial SplashScreen
         *
         * @param duration     duration of the SplashScreen
         * @param primaryStage primary stage
         * @throws IllegalStateException Cannot show the SplashScreen once primaryStage has been set visible
         */
        public Builder(@NotNull Duration duration, @NotNull Stage primaryStage) {
            totalTimeProperty = new SimpleObjectProperty<>(duration);

            if (primaryStage.isShowing())
                throw new IllegalStateException("Cannot show the SplashScreen once primaryStage has been set visible");

            var keyFrame = new KeyFrame(duration);
            timeline = new Timeline(keyFrame);
            timeline.setCycleCount(1);
            timeline.setOnFinished(event -> {
                if (primaryStage.isShowing())
                    throw new IllegalStateException("Cannot show the SplashScreen once primaryStage has been set visible");

                if (splashScreen != null)
                    splashScreen.close();
                primaryStage.show();
                if (callBack != null)
                    callBack.onFinished();
            });
        }

        /**
         * adds styles to style list and that list will be added to the scene
         *
         * @param styles SplashScreen styles
         * @return Builder
         */
        public Builder setStyles(@NotNull String... styles) {
            Collections.addAll(this.styles, styles);

            return this;
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
            splashScreen = new SplashScreen(this);
            return splashScreen;
        }
    }
}
