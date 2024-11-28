package model;

import entity.OrderDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDetailModel {

    public ArrayList<OrderDetail> getOrderDetailsByOrderId(int orderId) {
        ArrayList<OrderDetail> orderDetails = new ArrayList<>();
        String sql = "SELECT * FROM OrderDetails WHERE OrderID = ?";

        try (Connection conn = connection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, orderId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int orderDetailID = rs.getInt("OrderDetailID");
                String productID = rs.getString("ProductID");
                int quantity = rs.getInt("Quantity");
                double unitPrice = rs.getDouble("UnitPrice");
                double totalPrice = rs.getDouble("TotalPrice");

                OrderDetail detail = new OrderDetail(orderDetailID, orderId, productID, quantity, unitPrice, totalPrice);
                orderDetails.add(detail);
            }

        } catch (SQLException ex) {
            System.out.println("Error retrieving OrderDetails: " + ex.getMessage());
        }

        return orderDetails;
    }

    public boolean insertOrderDetail(OrderDetail orderDetail) {
        String sql = "INSERT INTO OrderDetails(OrderID, ProductID, Quantity, UnitPrice, TotalPrice) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = connection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setInt(1, orderDetail.getOrderID());
            pst.setString(2, orderDetail.getProductID());
            pst.setInt(3, orderDetail.getQuantity());
            pst.setDouble(4, orderDetail.getUnitPrice());
            pst.setDouble(5, orderDetail.getTotalPrice());

            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException ex) {
            System.out.println("Error inserting OrderDetail: " + ex.getMessage());
            return false;
        }
    }
    
    public float sum(int s){
        String sql = "  select  sum([TotalPrice]) from [OrderDetails]  where [OrderID] = ?";

        try (Connection conn = connection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, s);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                return rs.getFloat(1);
            }

        } catch (SQLException ex) {
            System.out.println("Error retrieving OrderDetails: " + ex.getMessage());
        }
        return 0;
    }
}
