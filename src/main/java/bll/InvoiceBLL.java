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

    /**
     * Insert Invoice into DB by object
     *
     * @param invoice Given Invoice object
     * @return True on success | False on failure
     */
    public Boolean insert(Invoice invoice) {
        return invoiceDAO.insert(invoice);
    }

    /**
     * Find Invoice from DB by ID
     *
     * @param id Given id
     * @return Invoice
     */
    public Invoice findById(int id) {
        Invoice st = invoiceDAO.findById(id);
        if (st == null) {
            throw new NoSuchElementException("The invoice with id =" + id + " was not found!");
        }
        return st;
    }

    /**
     * Get Invoices of a given Order
     *
     * @param order Given order
     * @return List of Invoices
     */
    public List<Invoice> findByOrder(Order order) {
        List<Invoice> result = invoiceDAO.findByOrder(order);
        if (result == null) {
            throw new NoSuchElementException("Invoices with order = " + order.getId() + " were not found!");
        }
        return result;
    }
}
