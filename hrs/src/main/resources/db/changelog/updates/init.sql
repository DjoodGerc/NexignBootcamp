CREATE TABLE tariff_type(
    id BIGSERIAL PRIMARY KEY,
    name varchar(255) NOT NULL
);

-- Create tariff_parameter table
CREATE TABLE tariff_parameter (
    id BIGSERIAL PRIMARY KEY,
    tariff_type_id INTEGER NOT NULL,
    initiating_internal_call_cost DECIMAL NOT NULL,
    recieving_internal_call_cost DECIMAL NOT NULL,
    initiating_external_call_cost DECIMAL NOT NULL,
    recieving_external_call_cost DECIMAL NOT NULL,
    monthly_minute_capacity INTEGER NOT NULL,
    monthly_fee DECIMAL NOT NULL,
    FOREIGN KEY (tariff_type_id) REFERENCES tariff_type(id)
);


CREATE TABLE tariff (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    description VARCHAR(255),
    tariff_parameter_id INTEGER NOT NULL,
    FOREIGN KEY (tariff_parameter_id) REFERENCES tariff_parameter(id)
);