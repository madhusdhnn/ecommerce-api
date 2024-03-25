package com.thetechmaddy.ecommerce.domains.users;

import com.thetechmaddy.ecommerce.domains.Audit;
import com.thetechmaddy.ecommerce.models.requests.CognitoUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user_pool")
public class User extends Audit {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "cognito_sub", unique = true)
    private String cognitoSub;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    public User(String cognitoSub, String email, String firstName, String lastName) {
        super(OffsetDateTime.now(), OffsetDateTime.now());
        this.cognitoSub = cognitoSub;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(CognitoUser cognitoUser) {
        super(OffsetDateTime.now(), OffsetDateTime.now());
        this.cognitoSub = cognitoUser.getCognitoSub();
        this.email = cognitoUser.getEmail();
        this.firstName = cognitoUser.getFirstName();
        this.lastName = cognitoUser.getLastName();
    }
}
