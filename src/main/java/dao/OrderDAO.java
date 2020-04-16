package dao;

import bll.InvoiceBLL;
import bll.ProductBLL;
import model.*;
import util.OrderTypes;

import java.util.LinkedList;
import java.util.List;

public class OrderDAO extends AbstractDAO<Order> {
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
