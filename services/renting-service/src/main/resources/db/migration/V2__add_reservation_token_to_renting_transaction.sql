ALTER TABLE renting_transaction
ADD COLUMN IF NOT EXISTS reservation_token VARCHAR(255);
