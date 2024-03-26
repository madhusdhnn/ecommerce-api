package com.thetechmaddy.ecommerce.models.carts;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.thetechmaddy.ecommerce.models.serializers.BigDecimalToDoubleTwoDecimalPlacesNumberSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class CheckoutData {

    private long cartId;

    @JsonSerialize(using = BigDecimalToDoubleTwoDecimalPlacesNumberSerializer.class)
    private BigDecimal netTotal;

    @JsonSerialize(using = BigDecimalToDoubleTwoDecimalPlacesNumberSerializer.class)
    private BigDecimal taxTotal;

    @JsonSerialize(using = BigDecimalToDoubleTwoDecimalPlacesNumberSerializer.class)
    private BigDecimal grossTotal;

}
