CREATE DATABASE monthly_budget;
USE monthly_budget;

CREATE TABLE income (
    id INT AUTO_INCREMENT PRIMARY KEY,
    date DATE NOT NULL,
    description VARCHAR(255),
    value DOUBLE NOT NULL
);

CREATE TABLE expense (
    id INT AUTO_INCREMENT PRIMARY KEY,
    date DATE NOT NULL,
    description VARCHAR(255),
    value DOUBLE NOT NULL,
    category ENUM('Needs', 'Wants') NOT NULL
);

CREATE TABLE IF NOT EXISTS reminders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type ENUM('Income', 'Expense') NOT NULL,
    date DATE NOT NULL,
    description VARCHAR(255),
    cost DOUBLE NOT NULL
);

CREATE TABLE invoice_sequence (
    id INT PRIMARY KEY,
    last_number INT
);

-- Insert initial record
INSERT INTO invoice_sequence (id, last_number) VALUES (1, 0);

CREATE TABLE tasks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    due_date DATE NOT NULL,
    priority VARCHAR(10),
    completed BOOLEAN DEFAULT FALSE
);




drop table income;
Drop table expense;