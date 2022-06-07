package com.amirali.fxdialogs.notifications;

import org.jetbrains.annotations.NotNull;

class NotificationInfo {
    private String id;
    private NotificationPosition position;
    private double height;

    public NotificationInfo(@NotNull NotificationPosition position, double height, @NotNull String id) {
        this.position = position;
        this.height = height;
        this.id = id;
    }

    public NotificationPosition getPosition() {
        return position;
    }

    public void setPosition(@NotNull NotificationPosition position) {
        this.position = position;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getId() {
        return id;
    }

    public void setId(@NotNull String id) {
        this.id = id;
    }
}