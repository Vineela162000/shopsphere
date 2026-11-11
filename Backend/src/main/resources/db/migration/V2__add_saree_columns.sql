-- Add the new saree columns to the existing table
ALTER TABLE products
    ADD COLUMN sku         VARCHAR(20),
    ADD COLUMN description VARCHAR(2000),
    ADD COLUMN weave       VARCHAR(50),
    ADD COLUMN occasion    VARCHAR(20) NOT NULL DEFAULT 'EVERYDAY',
    ADD COLUMN color       VARCHAR(30),
    ADD COLUMN stock       INTEGER     NOT NULL DEFAULT 0,
    ADD COLUMN bestseller  BOOLEAN     NOT NULL DEFAULT FALSE,
    ADD COLUMN active      BOOLEAN     NOT NULL DEFAULT TRUE,
    ADD COLUMN created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    ADD COLUMN updated_at  TIMESTAMPTZ NOT NULL DEFAULT now();

-- Fill in the two sarees we already have
UPDATE products
SET sku = 'SAR-0001', weave = 'Soft Silk', color = 'Red', occasion = 'FESTIVE', stock = 5,
    description = 'Soft silk saree with a contrast zari border.'
WHERE name = 'Red silk saree';

UPDATE products
SET sku = 'SAR-0002', weave = 'Mangalagiri', color = 'Blue', occasion = 'EVERYDAY', stock = 12,
    description = 'Breathable handloom cotton for everyday wear.'
WHERE name = 'Blue cotton saree';

-- Every row has values now, so make the rules strict
ALTER TABLE products
    ALTER COLUMN sku   SET NOT NULL,
    ALTER COLUMN weave SET NOT NULL,
    ALTER COLUMN color SET NOT NULL,
    ADD CONSTRAINT uq_products_sku       UNIQUE (sku),
    ADD CONSTRAINT chk_products_price    CHECK (price > 0),
    ADD CONSTRAINT chk_products_stock    CHECK (stock >= 0),
    ADD CONSTRAINT chk_products_occasion CHECK (occasion IN ('EVERYDAY', 'FESTIVE', 'BRIDAL'));

-- Indexes make filtering by weave, occasion, and price fast
CREATE INDEX idx_products_weave    ON products (weave);
CREATE INDEX idx_products_occasion ON products (occasion);
CREATE INDEX idx_products_price    ON products (price);

-- Trigger: keep updated_at current on every UPDATE
CREATE FUNCTION set_updated_at() RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_products_updated_at
    BEFORE UPDATE ON products
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();