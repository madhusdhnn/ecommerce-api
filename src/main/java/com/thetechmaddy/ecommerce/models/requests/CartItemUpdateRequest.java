package com.thetechmaddy.ecommerce.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemUpdateRequest {

    private int quantity;

}
