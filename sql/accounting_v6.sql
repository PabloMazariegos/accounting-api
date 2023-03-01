alter table sales
    add column status varchar(128);

alter table sales
    add column voided_at TIMESTAMP WITHOUT TIME ZONE NULL;

alter table purchases
    add column status varchar(128);

alter table purchases
    add column voided_at TIMESTAMP WITHOUT TIME ZONE NULL;