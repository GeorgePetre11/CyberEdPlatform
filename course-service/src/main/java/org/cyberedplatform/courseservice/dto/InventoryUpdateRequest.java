package org.cyberedplatform.courseservice.dto;

public class InventoryUpdateRequest {
    private int quantityChange;

    public InventoryUpdateRequest() {}

    public InventoryUpdateRequest(int quantityChange) {
        this.quantityChange = quantityChange;
    }

    public int getQuantityChange() {
        return quantityChange;
    }

    public void setQuantityChange(int quantityChange) {
        this.quantityChange = quantityChange;
    }
}
