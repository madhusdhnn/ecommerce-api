package com.thetechmaddy.ecommerce.domains;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import static com.thetechmaddy.ecommerce.models.validations.ValidationConstants.NOT_NULL_MESSAGE_SUFFIX;

@Getter
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address extends Audit {

    @NotNull(message = "address1" + " " + NOT_NULL_MESSAGE_SUFFIX)
    @Size(min = 1, message = "address1 should have at least 1 character")
    @Column(name = "address_1")
    private String addressOne;

    @Column(name = "address_2")
    private String addressTwo;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @NotNull(message = "zipCode" + " " + NOT_NULL_MESSAGE_SUFFIX)
    @Pattern(regexp = "^[0-9]{6}$", message = "zipCode must have 6 digits")
    @Column(name = "zip_code")
    private String zipCode;
}
