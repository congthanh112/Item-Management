
package com.thanhpdc.lab1.util;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class MyConnection {

    public static Connection makeConnection() {
        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://localhost:1433;databaseName=ItemManagement";
            conn = DriverManager.getConnection(url, "sa", "1");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error connecting to server");
        }
        return conn;
    } 
}
