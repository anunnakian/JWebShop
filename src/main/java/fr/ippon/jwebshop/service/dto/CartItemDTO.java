package fr.ippon.jwebshop.service.dto;

/**
 * A DTO representing an Order.
 */
public class CartItemDTO {

    private Long id;
    private int quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CartItemDTO{" +
            "id=" + id +
            ", quantity=" + quantity +
            '}';
    }
}
