package com.bigo.tindatrack.SQLite_Database;

import static com.bigo.tindatrack.SQLite_Database.TableManagement.createUserTable;
import static com.bigo.tindatrack.SQLite_Database.userManagement.AuthenticateUser.authenticateUser;
import static com.bigo.tindatrack.SQLite_Database.userManagement.CreateUser.createUser;

public class testArea {

    public static void main(String[] args) {

        createUserTable();
        System.out.println("\nTEST REGS:");
        createUser("test1", "123");
        createUser("admin", "testpass");
        createUser("admin", "testpass2");

        System.out.println("\nLogin test: ");
        authenticateUser("test1","123");
        authenticateUser("admin","213");

    }

}
