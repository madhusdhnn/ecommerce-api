package com.thetechmaddy.ecommerce.models.requests;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemUpdateRequest {

    @Min(value = 1, message = "Minimum required quantity value is 1")
    private Integer quantity;
    private Boolean isSelected;

    public CartItemUpdateRequest(Integer quantity) {
        this.quantity = quantity;
    }

    public CartItemUpdateRequest(boolean selected) {
        this.isSelected = selected;
    }
}
