CREATE TABLE user_entity (
    id          bigserial NOT NULL,
    time_zone   character varying(50) DEFAULT 'UTC',
    CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE sleep ADD duration_in_seconds integer;

INSERT INTO user_entity (id) SELECT DISTINCT user_id FROM sleep;

ALTER TABLE sleep ADD CONSTRAINT fk_sleep_user_id FOREIGN KEY (user_id) REFERENCES user_entity;
