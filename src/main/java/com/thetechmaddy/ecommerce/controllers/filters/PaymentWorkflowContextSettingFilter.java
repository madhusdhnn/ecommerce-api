package com.thetechmaddy.ecommerce.controllers.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thetechmaddy.ecommerce.exceptions.MissingIdempotencyIdHeaderException;
import com.thetechmaddy.ecommerce.models.contexts.PaymentWorkflowContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.thetechmaddy.ecommerce.models.AppConstants.PAYMENT_ID_HEADER_NAME;
import static com.thetechmaddy.ecommerce.utils.HttpUtils.sendErrorResponse;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Order(1)
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class PaymentWorkflowContextSettingFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private static final Pattern PAYMENTS_API_PATTERN = Pattern.compile(".*/payments/.*");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Matcher matcher = PAYMENTS_API_PATTERN.matcher(request.getRequestURI());
            if (matcher.matches()) {
                String idempotency = request.getHeader(PAYMENT_ID_HEADER_NAME);
                PaymentWorkflowContextHolder.setContext(Long.parseLong(idempotency));
            }
            filterChain.doFilter(request, response);
        } catch (NumberFormatException e) {
            sendErrorResponse(objectMapper, response, BAD_REQUEST,
                    () -> new MissingIdempotencyIdHeaderException(
                            String.format("%s header is missing in the request", PAYMENT_ID_HEADER_NAME), BAD_REQUEST));
        } finally {
            PaymentWorkflowContextHolder.clearContext();
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !isPaymentAPI(request);
    }

    private static boolean isPaymentAPI(HttpServletRequest request) {
        return PAYMENTS_API_PATTERN.matcher(request.getRequestURI()).matches();
    }
}
