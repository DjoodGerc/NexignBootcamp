ALTER TABLE subscriber ADD COLUMN last_month_tariffication_date TIMESTAMP;

ALTER TABLE tariff ADD COLUMN tariffication_type_id INT;

CREATE TABLE if not exists tariffication_type(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255)
);


ALTER TABLE tariff ADD CONSTRAINT fk_tariff_tariffication_type FOREIGN KEY (tariffication_type_id) REFERENCES tariffication_type(id);
