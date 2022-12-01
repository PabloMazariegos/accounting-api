alter table purchases
    add column amount_without_iva DECIMAL(8, 2) NOT NULL DEFAULT 0;

alter table sales
    add column amount_without_iva DECIMAL(8, 2) NOT NULL DEFAULT 0;