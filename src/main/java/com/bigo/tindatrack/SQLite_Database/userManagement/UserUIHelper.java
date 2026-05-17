package com.bigo.tindatrack.SQLite_Database.userManagement;

import com.bigo.tindatrack.data.models.User;
import javafx.scene.control.Label;

public class UserUIHelper {

    public static void setupUserUI(Label initial_top, Label initial_bottom, Label username_top, Label username_bottom, User user) {

        if(user == null){
            return;
        }

        username_top.setText(user.getUsername());
        username_bottom.setText(user.getUsername());

        String firstLetter = user.getUsername().charAt(0) + "";
//                user.getUsername()
//                        .substring(0, 1)
//                        .toUpperCase();

        initial_top.setText(firstLetter.toUpperCase());
        initial_bottom.setText(firstLetter.toUpperCase());


    }
}
