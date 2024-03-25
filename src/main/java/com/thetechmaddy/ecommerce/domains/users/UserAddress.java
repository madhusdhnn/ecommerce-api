package com.thetechmaddy.ecommerce.domains.users;

import com.thetechmaddy.ecommerce.domains.Address;
import com.thetechmaddy.ecommerce.domains.Audit;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user_addresses")
public class UserAddress extends Address {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "is_default")
    private boolean defaultAddress;

}
