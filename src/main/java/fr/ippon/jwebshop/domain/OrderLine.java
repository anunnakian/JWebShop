package fr.ippon.jwebshop.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A OrderLine.
 */
@Entity
@Table(name = "order_line")
public class OrderLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "qty")
    private Integer qty;

    @ManyToOne
    private OrderObject orderObject;

    @ManyToOne
    private Product product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQty() {
        return qty;
    }

    public OrderLine qty(Integer qty) {
        this.qty = qty;
        return this;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public OrderObject getOrderObject() {
        return orderObject;
    }

    public OrderLine orderObject(OrderObject orderObject) {
        this.orderObject = orderObject;
        return this;
    }

    public void setOrderObject(OrderObject orderObject) {
        this.orderObject = orderObject;
    }

    public Product getProduct() {
        return product;
    }

    public OrderLine product(Product product) {
        this.product = product;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderLine orderLine = (OrderLine) o;
        if(orderLine.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, orderLine.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OrderLine{" +
            "id=" + id +
            ", qty='" + qty + "'" +
            '}';
    }
}
