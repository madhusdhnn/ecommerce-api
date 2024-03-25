package com.thetechmaddy.ecommerce.domains;

import com.fasterxml.jackson.annotation.JsonView;
import com.thetechmaddy.ecommerce.models.JsonViews.ProductResponse;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "categories")
public class Category extends Audit {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    @JsonView(value = ProductResponse.class)
    private String name;

}
