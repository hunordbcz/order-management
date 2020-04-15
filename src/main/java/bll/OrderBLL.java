package bll;

import dao.OrderDAO;
import model.Invoice;
import model.Order;

import java.util.NoSuchElementException;

public class OrderBLL {
    private OrderDAO orderDAO;

    public OrderBLL() {
        orderDAO = new OrderDAO();
    }

    public Order findById(int id) {

        Order st = orderDAO.findById(id);
        if (st == null) {
            throw new NoSuchElementException("The student with id =" + id + " was not found!");
        }
        return st;
    }

    public Order findById(int id, Invoice invoice) {
        Order st = orderDAO.findById(id, invoice);
        if (st == null) {
            throw new NoSuchElementException("The student with id =" + id + " was not found!");
        }
        return st;
    }
}
