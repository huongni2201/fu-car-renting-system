ALTER TABLE car_information
ADD COLUMN IF NOT EXISTS reservation_token VARCHAR(255);
