package com.bigo.tindatrack.SQLite_Database;

import static com.bigo.tindatrack.SQLite_Database.userManagement.UsersTableManagement.createUserTable;
import static com.bigo.tindatrack.SQLite_Database.userManagement.AuthenticateUser.authenticateUser;
import static com.bigo.tindatrack.SQLite_Database.userManagement.PasswordHandler.changePass;
import static com.bigo.tindatrack.SQLite_Database.userManagement.PasswordHandler.verifyUser;

public class testArea {

    public static void main(String[] args) {

        // 1username, 2fullname, 3password, 4email, 5phoneNumber, 6storeName

        createUserTable();
//        System.out.println("\nTEST REGS:");
//        createUser("test1", "testfirst", "123", "test@j.com", "011", "sari");
//        createUser("admin", "admin gwapo", "123", "test@k.com","022", "adminStore");
//        createUser("admin", "admin gwapo", "213", "test@k.com","022", "adminStore");

        System.out.println("\nLogin test: ");
        authenticateUser("test1","newlols");
        authenticateUser("admin","123");
        System.out.println();

        System.out.println("TEST CHANGEPASS");
        verifyUser("test first", "011", "sari");
        System.out.println(changePass(verifyUser("test first", "011", "sari"), "newlols"));
        // why is the methods in a sout?
        // ans:
        // changePass returns a boolean function if it succeeded in changing the password
        // why does verifyUser inside the changePass perimeter?
        // verifyUser returns the username, was debating between id, username, and fullname
        // dint use fullname because edgecase: there might be two same fullnames entries
        // didnt use id because i wanted to use username lol since its unique but will change it later in case
        // something comes up

    }

}
