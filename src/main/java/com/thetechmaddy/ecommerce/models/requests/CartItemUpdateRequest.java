package com.thetechmaddy.ecommerce.models.requests;

import com.thetechmaddy.ecommerce.models.carts.CartItemStatus;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.thetechmaddy.ecommerce.models.carts.CartItemStatus.SELECTED;
import static com.thetechmaddy.ecommerce.models.carts.CartItemStatus.UN_SELECTED;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemUpdateRequest {

    @Min(value = 1, message = "Minimum required quantity value is 1")
    private Integer quantity;
    private CartItemStatus cartItemStatus;

    public CartItemUpdateRequest(Integer quantity) {
        this.quantity = quantity;
    }

    public CartItemUpdateRequest(boolean selected) {
        this.cartItemStatus = selected ? SELECTED : UN_SELECTED;
    }
}
