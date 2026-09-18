-- ==========================================================
-- V1__initial_schema.sql
-- Initial database schema migration for VJ-PAY (PostgreSQL)
-- Generated based on JPA Entity definitions:
--   - Wallet (com.coforge.entities.Wallet)
--   - Customer (com.coforge.entities.Customer)
--   - Beneficiary (com.coforge.entities.Beneficiary)
--   - BankAccount (com.coforge.entities.BankAccount)
--   - Transaction (com.coforge.entities.Transaction)
--   - BillPayment (com.coforge.entities.BillPayment)
-- ==========================================================

-- ----------------------------------------------------------
-- 1. Table: wallet
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS wallet (
    wallet_id BIGSERIAL PRIMARY KEY,
    balance DECIMAL(38, 2) NOT NULL
);

-- ----------------------------------------------------------
-- 2. Table: customers
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS customers (
    cust_id BIGSERIAL PRIMARY KEY,
    role VARCHAR(255) DEFAULT 'USER',
    cust_name VARCHAR(255) NOT NULL,
    mobile_number VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    pwd VARCHAR(255) NOT NULL,
    wallet_id BIGINT,
    CONSTRAINT uk_customers_mobile_number UNIQUE (mobile_number),
    CONSTRAINT uk_customers_email UNIQUE (email),
    CONSTRAINT uk_customers_wallet_id UNIQUE (wallet_id),
    CONSTRAINT fk_customers_wallet FOREIGN KEY (wallet_id) REFERENCES wallet (wallet_id)
);

-- ----------------------------------------------------------
-- 3. Table: beneficiary
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS beneficiary (
    beneficiary_id BIGSERIAL PRIMARY KEY,
    beneficiary_name VARCHAR(255) NOT NULL,
    mobile_number VARCHAR(255),
    wallet_id BIGINT,
    CONSTRAINT fk_beneficiary_wallet FOREIGN KEY (wallet_id) REFERENCES wallet (wallet_id)
);

CREATE INDEX IF NOT EXISTS idx_beneficiary_wallet_id ON beneficiary (wallet_id);

-- ----------------------------------------------------------
-- 4. Table: bank_account
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS bank_account (
    bank_account_id BIGSERIAL PRIMARY KEY,
    account_no VARCHAR(255) NOT NULL,
    ifsc_code VARCHAR(255) NOT NULL,
    bankname VARCHAR(255) NOT NULL,
    balance DOUBLE PRECISION NOT NULL,
    customer_id BIGINT,
    CONSTRAINT fk_bank_account_customer FOREIGN KEY (customer_id) REFERENCES customers (cust_id)
);

CREATE INDEX IF NOT EXISTS idx_bank_account_customer_id ON bank_account (customer_id);

-- ----------------------------------------------------------
-- 5. Table: transaction
-- Note: "transaction" is a reserved word in PostgreSQL, so we quote it
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS "transaction" (
    transaction_id BIGSERIAL PRIMARY KEY,
    transaction_type VARCHAR(255) NOT NULL,
    transaction_status VARCHAR(255) NOT NULL,
    transaction_amount DOUBLE PRECISION NOT NULL,
    transaction_date DATE NOT NULL,
    cust_id BIGINT,
    description VARCHAR(255),
    category VARCHAR(255),
    sub_category VARCHAR(255),
    CONSTRAINT fk_transaction_customer FOREIGN KEY (cust_id) REFERENCES customers (cust_id)
);

CREATE INDEX IF NOT EXISTS idx_transaction_cust_id ON "transaction" (cust_id);

-- ----------------------------------------------------------
-- 6. Table: bill_payment
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS bill_payment (
    bill_id BIGSERIAL PRIMARY KEY,
    payment_date TIMESTAMP,
    amount DOUBLE PRECISION NOT NULL,
    bill_type SMALLINT NOT NULL,
    bill_data JSONB NOT NULL,
    wallet_id BIGINT,
    CONSTRAINT fk_bill_payment_wallet FOREIGN KEY (wallet_id) REFERENCES wallet (wallet_id)
);

CREATE INDEX IF NOT EXISTS idx_bill_payment_wallet_id ON bill_payment (wallet_id);

-- ----------------------------------------------------------
-- 7. Sequences for GenerationType.AUTO entities
-- PostgreSQL uses native SEQUENCE objects instead of MySQL's table-based sequences
-- ----------------------------------------------------------
CREATE SEQUENCE IF NOT EXISTS beneficiary_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS transaction_seq START WITH 1 INCREMENT BY 50;
