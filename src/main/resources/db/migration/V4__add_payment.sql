    alter table if exists tbl_booking
       add column payment_id bigint;
  
  alter table if exists tbl_booking
       add constraint FK_BOOKING_PAYMENT
       foreign key (payment_id)
       references tbl_payment;

CREATE TABLE IF NOT EXISTS tbl_payment (
        id bigserial not null,
        amount numeric(38,2) not null,
        created_at timestamp(6),
        currency varchar(255),
        payment_provider varchar(255) not null check (payment_provider in ('PAYSTACK','FLUTTERWAVE')),
        payment_status varchar(255) not null check (payment_status in ('SUCCESS','FAILURE','PENDING')),
        reference varchar(255) not null,
        updated_at timestamp(6),
        primary key (id)
    );


