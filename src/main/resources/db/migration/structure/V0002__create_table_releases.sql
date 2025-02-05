CREATE TABLE releases (
    id uuid NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    account_id uuid NOT NULL,
    amount NUMERIC(15,2) NOT NULL,
    payment_type SMALLINT NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    deleted_at TIMESTAMPTZ,
    FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE
);
