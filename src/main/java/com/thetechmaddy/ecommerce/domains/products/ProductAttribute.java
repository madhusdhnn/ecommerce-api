package com.thetechmaddy.ecommerce.domains.products;


import com.fasterxml.jackson.annotation.JsonView;
import com.thetechmaddy.ecommerce.domains.Audit;
import com.thetechmaddy.ecommerce.models.JsonViews;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_attributes")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductAttribute extends Audit {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(value = {JsonViews.ProductDetailResponse.class})
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @Column(name = "attribute_name")
    @JsonView(value = {JsonViews.ProductDetailResponse.class})
    private String attributeName;

    @Setter
    @Column(name = "attribute_value")
    @JsonView(value = {JsonViews.ProductDetailResponse.class})
    private String attributeValue;

}