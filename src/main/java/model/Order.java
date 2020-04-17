package model;

import bll.ClientBLL;

import java.util.LinkedList;
import java.util.List;

public class Order {
    private Integer id;
    private Client client;
    private List<Invoice> x_invoices;

    public Order() {
        x_invoices = new LinkedList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClient() {
        return client.getId();
    }

    public Client getClientObj() {
        return client;
    }

    public void setClient(Integer client) {
        ClientBLL clientBLL = new ClientBLL();
        this.client = clientBLL.findById(client);
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Invoice> getX_invoices() {
        return x_invoices;
    }

    private Boolean containsInvoice(Invoice invoice) {
        for (Invoice inv : x_invoices) {
            if (inv.equals(invoice)) {
                return true;
            }
        }
        return false;
    }

    public void addX_Invoices(List<Invoice> x_invoices) {
        for (Invoice invoice : x_invoices) {
            if (!containsInvoice(invoice)) {
                this.x_invoices.add(invoice);
            }
        }
    }

    public void addX_Invoices(Invoice invoice) {
        this.x_invoices.add(invoice);
    }
}
