CREATE TABLE reserved_products (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT REFERENCES orders (id) NOT NULL,
    product_id BIGINT REFERENCES products (id) NOT NULL,
    quantity INT NOT NULL DEFAULT 0
);

CREATE INDEX idx_reserved_products_order_id ON reserved_products (order_id);

ALTER TABLE reserved_products
ADD CONSTRAINT reserved_products_order_id_product_id_unique UNIQUE (order_id, product_id);