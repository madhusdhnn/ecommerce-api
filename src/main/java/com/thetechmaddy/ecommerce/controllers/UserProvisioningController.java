package com.thetechmaddy.ecommerce.controllers;

import com.thetechmaddy.ecommerce.domains.users.User;
import com.thetechmaddy.ecommerce.models.requests.CognitoUser;
import com.thetechmaddy.ecommerce.models.responses.ApiResponse;
import com.thetechmaddy.ecommerce.services.UsersService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.thetechmaddy.ecommerce.models.AppConstants.INTERNAL_API_KEY_HEADER_NAME;

@RestController
@RequestMapping("/api/internal")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserProvisioningController extends BaseController {

    private final UsersService usersService;

    @PostMapping("/user-provision")
    @Parameter(in = ParameterIn.HEADER, name = INTERNAL_API_KEY_HEADER_NAME, required = true)
    public ApiResponse<User> provisionUser(@RequestBody @Valid CognitoUser userDetails) {
        return ApiResponse.success(usersService.provisionUser(userDetails));
    }
}
