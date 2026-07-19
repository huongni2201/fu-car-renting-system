INSERT INTO renting_transaction (renting_transaction_id, renting_date, total_price, customer_id, reservation_token, renting_status)
SELECT 1, DATE '2026-07-01', 1200000.00, 1, 'seed-token-rt-001', 'COMPLETED'
WHERE NOT EXISTS (SELECT 1 FROM renting_transaction WHERE renting_transaction_id = 1);

INSERT INTO renting_transaction (renting_transaction_id, renting_date, total_price, customer_id, reservation_token, renting_status)
SELECT 2, DATE '2026-07-05', 800000.00, 2, 'seed-token-rt-002', 'APPROVED'
WHERE NOT EXISTS (SELECT 1 FROM renting_transaction WHERE renting_transaction_id = 2);

INSERT INTO renting_transaction (renting_transaction_id, renting_date, total_price, customer_id, reservation_token, renting_status)
SELECT 3, DATE '2026-07-10', 1500000.00, 3, 'seed-token-rt-003', 'PENDING'
WHERE NOT EXISTS (SELECT 1 FROM renting_transaction WHERE renting_transaction_id = 3);

INSERT INTO renting_transaction (renting_transaction_id, renting_date, total_price, customer_id, reservation_token, renting_status)
SELECT 4, DATE '2026-07-15', 2200000.00, 4, 'seed-token-rt-004', 'CANCELLED'
WHERE NOT EXISTS (SELECT 1 FROM renting_transaction WHERE renting_transaction_id = 4);

INSERT INTO renting_transaction (renting_transaction_id, renting_date, total_price, customer_id, reservation_token, renting_status)
SELECT 5, DATE '2026-07-18', 1800000.00, 5, 'seed-token-rt-005', 'COMPLETED'
WHERE NOT EXISTS (SELECT 1 FROM renting_transaction WHERE renting_transaction_id = 5);

INSERT INTO renting_transaction (renting_transaction_id, renting_date, total_price, customer_id, reservation_token, renting_status)
SELECT 6, DATE '2026-07-20', 950000.00, 6, 'seed-token-rt-006', 'APPROVED'
WHERE NOT EXISTS (SELECT 1 FROM renting_transaction WHERE renting_transaction_id = 6);

INSERT INTO renting_transaction (renting_transaction_id, renting_date, total_price, customer_id, reservation_token, renting_status)
SELECT 7, DATE '2026-07-22', 3000000.00, 7, 'seed-token-rt-007', 'PENDING'
WHERE NOT EXISTS (SELECT 1 FROM renting_transaction WHERE renting_transaction_id = 7);

INSERT INTO renting_transaction (renting_transaction_id, renting_date, total_price, customer_id, reservation_token, renting_status)
SELECT 8, DATE '2026-07-24', 1250000.00, 8, 'seed-token-rt-008', 'COMPLETED'
WHERE NOT EXISTS (SELECT 1 FROM renting_transaction WHERE renting_transaction_id = 8);

INSERT INTO renting_transaction (renting_transaction_id, renting_date, total_price, customer_id, reservation_token, renting_status)
SELECT 9, DATE '2026-07-26', 2100000.00, 9, 'seed-token-rt-009', 'APPROVED'
WHERE NOT EXISTS (SELECT 1 FROM renting_transaction WHERE renting_transaction_id = 9);

INSERT INTO renting_transaction (renting_transaction_id, renting_date, total_price, customer_id, reservation_token, renting_status)
SELECT 10, DATE '2026-07-28', 760000.00, 10, 'seed-token-rt-010', 'CANCELLED'
WHERE NOT EXISTS (SELECT 1 FROM renting_transaction WHERE renting_transaction_id = 10);

INSERT INTO renting_detail (renting_detail_id, renting_transaction_id, car_id, start_date, end_date, price)
SELECT 1, 1, 1, DATE '2026-07-01', DATE '2026-07-03', 1200000.00
WHERE NOT EXISTS (SELECT 1 FROM renting_detail WHERE renting_detail_id = 1);

INSERT INTO renting_detail (renting_detail_id, renting_transaction_id, car_id, start_date, end_date, price)
SELECT 2, 2, 3, DATE '2026-07-06', DATE '2026-07-07', 800000.00
WHERE NOT EXISTS (SELECT 1 FROM renting_detail WHERE renting_detail_id = 2);

INSERT INTO renting_detail (renting_detail_id, renting_transaction_id, car_id, start_date, end_date, price)
SELECT 3, 3, 8, DATE '2026-07-12', DATE '2026-07-14', 1500000.00
WHERE NOT EXISTS (SELECT 1 FROM renting_detail WHERE renting_detail_id = 3);

INSERT INTO renting_detail (renting_detail_id, renting_transaction_id, car_id, start_date, end_date, price)
SELECT 4, 4, 2, DATE '2026-07-16', DATE '2026-07-19', 2200000.00
WHERE NOT EXISTS (SELECT 1 FROM renting_detail WHERE renting_detail_id = 4);

INSERT INTO renting_detail (renting_detail_id, renting_transaction_id, car_id, start_date, end_date, price)
SELECT 5, 5, 4, DATE '2026-07-18', DATE '2026-07-21', 1800000.00
WHERE NOT EXISTS (SELECT 1 FROM renting_detail WHERE renting_detail_id = 5);

INSERT INTO renting_detail (renting_detail_id, renting_transaction_id, car_id, start_date, end_date, price)
SELECT 6, 6, 6, DATE '2026-07-20', DATE '2026-07-21', 950000.00
WHERE NOT EXISTS (SELECT 1 FROM renting_detail WHERE renting_detail_id = 6);

INSERT INTO renting_detail (renting_detail_id, renting_transaction_id, car_id, start_date, end_date, price)
SELECT 7, 7, 7, DATE '2026-07-22', DATE '2026-07-27', 3000000.00
WHERE NOT EXISTS (SELECT 1 FROM renting_detail WHERE renting_detail_id = 7);

INSERT INTO renting_detail (renting_detail_id, renting_transaction_id, car_id, start_date, end_date, price)
SELECT 8, 8, 9, DATE '2026-07-24', DATE '2026-07-26', 1250000.00
WHERE NOT EXISTS (SELECT 1 FROM renting_detail WHERE renting_detail_id = 8);

INSERT INTO renting_detail (renting_detail_id, renting_transaction_id, car_id, start_date, end_date, price)
SELECT 9, 9, 10, DATE '2026-07-26', DATE '2026-07-29', 2100000.00
WHERE NOT EXISTS (SELECT 1 FROM renting_detail WHERE renting_detail_id = 9);

INSERT INTO renting_detail (renting_detail_id, renting_transaction_id, car_id, start_date, end_date, price)
SELECT 10, 10, 5, DATE '2026-07-28', DATE '2026-07-29', 760000.00
WHERE NOT EXISTS (SELECT 1 FROM renting_detail WHERE renting_detail_id = 10);

INSERT INTO saga_compensation_failure (id, car_id, reservation_token, operation, reason, created_at)
SELECT 1, 1, 'failure-token-001', 'RESERVE_CAR', 'Car service timeout during reservation', TIMESTAMP '2026-07-01 09:00:00'
WHERE NOT EXISTS (SELECT 1 FROM saga_compensation_failure WHERE id = 1);

INSERT INTO saga_compensation_failure (id, car_id, reservation_token, operation, reason, created_at)
SELECT 2, 2, 'failure-token-002', 'RELEASE_CAR', 'Release request returned conflict', TIMESTAMP '2026-07-02 10:15:00'
WHERE NOT EXISTS (SELECT 1 FROM saga_compensation_failure WHERE id = 2);

INSERT INTO saga_compensation_failure (id, car_id, reservation_token, operation, reason, created_at)
SELECT 3, 3, 'failure-token-003', 'RESERVE_CAR', 'Reservation token already exists', TIMESTAMP '2026-07-03 11:30:00'
WHERE NOT EXISTS (SELECT 1 FROM saga_compensation_failure WHERE id = 3);

INSERT INTO saga_compensation_failure (id, car_id, reservation_token, operation, reason, created_at)
SELECT 4, 4, 'failure-token-004', 'RELEASE_CAR', 'Car was not in rented status', TIMESTAMP '2026-07-04 13:45:00'
WHERE NOT EXISTS (SELECT 1 FROM saga_compensation_failure WHERE id = 4);

INSERT INTO saga_compensation_failure (id, car_id, reservation_token, operation, reason, created_at)
SELECT 5, 5, 'failure-token-005', 'RESERVE_CAR', 'Car was under maintenance', TIMESTAMP '2026-07-05 08:10:00'
WHERE NOT EXISTS (SELECT 1 FROM saga_compensation_failure WHERE id = 5);

INSERT INTO saga_compensation_failure (id, car_id, reservation_token, operation, reason, created_at)
SELECT 6, 6, 'failure-token-006', 'RELEASE_CAR', 'Network interruption while compensating', TIMESTAMP '2026-07-06 15:20:00'
WHERE NOT EXISTS (SELECT 1 FROM saga_compensation_failure WHERE id = 6);

INSERT INTO saga_compensation_failure (id, car_id, reservation_token, operation, reason, created_at)
SELECT 7, 7, 'failure-token-007', 'RESERVE_CAR', 'Car inventory was temporarily locked', TIMESTAMP '2026-07-07 16:25:00'
WHERE NOT EXISTS (SELECT 1 FROM saga_compensation_failure WHERE id = 7);

INSERT INTO saga_compensation_failure (id, car_id, reservation_token, operation, reason, created_at)
SELECT 8, 8, 'failure-token-008', 'RELEASE_CAR', 'Remote service returned unavailable', TIMESTAMP '2026-07-08 17:35:00'
WHERE NOT EXISTS (SELECT 1 FROM saga_compensation_failure WHERE id = 8);

INSERT INTO saga_compensation_failure (id, car_id, reservation_token, operation, reason, created_at)
SELECT 9, 9, 'failure-token-009', 'RESERVE_CAR', 'Customer validation failed after reservation attempt', TIMESTAMP '2026-07-09 18:40:00'
WHERE NOT EXISTS (SELECT 1 FROM saga_compensation_failure WHERE id = 9);

INSERT INTO saga_compensation_failure (id, car_id, reservation_token, operation, reason, created_at)
SELECT 10, 10, 'failure-token-010', 'RELEASE_CAR', 'Manual review required for compensation', TIMESTAMP '2026-07-10 19:55:00'
WHERE NOT EXISTS (SELECT 1 FROM saga_compensation_failure WHERE id = 10);

ALTER TABLE renting_transaction ALTER COLUMN renting_transaction_id RESTART WITH 11;
ALTER TABLE renting_detail ALTER COLUMN renting_detail_id RESTART WITH 11;
ALTER TABLE saga_compensation_failure ALTER COLUMN id RESTART WITH 11;
