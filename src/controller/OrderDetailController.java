package controller;

import entity.OrderDetail;
import model.OrderDetailModel;
import java.util.ArrayList;

public class OrderDetailController {

    private OrderDetailModel orderDetailModel;

    public OrderDetailController() {
        this.orderDetailModel = new OrderDetailModel();
    }

    public ArrayList<OrderDetail> getOrderDetailsByOrderId(int orderId) {
        return orderDetailModel.getOrderDetailsByOrderId(orderId);
    }

    public boolean addOrderDetail(OrderDetail orderDetail) {
        return orderDetailModel.insertOrderDetail(orderDetail);
    }

    public double calculateTotalAmount(int orderId) {
        ArrayList<OrderDetail> orderDetails = getOrderDetailsByOrderId(orderId);
        double totalAmount = 0;
        for (OrderDetail detail : orderDetails) {
            totalAmount += detail.getTotalPrice();
        }
        return totalAmount;
    }
}
