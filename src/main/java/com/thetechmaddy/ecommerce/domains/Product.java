package com.thetechmaddy.ecommerce.domains;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.thetechmaddy.ecommerce.models.JsonViews.ProductResponse;
import com.thetechmaddy.ecommerce.models.serializers.BigDecimalTwoDecimalPlacesSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Product extends Audit {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(value = ProductResponse.class)
    private long id;

    @Column(name = "name", nullable = false)
    @JsonView(value = ProductResponse.class)
    private String name;

    @Column(name = "description")
    @JsonView(value = ProductResponse.class)
    private String description;

    @Column(name = "price")
    @JsonSerialize(using = BigDecimalTwoDecimalPlacesSerializer.class)
    @JsonView(value = ProductResponse.class)
    private BigDecimal price;

    @JsonView(value = ProductResponse.class)
    @Column(name = "is_available")
    private boolean available;

    @OneToOne(optional = false)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @JsonView(value = ProductResponse.class)
    private Category category;

    public Product(String name, String description, BigDecimal price, boolean available, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.available = available;
        this.category = category;
    }
}
