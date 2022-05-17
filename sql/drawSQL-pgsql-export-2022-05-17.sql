CREATE TABLE "sales"(
    "id" UUID NOT NULL,
    "document_number" UUID NOT NULL,
    "serial" VARCHAR(255) NOT NULL,
    "number" VARCHAR(255) NOT NULL,
    "nit" VARCHAR(255) NOT NULL,
    "client_name" VARCHAR(255) NOT NULL,
    "amount" DECIMAL(8, 2) NOT NULL,
    "iva_amount" DECIMAL(8, 2) NOT NULL,
    "isr_amount" DECIMAL(8, 2) NOT NULL,
    "register_type" VARCHAR(255) NOT NULL,
    "sat_file_id" UUID NULL,
    "created_at" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    "created_by" VARCHAR(255) NOT NULL,
    "updated_at" TIMESTAMP(0) WITHOUT TIME ZONE NULL,
    "updated_by" VARCHAR(255) NULL
);
ALTER TABLE
    "sales" ADD PRIMARY KEY("id");
CREATE TABLE "taxes_configuration"(
    "id" UUID NOT NULL,
    "slug" VARCHAR(255) NOT NULL,
    "value" VARCHAR(255) NOT NULL
);
ALTER TABLE
    "taxes_configuration" ADD PRIMARY KEY("id");
ALTER TABLE
    "taxes_configuration" ADD CONSTRAINT "taxes_configuration_slug_unique" UNIQUE("slug");
CREATE TABLE "sat_files"(
    "id" UUID NOT NULL,
    "name" VARCHAR(255) NOT NULL,
    "type" VARCHAR(255) NOT NULL,
    "file" bytea NOT NULL,
    "uploaded_at" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    "uploaded_by" VARCHAR(255) NOT NULL,
    "updated_at" TIMESTAMP(0) WITHOUT TIME ZONE NULL,
    "updated_by" VARCHAR(255) NULL
);
ALTER TABLE
    "sat_files" ADD PRIMARY KEY("id");
CREATE TABLE "purchases"(
    "id" UUID NOT NULL,
    "document_type" VARCHAR(255) NOT NULL,
    "serial" VARCHAR(255) NOT NULL,
    "invoice_number" VARCHAR(255) NOT NULL,
    "nit" VARCHAR(255) NOT NULL,
    "client_name" VARCHAR(255) NOT NULL,
    "amount" DECIMAL(8, 2) NOT NULL,
    "iva_amount" DECIMAL(8, 2) NOT NULL,
    "register_type" VARCHAR(255) NOT NULL,
    "sat_file_id" UUID NULL,
    "created_at" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    "created_by" VARCHAR(255) NOT NULL,
    "updated_at" TIMESTAMP(0) WITHOUT TIME ZONE NULL,
    "updated_by" VARCHAR(255) NULL
);
ALTER TABLE
    "purchases" ADD PRIMARY KEY("id");
CREATE TABLE "tax_forms"(
    "id" UUID NOT NULL,
    "number" BIGINT NOT NULL,
    "access_number" BIGINT NOT NULL,
    "type" VARCHAR(255) NOT NULL,
    "amount" DECIMAL(8, 2) NULL,
    "created_at" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    "created_by" VARCHAR(255) NOT NULL,
    "updated_at" TIMESTAMP(0) WITHOUT TIME ZONE NULL,
    "updated_by" VARCHAR(255) NULL
);
ALTER TABLE
    "tax_forms" ADD PRIMARY KEY("id");
CREATE TABLE "isr_annual_integration"(
    "id" UUID NOT NULL,
    "isr_calculated" DECIMAL(8, 2) NOT NULL,
    "retention_amount" DECIMAL(8, 2) NOT NULL,
    "period_from" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    "base_amount" DECIMAL(8, 2) NOT NULL,
    "period_to" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL
);
ALTER TABLE
    "isr_annual_integration" ADD PRIMARY KEY("id");
ALTER TABLE
    "sales" ADD CONSTRAINT "sales_sat_file_id_foreign" FOREIGN KEY("sat_file_id") REFERENCES "sat_files"("id");
ALTER TABLE
    "purchases" ADD CONSTRAINT "purchases_sat_file_id_foreign" FOREIGN KEY("sat_file_id") REFERENCES "sat_files"("id");