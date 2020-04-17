package bll;

import dao.ClientDAO;
import model.Client;

import java.util.List;
import java.util.NoSuchElementException;

public class ClientBLL {
    private ClientDAO clientDAO;

    public ClientBLL() {
        clientDAO = new ClientDAO();
    }

    public Boolean insert(String name, String address) {
        Client client = new Client(name, address);
        return this.insert(client);
    }

    public Boolean insert(Client client) {
        if (!clientDAO.insert(client)) {
            //todo throw new Couldn't insert.
            return false;
        }
        return true;
    }

    public Boolean delete(String name) {
        if (!clientDAO.deleteByName(name)) {
            //todo throw new Couldn't insert.
            return false;
        }
        return true;
    }

    public Client findById(int id) {
        Client st = clientDAO.findById(id);
        if (st == null) {
            throw new NoSuchElementException("The product with id =" + id + " was not found!");
        }
        return st;
    }

    public Client findByName(String name) {
        Client st = clientDAO.findByName(name);
        if (st == null) {
            throw new NoSuchElementException("The product with id =" + name + " was not found!");
        }
        return st;
    }

    public List<Client> findAll() {
        List<Client> st = clientDAO.findAll();
        if (st == null) {
            throw new NoSuchElementException("The product with id =" + "all" + " was not found!");
        }
        return st;
    }
}
