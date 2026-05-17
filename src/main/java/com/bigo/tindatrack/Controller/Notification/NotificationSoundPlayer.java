package com.bigo.tindatrack.Controller.Notification;

import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class NotificationSoundPlayer {

    private static MediaPlayer player;

    public static void play() {
        Platform.runLater(() -> {
            try {
                URL resource = NotificationSoundPlayer.class
                        .getResource("/com/bigo/tindatrack/sounds/notificication.wav");

                if (resource == null) {
                    System.err.println("Sound file not found. Check: /sounds/notification.wav");
                    return;
                }

                // Stop old player first
                if (player != null) {
                    player.stop();
                    player.dispose();
                    player = null;
                }

                Media sound = new Media(resource.toExternalForm());
                player = new MediaPlayer(sound);
                player.setVolume(0.8); // max is 1.0

                // Cleanup after sound finishes
                player.setOnEndOfMedia(() -> {
                    player.dispose();
                    player = null;
                });

                player.setOnError(() ->
                        System.err.println("MediaPlayer error: " + player.getError())
                );

                player.play();

            } catch (Exception e) {
                System.err.println("Sound playback error: " + e.getMessage());
            }
        });
    }
}