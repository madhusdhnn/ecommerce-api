package com.thetechmaddy.ecommerce.models.carts;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.thetechmaddy.ecommerce.domains.carts.CartItem;
import com.thetechmaddy.ecommerce.models.JsonViews.CheckoutCartResponse;
import com.thetechmaddy.ecommerce.models.serializers.BigDecimalToDoubleTwoDecimalPlacesNumberSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class CheckoutData {

    @JsonView(CheckoutCartResponse.class)
    private List<CartItem> cartItems;

    @JsonView(CheckoutCartResponse.class)
    @JsonSerialize(using = BigDecimalToDoubleTwoDecimalPlacesNumberSerializer.class)
    private BigDecimal netTotal;

    @JsonView(CheckoutCartResponse.class)
    @JsonSerialize(using = BigDecimalToDoubleTwoDecimalPlacesNumberSerializer.class)
    private BigDecimal taxTotal;

    @JsonView(CheckoutCartResponse.class)
    @JsonSerialize(using = BigDecimalToDoubleTwoDecimalPlacesNumberSerializer.class)
    private BigDecimal grossTotal;

}
