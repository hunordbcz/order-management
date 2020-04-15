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

    public Client getClient() {
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

    public void setX_invoices(List<Invoice> x_invoices) {
        this.x_invoices = x_invoices;
    }
}
