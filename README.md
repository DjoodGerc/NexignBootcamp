Проект тестового задания для поступления на Nx Bootcamp
Spring H2 Liquibase Lombok Mapstruct Jackson
бд: 


Create table if not exists subscriber(
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    number varchar(11) UNIQUE
);

//от id можно отказаться и использовать number, как pk, но, в перспективе расширения сервиса мы хотим иметь id абонентов, чтобы не работать с номерами


Create table if not exists cdr(
   id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   initiating_id integer,
   receiving_id integer,
   start_call TIMESTAMP,
   end_call TIMESTAMP,
   FOREIGN KEY (initiating_id) REFERENCES subscriber(id),
   FOREIGN KEY (receiving_id) REFERENCES subscriber(id)
);
бд создается автоматически при помощи liquibase

1)    
    POST http://localhost:8080/generate
создает n (по умолчанию 10) пользователей с случайным номером телефона
+генерирует случайное кол-во cdr записей звонков между ними (от 1 до 5000)
  POST http://localhost:8080/generate/{nSubs}
тоже но указываем кол-во абонентов
 - Создает записи в хронологическом порядке от за год (последняя возможная дата вчера), длина звонка -случайное значение от 1 секунды до 1 дня

2) 
  GET http://localhost:8080/getUdr
Udr для всех пользователей за весь период
  GET http://localhost:8080/getUdr/byYear/{year}/byMonth/{month}
Udr для всех пользователей за yyyy-mm
  GET http://localhost:8080/getUdr/getUdr/byNumber/{number}
Udr для абонента с номером number за весь период тарификации
Если абонента с таким номером не нашлось HTTP.NOT_FOUND
  GET http://localhost:8080/getUdr/getUdr/byNumber/{number}/byYear/{year}/byMonth/{month}
Udr для абонента с номером number за yyyy-mm
Если абонента с таким номером не нашлось HTTP.NOT_FOUND
-Возвращает udr записи типа 
{
    "msisdn": "79992221122",
    "incomingCall": {
        "totalTime": "02:12:13"
    },
    "outcomingCall": {
        "totalTime": "00:02:50"
    }
}

3) 
  POST http://localhost:8080/createCdrReport
  example body:
{
  "number": "79284765839",
  "startDate": "2023-10-26T22:00:00",
  "endDate": "2023-10-28T06:00:00"
}
Создает csv файл в папке /reports с названием {number}_{UUID}.csv, содержащий cdr записи за указанный период 
OK: возвращает {number}_{UUID} (сsv создан)
NOT_FOUND: number не найден возвращает  Subscriber not found_{UUID}
INTERNAL_SERVER_ERROR: ловит ошибки Writer {e.message}_{UUID}
BAD_REQUEST:  неправильно переданы даты Invalid dates_{UUID}
  
  
  
