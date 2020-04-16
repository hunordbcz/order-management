package dao;

import model.Pair;
import model.Product;
import util.OrderTypes;

import java.util.LinkedList;
import java.util.List;

public class ProductDAO extends AbstractDAO<Product> {
    public Product findByName(String name) {
        List<Pair<String, Object>> rules = new LinkedList<>();
        rules.add(new Pair<>("name", name));
        List<Product> response = this.select(rules, OrderTypes.ASC);
        if (response != null && !response.isEmpty()) {
            return this.select(rules, OrderTypes.ASC).get(0);
        }
        return null;
    }

    public Product findByName(Product product) {
        return this.findByName(product.getName());
    }

    public Boolean deleteByName(String name) {
        List<Pair<String, Object>> rules = new LinkedList<>();
        rules.add(new Pair<>("name", name));
        return this.delete(rules);
    }
}
