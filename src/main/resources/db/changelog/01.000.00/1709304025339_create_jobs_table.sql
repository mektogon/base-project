--liquibase formatted sql
--changeset Maxim Dorofeev:1709304025339_create_jobs_table.sql

CREATE TABLE job
(
    name        VARCHAR(100) PRIMARY KEY NOT NULL,
    description VARCHAR(500),
    enabled     BOOLEAN                  NOT NULL DEFAULT TRUE,
    create_date TIMESTAMP                NOT NULL DEFAULT NOW(),
    update_date TIMESTAMP                NOT NULL DEFAULT NOW(),
    version     BIGINT                   NOT NULL DEFAULT 0
);

-- ROLLBACK DROP TABLE job;

COMMENT ON TABLE job IS 'Таблица с запускаемыми задачами';

COMMENT ON COLUMN job.name IS 'Уникальное наименование задачи';
COMMENT ON COLUMN job.description IS 'Описание задачи';
COMMENT ON COLUMN job.enabled IS 'Включена ли задача (true\false)';
COMMENT ON COLUMN job.create_date IS 'Дата создания записи';
COMMENT ON COLUMN job.update_date IS 'Дата последнего обновления записи';
COMMENT ON COLUMN job.version IS 'Номер версии объекта';