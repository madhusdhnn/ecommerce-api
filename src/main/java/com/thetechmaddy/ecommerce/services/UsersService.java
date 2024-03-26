package com.thetechmaddy.ecommerce.services;

import com.thetechmaddy.ecommerce.domains.users.User;
import com.thetechmaddy.ecommerce.domains.users.UserAddress;
import com.thetechmaddy.ecommerce.models.requests.CognitoUser;

import java.util.List;

public interface UsersService {

    List<UserAddress> getUserAddresses(String userId);

    User provisionUser(CognitoUser userDetails);
}
