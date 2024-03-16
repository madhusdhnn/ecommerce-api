package com.thetechmaddy.ecommerce.domains.products;


import com.thetechmaddy.ecommerce.domains.Audit;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

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
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @Column(name = "attribute_name")
    private String attributeName;

    @Setter
    @Column(name = "attribute_value")
    private String attributeValue;

    public ProductAttribute(String key, String value, Product product) {
        super(OffsetDateTime.now(), OffsetDateTime.now());
        this.attributeName = key;
        this.attributeValue = value;
        this.product = product;
    }
}