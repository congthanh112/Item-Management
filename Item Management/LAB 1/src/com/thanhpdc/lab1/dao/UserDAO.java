package com.thanhpdc.lab1.dao;

import com.thanhpdc.lab1.util.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public static String checkLogin(String userID, String password) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String result = "";

        try {
            conn = MyConnection.makeConnection();
            if (conn != null) {
                String query = "SELECT fullName FROM tblUsers \n"
                        + "WHERE userID=? COLLATE SQL_Latin1_General_CP1_CS_AS \n"
                        + "AND password=? COLLATE SQL_Latin1_General_CP1_CS_AS";
                pst = conn.prepareStatement(query);
                pst.setString(1, userID);
                pst.setString(2, password);
                rs = pst.executeQuery();

                if (rs.next()) {
                    result = rs.getString("fullName");
                }
            }
        } catch (Exception e) {
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return result;
    }

    public static String getStatus(String userID, String password) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String result = "";
        try {
            conn = MyConnection.makeConnection();
            if (conn != null) {
                String query = "SELECT status FROM tblUsers WHERE userID=? AND password=?";
                pst = conn.prepareStatement(query);
                pst.setString(1, userID);
                pst.setString(2, password);
                rs = pst.executeQuery();

                if (rs.next()) {
                    result = rs.getString("status");
                }
            }
        } catch (Exception e) {
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return result;
    }

    public static boolean checkUserID(String userID) {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = MyConnection.makeConnection();
            if (conn != null) {
                String query = "SELECT fullName FROM tblUsers WHERE userID = ?";
                pst = conn.prepareStatement(query);
                pst.setString(1, userID);
                rs = pst.executeQuery();
                while (rs.next()) {
                    result = true;
                }
            }
        } catch (Exception e) {
        }
        return result;
    }

    public static int addUser(String userID, String userName, String password, boolean status) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;

        int result = 0;
        try {
            conn = MyConnection.makeConnection();
            if (conn != null) {
                String query = "INSERT INTO tblUsers VALUES(?,?,?,?)";
                pst = conn.prepareStatement(query);
                pst.setString(1, userID);
                pst.setString(2, userName);
                pst.setString(3, password);
                pst.setBoolean(4, status);
                result = pst.executeUpdate();
            }
        } catch (Exception e) {
        } finally {
            if (pst != null) {
                pst.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return result;
    }
}
