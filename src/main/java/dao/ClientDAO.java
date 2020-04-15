package dao;

import model.Client;
import model.Pair;

import java.util.LinkedList;
import java.util.List;

public class ClientDAO extends AbstractDAO<Client> {
    public Boolean deleteByName(String name) {
        List<Pair<String, Object>> rules = new LinkedList<>();
        rules.add(new Pair<>("name", name));
        return this.delete(rules);
    }
}
