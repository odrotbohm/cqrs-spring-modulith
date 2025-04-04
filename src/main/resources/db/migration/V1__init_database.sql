create table event_publication
(
    id               uuid not null
        constraint pk_event_publication primary key,
    completion_date  timestamp,
    event_type       varchar(255),
    listener_id      varchar(255),
    publication_date timestamp(6) with time zone,
    serialized_event varchar(255)
);


create table event_publication_archive
(
    id               uuid not null
        constraint pk_event_publication_archive primary key,
    completion_date  timestamp,
    event_type       varchar(255),
    listener_id      varchar(255),
    publication_date timestamp(6) with time zone,
    serialized_event varchar(255)
);


create table product
(
    id          uuid
        constraint pk_product primary key,
    name        varchar(255),
    description varchar(255),
    category    varchar(255),
    price       numeric(38, 2)
        constraint chk_product_price check (price >= (0):: numeric),
    stock       integer
);

create index idx_product_category
    on product (category);

create table product_view
(
    id             uuid
        constraint pk_product_view primary key,
    average_rating double precision,
    category       varchar(255),
    description    varchar(255),
    name           varchar(255),
    price          numeric(38, 2),
    review_count   integer,
    stock          integer
);

create table review
(
    id         uuid
        constraint pk_review primary key,
    author     varchar(255),
    product_id uuid not null
        constraint fk_review_product references product,
    vote       integer
        constraint chk_vote check (vote >= (0):: numeric and vote <= (5):: numeric),
    comment    varchar(255)
);

create index idx_review_product_id
    on review (product_id);

