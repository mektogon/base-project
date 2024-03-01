--liquibase formatted sql
--changeset Maxim Dorofeev:1709303888989_create_common_settings_table.sql

CREATE TABLE common_settings
(
    id          UUID PRIMARY KEY    NOT NULL,
    name        VARCHAR(100) UNIQUE NOT NULL,
    value       VARCHAR(250)        NOT NULL,
    description VARCHAR(500),
    create_date TIMESTAMP           NOT NULL DEFAULT NOW(),
    update_date TIMESTAMP           NOT NULL DEFAULT NOW(),
    version     BIGINT              NOT NULL DEFAULT 0
);

-- ROLLBACK DROP TABLE common_settings;

COMMENT ON TABLE common_settings IS 'Таблица с универсальными рубильниками';

COMMENT ON COLUMN common_settings.id IS 'Идентификатор записи';
COMMENT ON COLUMN common_settings.name IS 'Уникальное наименование рубильника';
COMMENT ON COLUMN common_settings.value IS 'Значение рубильника';
COMMENT ON COLUMN common_settings.description IS 'Описание рубильника';
COMMENT ON COLUMN common_settings.create_date IS 'Дата создания записи';
COMMENT ON COLUMN common_settings.update_date IS 'Дата последнего обновления записи';
COMMENT ON COLUMN common_settings.version IS 'Номер версии объекта';