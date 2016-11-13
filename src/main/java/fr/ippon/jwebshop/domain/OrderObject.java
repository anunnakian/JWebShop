package fr.ippon.jwebshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A OrderObject.
 */
@Entity
@Table(name = "order_object")
public class OrderObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "order_number")
    private Integer orderNumber;

    @OneToMany(mappedBy = "orderObject")
    @JsonIgnore
    private Set<OrderLine> orderLines = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public OrderObject orderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Set<OrderLine> getOrderLines() {
        return orderLines;
    }

    public OrderObject orderLines(Set<OrderLine> orderLines) {
        this.orderLines = orderLines;
        return this;
    }

    public OrderObject addOrderLine(OrderLine orderLine) {
        orderLines.add(orderLine);
        orderLine.setOrderObject(this);
        return this;
    }

    public OrderObject removeOrderLine(OrderLine orderLine) {
        orderLines.remove(orderLine);
        orderLine.setOrderObject(null);
        return this;
    }

    public void setOrderLines(Set<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderObject orderObject = (OrderObject) o;
        if(orderObject.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, orderObject.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OrderObject{" +
            "id=" + id +
            ", orderNumber='" + orderNumber + "'" +
            '}';
    }
}
