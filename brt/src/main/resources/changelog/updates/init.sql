CREATE TABLE if not exist call_type(
    id SERIAL PRIMARY KEY,
    name varchar(255)

);

CREATE TABLE if not exist change_type(
    id SERIAL PRIMARY KEY,
    name varchar(255)
);

CREATE TABLE if not exist tariff (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    is_active BOOLEAN,
    creation_date TIMESTAMP,
    description VARCHAR(255)
);

-- Create subscriber table
CREATE TABLE if not exist subscriber (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    msisdn VARCHAR(11),
    tariff_id INT,
    balance MONEY,
    registration_date TIMESTAMP,
    passport_data VARCHAR(10),
    tariff_balance INTEGER,
    is_active BOOLEAN,
    FOREIGN KEY (tariff_id) REFERENCES tariff(id)
);

-- Create call table
CREATE TABLE if not exist call (
    id SERIAL PRIMARY KEY,
    subscriber_id INT,
    start_call TIMESTAMP,
    end_call TIMESTAMP,
    total_cost MONEY,
    call_type_id INT,
    is_romashka_call BOOLEAN,
    FOREIGN KEY (subscriber_id) REFERENCES subscriber(id),
    FOREIGN KEY (call_type_id) REFERENCES call_type(id)

);

-- Create balance_changes table
CREATE TABLE if not exist balance_changes (
    id SERIAL PRIMARY KEY,
    subscriber_id INT,
    value MONEY,
    date TIMESTAMP,
    change_type_id INT,
    FOREIGN KEY (subscriber_id) REFERENCES subscriber(id),
    FOREIGN KEY (change_type_id) REFERENCES change_type(id)
);
