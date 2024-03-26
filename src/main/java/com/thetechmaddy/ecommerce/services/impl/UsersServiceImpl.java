package com.thetechmaddy.ecommerce.services.impl;

import com.thetechmaddy.ecommerce.domains.users.User;
import com.thetechmaddy.ecommerce.domains.users.User.UserBuilder;
import com.thetechmaddy.ecommerce.domains.users.UserAddress;
import com.thetechmaddy.ecommerce.models.requests.CognitoUser;
import com.thetechmaddy.ecommerce.repositories.UserAddressesRepository;
import com.thetechmaddy.ecommerce.repositories.UsersRepository;
import com.thetechmaddy.ecommerce.services.CartsService;
import com.thetechmaddy.ecommerce.services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;


@Primary
@Service("usersServiceImpl")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UsersServiceImpl implements UsersService {

    private final CartsService cartsService;

    private final UsersRepository usersRepository;
    private final UserAddressesRepository userAddressesRepository;

    @Override
    public List<UserAddress> getUserAddresses(String userId) {
        return userAddressesRepository.findAllByUserId(userId);
    }

    @Override
    public User provisionUser(CognitoUser userDetails) {
        cartsService.createCart(userDetails.getCognitoSub());
        return createUser(userDetails);
    }

    private User createUser(CognitoUser userDetails) {
        UserBuilder builder = User.builder();

        User newUser = builder.firstName(userDetails.getFirstName())
                .lastName(userDetails.getLastName())
                .email(userDetails.getEmail())
                .cognitoSub(userDetails.getCognitoSub())
                .build();

        return usersRepository.save(newUser);
    }
}
