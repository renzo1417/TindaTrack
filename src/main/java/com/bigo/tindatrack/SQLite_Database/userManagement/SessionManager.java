package com.bigo.tindatrack.SQLite_Database.userManagement;

import com.bigo.tindatrack.data.models.User;

import java.io.*;

//para pang serialize sa atong user
public class SessionManager {
    public static final String  SESSION_FILE = "current_user.ser";

    public static void saveUser(User user){
        try(ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(SESSION_FILE)
        )){
            oos.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static User loadUser(){
        File file = new File(SESSION_FILE);

        if(!file.exists()){
            System.out.println("file does not exist");
            return null;
        }
        try(ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(SESSION_FILE)
        )){
            return (User)ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void clearSession(){
        File file = new File(SESSION_FILE);
        if(file.exists()){
            file.delete();
        }

    }

    public static boolean hasSession() {
        return new File(SESSION_FILE).exists();
    }

}

