package com.thetechmaddy.ecommerce.domains;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.thetechmaddy.ecommerce.models.JsonViews.CartResponse;
import com.thetechmaddy.ecommerce.models.JsonViews.ProductResponse;
import com.thetechmaddy.ecommerce.models.serializers.BigDecimalTwoDecimalPlacesSerializer;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NamedEntityGraph(name = "Product.attributes", attributeNodes = @NamedAttributeNode("attributes"))
public class Product extends Timestamp {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(value = {ProductResponse.class, CartResponse.class})
    private long id;

    @Column(name = "name", nullable = false)
    @JsonView(value = {ProductResponse.class, CartResponse.class})
    private String name;

    @Column(name = "description")
    @JsonView(value = {ProductResponse.class, CartResponse.class})
    private String description;

    @Setter
    @Column(name = "price")
    @JsonSerialize(using = BigDecimalTwoDecimalPlacesSerializer.class)
    @JsonView(value = {ProductResponse.class, CartResponse.class})
    private BigDecimal price;

    @JsonView(value = {ProductResponse.class, CartResponse.class})
    @Column(name = "is_available")
    private boolean available;

    @OneToOne(optional = false)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @JsonView(value = ProductResponse.class)
    private Category category;

    @EqualsAndHashCode.Exclude
    @JsonView(value = {ProductResponse.class})
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "product")
    private List<ProductAttribute> attributes = new ArrayList<>();

    public Product(String name, String description, BigDecimal price, boolean available, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.available = available;
        this.category = category;
    }

    public Product(String name, String description, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }
}
