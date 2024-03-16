package com.thetechmaddy.ecommerce.domains.users;

import com.thetechmaddy.ecommerce.domains.Audit;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user_addresses")
@EqualsAndHashCode(callSuper = true)
public class UserAddress extends Audit {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "is_default")
    private boolean defaultAddress;

    @Column(name = "address_1")
    private String addressOne;

    @Column(name = "address_2")
    private String addressTwo;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "zip_code")
    private String zipCode;

}
