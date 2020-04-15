package bll;

import dao.InvoiceDAO;
import model.Invoice;
import model.Order;

import java.util.List;
import java.util.NoSuchElementException;

public class InvoiceBLL {
    private InvoiceDAO invoiceDAO;

    public InvoiceBLL() {
        invoiceDAO = new InvoiceDAO();
    }

    public Invoice findById(int id) {
        Invoice st = invoiceDAO.findById(id);
        if (st == null) {
            throw new NoSuchElementException("The product with id =" + id + " was not found!");
        }
        return st;
    }

    public List<Invoice> findByOrder(Order order) {
        List<Invoice> result = invoiceDAO.findByOrder(order);
        if (result == null) {
            throw new NoSuchElementException("Invoice with order = " + order.getId() + " doesn't exist!");
        }
        return result;
    }
}
