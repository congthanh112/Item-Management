package com.thanhpdc.lab1.dao;

import com.thanhpdc.lab1.dto.ItemDTO;
import util.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemDAO {

    public static ArrayList<ItemDTO> getItem() throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<ItemDTO> list = new ArrayList<>();
        try {
            conn = MyConnection.makeConnection();
            if (conn != null) {
                String sql = "SELECT itemCode,itemName,unit,price,supplying,tblItems.supCode AS supCode, supName\n"
                        + "FROM tblItems INNER JOIN tblSuppliers ON tblSuppliers.supCode = tblItems.supCode";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                while (rs.next()) {
                    String itemCode = rs.getString("itemCode");
                    String itemName = rs.getString("itemName");
                    String unit = rs.getString("unit");
                    float price = rs.getFloat("price");
                    boolean supplying = rs.getBoolean("supplying");
                    String itemSupCode = rs.getString("supCode");                             
                    String itemSupName = rs.getString("supName");                             
                    ItemDTO s = new ItemDTO(itemCode, itemName, itemSupCode, unit, price, supplying, itemSupName);
                    list.add(s);
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
        return list;
    }
    
    public static boolean checkItemCodeExist(String itemCode) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = MyConnection.makeConnection();

            if (conn != null) {
                String query = "SELECT itemCode FROM tblItems WHERE itemCode = ?";
                pst = conn.prepareStatement(query);   
                pst.setString(1, itemCode);
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

    public static int addItem(String itemCode, String itemName, String unit, float price, boolean supplying, String supCode) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;
        
        int result = 0;
        try {
            conn = MyConnection.makeConnection();
            if (conn != null) {
                String query = "INSERT tblItems VALUES(?,?,?,?,?,?)";
                pst = conn.prepareStatement(query);
                pst.setString(1, itemCode);
                pst.setString(2, itemName);
                pst.setString(3, unit);
                pst.setFloat(4, price);
                pst.setBoolean(5, supplying);
                pst.setString(6, supCode);
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

    public static int updateItem(String itemCode, String itemName, String unit, float price, boolean supplying, String supCode) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;       
        int result = 0;
        try {
            conn = MyConnection.makeConnection();
            if (conn != null) {
                String query = "UPDATE tblItems SET itemName=?,unit=?,price=?,supplying=?,supCode=? where itemCode=?";
                pst = conn.prepareStatement(query);
                pst.setString(1, itemName);
                pst.setString(2, unit);
                pst.setFloat(3, price);
                pst.setBoolean(4, supplying);
                pst.setString(5, supCode);
                pst.setString(6, itemCode);
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

    public static int deleteItem(String itemCode) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;     
        int result = 0;
        try {
            conn = MyConnection.makeConnection();
            if (conn != null) {
                String query = "DELETE FROM tblItems WHERE itemCode=?";
                pst = conn.prepareStatement(query);
                pst.setString(1, itemCode);
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
