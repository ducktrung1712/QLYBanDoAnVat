/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import entity.Orders;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author daica
 */
public class Ordermodel {
    public ArrayList<Orders> DSOrders() {
        ArrayList<Orders> ListOrders = new ArrayList<>();
        String sql = "SELECT * FROM Orders";

        try (Connection conn = connection.getConnection();
             Statement stas = conn.createStatement();
             ResultSet rs = stas.executeQuery(sql)) {

            while (rs.next()) {
                Orders tb = new Orders(rs.getInt("OrderID"), rs.getDate("OrderDate"), rs.getFloat("TotalAmount"), rs.getString("Status"));
                ListOrders.add(tb);
            }
        } catch (SQLException ex) {
            System.out.println("Error! " + ex.toString());
        }

        return ListOrders;
    }
    public int Insert(Orders insertOrder) {
        PreparedStatement sttm = null;
        Connection conn = null;
        try {
            String sql = "INSERT INTO Orders(OrderDate,TotalAmount, Status) VALUES (?, ?, ?)";
            conn = connection.getConnection();
            sttm = conn.prepareStatement(sql);
            sttm.setDate(1, (Date) insertOrder.getOrderDate());
            sttm.setFloat(2, insertOrder.getTotalAmount());
            sttm.setString(3, insertOrder.getStatus());

            if (sttm.executeUpdate() > 0) {
                System.out.println("Insert thành công");
                return 1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Ordermodel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (sttm != null) {
                try {
                    sttm.close();
                } catch (SQLException e) {
                    Logger.getLogger(Ordermodel.class.getName()).log(Level.SEVERE, null, e);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    Logger.getLogger(Ordermodel.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
        return -1;
    }
    public boolean Update(Orders updateOrder) {
    String sql = "UPDATE Orders SET Status = ? WHERE OrderID = ?";
    try (Connection conn = connection.getConnection();
         PreparedStatement sttm = conn.prepareStatement(sql)) {
        sttm.setString(1, updateOrder.getStatus());
        sttm.setInt(2, updateOrder.getOrderID());
        return sttm.executeUpdate() > 0;
    } catch (SQLException ex) {
        Logger.getLogger(Categorymodel.class.getName()).log(Level.SEVERE, null, ex);
    }
    return false;
}
     public boolean Delete(int OrderID) {
        String sqlDeleteOrderDetails = "DELETE FROM OrderDetails WHERE OrderID = ?";
        String sqlDeleteOrder = "DELETE FROM Orders WHERE OrderID = ?";
        Connection conn = null;
        PreparedStatement sttmDeleteOrderDetails = null;
        PreparedStatement sttmDeleteOrder = null;

        try {
            conn = connection.getConnection();
            conn.setAutoCommit(false); // Begin transaction

            // Delete order details
            sttmDeleteOrderDetails = conn.prepareStatement(sqlDeleteOrderDetails);
            sttmDeleteOrderDetails.setInt(1, OrderID);
            sttmDeleteOrderDetails.executeUpdate();

            // Delete order
            sttmDeleteOrder = conn.prepareStatement(sqlDeleteOrder);
            sttmDeleteOrder.setInt(1, OrderID);
            if (sttmDeleteOrder.executeUpdate() > 0) {
                conn.commit(); // Commit transaction
                System.out.println("Delete thành công");
                return true;
            } else {
                conn.rollback(); // Rollback transaction if order delete fails
            }
        } catch (SQLException ex) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback transaction if any exception occurs
                } catch (SQLException e) {
                    Logger.getLogger(Ordermodel.class.getName()).log(Level.SEVERE, null, e);
                }
            }
            Logger.getLogger(Ordermodel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (sttmDeleteOrderDetails != null) {
                try {
                    sttmDeleteOrderDetails.close();
                } catch (SQLException e) {
                    Logger.getLogger(Ordermodel.class.getName()).log(Level.SEVERE, null, e);
                }
            }
            if (sttmDeleteOrder != null) {
                try {
                    sttmDeleteOrder.close();
                } catch (SQLException e) {
                    Logger.getLogger(Ordermodel.class.getName()).log(Level.SEVERE, null, e);
                }
            }
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    Logger.getLogger(Ordermodel.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
        return false;
    }
}
