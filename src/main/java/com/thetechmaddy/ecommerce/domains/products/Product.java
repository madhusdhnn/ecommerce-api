package com.thetechmaddy.ecommerce.domains.products;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.thetechmaddy.ecommerce.domains.Audit;
import com.thetechmaddy.ecommerce.domains.Category;
import com.thetechmaddy.ecommerce.models.JsonViews.*;
import com.thetechmaddy.ecommerce.models.serializers.BigDecimalToDoubleTwoDecimalPlacesNumberSerializer;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.thetechmaddy.ecommerce.utils.NumberUtils.formatAsTwoDecimalPlaces;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(name = "Products", attributeNodes = {
        @NamedAttributeNode("attributes"),
        @NamedAttributeNode("category")
})
public class Product extends Audit {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(value = {OrderInitiateResponse.class, ProductDetailResponse.class, ProductResponse.class, CartResponse.class, CheckoutCartResponse.class})
    private long id;

    @Column(name = "name", nullable = false)
    @JsonView(value = {OrderInitiateResponse.class, ProductDetailResponse.class, ProductResponse.class, CartResponse.class, CheckoutCartResponse.class})
    private String name;

    @Column(name = "description")
    @JsonView(value = {OrderInitiateResponse.class, ProductDetailResponse.class, ProductResponse.class, CartResponse.class, CheckoutCartResponse.class})
    private String description;

    @Column(name = "sku_code")
    @JsonView(value = {OrderInitiateResponse.class, ProductDetailResponse.class, ProductResponse.class, CartResponse.class, CheckoutCartResponse.class})
    private String skuCode;

    @Setter
    @Column(name = "unit_price")
    @JsonSerialize(using = BigDecimalToDoubleTwoDecimalPlacesNumberSerializer.class)
    @JsonView(value = {ProductDetailResponse.class, ProductResponse.class, CartResponse.class, CheckoutCartResponse.class})
    private BigDecimal unitPrice;

    @Setter
    @Column(name = "gross_amount")
    @JsonSerialize(using = BigDecimalToDoubleTwoDecimalPlacesNumberSerializer.class)
    @JsonView(value = {ProductDetailResponse.class, ProductResponse.class, CartResponse.class, CheckoutCartResponse.class})
    private BigDecimal grossAmount;

    @Column(name = "tax_percentage")
    @JsonSerialize(using = BigDecimalToDoubleTwoDecimalPlacesNumberSerializer.class)
    @JsonView(value = {ProductDetailResponse.class, ProductResponse.class, CartResponse.class, CheckoutCartResponse.class})
    private BigDecimal taxPercentage;

    @Setter
    @Column(name = "tax_amount")
    @JsonSerialize(using = BigDecimalToDoubleTwoDecimalPlacesNumberSerializer.class)
    @JsonView(value = {ProductDetailResponse.class, ProductResponse.class, CartResponse.class, CheckoutCartResponse.class})
    private BigDecimal taxAmount;

    @Setter
    @JsonView(value = {ProductDetailResponse.class, ProductResponse.class, CartResponse.class, CheckoutCartResponse.class})
    @Column(name = "stock_quantity")
    private int stockQuantity;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @JsonView(value = ProductResponse.class)
    private Category category;

    @Setter
    @EqualsAndHashCode.Exclude
    @JsonView(value = {ProductDetailResponse.class})
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "product")
    private List<ProductAttribute> attributes = new ArrayList<>();

    public Product(String name, String description, BigDecimal unitPrice, boolean available, Category category) {
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;
        this.skuCode = name;
        this.taxPercentage = new BigDecimal("12");
        this.taxAmount = formatAsTwoDecimalPlaces(this.unitPrice.multiply(new BigDecimal("0.12")));
        this.grossAmount = formatAsTwoDecimalPlaces(this.unitPrice.add(this.taxAmount));
        this.stockQuantity = available ? 10 : 0;
        this.category = category;
    }

    public Product(String name, String description, BigDecimal unitPrice, int quantity) {
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;
        this.stockQuantity = quantity;
    }

    public boolean isInStock() {
        return this.stockQuantity > 0;
    }

    public void decrementStockQuantity(int reductionCount) {
        int quantity = this.stockQuantity;

        int newQuantity;
        this.stockQuantity = (newQuantity = quantity - reductionCount) <= 0 ? 0 : newQuantity;
    }

    public void incrementStockQuantity(int incrementCount) {
        this.stockQuantity += incrementCount;
    }

    public boolean hasInsufficientQuantity(int quantity) {
        return this.stockQuantity < quantity;
    }
}
