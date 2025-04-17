CREATE TABLE call_type(
    id SERIAL PRIMARY KEY,
    name varchar(255)

);

CREATE TABLE change_type(
    id SERIAL PRIMARY KEY,
    name varchar(255)
);

CREATE TABLE tariff (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    is_active BOOLEAN,
    creation_date TIMESTAMP,
    description VARCHAR(255)
);

-- Create subscriber table
CREATE TABLE subscriber (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    msisdn VARCHAR(11),
    tariff_id INTEGER,
    balance DECIMAL,
    registration_date TIMESTAMP,
    passport_data VARCHAR(10),
    tariff_balance INTEGER,
    is_active BOOLEAN,
    FOREIGN KEY (tariff_id) REFERENCES tariff(id)
);

-- Create call table
CREATE TABLE  call (
    id SERIAL PRIMARY KEY,
    subscriber_id INT,
    opponent_msisdn varchar(11),
    start_call TIMESTAMP,
    end_call TIMESTAMP,
    total_cost DECIMAL,
    call_type_id INT,
    is_romashka_call BOOLEAN,
    FOREIGN KEY (subscriber_id) REFERENCES subscriber(id),
    FOREIGN KEY (call_type_id) REFERENCES call_type(id)

);

-- Create balance_changes table
CREATE TABLE balance_changes (
    id SERIAL PRIMARY KEY,
    subscriber_id INT,
    value DECIMAL,
    date TIMESTAMP,
    change_type_id INT,
    FOREIGN KEY (subscriber_id) REFERENCES subscriber(id),
    FOREIGN KEY (change_type_id) REFERENCES change_type(id)
);
