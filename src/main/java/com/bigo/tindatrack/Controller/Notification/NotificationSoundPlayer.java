package com.bigo.tindatrack.Controller.Notification;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
public class NotificationSoundPlayer {
    private static MediaPlayer player;

    public static void play() {
        try {
            URL resource = NotificationSoundPlayer.class
                    .getResource("/com/bigo/tindatrack/sounds/notification.wav");

            if (resource == null) {
                System.err.println("Sound file not found.");
                return;
            }

            if (player != null) {
                player.dispose();
            }

            Media  sound  = new Media(resource.toExternalForm());
            player = new MediaPlayer(sound);
            player.setVolume(0.8);
            player.play();

        } catch (Exception e) {
            System.err.println("Sound playback error: " + e.getMessage());
        }
    }
}
