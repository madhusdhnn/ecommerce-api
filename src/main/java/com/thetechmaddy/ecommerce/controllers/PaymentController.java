package com.thetechmaddy.ecommerce.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.thetechmaddy.ecommerce.domains.payments.Payment;
import com.thetechmaddy.ecommerce.models.JsonViews;
import com.thetechmaddy.ecommerce.models.contexts.PaymentWorkflowContextHolder;
import com.thetechmaddy.ecommerce.models.payments.PaymentInfo;
import com.thetechmaddy.ecommerce.models.requests.CognitoUser;
import com.thetechmaddy.ecommerce.models.responses.ApiResponse;
import com.thetechmaddy.ecommerce.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.thetechmaddy.ecommerce.models.AppConstants.CURRENT_USER_REQUEST_ATTRIBUTE;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class PaymentController extends BaseController {


    private final PaymentService paymentService;

    @PostMapping("/process")
    @JsonView(value = {JsonViews.ProcessPaymentResponse.class})
    public ApiResponse<Payment> processPayment(
            @RequestAttribute(name = CURRENT_USER_REQUEST_ATTRIBUTE) CognitoUser cognitoUser,
            @RequestBody PaymentInfo paymentInfo
    ) {
        Long idempotencyId = PaymentWorkflowContextHolder.getContext();
        return ApiResponse.success(paymentService.processPayment(idempotencyId, paymentInfo, cognitoUser));
    }

    @GetMapping("/status")
    @JsonView(JsonViews.PaymentStatusResponse.class)
    public ApiResponse<Payment> getPaymentStatus() {
        Long idempotencyId = PaymentWorkflowContextHolder.getContext();
        return ApiResponse.success(paymentService.getStatus(idempotencyId));
    }

}
