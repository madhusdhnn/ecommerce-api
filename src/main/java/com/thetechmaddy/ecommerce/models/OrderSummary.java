package com.thetechmaddy.ecommerce.models;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.thetechmaddy.ecommerce.domains.orders.Order;
import com.thetechmaddy.ecommerce.models.JsonViews.GetOrderResponse;
import com.thetechmaddy.ecommerce.models.serializers.BigDecimalToDoubleTwoDecimalPlacesNumberSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummary {

    @JsonView(value = {GetOrderResponse.class})
    private long id;

    @JsonView(value = {GetOrderResponse.class})
    private OrderStatus status;

    @JsonView(value = {GetOrderResponse.class})
    @JsonSerialize(using = BigDecimalToDoubleTwoDecimalPlacesNumberSerializer.class)
    private BigDecimal netTotal;

    @JsonView(value = {GetOrderResponse.class})
    @JsonSerialize(using = BigDecimalToDoubleTwoDecimalPlacesNumberSerializer.class)
    private BigDecimal grosstTotal;

    public OrderSummary(Order order) {
        this.id = order.getId();
        this.status = order.getStatus();
        this.netTotal = order.getNetTotal();
        this.grosstTotal = order.getGrossTotal();
    }
}
