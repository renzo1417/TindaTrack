package com.bigo.tindatrack.SQLite_Database;

import static com.bigo.tindatrack.SQLite_Database.productsManagement.ProductManagement.*;
import static com.bigo.tindatrack.SQLite_Database.productsManagement.productTableManagement.createProductTable;
import static com.bigo.tindatrack.SQLite_Database.userManagement.CreateUser.createUser;
import static com.bigo.tindatrack.SQLite_Database.userManagement.UsersTableManagement.createUserTable;
import static com.bigo.tindatrack.SQLite_Database.userManagement.AuthenticateUser.authenticateUser;
import static com.bigo.tindatrack.SQLite_Database.userManagement.PasswordHandler.changePass;
import static com.bigo.tindatrack.SQLite_Database.userManagement.PasswordHandler.verifyUser;

public class testArea {

    public static void main(String[] args) {

        // 1username, 2fullname, 3password, 4email, 5phoneNumber, 6storeName

        createUserTable();
        createUser("test1", "testfirst", "123", "test@j.com", "011", "sari");
        createUser("admin", "admin gwapo", "123", "test@k.com","022", "adminStore");

        System.out.println("\nLogin test: ");
        authenticateUser("test1","123");
        authenticateUser("admin","123");
        System.out.println();

        createProductTable();
//        createNotificationsTable();

        viewInventory();

    }

}
