package dao;

import model.Invoice;
import model.Order;
import model.Pair;

import java.util.LinkedList;
import java.util.List;

public class OrderDAO extends AbstractDAO<Order> {
    public Order findById(int id, Invoice invoice) {
        this.previous = invoice;
        List<Pair<String, Object>> rules = new LinkedList<>();
        rules.add(new Pair<>("id", id));

        List<Order> response = select(rules);
        if (response != null && !response.isEmpty()) {
            return response.get(0);
        }
        return null;
    }
}
