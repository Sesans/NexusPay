CREATE TABLE accounts (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    currency_code VARCHAR(3) NOT NULL,
    balance_cents BIGINT NOT NULL DEFAULT 0,
    version INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_balance_positive CHECK (balance_cents >=0),
    CONSTRAINT uk_user_currency UNIQUE (user_id, currency_code)
);

CREATE TABLE transactions(
    ID BIGSERIAL PRIMARY KEY,
    correlation_id UUID NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL
);

CREATE TABLE ledger_entries(
    id BIGSERIAL PRIMARY KEY,
    account_id UUID NOT NULL,
    transaction_id BIGINT NOT NULL,
    amount BIGINT NOT NULL,
    entry_type VARCHAR(10) NOT NULL,
    current_hash VARCHAR(64) NOT NULL,
    previous_hash VARCHAR(64) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_ledger_account FOREIGN KEY (account_id) REFERENCES accounts (id),
    CONSTRAINT fk_ledger_transaction FOREIGN KEY (transaction_id) REFERENCES transactions (id)
);

CREATE INDEX idx_ledger_account_date ON ledger_entries (account_id, created_at DESC);