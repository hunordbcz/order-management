package dao;

import model.Pair;
import model.Product;
import util.OrderTypes;

import java.util.LinkedList;
import java.util.List;

public class ProductDAO extends AbstractDAO<Product> {

    /**
     * Finds a Product by it's name
     *
     * @param name The given Name
     * @return Product if found | NULL otherwise
     */
    public Product findByName(String name) {
        List<Pair<String, Object>> rules = new LinkedList<>();
        rules.add(new Pair<>("name", name));
        List<Product> response = this.select(rules, OrderTypes.ASC);
        if (response != null && !response.isEmpty()) {
            return this.select(rules, OrderTypes.ASC).get(0);
        }
        return null;
    }

    /**
     * Find a Product by a Product object's name
     *
     * @param product The reference Product
     * @return Product if found | NULL otherwise
     */
    public Product findByName(Product product) {
        return this.findByName(product.getName());
    }

    /**
     * Delete a Product by it's name
     *
     * @param name The given Name
     * @return True on success | False on failure
     */
    public Boolean deleteByName(String name) {
        List<Pair<String, Object>> rules = new LinkedList<>();
        rules.add(new Pair<>("name", name));
        return this.delete(rules);
    }
}
