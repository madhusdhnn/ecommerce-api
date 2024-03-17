package com.thetechmaddy.ecommerce.models.mappers;

import com.thetechmaddy.ecommerce.domains.carts.CartItem;
import com.thetechmaddy.ecommerce.domains.orders.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Component
@Mapper(componentModel = SPRING)
public interface CartItemToOrderItemMapper {

    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "product", target = "product")
    @Mapping(source = "product.unitPrice", target = "unitPrice")
    @Mapping(
            expression = "java(cartItem.getProduct().getUnitPrice().multiply(new BigDecimal(cartItem.getQuantity())))",
            target = "netAmount"
    )
    @Mapping(
            expression = "java(cartItem.getProduct().getGrossAmount().multiply(new BigDecimal(cartItem.getQuantity())))",
            target = "grossAmount"
    )
    @Mapping(
            expression = "java(cartItem.getProduct().getTaxAmount().multiply(new BigDecimal(cartItem.getQuantity())))",
            target = "taxAmount"
    )
    @Mapping(source = "product.taxPercentage", target = "taxPercentage")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "order", ignore = true)
    OrderItem cartItemToOrderItem(CartItem cartItem);

}
