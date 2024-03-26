package com.thetechmaddy.ecommerce.controllers;

import com.thetechmaddy.ecommerce.models.AppConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = AppConstants.SECURITY_SCHEME_NAME)
public class AuthenticationAwareController extends BaseController {
}
