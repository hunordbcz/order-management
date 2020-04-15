package model;

import bll.OrderBLL;
import bll.ProductBLL;

public class Invoice {
    private Integer id;
    //    private Order x_order;
//    private Product x_product;
//    private Integer product_id;
    private Product product;
    private Order _order;
    private String series;
    private Integer nr;
    private Integer product_quantity;

    public Invoice() {

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
        this._order = orderBLL.findById(order);
    }

//    public void setProduct_id(Integer product_id) {
//        this.product_id = product_id;
//        ProductBLL productBLL = new ProductBLL();
//        this.product = productBLL.findById(product_id);
//    }

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
