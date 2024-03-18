package com.thetechmaddy.ecommerce.models.contexts;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentWorkflowContextHolder {

    private static final ThreadLocal<Long> PAYMENT_ID_CONTEXT_HOLDER = new ThreadLocal<>();

    public static void setContext(Long idempotencyId) {
        PAYMENT_ID_CONTEXT_HOLDER.set(idempotencyId);
    }

    public static Long getContext() {
        return PAYMENT_ID_CONTEXT_HOLDER.get();
    }

    public static void clearContext() {
        PAYMENT_ID_CONTEXT_HOLDER.remove();
    }
}
