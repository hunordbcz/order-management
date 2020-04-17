package model;

import bll.OrderBLL;
import bll.ProductBLL;

import java.util.Objects;

public class Invoice {
    private Integer id;
    private Product product;
    private Order _order;
    private Double product_quantity;

    public Invoice() {

    }

    public Invoice(Order order, Product product, Double quantity) {
        this._order = order;
        this.product = product;
        this.product_quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invoice invoice = (Invoice) o;
        return Objects.equals(id, invoice.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product getProductObj() {
        return product;
    }

    public Integer getProduct() {
        return product.getId();
    }

    public void setProduct(Integer product) {
        ProductBLL productBLL = new ProductBLL();
        this.product = productBLL.findById(product);
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer get_order() {
        return _order.getId();
    }

    public Order get_orderObj() {
        return _order;
    }

    public void set_order(Integer order) {
        OrderBLL orderBLL = new OrderBLL();
        this._order = orderBLL.findById(order, this);
    }

    public void set_order(Order order) {
        this._order = order;
    }

    public Double getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(Double product_quantity) {
        this.product_quantity = product_quantity;
    }
}
