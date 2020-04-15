package model;

public class Invoice {
    private Integer id;
    private Integer product_id;
    private String series;
    private Integer nr;
    private Integer product_quantity;

    public Invoice(Integer product_id, String series, Integer nr, Integer product_quantity) {
        this.product_id = product_id;
        this.series = series;
        this.nr = nr;
        this.product_quantity = product_quantity;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", product_id=" + product_id +
                ", series='" + series + '\'' +
                ", nr=" + nr +
                ", product_quantity=" + product_quantity +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
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
