CREATE TABLE User (
    user_id INT PRIMARY KEY,
    password_hash VARCHAR(255) NOT NULL,
    name VARCHAR(100),
    surname VARCHAR(100),
    gender VARCHAR(20),
    date_of_birth DATE,
    email VARCHAR(150) UNIQUE
);

CREATE TABLE Admin (
    user_id INT PRIMARY KEY,
    permission VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);

CREATE TABLE Customer (
    user_id INT PRIMARY KEY,
    wishList_id VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);

CREATE TABLE Address (
    address_id INT PRIMARY KEY,
    user_id INT,
    address_type VARCHAR(50),
    country VARCHAR(100),
    city VARCHAR(100),
    street VARCHAR(200),
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);

CREATE TABLE Product (
    product_id VARCHAR(50) PRIMARY KEY,
    product_name VARCHAR(100),
    category VARCHAR(100),
    price DOUBLE,
    description TEXT,
    shippingCost DOUBLE,
    color VARCHAR(50),
    stock_count INT,
    inventory_id VARCHAR(50),
    restocked_date DATE
);

CREATE TABLE Review (
    review_id VARCHAR(50) PRIMARY KEY,
    user_id INT,
    product_id VARCHAR(50),
    rating INT,
    comment TEXT,
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (product_id) REFERENCES Product(product_id)
);

CREATE TABLE Discount (
    discount_code VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100),
    percentage DOUBLE
);

CREATE TABLE OrderTable (
    order_id VARCHAR(50) PRIMARY KEY,
    user_id INT,
    discount_code VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (discount_code) REFERENCES Discount(discount_code)
);

CREATE TABLE OrderItem (
    order_item_id VARCHAR(50) PRIMARY KEY,
    order_id VARCHAR(50),
    product_id VARCHAR(50),
    quantity INT,
    price DOUBLE,
    FOREIGN KEY (order_id) REFERENCES OrderTable(order_id),
    FOREIGN KEY (product_id) REFERENCES Product(product_id)
);

CREATE TABLE Cart (
    cart_id VARCHAR(50) PRIMARY KEY,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);

CREATE TABLE CartItem (
    cart_item_id VARCHAR(50) PRIMARY KEY,
    cart_id VARCHAR(50),
    product_id VARCHAR(50),
    quantity INT,
    FOREIGN KEY (cart_id) REFERENCES Cart(cart_id),
    FOREIGN KEY (product_id) REFERENCES Product(product_id)
);

CREATE TABLE Payment (
    payment_id INT PRIMARY KEY,
    user_id INT,
    order_id VARCHAR(50),
    amount DOUBLE,
    status VARCHAR(50),
    payment_date DATE,
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (order_id) REFERENCES OrderTable(order_id)
);

CREATE TABLE Shipping (
    shipping_id INT PRIMARY KEY,
    order_id VARCHAR(50),
    carrier VARCHAR(100),
    trackingNumber VARCHAR(100),
    status VARCHAR(50),
    address_id INT,
    FOREIGN KEY (order_id) REFERENCES OrderTable(order_id),
    FOREIGN KEY (address_id) REFERENCES Address(address_id)
);

CREATE TABLE Seller (
    seller_id INT PRIMARY KEY
    -- Eğer kullanıcıya bağlıysa: FOREIGN KEY (seller_id) REFERENCES User(user_id)
);

CREATE TABLE SellerProduct (
    seller_id INT,
    product_id VARCHAR(50),
    PRIMARY KEY (seller_id, product_id),
    FOREIGN KEY (seller_id) REFERENCES Seller(seller_id),
    FOREIGN KEY (product_id) REFERENCES Product(product_id)
);
