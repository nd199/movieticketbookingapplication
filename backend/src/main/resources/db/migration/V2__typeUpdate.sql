ALTER TABLE movie
    ALTER COLUMN cost TYPE DOUBLE PRECISION USING (cost::DOUBLE PRECISION);
ALTER TABLE movie
    ALTER COLUMN rating TYPE DOUBLE PRECISION USING (rating::DOUBLE PRECISION);
ALTER TABLE customer
    ALTER COLUMN email TYPE TEXT USING (email::TEXT);
ALTER TABLE customer
    ALTER COLUMN password TYPE TEXT USING (password::TEXT);