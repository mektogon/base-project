--liquibase formatted sql
--changeset Maxim Dorofeev:1709303888989_create_common_setting_table.sql

CREATE TABLE common_setting
(
    id               UUID PRIMARY KEY    NOT NULL,
    code             VARCHAR(128) UNIQUE NOT NULL,
    value            VARCHAR(256)        NOT NULL,
    description      VARCHAR(512)        NULL,
    create_date_time TIMESTAMPTZ         NOT NULL,
    update_date_time TIMESTAMPTZ         NOT NULL,
    version          BIGINT              NOT NULL DEFAULT 0
);

-- ROLLBACK DROP TABLE common_setting;

COMMENT ON TABLE common_setting IS 'Таблица с универсальными рубильниками';

COMMENT ON COLUMN common_setting.id IS 'Идентификатор записи';
COMMENT ON COLUMN common_setting.code IS 'Код рубильника';
COMMENT ON COLUMN common_setting.value IS 'Значение рубильника';
COMMENT ON COLUMN common_setting.description IS 'Описание рубильника';
COMMENT ON COLUMN common_setting.create_date_time IS 'Дата и время создания записи';
COMMENT ON COLUMN common_setting.update_date_time IS 'Дата и время последнего обновления записи';
COMMENT ON COLUMN common_setting.version IS 'Номер версии объекта';