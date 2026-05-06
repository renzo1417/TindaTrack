package com.bigo.tindatrack.SQLite_Database.StockManagement;

import com.bigo.tindatrack.data.StockDetails.StockDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.bigo.tindatrack.SQLite_Database.ConnectionBridge.connect;

public class StockFetchFromTable {
    public static ObservableList<StockDetails> getActivitiesFromDB(int ownerID) {
        String query = "SELECT id, name, oldQty, newQty, reason, date FROM stockactivity WHERE owner_id = ?";
        ObservableList<StockDetails> list = FXCollections.observableArrayList();

        try (Connection connected = connect(); PreparedStatement pstmt = connected.prepareStatement(query)) {
            pstmt.setInt(1, ownerID);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int oldQty = rs.getInt("oldQty");
                int newQty = rs.getInt("newQty");
                String reason = rs.getString("reason");
                String date = rs.getString("date");

                StockDetails stock = new StockDetails(name, oldQty, newQty, reason);
                stock.setDate(date);
                stock.setId(id);

                list.add(0,stock);
            }
        } catch (SQLException e) {
            System.err.println("ERROR getting stock activities from DB");
            e.printStackTrace();
        }

        return list;
    }
}
