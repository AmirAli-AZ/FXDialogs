package com.amirali.fxdialogs.notifications;

import org.jetbrains.annotations.NotNull;

record NotificationInfo(@NotNull NotificationPosition position, @NotNull String id, double height) {}