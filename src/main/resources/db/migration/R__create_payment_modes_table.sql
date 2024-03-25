CREATE TABLE IF NOT EXISTS payment_modes_master (
    id BIGSERIAL PRIMARY KEY,
    payment_mode TEXT NOT NULL,
    is_supported BOOLEAN NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

DELETE FROM payment_modes_master;

INSERT INTO payment_modes_master (payment_mode, is_supported) VALUES
('UPI', true),
('DEBIT_CARD', true),
('CREDIT_CARD', true),
('NET_BANKING', true),
('CASH_ON_DELIVERY', true);
