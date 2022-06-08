package com.amirali.fxdialogs.notifications;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Amir Ali
 */

final class NotificationInfoLogger {

    /**
     * save notification info to json file
     *
     * @param info notification info
     * @throws IOException file reader,writer exception
     */
    public static void save(@NotNull NotificationInfo info) throws IOException {
        var file = getFile();
        var gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        if (Files.exists(file.toPath())) {
            var reader = new FileReader(file);
            var data = gson.fromJson(reader, NotificationInfo[].class);
            reader.close();
            var writer = new FileWriter(file);
            if (data != null && data.length > 0) {
                var dataList = new ArrayList<>(List.of(data));
                dataList.add(info);
                gson.toJson(dataList, writer);
            }else {
                gson.toJson(new NotificationInfo[]{info}, writer);
            }
            writer.flush();
            writer.close();
        }else {
            var writer = new FileWriter(file);
            gson.toJson(new NotificationInfo[]{info}, writer);
            writer.flush();
            writer.close();
        }
    }

    /**
     * remove if notification id is exists
     *
     * @param info NotificationInfo
     * @throws IOException file reader,writer exception
     */
    public static void removeIf(@NotNull NotificationInfo info) throws IOException {
        var file = getFile();
        if (Files.exists(file.toPath())) {
            var gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();
            var reader = new FileReader(file);
            var data = gson.fromJson(reader, NotificationInfo[].class);
            reader.close();
            if (data != null && data.length > 0) {
                var dataList = new ArrayList<>(List.of(data));
                dataList.removeIf(predicate -> predicate.getId().equals(info.getId()));
                var writer = new FileWriter(file);
                gson.toJson(dataList, writer);
                writer.flush();
                writer.close();
            }
        }
    }

    /**
     * returns list of notifications info if json file is exists
     *
     * @return List
     * @throws IOException file reader,writer exception
     */
    public static List<NotificationInfo> getNotificationsInfo() throws IOException {
        var file = getFile();
        if (Files.exists(file.toPath())) {
            var gson = new Gson();
            var reader = new FileReader(file);
            var data = gson.fromJson(reader, NotificationInfo[].class);
            reader.close();
            if (data != null && data.length > 0)
                return new ArrayList<>(List.of(data));
        }
        return new ArrayList<>();
    }

    private static File getFile() throws IOException {
        var file = new File(
                System.getProperty("user.home") +
                        File.separator +
                        "fxdialogs" +
                        File.separator +
                        "notification-logs.json"
        );
        var parent = file.getParentFile();

        if (!Files.exists(parent.toPath())) {
            Files.createDirectories(parent.toPath());
            Files.setAttribute(parent.toPath(), "dos:hidden", true);
        }

        return file;
    }
}