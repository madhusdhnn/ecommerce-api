CREATE TABLE carts (
    id BIGSERIAL PRIMARY KEY,
    user_id TEXT NOT NULL,
    status TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

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

ALTER TABLE cart_items ADD CONSTRAINT cart_product_unique UNIQUE (cart_id, product_id);