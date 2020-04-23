package bll;

import dao.OrderDAO;
import model.Client;
import model.Invoice;
import model.Order;
import model.Product;
import presentation.Controller;

import java.util.List;
import java.util.NoSuchElementException;

public class OrderBLL {
    private OrderDAO orderDAO;

    public OrderBLL() {
        orderDAO = new OrderDAO();
    }

    /**
     * Make a new Order for a Client with a given Quantity of Products
     *
     * @param client   Given client
     * @param product  Given product
     * @param quantity Given quantity
     * @return True on success | False on failure
     */
    public Boolean make(Client client, Product product, Double quantity) {
        if (!orderDAO.make(client, product, quantity)) {
            Controller.outOfStock();
        }

        Controller.printOrder(this.getLast());
        return true;
    }

    /**
     * Get the last Order from the DB
     *
     * @return Order
     */
    public Order getLast() {
        Order st = orderDAO.findLast();
        if (st == null) {
            throw new NoSuchElementException("No order was found");
        }
        return st;
    }

    /**
     * Insert Order into DB by object
     *
     * @param order Given order object
     * @return True on success | False on failure
     */
    public Boolean insert(Order order) {
        if (!orderDAO.insert(order)) {
            //todo throw new Couldn't insert.
            return false;
        }
        return true;
    }

    /**
     * Find Order from DB by ID
     *
     * @param id Given id
     * @return True on success | False on failure
     */
    public Order findById(int id) {
        Order st = orderDAO.findById(id);
        if (st == null) {
            throw new NoSuchElementException("The order with id =" + id + " was not found!");
        }
        return st;
    }

    /**
     * Find Order from DB by ID with adding Invoice to it's list
     *
     * @param id      Given id
     * @param invoice Given invoice
     * @return Order
     */
    public Order findById(int id, Invoice invoice) {
        Order st = orderDAO.findById(id, invoice);
        if (st == null) {
            throw new NoSuchElementException("The order with id =" + id + " was not found!");
        }
        return st;
    }

    /**
     * Get every Order from DB
     *
     * @return List of Orders
     */
    public List<Order> findAll() {
        List<Order> st = orderDAO.findAll();
        if (st == null) {
            throw new NoSuchElementException("No orders were found!");
        }
        return st;
    }
}
