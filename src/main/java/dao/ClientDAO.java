package dao;

import model.Client;
import model.Pair;
import util.OrderTypes;

import java.util.LinkedList;
import java.util.List;

public class ClientDAO extends AbstractDAO<Client> {
    public Boolean deleteByName(String name) {
        List<Pair<String, Object>> rules = new LinkedList<>();
        rules.add(new Pair<>("name", name));
        return this.delete(rules);
    }

    public Client findByName(String name) {
        List<Pair<String, Object>> rules = new LinkedList<>();
        rules.add(new Pair<>("name", name));
        List<Client> response = this.select(rules, OrderTypes.ASC);
        if (response != null && !response.isEmpty()) {
            return this.select(rules, OrderTypes.ASC).get(0);
        }
        return null;
    }
}
