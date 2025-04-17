create table  operator(
    id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar
);

Create table  subscriber(
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    msisdn varchar(11) UNIQUE,
    operator_id integer,
    FOREIGN KEY (operator_id) REFERENCES operator(id)
);
Create table  call_data(
   id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   initiating_id integer,
   receiving_id integer,
   start_call TIMESTAMP,
   end_call TIMESTAMP,
   FOREIGN KEY (initiating_id) REFERENCES subscriber(id),
   FOREIGN KEY (receiving_id) REFERENCES subscriber(id)
);