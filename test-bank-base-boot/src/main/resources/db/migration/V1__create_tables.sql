create table TEST."ACCOUNT" (iban varchar(255) not null primary key, amount decimal(10,2) not null, created_at timestamp, created_by varchar(255), updated_at timestamp, updated_by varchar(255), deleted boolean);
create table TEST."TRANSACTION" (id varchar(255) not null primary key, account_iban varchar(255) not null references account(iban), date timestamp, amount decimal(10,2) not null, fee decimal(10,2) not null, reference varchar(255), description varchar(255), created_at timestamp, created_by varchar(255), updated_at timestamp, updated_by varchar(255), deleted boolean);


