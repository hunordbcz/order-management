package bll;

import dao.ProductDAO;
import model.Product;

import java.util.List;
import java.util.NoSuchElementException;

public class ProductBLL {
    private ProductDAO productDAO;

    public ProductBLL() {
        productDAO = new ProductDAO();
    }

    public Boolean insert(String name, Double quantity, Double price) {
        Product product = new Product(name, quantity, price);
        return this.insert(product);
    }

    public Boolean insert(Product product) {
        Product oldProduct = productDAO.findByName(product);
        if (oldProduct != null) {
            oldProduct.addQuantity(product.getQuantity());
            return productDAO.update(oldProduct);
        }

        if (!productDAO.insert(product)) {
            //todo throw new Couldn't insert.
            return false;
        }
        return true;
    }

    public Boolean delete(String name) {
        if (!productDAO.deleteByName(name)) {
            //todo throw new Couldn't insert.
            return false;
        }
        return true;
    }

    public Product findById(int id) {
        Product st = productDAO.findById(id);
        if (st == null) {
            throw new NoSuchElementException("The product with id =" + id + " was not found!");
        }
        return st;
    }

    public Product findByName(String name) {
        Product st = productDAO.findByName(name);
        if (st == null) {
            throw new NoSuchElementException("The product with id =" + name + " was not found!");
        }
        return st;
    }

    public List<Product> findAll() {
        List<Product> st = productDAO.findAll();
        if (st == null) {
            throw new NoSuchElementException("The product with id =" + "all" + " was not found!");
        }
        return st;
    }

    public Boolean update(Product product) {
        return productDAO.update(product);
    }
}
