package dao;

import bll.InvoiceBLL;
import bll.ProductBLL;
import model.*;
import util.OrderTypes;

import java.util.LinkedList;
import java.util.List;

public class OrderDAO extends AbstractDAO<Order> {

    /**
     * Searches for an Order by ID and if found, add invoice to it's invoice list
     * ( Used to maintain the same objects, not to create new ones )
     *
     * @param id      The given ID
     * @param invoice The given Invoice to add later on
     * @return Order if found | NULL otherwise
     */
    public Order findById(int id, Invoice invoice) {
        this.previous = invoice;
        List<Pair<String, Object>> rules = new LinkedList<>();
        rules.add(new Pair<>("id", id));

        List<Order> response = select(rules, OrderTypes.ASC);
        if (response != null && !response.isEmpty()) {
            return response.get(0);
        }
        return null;
    }

    /**
     * Make a new order for a Client, with Quantity * Products
     *
     * @param client   The given client
     * @param product  The given product
     * @param quantity The given quantity
     * @return True on success | False on failure
     */
    public boolean make(Client client, Product product, Double quantity) {
        ProductBLL productBLL = new ProductBLL();
        InvoiceBLL invoiceBLL = new InvoiceBLL();

        if (quantity > product.getQuantity()) {
            return false;
        }
        product.addQuantity(-quantity);
        productBLL.update(product);
        Order order = new Order();
        order.setClient(client);
        this.insert(order);
        order = this.findLast();

        Invoice invoice = new Invoice(order, product, quantity);

        invoiceBLL.insert(invoice);

        return true;
    }
}
