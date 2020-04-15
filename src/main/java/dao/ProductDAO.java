package dao;

import model.Pair;
import model.Product;

import java.util.LinkedList;
import java.util.List;

public class ProductDAO extends AbstractDAO<Product> {
    public Product findByName(Product product) {
        List<Pair<String, Object>> rules = new LinkedList<>();
        rules.add(new Pair<>("name", product.getName()));
        List<Product> response = this.select(rules);
        if (response != null && !response.isEmpty()) {
            return this.select(rules).get(0);
        }
        return null;
    }

    public Boolean deleteByName(String name) {
        List<Pair<String, Object>> rules = new LinkedList<>();
        rules.add(new Pair<>("name", name));
        return this.delete(rules);
    }
}
