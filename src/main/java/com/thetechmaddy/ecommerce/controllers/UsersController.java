package com.thetechmaddy.ecommerce.controllers;

import com.thetechmaddy.ecommerce.domains.users.UserAddress;
import com.thetechmaddy.ecommerce.models.requests.CognitoUser;
import com.thetechmaddy.ecommerce.models.responses.ApiResponse;
import com.thetechmaddy.ecommerce.services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.thetechmaddy.ecommerce.models.AppConstants.CURRENT_USER_REQUEST_ATTRIBUTE;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UsersController extends AuthenticationAwareController {

    private final UsersService usersService;

    @GetMapping("/addresses")
    public ApiResponse<List<UserAddress>> getAllUserAddresses(
            @RequestAttribute(name = CURRENT_USER_REQUEST_ATTRIBUTE) CognitoUser cognitoUser) {
        return ApiResponse.success(usersService.getUserAddresses(cognitoUser.getCognitoSub()));
    }
}
