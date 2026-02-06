--liquibase formatted sql
--changeset Maxim Dorofeev:1765975903902_create_user_info_table.sql
--comment AS-3

CREATE TABLE user_info
(
    id                          UUID PRIMARY KEY                                                                    NOT NULL,
    email                       VARCHAR(256)                                                                        NOT NULL UNIQUE,
    type                        VARCHAR(16)  CHECK ( type IN ('USER',  'ORGANIZATION') )                            NOT NULL,
    email_status                VARCHAR(16)  CHECK ( email_status IN ('VERIFIED',  'NOT_VERIFIED') )                NOT NULL DEFAULT 'NOT_VERIFIED',
    password_hash               VARCHAR(256)                                                                        NOT NULL,
    password_expiration_date    DATE                                                                                NOT NULL,
    first_name                  VARCHAR(128)                                                                        NOT NULL,
    last_name                   VARCHAR(128)                                                                        NULL,
    middle_name                 VARCHAR(128)                                                                        NULL,
    user_status                 VARCHAR(32)  CHECK ( user_status IN ('ACTIVE', 'BLOCKED', 'DELETED', 'EXPIRED') )   NOT NULL DEFAULT 'ACTIVE',
    phone_number                VARCHAR(16)                                                                         NULL UNIQUE,
    phone_status                VARCHAR(16)  CHECK ( phone_status IN ('VERIFIED',  'NOT_VERIFIED') )                NOT NULL DEFAULT 'NOT_VERIFIED',
    last_login_date_time        TIMESTAMPTZ                                                                         NOT NULL,
    create_date_time            TIMESTAMPTZ                                                                         NOT NULL,
    update_date_time            TIMESTAMPTZ                                                                         NOT NULL,
    version                     INTEGER                                                                             NOT NULL DEFAULT 0
);

-- ROLLBACK DROP TABLE user_info;

COMMENT ON TABLE user_info IS 'Пользовательская информация';

COMMENT ON COLUMN user_info.id IS 'Идентификатор записи';

COMMENT ON COLUMN user_info.email IS 'E-mail пользователя';
COMMENT ON COLUMN user_info.type IS 'Тип аккаунта (Пользователь/Организация)';
COMMENT ON COLUMN user_info.email_status IS 'Верификация E-mail';
COMMENT ON COLUMN user_info.password_hash IS 'Хеш пароля';
COMMENT ON COLUMN user_info.password_expiration_date IS 'Дата истечения пароля';
COMMENT ON COLUMN user_info.first_name IS 'Имя пользователя/Наименование организации';
COMMENT ON COLUMN user_info.last_name IS 'Фамилия пользователя';
COMMENT ON COLUMN user_info.middle_name IS 'Отчество пользователя';
COMMENT ON COLUMN user_info.user_status IS 'Статус аккаунта';
COMMENT ON COLUMN user_info.phone_number IS 'Номер телефона';
COMMENT ON COLUMN user_info.phone_status IS 'Верификация телефона';
COMMENT ON COLUMN user_info.last_login_date_time IS 'Дата и время последнего логина пользователя';

COMMENT ON COLUMN user_info.create_date_time IS 'Дата и время создания записи';
COMMENT ON COLUMN user_info.update_date_time IS 'Дата и время последнего обновления записи';
COMMENT ON COLUMN user_info.version IS 'Номер версии объекта';