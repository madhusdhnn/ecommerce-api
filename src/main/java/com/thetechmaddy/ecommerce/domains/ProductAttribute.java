package com.thetechmaddy.ecommerce.domains;


import com.fasterxml.jackson.annotation.JsonView;
import com.thetechmaddy.ecommerce.models.JsonViews.ProductResponse;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_attribute")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductAttribute extends Timestamp {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(value = {ProductResponse.class})
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @Column(name = "attribute_name")
    @JsonView(value = {ProductResponse.class})
    private String attributeName;

    @Setter
    @Column(name = "attribute_value")
    @JsonView(value = {ProductResponse.class})
    private String attributeValue;
}