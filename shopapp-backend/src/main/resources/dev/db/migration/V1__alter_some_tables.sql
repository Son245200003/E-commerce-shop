ALTER TABLE categories MODIFY name VARCHAR(50) UNIQUE;

ALTER TABLE products MODIFY price DECIMAL(10,2);

ALTER TABLE products MODIFY thumbnail VARCHAR(255);

ALTER TABLE users ALTER  COLUMN role_id SET DEFAULT 2;

ALTER TABLE order_details MODIFY COLUMN price DECIMAL(10,2),
    MODIFY COLUMN number_of_products INT DEFAULT 1,
    MODIFY COLUMN total_money DECIMAL(10,2) DEFAULT 0;