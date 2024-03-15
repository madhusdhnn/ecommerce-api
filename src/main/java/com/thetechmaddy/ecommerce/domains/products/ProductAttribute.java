package com.thetechmaddy.ecommerce.domains.products;


import com.thetechmaddy.ecommerce.domains.Audit;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_attribute")
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
}