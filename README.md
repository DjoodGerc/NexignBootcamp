Проект тестового задания для поступления на Nx Bootcamp  
Spring H2 Liquibase Lombok Mapstruct Jackson  
бд: 
```
create table if not exists operator(
    id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar
);

Create table if not exists subscriber(
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    msisdn varchar(11) UNIQUE,
    operator_id integer,
    FOREIGN KEY (operator_id) REFERENCES operator(id)
);
Create table if not exists call_data(
   id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   initiating_id integer,
   receiving_id integer,
   start_call TIMESTAMP,
   end_call TIMESTAMP,
   FOREIGN KEY (initiating_id) REFERENCES subscriber(id),
   FOREIGN KEY (receiving_id) REFERENCES subscriber(id)
);
```
бд создается автоматически при помощи liquibase  

абоненты:  

```
INSERT INTO operator (name) values
    ('Romashka'),
    ('MTS'),
    ('Megafon');
INSERT INTO subscriber (msisdn,operator_id) VALUES
    ('79123456789', 1),
    ('79234567890', 2),
    ('79345678901', 3),
    ('79456789012', 1),
    ('79567890123', 1),
    ('79678901234', 2),
    ('79789012345', 2),
    ('79890123456', 1),
    ('79901234567', 3),
    ('79012345678', 1);
```
1) 
```
POST http://localhost:8080/generate
```
10 абонентов уже лежат в дб
генерирует случайное кол-во cdr записей звонков между ними (от 1 до 5000)  
  
```
POST http://localhost:8080/generate/{bot}/{top}
```
10 абонентов уже лежат в дб  
генерирует случайное кол-во cdr записей звонков между ними (от {bot} до {top})  
!Если бд не пуста выводится OK: Data already generated
2)
```
GET http://localhost:8080/getUdr
```
Udr для всех пользователей ОПЕРАТОРА РОМАШКА за весь период  
```
GET http://localhost:8080/getUdr/byYear/{year}/byMonth/{month}
```
Udr для всех пользователей ОПЕРАТОРА РОМАШКА за yyyy-mm  
```
GET http://localhost:8080/getUdr/getUdr/byMsisdn/{msisdn}
```
Udr для абонента ОПЕРАТОРА РОМАШКА с номером msisdn за весь период тарификации  
Если абонента с таким номером не нашлось HTTP.NOT_FOUND  
Если абонент не оператора Ромашка: Subscriber of another operator NOT_FOUND
```
GET http://localhost:8080/getUdr/getUdr/byMsisdn/{msisdn}/byYear/{year}/byMonth/{month}
```
Udr для абонента ОПЕРАТОРА РОМАШКА с номером msisdn за yyyy-mm  
Если абонента с таким номером не нашлось HTTP.NOT_FOUND  
-Возвращает udr записи типа 
```
{
    "msisdn": "79992221122",
    "incomingCall": {
        "totalTime": "02:12:13"
    },
    "outcomingCall": {
        "totalTime": "00:02:50"
    }
}
```
Если абонент не оператора Ромашка: Subscriber of another operator NOT_FOUND  
3) 
```
POST http://localhost:8080/createCdrReport
```
  example body:  
```
{
  "msisdn": "79284765839",
  "startDate": "2023-10-26T22:00:00",
  "endDate": "2023-10-28T06:00:00"
}
```
Создает csv файл в папке /reports с названием {msisdn}\_{UUID}.csv, содержащий cdr записи за указанный период   
OK: возвращает {msisdn}\_{UUID} (сsv создан)  
NOT_FOUND: msisdn не найден возвращает  Subscriber not found_{UUID}  
INTERNAL_SERVER_ERROR: ловит ошибки Writer {e.message}\_{UUID}   
BAD_REQUEST:  неправильно переданы даты Invalid dates\_{UUID}  
  
  
  
