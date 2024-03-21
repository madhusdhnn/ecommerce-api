CREATE TABLE delivery_details (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT REFERENCES orders (id) NOT NULL,
    customer_email TEXT,
    customer_name TEXT,
    shipping_address_1 TEXT,
    shipping_address_2 TEXT,
    shipping_city TEXT,
    shipping_state TEXT,
    shipping_zip_code TEXT,
    billing_address_1 TEXT,
    billing_address_2 TEXT,
    billing_city TEXT,
    billing_state TEXT,
    billing_zip_code TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_delivery_details_order_id ON delivery_details (order_id);

ALTER TABLE delivery_details ADD CONSTRAINT delivery_details_order_id UNIQUE (order_id);

CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT REFERENCES orders (id) NOT NULL,
    payment_mode TEXT NOT NULL,
    status TEXT NOT NULL,
    amount DOUBLE PRECISION NOT NULL DEFAULT 0.00,
    payment_date TIMESTAMPTZ,
    transaction_id TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE payments ADD CONSTRAINT payments_order_id UNIQUE (order_id);

CREATE INDEX idx_payments_order_id ON payments (order_id);
