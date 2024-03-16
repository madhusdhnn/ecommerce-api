CREATE TABLE user_pool (
    id BIGSERIAL PRIMARY KEY,
    cognito_sub TEXT UNIQUE NOT NULL,
    email TEXT UNIQUE NOT NULL,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_addresses (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES user_pool (id) NOT NULL,
    is_default BOOLEAN NOT NULL,
    address_1 TEXT,
    address_2 TEXT,
    city TEXT,
    state TEXT,
    zip_code TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_user_address_user_id ON user_addresses (user_id);

CREATE TABLE categories (
  id BIGSERIAL PRIMARY KEY,
  name TEXT UNIQUE NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO categories (name) VALUES
('Electronics'),
('Apparel'),
('Home & Garden'),
('Health & Beauty'),
('Toys & Games'),
('Books & Media'),
('Sports & Outdoors'),
('Automotive'),
('Office Supplies'),
('Pet Supplies'),
('Jewelry & Watches'),
('Baby & Kids'),
('Healthcare & Medical'),
('Crafts & Hobbies'),
('Travel & Luggage'),
('Electrical & Lighting'),
('Industrial & Scientific'),
('Gifts & Specialty');

CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    category_id BIGINT REFERENCES categories (id) NOT NULL,
    name TEXT NOT NULL,
    description TEXT,
    sku_code TEXT NOT NULL UNIQUE,
    stock_quantity INT NOT NULL DEFAULT 0,
    unit_price DOUBLE PRECISION NOT NULL DEFAULT 0.00,
    gross_amount DOUBLE PRECISION NOT NULL DEFAULT 0.00,
    tax_percentage DOUBLE PRECISION NOT NULL DEFAULT 0.00,
    tax_amount DOUBLE PRECISION NOT NULL DEFAULT 0.00,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_product_category_id ON products (category_id);

CREATE TABLE product_attributes (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT REFERENCES products (id) NOT NULL,
    attribute_name TEXT,
    attribute_value TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_product_attributes_product_id ON product_attributes (product_id);
