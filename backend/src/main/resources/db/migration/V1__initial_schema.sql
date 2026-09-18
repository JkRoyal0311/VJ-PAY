-- ==========================================================
-- V1__initial_schema.sql
-- Initial database schema migration for VJ-PAY
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
-- Entity: com.coforge.entities.Wallet
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS wallet (
    wallet_id BIGINT NOT NULL AUTO_INCREMENT,
    balance DECIMAL(38, 2) NOT NULL,
    PRIMARY KEY (wallet_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------
-- 2. Table: customers
-- Entity: com.coforge.entities.Customer
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS customers (
    cust_id BIGINT NOT NULL AUTO_INCREMENT,
    role VARCHAR(255) DEFAULT 'USER',
    cust_name VARCHAR(255) NOT NULL,
    mobile_number VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    pwd VARCHAR(255) NOT NULL,
    wallet_id BIGINT,
    PRIMARY KEY (cust_id),
    UNIQUE KEY uk_customers_mobile_number (mobile_number),
    UNIQUE KEY uk_customers_email (email),
    UNIQUE KEY uk_customers_wallet_id (wallet_id),
    CONSTRAINT fk_customers_wallet FOREIGN KEY (wallet_id) REFERENCES wallet (wallet_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------
-- 3. Table: beneficiary
-- Entity: com.coforge.entities.Beneficiary
-- Target of Wallet's @OneToMany @JoinColumn(name = "wallet_id")
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS beneficiary (
    beneficiary_id BIGINT NOT NULL AUTO_INCREMENT,
    beneficiary_name VARCHAR(255) NOT NULL,
    mobile_number VARCHAR(255),
    wallet_id BIGINT,
    PRIMARY KEY (beneficiary_id),
    KEY idx_beneficiary_wallet_id (wallet_id),
    CONSTRAINT fk_beneficiary_wallet FOREIGN KEY (wallet_id) REFERENCES wallet (wallet_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------
-- 4. Table: bank_account
-- Entity: com.coforge.entities.BankAccount
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS bank_account (
    bank_account_id BIGINT NOT NULL AUTO_INCREMENT,
    account_no VARCHAR(255) NOT NULL,
    ifsc_code VARCHAR(255) NOT NULL,
    bankname VARCHAR(255) NOT NULL,
    balance DOUBLE NOT NULL,
    customer_id BIGINT,
    PRIMARY KEY (bank_account_id),
    KEY idx_bank_account_customer_id (customer_id),
    CONSTRAINT fk_bank_account_customer FOREIGN KEY (customer_id) REFERENCES customers (cust_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------
-- 5. Table: transaction
-- Entity: com.coforge.entities.Transaction
-- Enums: TransactionCategory, TransactionSubCategory (STRING)
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `transaction` (
    transaction_id BIGINT NOT NULL AUTO_INCREMENT,
    transaction_type VARCHAR(255) NOT NULL,
    transaction_status VARCHAR(255) NOT NULL,
    transaction_amount DOUBLE NOT NULL,
    transaction_date DATE NOT NULL,
    cust_id BIGINT,
    description VARCHAR(255),
    category VARCHAR(255),
    sub_category VARCHAR(255),
    PRIMARY KEY (transaction_id),
    KEY idx_transaction_cust_id (cust_id),
    CONSTRAINT fk_transaction_customer FOREIGN KEY (cust_id) REFERENCES customers (cust_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------
-- 6. Table: bill_payment
-- Entity: com.coforge.entities.BillPayment
-- Enum: BillType (ORDINAL -> SMALLINT)
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS bill_payment (
    bill_id BIGINT NOT NULL AUTO_INCREMENT,
    payment_date DATETIME(6),
    amount DOUBLE NOT NULL,
    bill_type SMALLINT NOT NULL,
    bill_data JSON NOT NULL,
    wallet_id BIGINT,
    PRIMARY KEY (bill_id),
    KEY idx_bill_payment_wallet_id (wallet_id),
    CONSTRAINT fk_bill_payment_wallet FOREIGN KEY (wallet_id) REFERENCES wallet (wallet_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------
-- 7. Sequence tables for GenerationType.AUTO
-- Required by Hibernate 6 MySQL Dialect when AUTO strategy is used
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS beneficiary_seq (
    next_val BIGINT
) ENGINE=InnoDB;

INSERT INTO beneficiary_seq (next_val)
SELECT 1 WHERE NOT EXISTS (SELECT 1 FROM beneficiary_seq);

CREATE TABLE IF NOT EXISTS transaction_seq (
    next_val BIGINT
) ENGINE=InnoDB;

INSERT INTO transaction_seq (next_val)
SELECT 1 WHERE NOT EXISTS (SELECT 1 FROM transaction_seq);
