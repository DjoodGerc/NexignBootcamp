INSERT INTO call_type (name) values
    ('Исходящий'),
    ('Входящий');

INSERT INTO change_type (name) values
    ('Пополнение'),
    ('Списание');



INSERT INTO subscriber (
    msisdn, tariff_id, balance, tariff_balance, is_active,
    name, registration_date, passport_data, last_month_tariffication_date
) VALUES
    ('79123456789', 11, 100, 0, true, 'Райан Гослинг', '2023-03-15 11:23:45', '1234567890',null),
    ('79456789012', 11, 100, 0, true, 'Виктор Пелевин', '2023-07-22 14:05:33', '2345678901', null),
    ('79567890123', 11, 100, 0, true, 'Ирина Никулина', '2023-01-05 09:12:18', '3456789012',null),
    ('79890123456', 12, 100, 50, true, 'Виталий Цаль', '2023-11-30 16:45:07', '4567890123',null),
    ('79012345678', 12, 100, 50, true, 'Екатерина Мизулина', '2023-05-19 08:30:22', '5678901234',null);