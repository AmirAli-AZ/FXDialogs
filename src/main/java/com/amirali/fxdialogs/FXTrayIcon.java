package com.amirali.fxdialogs;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Amir Ali
 *
 * Inspired by XDSSWAR NiceTrayIcon
 */

public class FXTrayIcon extends TrayIcon {

    private ContextMenu menu;

    /**
     * initial FXTrayIcon
     *
     * @param image tray icon image
     * @param menu tray icon menu
     */
    public FXTrayIcon(@NotNull Image image, @NotNull ContextMenu menu) {
        super(image);

        this.menu = menu;
        init();
    }

    /**
     * initial FXTrayIcon
     *
     * @param image tray icon image
     * @param tooltip tray icon tooltip text
     * @param menu tray icon menu
     */
    public FXTrayIcon(@NotNull Image image, @NotNull String tooltip, @NotNull ContextMenu menu) {
        super(image, tooltip);

        this.menu = menu;
        init();
    }

    /**
     * initial FXTrayIcon
     *
     * @param image tray icon image
     */
    public FXTrayIcon(@NotNull Image image) {
        super(image);

        init();
    }

    /**
     * initial FXTrayIcon
     *
     * @param image tray icon image
     * @param tooltip tray icon tooltip text
     */
    public FXTrayIcon(@NotNull Image image, @NotNull String tooltip) {
        super(image, tooltip);

        init();
    }

    private void init() {
        setImageAutoSize(true);

        if (menu != null && menu.getItems().size() > 0) {
            menu.setAutoHide(true);

            var iconStage = new Stage();
            iconStage.initStyle(StageStyle.UTILITY);
            iconStage.setWidth(0);
            iconStage.setHeight(0);
            iconStage.setOpacity(0);
            iconStage.setX(Double.MAX_VALUE);
            iconStage.setScene(new Scene(new StackPane()));
            iconStage.show();

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    Platform.runLater(() -> {
                        iconStage.requestFocus();
                        menu.show(iconStage, e.getX(), e.getY());
                    });
                }
            });
        }
    }
}
