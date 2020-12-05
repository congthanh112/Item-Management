package com.thanhpdc.lab1.dao;

import com.thanhpdc.lab1.dto.SupplierDTO;
import util.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupplierDAO {

    public static ArrayList<SupplierDTO> getSupplier() throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<SupplierDTO> listSupplier = new ArrayList<>();
        try {
            conn = MyConnection.makeConnection();
            if (conn != null) {
                String query = "SELECT supCode, supName, address, collaborating FROM dbo.tblSuppliers";
                pst = conn.prepareStatement(query);
                rs = pst.executeQuery();
                while (rs.next()) {
                    String supCode = rs.getString("supCode");
                    String supName = rs.getString("supName");
                    String address = rs.getString("address");
                    boolean collaborating = rs.getBoolean("collaborating");
                    SupplierDTO s = new SupplierDTO(supCode, supName, address, collaborating);
                    listSupplier.add(s);
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
        return listSupplier;
    }

    public static boolean checkSupCodeExist(String supCode) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = MyConnection.makeConnection();
            if (conn != null) {
                String query = "SELECT supCode FROM tblSuppliers WHERE supCode = ?" ;
                pst = conn.prepareStatement(query);  
                pst.setString(1, supCode);
                rs = pst.executeQuery();
                while (rs.next()) {
                    result = true;
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
    public static boolean checkSupplying(String supCode) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = MyConnection.makeConnection();
            if (conn != null) {
                String query = "SELECT supName FROM tblItems JOIN tblSuppliers ON tblSuppliers.supCode = tblItems.supCode WHERE tblItems.supCode=?";
                pst = conn.prepareStatement(query);
                pst.setString(1, supCode);
                rs = pst.executeQuery();
                while (rs.next()) {
                    result = true;
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

    public static int addSupplier(String supCode, String supName, String address, boolean collaborating) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int result = 0;
        try {
            conn = MyConnection.makeConnection();
            if (conn != null) {
                String query = "INSERT tblSuppliers VALUES(?,?,?,?)";
                pst = conn.prepareStatement(query);
                pst.setString(1, supCode);
                pst.setString(2, supName);
                pst.setString(3, address);
                pst.setBoolean(4, collaborating);
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

    public static int updateSupplier(String supCode, String supName, String address, boolean collaborating) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;
        int result = 0;
        try {
            conn = MyConnection.makeConnection();

            if (conn != null) {
                String query = "UPDATE tblSuppliers SET supName=?,address=?,collaborating=? WHERE supCode=?";
                pst = conn.prepareStatement(query);
                pst.setString(1, supName);
                pst.setString(2, address);
                pst.setBoolean(3, collaborating);
                pst.setString(4, supCode);
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

    public static int deleteSupplier(String supCode) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;
        int result = 0;
        try {
            conn = MyConnection.makeConnection();
            if (conn != null) {
                String query = "DELETE FROM dbo.tblSuppliers WHERE supCode=?";
                pst = conn.prepareStatement(query);
                pst.setString(1, supCode);
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
