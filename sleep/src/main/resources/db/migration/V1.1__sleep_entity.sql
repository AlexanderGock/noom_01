CREATE TABLE sleep (
    id          bigserial NOT NULL,
    user_id     bigserial NOT NULL,
    sleep_from  time NOT NULL,
    sleep_to    time NOT NULL,
    sleep_day   date NOT NULL,
    mood        character varying(4) NOT NULL,

    CONSTRAINT pk_sleep PRIMARY KEY (id),
    CONSTRAINT uc_sleep_user_id_sleep_day UNIQUE (user_id, sleep_day)
);
