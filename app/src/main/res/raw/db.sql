CREATE TABLE products (
    id INTEGER PRIMARY KEY,
    name TEXT,
    upc TEXT,
    size INTEGER,
    unit INTEGER
);
CREATE TABLE shops (
    id INTEGER PRIMARY KEY,
    name TEXT,
    location TEXT,
    coordinates TEXT
);
CREATE TABLE prices (
    product_id INTEGER,
    shop_id INTEGER,
    price_date DATETIME,
    price REAL,
    is_offer BOOLEAN,
    PRIMARY KEY(product_id, shop_id)
    FOREIGN KEY(product_id) REFERENCES products(id),
    FOREIGN KEY(shop_id) REFERENCES shops(id)
);
CREATE TABLE last_updated(
    product_id INTEGER PRIMARY KEY,
    date_updated DATETIME,
    FOREIGN KEY(product_id) REFERENCES products(id)
);