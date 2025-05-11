--liquibase formatted sql
--changeset Maxim Dorofeev:1709304025339_create_job_table.sql

CREATE TABLE job
(
    id               UUID PRIMARY KEY                                                            NOT NULL,
    code             VARCHAR(128) UNIQUE                                                         NOT NULL,
    display_name     VARCHAR(128)                                                                NOT NULL,
    description      VARCHAR(512)                                                                NULL,
    enabled          BOOLEAN                                                                     NOT NULL,
    status           VARCHAR(32) CHECK ( status IN ('IDLE', 'IN_PROGRESS') )                     NOT NULL,
    type             VARCHAR(16) CHECK ( type IN ('SCHEDULER', 'LOADER', 'MIGRATION', 'OTHER') ) NOT NULL,
    create_date_time TIMESTAMPTZ                                                                 NOT NULL,
    update_date_time TIMESTAMPTZ                                                                 NOT NULL,
    version          BIGINT                                                                      NOT NULL DEFAULT 0
);

-- ROLLBACK DROP TABLE job;

COMMENT ON TABLE job IS 'Таблица с запускаемыми задачами';

COMMENT ON COLUMN job.id IS 'Идентификатор записи';
COMMENT ON COLUMN job.code IS 'Код задачи';
COMMENT ON COLUMN job.display_name IS 'Наименование задачи';
COMMENT ON COLUMN job.description IS 'Описание задачи';
COMMENT ON COLUMN job.enabled IS 'Включена ли задача (true\false)';
COMMENT ON COLUMN job.status IS 'Статус задачи работы задачи';
COMMENT ON COLUMN job.type IS 'Тип задачи';
COMMENT ON COLUMN job.create_date_time IS 'Дата и время создания записи';
COMMENT ON COLUMN job.update_date_time IS 'Дата и время последнего обновления записи';
COMMENT ON COLUMN job.version IS 'Номер версии объекта';