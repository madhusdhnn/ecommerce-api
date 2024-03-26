package com.thetechmaddy.ecommerce.controllers;

import com.thetechmaddy.ecommerce.domains.users.UserAddress;
import com.thetechmaddy.ecommerce.models.requests.CognitoUser;
import com.thetechmaddy.ecommerce.models.responses.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.thetechmaddy.ecommerce.models.AppConstants.CURRENT_USER_REQUEST_ATTRIBUTE;

@RestController
@RequestMapping("/api/users")
public class UsersController extends BaseController {

    @GetMapping("/addresses")
    public ApiResponse<List<UserAddress>> getAllUserAddresses(
            @RequestAttribute(name = CURRENT_USER_REQUEST_ATTRIBUTE) CognitoUser cognitoUser) {
        return ApiResponse.success(List.of());
    }
}
