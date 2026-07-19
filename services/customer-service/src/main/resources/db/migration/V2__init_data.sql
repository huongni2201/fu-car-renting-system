INSERT INTO customer (customer_id, customer_name, telephone, email, customer_birthday, customer_status, password)
SELECT 1, 'System Administrator', '0901000000', 'admin@example.com', '1990-01-01', 'ACTIVE', '$2a$10$/iu8i1LZqWsKs0KvTuFp7.NTt2pfPrr2mxMKhL.fVzQf6yhhzkkfa'
WHERE NOT EXISTS (SELECT 1 FROM customer WHERE customer_id = 1);

INSERT INTO customer (customer_id, customer_name, telephone, email, customer_birthday, customer_status, password)
SELECT 2, 'Nguyen Van An', '0901000001', 'an.nguyen@example.com', '1998-04-12', 'ACTIVE', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'
WHERE NOT EXISTS (SELECT 1 FROM customer WHERE customer_id = 2);

INSERT INTO customer (customer_id, customer_name, telephone, email, customer_birthday, customer_status, password)
SELECT 3, 'Tran Thi Binh', '0901000002', 'binh.tran@example.com', '1999-08-21', 'ACTIVE', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'
WHERE NOT EXISTS (SELECT 1 FROM customer WHERE customer_id = 3);

INSERT INTO customer (customer_id, customer_name, telephone, email, customer_birthday, customer_status, password)
SELECT 4, 'Le Minh Chau', '0901000003', 'chau.le@example.com', '1997-11-05', 'ACTIVE', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'
WHERE NOT EXISTS (SELECT 1 FROM customer WHERE customer_id = 4);

INSERT INTO customer (customer_id, customer_name, telephone, email, customer_birthday, customer_status, password)
SELECT 5, 'Pham Quoc Dung', '0901000004', 'dung.pham@example.com', '1996-02-17', 'INACTIVE', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'
WHERE NOT EXISTS (SELECT 1 FROM customer WHERE customer_id = 5);

INSERT INTO customer (customer_id, customer_name, telephone, email, customer_birthday, customer_status, password)
SELECT 6, 'Hoang Thu Ha', '0901000005', 'ha.hoang@example.com', '2000-06-30', 'BANNED', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'
WHERE NOT EXISTS (SELECT 1 FROM customer WHERE customer_id = 6);

INSERT INTO customer (customer_id, customer_name, telephone, email, customer_birthday, customer_status, password)
SELECT 7, 'Do Thanh Khoa', '0901000006', 'khoa.do@example.com', '1995-09-09', 'ACTIVE', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'
WHERE NOT EXISTS (SELECT 1 FROM customer WHERE customer_id = 7);

INSERT INTO customer (customer_id, customer_name, telephone, email, customer_birthday, customer_status, password)
SELECT 8, 'Bui Mai Linh', '0901000007', 'linh.bui@example.com', '1994-12-24', 'ACTIVE', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'
WHERE NOT EXISTS (SELECT 1 FROM customer WHERE customer_id = 8);

INSERT INTO customer (customer_id, customer_name, telephone, email, customer_birthday, customer_status, password)
SELECT 9, 'Ngo Hai Nam', '0901000008', 'nam.ngo@example.com', '1993-03-15', 'ACTIVE', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'
WHERE NOT EXISTS (SELECT 1 FROM customer WHERE customer_id = 9);

INSERT INTO customer (customer_id, customer_name, telephone, email, customer_birthday, customer_status, password)
SELECT 10, 'Vo Gia Han', '0901000009', 'han.vo@example.com', '2001-10-02', 'ACTIVE', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'
WHERE NOT EXISTS (SELECT 1 FROM customer WHERE customer_id = 10);

ALTER TABLE customer ALTER COLUMN customer_id RESTART WITH 11;
