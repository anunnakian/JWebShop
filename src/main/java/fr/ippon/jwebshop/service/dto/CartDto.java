package fr.ippon.jwebshop.service.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * A DTO representing an Order.
 */
public class CartDTO {

    private float subTotal;
    private float totalCost;
    private List<CartItemDTO> items;

    public CartDTO() {
        items = new ArrayList<>();
    }

    public float getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(float subTotal) {
        this.subTotal = subTotal;
    }

    public List<CartItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemDTO> items) {
        this.items = items;
    }

    public float getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(float totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public String toString() {
        return "CartDTO{" +
            "subTotal=" + subTotal +
            ", totalCost=" + totalCost +
            ", items=" + items +
            '}';
    }
}
