INSERT INTO products (category_id, name, is_available, price) VALUES
((SELECT id FROM categories WHERE name = 'Electronics'), 'iPhone 15 128 GB', true, 86000.00),
((SELECT id FROM categories WHERE name = 'Electronics'), 'iPhone 15 256 GB', true, 99000.00),
((SELECT id FROM categories WHERE name = 'Electronics'), 'iPhone 15 512 GB', true, 115000.00),
((SELECT id FROM categories WHERE name = 'Electronics'), 'iPhone 14 128 GB', true, 86000.00),
((SELECT id FROM categories WHERE name = 'Electronics'), 'iPhone 14 256 GB', true, 99000.00),
((SELECT id FROM categories WHERE name = 'Electronics'), 'iPhone 14 512 GB', true, 115000.00),
((SELECT id FROM categories WHERE name = 'Electronics'), 'iPhone 13 128 GB', true, 86000.00),
((SELECT id FROM categories WHERE name = 'Electronics'), 'iPhone 13 256 GB', false, 99000.00),
((SELECT id FROM categories WHERE name = 'Electronics'), 'iPhone 13 512 GB', true, 115000.00),
((SELECT id FROM categories WHERE name = 'Electronics'), 'iPhone 15 Pro 128 GB', true, 120000.00),
((SELECT id FROM categories WHERE name = 'Electronics'), 'iPhone 15 Pro 256 GB', true, 135000.00),
((SELECT id FROM categories WHERE name = 'Electronics'), 'iPhone 15 Pro 512 GB', false, 165000.00),
((SELECT id FROM categories WHERE name = 'Electronics'), 'iPhone 14 Pro 128 GB', true, 120000.00),
((SELECT id FROM categories WHERE name = 'Electronics'), 'iPhone 14 Pro 256 GB', false, 135000.00),
((SELECT id FROM categories WHERE name = 'Electronics'), 'iPhone 14 Pro 512 GB', true, 165000.00),
((SELECT id FROM categories WHERE name = 'Electronics'), 'iPhone 13 Pro 128 GB', true, 120000.00),
((SELECT id FROM categories WHERE name = 'Electronics'), 'iPhone 13 Pro 256 GB', false, 135000.00),
((SELECT id FROM categories WHERE name = 'Electronics'), 'iPhone 13 Pro 512 GB', true, 165000.00),
((SELECT id FROM categories WHERE name = 'Health & Beauty'), 'Moisturiser 100 ml', true, 570.00),
((SELECT id FROM categories WHERE name = 'Health & Beauty'), 'SPF 50 Sun Screen', true, 430.00),
((SELECT id FROM categories WHERE name = 'Health & Beauty'), 'Face Wash 100 ml', true, 390.00);

INSERT INTO product_attribute (product_id, attribute_name, attribute_value) VALUES
((SELECT id FROM products WHERE name = 'iPhone 15 128 GB'), 'color', 'blue'),((SELECT id FROM products WHERE name = 'iPhone 15 128 GB'), 'color', 'red'),((SELECT id FROM products WHERE name = 'iPhone 15 128 GB'), 'color', 'yellow'),((SELECT id FROM products WHERE name = 'iPhone 15 128 GB'), 'color', 'lilac'),((SELECT id FROM products WHERE name = 'iPhone 15 128 GB'), 'size', '128 GB'),
((SELECT id FROM products WHERE name = 'iPhone 15 256 GB'), 'color', 'blue'),((SELECT id FROM products WHERE name = 'iPhone 15 256 GB'), 'color', 'red'),((SELECT id FROM products WHERE name = 'iPhone 15 256 GB'), 'color', 'yellow'),((SELECT id FROM products WHERE name = 'iPhone 15 256 GB'), 'color', 'lilac'),((SELECT id FROM products WHERE name = 'iPhone 15 256 GB'), 'size', '256 GB'),
((SELECT id FROM products WHERE name = 'iPhone 15 512 GB'), 'color', 'blue'),((SELECT id FROM products WHERE name = 'iPhone 15 512 GB'), 'color', 'red'),((SELECT id FROM products WHERE name = 'iPhone 15 512 GB'), 'color', 'yellow'),((SELECT id FROM products WHERE name = 'iPhone 15 512 GB'), 'color', 'lilac'),((SELECT id FROM products WHERE name = 'iPhone 15 512 GB'), 'size', '512 GB'),
((SELECT id FROM products WHERE name = 'iPhone 14 128 GB'), 'color', 'blue'),((SELECT id FROM products WHERE name = 'iPhone 14 128 GB'), 'color', 'red'),((SELECT id FROM products WHERE name = 'iPhone 14 128 GB'), 'color', 'yellow'),((SELECT id FROM products WHERE name = 'iPhone 14 128 GB'), 'color', 'lilac'),((SELECT id FROM products WHERE name = 'iPhone 14 128 GB'), 'size', '128 GB'),
((SELECT id FROM products WHERE name = 'iPhone 14 256 GB'), 'color', 'blue'),((SELECT id FROM products WHERE name = 'iPhone 14 256 GB'), 'color', 'red'),((SELECT id FROM products WHERE name = 'iPhone 14 256 GB'), 'color', 'yellow'),((SELECT id FROM products WHERE name = 'iPhone 14 256 GB'), 'color', 'lilac'),((SELECT id FROM products WHERE name = 'iPhone 14 256 GB'), 'size', '256 GB'),
((SELECT id FROM products WHERE name = 'iPhone 14 512 GB'), 'color', 'blue'),((SELECT id FROM products WHERE name = 'iPhone 14 512 GB'), 'color', 'red'),((SELECT id FROM products WHERE name = 'iPhone 14 512 GB'), 'color', 'yellow'),((SELECT id FROM products WHERE name = 'iPhone 14 512 GB'), 'color', 'lilac'),((SELECT id FROM products WHERE name = 'iPhone 14 512 GB'), 'size', '512 GB'),
((SELECT id FROM products WHERE name = 'iPhone 13 128 GB'), 'color', 'blue'),((SELECT id FROM products WHERE name = 'iPhone 13 128 GB'), 'color', 'red'),((SELECT id FROM products WHERE name = 'iPhone 13 128 GB'), 'color', 'yellow'),((SELECT id FROM products WHERE name = 'iPhone 13 128 GB'), 'color', 'lilac'),((SELECT id FROM products WHERE name = 'iPhone 13 128 GB'), 'size', '128 GB'),
((SELECT id FROM products WHERE name = 'iPhone 13 256 GB'), 'color', 'blue'),((SELECT id FROM products WHERE name = 'iPhone 13 256 GB'), 'color', 'red'),((SELECT id FROM products WHERE name = 'iPhone 13 256 GB'), 'color', 'yellow'),((SELECT id FROM products WHERE name = 'iPhone 13 256 GB'), 'color', 'lilac'),((SELECT id FROM products WHERE name = 'iPhone 13 256 GB'), 'size', '256 GB'),
((SELECT id FROM products WHERE name = 'iPhone 13 512 GB'), 'color', 'blue'),((SELECT id FROM products WHERE name = 'iPhone 13 512 GB'), 'color', 'red'),((SELECT id FROM products WHERE name = 'iPhone 13 512 GB'), 'color', 'yellow'),((SELECT id FROM products WHERE name = 'iPhone 13 512 GB'), 'color', 'lilac'),((SELECT id FROM products WHERE name = 'iPhone 13 512 GB'), 'size', '512 GB'),
((SELECT id FROM products WHERE name = 'iPhone 15 Pro 128 GB'), 'color', 'blue'),((SELECT id FROM products WHERE name = 'iPhone 15 Pro 128 GB'), 'color', 'red'),((SELECT id FROM products WHERE name = 'iPhone 15 Pro 128 GB'), 'color', 'yellow'),((SELECT id FROM products WHERE name = 'iPhone 15 Pro 128 GB'), 'color', 'lilac'),((SELECT id FROM products WHERE name = 'iPhone 15 Pro 128 GB'), 'size', '128 GB'),
((SELECT id FROM products WHERE name = 'iPhone 15 Pro 256 GB'), 'color', 'blue'),((SELECT id FROM products WHERE name = 'iPhone 15 Pro 256 GB'), 'color', 'red'),((SELECT id FROM products WHERE name = 'iPhone 15 Pro 256 GB'), 'color', 'yellow'),((SELECT id FROM products WHERE name = 'iPhone 15 Pro 256 GB'), 'color', 'lilac'),((SELECT id FROM products WHERE name = 'iPhone 15 Pro 256 GB'), 'size', '256 GB'),
((SELECT id FROM products WHERE name = 'iPhone 15 Pro 512 GB'), 'color', 'blue'),((SELECT id FROM products WHERE name = 'iPhone 15 Pro 512 GB'), 'color', 'red'),((SELECT id FROM products WHERE name = 'iPhone 15 Pro 512 GB'), 'color', 'yellow'),((SELECT id FROM products WHERE name = 'iPhone 15 Pro 512 GB'), 'color', 'lilac'),((SELECT id FROM products WHERE name = 'iPhone 15 Pro 512 GB'), 'size', '512 GB'),
((SELECT id FROM products WHERE name = 'iPhone 14 Pro 128 GB'), 'color', 'blue'),((SELECT id FROM products WHERE name = 'iPhone 14 Pro 128 GB'), 'color', 'red'),((SELECT id FROM products WHERE name = 'iPhone 14 Pro 128 GB'), 'color', 'yellow'),((SELECT id FROM products WHERE name = 'iPhone 14 Pro 128 GB'), 'color', 'lilac'),((SELECT id FROM products WHERE name = 'iPhone 14 Pro 128 GB'), 'size', '128 GB'),
((SELECT id FROM products WHERE name = 'iPhone 14 Pro 256 GB'), 'color', 'blue'),((SELECT id FROM products WHERE name = 'iPhone 14 Pro 256 GB'), 'color', 'red'),((SELECT id FROM products WHERE name = 'iPhone 14 Pro 256 GB'), 'color', 'yellow'),((SELECT id FROM products WHERE name = 'iPhone 14 Pro 256 GB'), 'color', 'lilac'),((SELECT id FROM products WHERE name = 'iPhone 14 Pro 256 GB'), 'size', '256 GB'),
((SELECT id FROM products WHERE name = 'iPhone 14 Pro 512 GB'), 'color', 'blue'),((SELECT id FROM products WHERE name = 'iPhone 14 Pro 512 GB'), 'color', 'red'),((SELECT id FROM products WHERE name = 'iPhone 14 Pro 512 GB'), 'color', 'yellow'),((SELECT id FROM products WHERE name = 'iPhone 14 Pro 512 GB'), 'color', 'lilac'),((SELECT id FROM products WHERE name = 'iPhone 14 Pro 512 GB'), 'size', '512 GB'),
((SELECT id FROM products WHERE name = 'iPhone 13 Pro 128 GB'), 'color', 'blue'),((SELECT id FROM products WHERE name = 'iPhone 13 Pro 128 GB'), 'color', 'red'),((SELECT id FROM products WHERE name = 'iPhone 13 Pro 128 GB'), 'color', 'yellow'),((SELECT id FROM products WHERE name = 'iPhone 13 Pro 128 GB'), 'color', 'lilac'),((SELECT id FROM products WHERE name = 'iPhone 13 Pro 128 GB'), 'size', '128 GB'),
((SELECT id FROM products WHERE name = 'iPhone 13 Pro 256 GB'), 'color', 'blue'),((SELECT id FROM products WHERE name = 'iPhone 13 Pro 256 GB'), 'color', 'red'),((SELECT id FROM products WHERE name = 'iPhone 13 Pro 256 GB'), 'color', 'yellow'),((SELECT id FROM products WHERE name = 'iPhone 13 Pro 256 GB'), 'color', 'lilac'),((SELECT id FROM products WHERE name = 'iPhone 13 Pro 256 GB'), 'size', '256 GB'),
((SELECT id FROM products WHERE name = 'iPhone 13 Pro 512 GB'), 'color', 'blue'),((SELECT id FROM products WHERE name = 'iPhone 13 Pro 512 GB'), 'color', 'red'),((SELECT id FROM products WHERE name = 'iPhone 13 Pro 512 GB'), 'color', 'yellow'),((SELECT id FROM products WHERE name = 'iPhone 13 Pro 512 GB'), 'color', 'lilac'),((SELECT id FROM products WHERE name = 'iPhone 13 Pro 512 GB'), 'size', '512 GB'),
((SELECT id FROM products WHERE name = 'Moisturiser 100 ml'), 'Active Ingredients', 'Vitamin E, Panthenol (Vitamin B5), Niacinamide (Vitamin B3)'),((SELECT id FROM products WHERE name = 'Moisturiser 100 ml'), 'Net Quantity', '100 ml'),((SELECT id FROM products WHERE name = 'Moisturiser 100 ml'), 'Item form', 'Lotion'),
((SELECT id FROM products WHERE name = 'Face Wash 100 ml'), 'Net Quantity', '1 count'), ((SELECT id FROM products WHERE name = 'Face Wash 100 ml'), 'Country of Origin', 'India'),
((SELECT id FROM products WHERE name = 'SPF 50 Sun Screen'), 'Net Quantity', '100 g');

INSERT INTO user_pool (email, cognito_sub, first_name, last_name) VALUES
('john.doe@example.com', '2d309cdb-8911-4e62-99d8-00ddefbdc3d1', 'John', 'Doe'),
('jane.doe@example.com', 'dfac34a6-9994-42a4-be3c-86e568cb65c8', 'Jane', 'Doe');

INSERT INTO carts (user_id, status) VALUES
('2d309cdb-8911-4e62-99d8-00ddefbdc3d1', 'UN_LOCKED'),
('dfac34a6-9994-42a4-be3c-86e568cb65c8', 'UN_LOCKED');
