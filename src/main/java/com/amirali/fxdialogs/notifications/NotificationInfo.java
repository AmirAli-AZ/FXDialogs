package com.amirali.fxdialogs.notifications;

class NotificationInfo {
    private String id;
    private NotificationPosition position;
    private double height;

    public NotificationInfo(NotificationPosition position, double height, String id) {
        this.position = position;
        this.height = height;
        this.id = id;
    }

    public NotificationPosition getPosition() {
        return position;
    }

    public void setPosition(NotificationPosition position) {
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

    public void setId(String id) {
        this.id = id;
    }
}