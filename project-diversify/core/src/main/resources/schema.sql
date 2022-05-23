create type asset_type as enum ('SHARE', 'ETF', 'BOND', 'CURRENCY');

create table if not exists assets
(
    id     integer generated always as identity primary key,
    type   asset_type not null,
    figi   varchar    not null unique,
    isin   varchar    not null,
    ticker varchar,
    data   jsonb      not null
);

create table if not exists "user"
(
    id    integer generated always as identity primary key,
    email varchar not null unique,
    name  varchar not null
);

create type account_type as enum ('UNSPECIFIED','TINKOFF','TINKOFF_IIS','INVEST_BOX');

create table if not exists account
(
    id     integer generated always as identity PRIMARY KEY,
    tkc_id varchar      not null unique,
    type   account_type not null,
    name   varchar
);

create table if not exists portfolio
(
    id         uuid                 default gen_random_uuid() primary key,
    user_id    integer     not null references "user",
    account_id varchar     not null,
    loaded_at  timestamptz not null default now(),
    portfolio  jsonb       not null,
    positions  jsonb       not null
);

create table if not exists asset_custom_data
(
    id         integer generated always as identity primary key,
    user_id    integer     not null references "user",
    asset_id   integer     not null references asset,
    data       jsonb       not null,
    created_at timestamptz not null default now(),
    unique (user_id, asset_id)
);

