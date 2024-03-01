--liquibase formatted sql
--changeset Maxim Dorofeev:1709304025339_create_task_table.sql

CREATE TABLE task
(
    name        VARCHAR(100) PRIMARY KEY NOT NULL,
    description VARCHAR(500),
    enabled     BOOLEAN                  NOT NULL DEFAULT TRUE,
    create_date TIMESTAMP                NOT NULL DEFAULT NOW(),
    update_date TIMESTAMP                NOT NULL DEFAULT NOW(),
    version     BIGINT                   NOT NULL DEFAULT 0
);

-- ROLLBACK DROP TABLE task;

COMMENT ON TABLE task IS 'Таблица с запускаемыми задачами';

COMMENT ON COLUMN task.name IS 'Уникальное наименование задачи';
COMMENT ON COLUMN task.description IS 'Описание задачи';
COMMENT ON COLUMN task.enabled IS 'Включена ли задача (true\false)';
COMMENT ON COLUMN task.create_date IS 'Дата создания записи';
COMMENT ON COLUMN task.update_date IS 'Дата последнего обновления записи';
COMMENT ON COLUMN task.version IS 'Номер версии объекта';