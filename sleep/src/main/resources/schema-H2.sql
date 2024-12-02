CREATE TABLE user_entity (
                      id          bigserial NOT NULL,
                      time_zone   character varying(50) DEFAULT 'UTC',
                      CONSTRAINT  pk_user PRIMARY KEY (id)
);

CREATE TABLE sleep (
                       id                       bigserial NOT NULL,
                       user_id                  bigserial NOT NULL,
                       sleep_from               time NOT NULL,
                       sleep_to                 time NOT NULL,
                       sleep_day                date NOT NULL,
                       mood                     character varying(4) NOT NULL,
                       duration_in_seconds      integer,

                       CONSTRAINT pk_sleep PRIMARY KEY (id),
                       CONSTRAINT uc_sleep_user_id_sleep_day UNIQUE (user_id, sleep_day),
                       CONSTRAINT fk_sleep_user_id FOREIGN KEY (user_id) REFERENCES user_entity
);
