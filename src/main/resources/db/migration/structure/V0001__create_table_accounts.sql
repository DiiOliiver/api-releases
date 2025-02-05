CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE accounts (
    id uuid NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    cpf VARCHAR(20) NOT NULL,
    membership_number VARCHAR(80) NOT NULL,
    balance NUMERIC(15,2) NOT NULL DEFAULT 0.00,
    version INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    deleted_at TIMESTAMPTZ
);

-- Usuário mockado
insert into accounts (name, email, "password", cpf, membership_number) values (
   'Diego Feitosa', 'diego.feitosa.oliveira@hotmail.com',
   '$2a$10$tqXYtD6PAxA3sK7x6cuH8.9qTp2IBW/i7Ku55sy.FY4pklUHc22by', '02515545203', '578576-6'
);
