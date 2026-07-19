INSERT INTO manufacturers (manufacturer_id, manufacturer_name, description, manufacturer_country)
SELECT 1, 'Toyota', 'Reliable passenger cars and hybrids', 'Japan'
WHERE NOT EXISTS (SELECT 1 FROM manufacturers WHERE manufacturer_id = 1);

INSERT INTO manufacturers (manufacturer_id, manufacturer_name, description, manufacturer_country)
SELECT 2, 'Honda', 'Compact cars and family vehicles', 'Japan'
WHERE NOT EXISTS (SELECT 1 FROM manufacturers WHERE manufacturer_id = 2);

INSERT INTO manufacturers (manufacturer_id, manufacturer_name, description, manufacturer_country)
SELECT 3, 'Hyundai', 'Modern cars for city and travel', 'South Korea'
WHERE NOT EXISTS (SELECT 1 FROM manufacturers WHERE manufacturer_id = 3);

INSERT INTO manufacturers (manufacturer_id, manufacturer_name, description, manufacturer_country)
SELECT 4, 'VinFast', 'Vietnamese electric vehicle manufacturer', 'Vietnam'
WHERE NOT EXISTS (SELECT 1 FROM manufacturers WHERE manufacturer_id = 4);

INSERT INTO manufacturers (manufacturer_id, manufacturer_name, description, manufacturer_country)
SELECT 5, 'Ford', 'Utility cars and vans for travel fleets', 'United States'
WHERE NOT EXISTS (SELECT 1 FROM manufacturers WHERE manufacturer_id = 5);

INSERT INTO manufacturers (manufacturer_id, manufacturer_name, description, manufacturer_country)
SELECT 6, 'BMW', 'Premium sedans and SUVs', 'Germany'
WHERE NOT EXISTS (SELECT 1 FROM manufacturers WHERE manufacturer_id = 6);

INSERT INTO manufacturers (manufacturer_id, manufacturer_name, description, manufacturer_country)
SELECT 7, 'Mercedes-Benz', 'Luxury passenger cars', 'Germany'
WHERE NOT EXISTS (SELECT 1 FROM manufacturers WHERE manufacturer_id = 7);

INSERT INTO manufacturers (manufacturer_id, manufacturer_name, description, manufacturer_country)
SELECT 8, 'Kia', 'Family cars and city SUVs', 'South Korea'
WHERE NOT EXISTS (SELECT 1 FROM manufacturers WHERE manufacturer_id = 8);

INSERT INTO manufacturers (manufacturer_id, manufacturer_name, description, manufacturer_country)
SELECT 9, 'Mazda', 'Efficient sedans and crossovers', 'Japan'
WHERE NOT EXISTS (SELECT 1 FROM manufacturers WHERE manufacturer_id = 9);

INSERT INTO manufacturers (manufacturer_id, manufacturer_name, description, manufacturer_country)
SELECT 10, 'Nissan', 'Passenger cars for daily rental', 'Japan'
WHERE NOT EXISTS (SELECT 1 FROM manufacturers WHERE manufacturer_id = 10);

INSERT INTO suppliers (supplier_id, supplier_name, supplier_description, supplier_address)
SELECT 1, 'FPT Rental Center', 'Main car rental supplier', 'Hoa Lac Hi-Tech Park, Hanoi'
WHERE NOT EXISTS (SELECT 1 FROM suppliers WHERE supplier_id = 1);

INSERT INTO suppliers (supplier_id, supplier_name, supplier_description, supplier_address)
SELECT 2, 'Saigon Auto Partner', 'Southern region car supplier', 'District 1, Ho Chi Minh City'
WHERE NOT EXISTS (SELECT 1 FROM suppliers WHERE supplier_id = 2);

INSERT INTO suppliers (supplier_id, supplier_name, supplier_description, supplier_address)
SELECT 3, 'Da Nang Mobility', 'Central region car supplier', 'Hai Chau, Da Nang'
WHERE NOT EXISTS (SELECT 1 FROM suppliers WHERE supplier_id = 3);

INSERT INTO suppliers (supplier_id, supplier_name, supplier_description, supplier_address)
SELECT 4, 'Can Tho Car Hub', 'Mekong Delta supplier', 'Ninh Kieu, Can Tho'
WHERE NOT EXISTS (SELECT 1 FROM suppliers WHERE supplier_id = 4);

INSERT INTO suppliers (supplier_id, supplier_name, supplier_description, supplier_address)
SELECT 5, 'Hue Travel Fleet', 'Heritage city rental supplier', 'Phu Hoi, Hue'
WHERE NOT EXISTS (SELECT 1 FROM suppliers WHERE supplier_id = 5);

INSERT INTO suppliers (supplier_id, supplier_name, supplier_description, supplier_address)
SELECT 6, 'Nha Trang Rental', 'Coastal travel supplier', 'Loc Tho, Nha Trang'
WHERE NOT EXISTS (SELECT 1 FROM suppliers WHERE supplier_id = 6);

INSERT INTO suppliers (supplier_id, supplier_name, supplier_description, supplier_address)
SELECT 7, 'Hai Phong Transport', 'Northern port city supplier', 'Ngo Quyen, Hai Phong'
WHERE NOT EXISTS (SELECT 1 FROM suppliers WHERE supplier_id = 7);

INSERT INTO suppliers (supplier_id, supplier_name, supplier_description, supplier_address)
SELECT 8, 'Hanoi Premium Cars', 'Premium sedan supplier', 'Cau Giay, Hanoi'
WHERE NOT EXISTS (SELECT 1 FROM suppliers WHERE supplier_id = 8);

INSERT INTO suppliers (supplier_id, supplier_name, supplier_description, supplier_address)
SELECT 9, 'Binh Duong Logistics', 'Industrial zone fleet supplier', 'Thu Dau Mot, Binh Duong'
WHERE NOT EXISTS (SELECT 1 FROM suppliers WHERE supplier_id = 9);

INSERT INTO suppliers (supplier_id, supplier_name, supplier_description, supplier_address)
SELECT 10, 'Vung Tau Weekend Cars', 'Weekend trip supplier', 'Ward 1, Vung Tau'
WHERE NOT EXISTS (SELECT 1 FROM suppliers WHERE supplier_id = 10);

INSERT INTO car_information (car_id, car_name, car_description, number_of_doors, seating_capacity, fuel_type, manufacturing_year, car_status, reservation_token, manufacturer_id, supplier_id)
SELECT 1, 'Toyota Vios 2023', 'Economy sedan for city trips', 4, 5, 'GASOLINE', 2023, 'AVAILABLE', NULL, 1, 1
WHERE NOT EXISTS (SELECT 1 FROM car_information WHERE car_id = 1);

INSERT INTO car_information (car_id, car_name, car_description, number_of_doors, seating_capacity, fuel_type, manufacturing_year, car_status, reservation_token, manufacturer_id, supplier_id)
SELECT 2, 'Honda City 2022', 'Compact sedan with good fuel economy', 4, 5, 'GASOLINE', 2022, 'AVAILABLE', NULL, 2, 1
WHERE NOT EXISTS (SELECT 1 FROM car_information WHERE car_id = 2);

INSERT INTO car_information (car_id, car_name, car_description, number_of_doors, seating_capacity, fuel_type, manufacturing_year, car_status, reservation_token, manufacturer_id, supplier_id)
SELECT 3, 'Hyundai Accent 2021', 'Comfortable sedan for daily rental', 4, 5, 'GASOLINE', 2021, 'RENTED', 'seed-token-rt-002', 3, 2
WHERE NOT EXISTS (SELECT 1 FROM car_information WHERE car_id = 3);

INSERT INTO car_information (car_id, car_name, car_description, number_of_doors, seating_capacity, fuel_type, manufacturing_year, car_status, reservation_token, manufacturer_id, supplier_id)
SELECT 4, 'VinFast VF e34 2023', 'Electric SUV for local travel', 5, 5, 'ELECTRIC', 2023, 'AVAILABLE', NULL, 4, 2
WHERE NOT EXISTS (SELECT 1 FROM car_information WHERE car_id = 4);

INSERT INTO car_information (car_id, car_name, car_description, number_of_doors, seating_capacity, fuel_type, manufacturing_year, car_status, reservation_token, manufacturer_id, supplier_id)
SELECT 5, 'Toyota Innova 2020', 'Seven-seat MPV for family trips', 5, 7, 'GASOLINE', 2020, 'MAINTENANCE', NULL, 1, 3
WHERE NOT EXISTS (SELECT 1 FROM car_information WHERE car_id = 5);

INSERT INTO car_information (car_id, car_name, car_description, number_of_doors, seating_capacity, fuel_type, manufacturing_year, car_status, reservation_token, manufacturer_id, supplier_id)
SELECT 6, 'Honda CR-V Hybrid 2024', 'Hybrid SUV for long distance travel', 5, 5, 'HYBRID', 2024, 'AVAILABLE', NULL, 2, 3
WHERE NOT EXISTS (SELECT 1 FROM car_information WHERE car_id = 6);

INSERT INTO car_information (car_id, car_name, car_description, number_of_doors, seating_capacity, fuel_type, manufacturing_year, car_status, reservation_token, manufacturer_id, supplier_id)
SELECT 7, 'Ford Transit 2022', 'Passenger van for group travel', 4, 16, 'DIESEL', 2022, 'AVAILABLE', NULL, 5, 4
WHERE NOT EXISTS (SELECT 1 FROM car_information WHERE car_id = 7);

INSERT INTO car_information (car_id, car_name, car_description, number_of_doors, seating_capacity, fuel_type, manufacturing_year, car_status, reservation_token, manufacturer_id, supplier_id)
SELECT 8, 'BMW 320i 2021', 'Premium sedan for business travel', 4, 5, 'GASOLINE', 2021, 'RENTED', 'seed-token-rt-003', 6, 8
WHERE NOT EXISTS (SELECT 1 FROM car_information WHERE car_id = 8);

INSERT INTO car_information (car_id, car_name, car_description, number_of_doors, seating_capacity, fuel_type, manufacturing_year, car_status, reservation_token, manufacturer_id, supplier_id)
SELECT 9, 'Mazda CX-5 2023', 'Crossover SUV for weekend trips', 5, 5, 'GASOLINE', 2023, 'AVAILABLE', NULL, 9, 6
WHERE NOT EXISTS (SELECT 1 FROM car_information WHERE car_id = 9);

INSERT INTO car_information (car_id, car_name, car_description, number_of_doors, seating_capacity, fuel_type, manufacturing_year, car_status, reservation_token, manufacturer_id, supplier_id)
SELECT 10, 'Mercedes-Benz C200 2022', 'Luxury sedan for premium rental', 4, 5, 'GASOLINE', 2022, 'INACTIVE', NULL, 7, 8
WHERE NOT EXISTS (SELECT 1 FROM car_information WHERE car_id = 10);

ALTER TABLE manufacturers ALTER COLUMN manufacturer_id RESTART WITH 11;
ALTER TABLE suppliers ALTER COLUMN supplier_id RESTART WITH 11;
ALTER TABLE car_information ALTER COLUMN car_id RESTART WITH 11;
