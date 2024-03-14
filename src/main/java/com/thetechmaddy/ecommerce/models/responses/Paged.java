package com.thetechmaddy.ecommerce.models.responses;

import com.fasterxml.jackson.annotation.JsonView;
import com.thetechmaddy.ecommerce.models.JsonViews.ProductResponse;

import java.util.List;

@JsonView(ProductResponse.class)
public record Paged<T>(List<T> data, Integer page, Long total, Integer size) {
}
