-- Insert first product
INSERT INTO product (id, name, description, category, price, stock) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d479', 'Laptop Pro', 'High performance laptop', 'Electronics', 1299.99, 25);

-- Insert second product
INSERT INTO product (id, name, description, category, price, stock) VALUES
    ('3a7e5bf8-7f72-41c1-9daa-e4604f0c0467', 'Gaming Mouse', 'Ergonomic gaming mouse with RGB', 'Gaming Accessories', 59.99, 100);

-- Reviews for first product (Laptop Pro)
INSERT INTO review (id, author, reviews_id, product, vote, comment) VALUES
    ('b5e6a6a0-5f9d-4df8-9ce6-10a3d17a15b4', 'John Doe', 'f47ac10b-58cc-4372-a567-0e02b2c3d479', 'f47ac10b-58cc-4372-a567-0e02b2c3d479', 5, 'Excellent performance and battery life!');

INSERT INTO review (id, author, reviews_id, product, vote, comment) VALUES
    ('7d8f3a40-9c1a-4c2b-bfc7-7c9123456789', 'Jane Smith', 'f47ac10b-58cc-4372-a567-0e02b2c3d479', 'f47ac10b-58cc-4372-a567-0e02b2c3d479', 4, 'Great laptop, just a bit heavy.');

INSERT INTO review (id, author, reviews_id, product, vote, comment) VALUES
    ('e12f8a9b-7c3d-4e5f-6a7b-8c9d0e1f2a3b', 'Bob Johnson', 'f47ac10b-58cc-4372-a567-0e02b2c3d479', 'f47ac10b-58cc-4372-a567-0e02b2c3d479', 5, 'Fast processor and beautiful display!');

-- Reviews for second product (Gaming Mouse)
INSERT INTO review (id, author, reviews_id, product, vote, comment) VALUES
    ('6f9a7b2c-3d4e-5f6a-7b8c-9d0e1f2a3b4c', 'Alex Wilson', '3a7e5bf8-7f72-41c1-9daa-e4604f0c0467', '3a7e5bf8-7f72-41c1-9daa-e4604f0c0467', 4, 'Comfortable grip and responsive buttons.');

INSERT INTO review (id, author, reviews_id, product, vote, comment) VALUES
    ('d1e2f3a4-b5c6-7d8e-9f0a-1b2c3d4e5f6a', 'Sarah Miller', '3a7e5bf8-7f72-41c1-9daa-e4604f0c0467', '3a7e5bf8-7f72-41c1-9daa-e4604f0c0467', 3, 'Good mouse but RGB settings are confusing.');

INSERT INTO review (id, author, reviews_id, product, vote, comment) VALUES
    ('1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d', 'Mike Thompson', '3a7e5bf8-7f72-41c1-9daa-e4604f0c0467', '3a7e5bf8-7f72-41c1-9daa-e4604f0c0467', 5, 'Best gaming mouse I have ever used!');