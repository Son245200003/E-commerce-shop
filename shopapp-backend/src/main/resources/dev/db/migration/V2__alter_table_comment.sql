Create Table comments(
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT,
    user_id INT,
    content VARCHAR(255),
    create_at DATETIME,
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
)