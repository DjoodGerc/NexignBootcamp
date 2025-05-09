CREATE TABLE call_type(
    id BIGSERIAL PRIMARY KEY,
    name varchar(255)

);

CREATE TABLE change_type(
    id BIGSERIAL PRIMARY KEY,
    name varchar(255)
);

CREATE TABLE subscriber (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    msisdn VARCHAR(11) UNIQUE,
    tariff_id INTEGER,
    balance DECIMAL,
    registration_date TIMESTAMP,
    passport_data VARCHAR(10) UNIQUE,
    tariff_balance INTEGER,
    is_active BOOLEAN
);

CREATE TABLE  call (
    id BIGSERIAL PRIMARY KEY,
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

CREATE TABLE balance_changes (
    id BIGSERIAL PRIMARY KEY,
    subscriber_id INT,
    value DECIMAL,
    date TIMESTAMP,
    change_type_id INT,
    FOREIGN KEY (subscriber_id) REFERENCES subscriber(id),
    FOREIGN KEY (change_type_id) REFERENCES change_type(id)
);
