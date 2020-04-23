package dao;

import model.Invoice;
import model.Order;
import model.Pair;
import util.OrderTypes;

import java.util.LinkedList;
import java.util.List;

public class InvoiceDAO extends AbstractDAO<Invoice> {

    /**
     * Searches for invoices by an Order
     *
     * @param order The given order
     * @return List of Invoices that were found
     */
    public List<Invoice> findByOrder(Order order) {
        this.previous = order;

        List<Pair<String, Object>> reference = new LinkedList<>();
        reference.add(new Pair<>("_order", order.getId()));
        return select(reference, OrderTypes.ASC);
    }
}
