package bll;

import dao.OrderDAO;
import exceptions.OutOfStock;
import model.Client;
import model.Invoice;
import model.Order;
import model.Product;
import presentation.Controller;

import java.util.NoSuchElementException;

public class OrderBLL {
    private OrderDAO orderDAO;

    public OrderBLL() {
        orderDAO = new OrderDAO();
    }

    public Boolean make(Client client, Product product, Double quantity) throws OutOfStock {
        if (!orderDAO.make(client, product, quantity)) {
            Controller.outOfStock();
        }
        Controller.printOrder(client, product, quantity);
        return true;
    }

    public Order getLast() {
        Order st = orderDAO.findLast();
        if (st == null) {
            throw new NoSuchElementException("The student with id =" + "last" + " was not found!");
        }
        return st;
    }

    public Boolean insert(Order order) {
        if (!orderDAO.insert(order)) {
            //todo throw new Couldn't insert.
            return false;
        }
        return true;
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
