package model;

import bll.OrderBLL;
import bll.ProductBLL;

import java.util.Objects;

public class Invoice {
    private Integer id;
    private Product product;
    private Order _order;
    private String series;
    private Integer nr;
    private Integer product_quantity;

    public Invoice() {

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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Integer product) {
        ProductBLL productBLL = new ProductBLL();
        this.product = productBLL.findById(product);
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order get_order() {
        return _order;
    }

    public void set_order(Integer order) {
        OrderBLL orderBLL = new OrderBLL();
        this._order = orderBLL.findById(order, this);
    }

    public void set_order(Order order) {
        this._order = order;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public Integer getNr() {
        return nr;
    }

    public void setNr(Integer nr) {
        this.nr = nr;
    }

    public Integer getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(Integer product_quantity) {
        this.product_quantity = product_quantity;
    }
}
