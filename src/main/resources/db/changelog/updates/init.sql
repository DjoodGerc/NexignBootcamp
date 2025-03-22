Create table if not exists subscriber(
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    number varchar(11) UNIQUE
);
Create table if not exists cdr(
   id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   initiating_id integer,
   receiving_id integer,
   start_call TIMESTAMP,
   end_call TIMESTAMP,
   FOREIGN KEY (initiating_id) REFERENCES subscriber(id),
   FOREIGN KEY (receiving_id) REFERENCES subscriber(id)
);