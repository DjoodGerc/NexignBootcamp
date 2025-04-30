INSERT INTO tariff_type (id,name) values
    (1,'Поивентный'),
    (2,'Помесячный'),
    (3,'Безлимит');

INSERT INTO tariff_parameter (
    id,
    tariff_type_id,
    initiating_internal_call_cost,
    recieving_internal_call_cost,
    initiating_external_call_cost,
    recieving_external_call_cost,
    monthly_minute_capacity,
    monthly_fee
) VALUES
( 1,1, 1.5, 0, 2.5, 0, 0, 0),
( 2,2, 1.5, 0, 2.5, 0, 50, 100);

INSERT INTO tariff (id,name,tariff_parameter_id,is_active,creation_date,description) values
    (11,'Классика',1,true,now(),'Исходящие: Абонентам «Ромашка» = 1.5 у.е./минута; Абонентам других операторов = 2.5 у.е./минута Входящие: Бесплатно'),
    (12,'Помесячный',2,true,now(),'50 минут входящие и исходящие = 100 у.е. /месяц. Начиная с 51 минуты расчет продолжается по тарифу «Классика».');


