create table currency_data
(
    currency_code    varchar(3) unique,
    numeric_code     integer unique,
    rate_to_usd      numeric(16, 8),
    id               bigserial not null,
    last_modified_at TIMESTAMP,
    display_name     varchar(60),
    primary key (id)
)
