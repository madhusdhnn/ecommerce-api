CREATE TABLE carts (
    id BIGSERIAL PRIMARY KEY,
    user_id TEXT NOT NULL,
    status TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_carts_user_id ON carts (user_id);

ALTER TABLE carts ADD CONSTRAINT user_cart_unique UNIQUE (user_id);

CREATE TABLE cart_items (
    id BIGSERIAL PRIMARY KEY,
    cart_id BIGINT REFERENCES carts (id) NOT NULL,
    product_id BIGINT REFERENCES products (id) NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    status TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_cart_items_cart_id ON cart_items (cart_id);

ALTER TABLE cart_items ADD CONSTRAINT cart_product_unique UNIQUE (cart_id, product_id);

CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    user_id TEXT NOT NULL,
    status TEXT NOT NULL,
    net_total DOUBLE PRECISION DEFAULT 0.00,
    gross_total DOUBLE PRECISION DEFAULT 0.00,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_orders_user_id ON orders (user_id);

CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT REFERENCES orders (id) NOT NULL,
    product_id BIGINT REFERENCES products (id) NOT NULL,
    quantity INT NOT NULL,
    status TEXT NOT NULL,
    unit_price DOUBLE PRECISION DEFAULT 0.00,
    net_amount DOUBLE PRECISION DEFAULT 0.00,
    tax_amount DOUBLE PRECISION DEFAULT 0.00,
    gross_amount DOUBLE PRECISION DEFAULT 0.00,
    tax_percentage DOUBLE PRECISION DEFAULT 0.00,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE order_items ADD CONSTRAINT order_items_unique UNIQUE (order_id, product_id);
