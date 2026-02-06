--liquibase formatted sql
--changeset Maxim Dorofeev:1766117323828_create_user_password_history_table.sql
--comment AS-3

CREATE TABLE user_password_history
(
    id                          UUID PRIMARY KEY    NOT NULL,
    user_info_id                UUID                NOT NULL REFERENCES user_info (id),
    password_hash               VARCHAR(256)        NOT NULL,
    password_hash_algorithm     VARCHAR(8)          NOT NULL,
    create_date_time            TIMESTAMPTZ         NOT NULL,
    update_date_time            TIMESTAMPTZ         NOT NULL,
    version                     INTEGER             NOT NULL DEFAULT 0
);

-- ROLLBACK DROP TABLE user_password_history;

COMMENT ON TABLE user_password_history IS 'История паролей пользователя';

COMMENT ON COLUMN user_password_history.id IS 'Идентификатор записи';

COMMENT ON COLUMN user_password_history.user_info_id IS 'Идентификатор пользователя';
COMMENT ON COLUMN user_password_history.password_hash IS 'Хеш пароля';
COMMENT ON COLUMN user_password_history.password_hash_algorithm IS 'Алгоритм хеширования пароля';

COMMENT ON COLUMN user_password_history.create_date_time IS 'Дата и время создания записи';
COMMENT ON COLUMN user_password_history.update_date_time IS 'Дата и время последнего обновления записи';
COMMENT ON COLUMN user_password_history.version IS 'Номер версии объекта';

CREATE INDEX IF NOT EXISTS idx_user_password_history_user_info_id ON user_password_history (user_info_id);