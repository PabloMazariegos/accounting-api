alter table tax_forms
    drop column amount;

alter table tax_forms
alter column "number" type varchar(255);

alter table tax_forms
alter column "access_number" type varchar(255);