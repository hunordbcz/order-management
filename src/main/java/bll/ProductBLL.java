package bll;

import dao.ProductDAO;
import model.Product;

import java.util.List;
import java.util.NoSuchElementException;

public class ProductBLL {
    private final ProductDAO productDAO;

    public ProductBLL() {
        productDAO = new ProductDAO();
    }

    /**
     * Insert product into DB by Name, Quantity and Price
     *
     * @param name     Given name
     * @param quantity Given quantity
     * @param price    Given price
     * @return True on success | False on failure
     */
    public Boolean insert(String name, Double quantity, Double price) {
        Product product = new Product(name, quantity, price);
        return this.insert(product);
    }

    /**
     * Insert product into DB by Object
     *
     * @param product The given object
     * @return True on success | False on failure
     */
    public Boolean insert(Product product) {
        Product oldProduct = productDAO.findByName(product);
        if (oldProduct != null) {
            oldProduct.addQuantity(product.getQuantity());
            return productDAO.update(oldProduct);
        }

        return productDAO.insert(product);
    }

    /**
     * Delete Product from DB by name
     *
     * @param name Given name
     * @return True on success | False on failure
     */
    public Boolean delete(String name) {
        return productDAO.deleteByName(name);
    }

    /**
     * Find product from DB by ID
     *
     * @param id Given id
     * @return Product
     */
    public Product findById(int id) {
        Product st = productDAO.findById(id);
        if (st == null) {
            throw new NoSuchElementException("The product with id =" + id + " was not found!");
        }
        return st;
    }

    /**
     * Find product from DB by Name
     *
     * @param name Given name
     * @return Product
     */
    public Product findByName(String name) {
        Product st = productDAO.findByName(name);
        if (st == null) {
            throw new NoSuchElementException("The product with name =" + name + " was not found!");
        }
        return st;
    }

    /**
     * Find all the product in DB
     *
     * @return List of Products
     */
    public List<Product> findAll() {
        List<Product> st = productDAO.findAll();
        if (st == null) {
            throw new NoSuchElementException("No products were found!");
        }
        return st;
    }

    /**
     * Update a product in the DB
     *
     * @param product Given product
     * @return True on success | False on failure
     */
    public Boolean update(Product product) {
        return productDAO.update(product);
    }
}
