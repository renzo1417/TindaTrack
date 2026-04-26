package com.bigo.tindatrack.SQLite_Database;

import static com.bigo.tindatrack.SQLite_Database.TableManagement.createUserTable;
import static com.bigo.tindatrack.SQLite_Database.userManagement.AuthenticateUser.authenticateUser;
import static com.bigo.tindatrack.SQLite_Database.userManagement.CreateUser.createUser;

public class testArea {

    public static void main(String[] args) {

        // 1username, 2fullname, 3password, 4email, 5phoneNumber, 6storeName

//        createUserTable();
        System.out.println("\nTEST REGS:");
        createUser("test1", "test first", "123", "test@j.com", "011", "sari");
        createUser("admin", "admin gwapo", "123", "test@k.com","022", "adminStore");
        createUser("admin", "admin gwapo", "213", "test@k.com","022", "adminStore");

        System.out.println("\nLogin test: ");
        authenticateUser("test1","123");
        authenticateUser("admin","123");

    }

}
