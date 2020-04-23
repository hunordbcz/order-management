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

    /**
     * Insert Client into DB by Name and Address
     *
     * @param name    Given name
     * @param address Given address
     * @return True on success | False on failure
     */
    public Boolean insert(String name, String address) {
        Client client = new Client(name, address);
        return this.insert(client);
    }

    /**
     * Insert Client into DB by object
     *
     * @param client Given client object
     * @return True on success | False on failure
     */
    public Boolean insert(Client client) {
        return clientDAO.insert(client);
    }

    /**
     * Delete Client from DB by name
     *
     * @param name Given name
     * @return True on success | False on failure
     */
    public Boolean delete(String name) {
        return clientDAO.deleteByName(name);
    }

    /**
     * Find Client from DB by ID
     *
     * @param id Given ID
     * @return Client
     */
    public Client findById(int id) {
        Client st = clientDAO.findById(id);
        if (st == null) {
            throw new NoSuchElementException("The client with id =" + id + " was not found!");
        }
        return st;
    }

    /**
     * Find Client from DB by Name
     *
     * @param name Given name
     * @return Client
     */
    public Client findByName(String name) {
        Client st = clientDAO.findByName(name);
        if (st == null) {
            throw new NoSuchElementException("The client with id =" + name + " was not found!");
        }
        return st;
    }

    /**
     * Get every Client from DB
     *
     * @return List of Clients
     */
    public List<Client> findAll() {
        List<Client> st = clientDAO.findAll();
        if (st == null) {
            throw new NoSuchElementException("No client was not found!");
        }
        return st;
    }
}
