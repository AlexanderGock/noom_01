-- user 101 has 5 records within 30-days interval and 1 record outside 30-days interval
INSERT INTO sleep (user_id, sleep_from, sleep_to, sleep_day, mood) VALUES (101, '23:30:00', '07:00:00', current_date, 'OK');
INSERT INTO sleep (user_id, sleep_from, sleep_to, sleep_day, mood) VALUES (101, '23:45:00', '07:30:00', current_date - 1, 'GOOD');
INSERT INTO sleep (user_id, sleep_from, sleep_to, sleep_day, mood) VALUES (101, '00:00:00', '07:20:00', current_date - 2, 'BAD');
INSERT INTO sleep (user_id, sleep_from, sleep_to, sleep_day, mood) VALUES (101, '22:40:00', '06:00:00', current_date - 4, 'GOOD');
INSERT INTO sleep (user_id, sleep_from, sleep_to, sleep_day, mood) VALUES (101, '23:20:00', '07:00:00', current_date - 5, 'GOOD');
INSERT INTO sleep (user_id, sleep_from, sleep_to, sleep_day, mood) VALUES (101, '01:00:00', '08:12:00', current_date - 30, 'OK');

-- user 102 has no record for the last night
INSERT INTO sleep (user_id, sleep_from, sleep_to, sleep_day, mood) VALUES (102, '23:45:00', '07:30:00', current_date - 1, 'GOOD');
INSERT INTO sleep (user_id, sleep_from, sleep_to, sleep_day, mood) VALUES (102, '00:00:00', '07:20:00', current_date - 2, 'BAD');

-- user 103 may fall asleep before and after midnight
INSERT INTO sleep (user_id, sleep_from, sleep_to, sleep_day, mood) VALUES (103, '23:20:00', '07:00:00', current_date, 'GOOD');
INSERT INTO sleep (user_id, sleep_from, sleep_to, sleep_day, mood) VALUES (103, '01:00:00', '08:12:00', current_date - 1, 'OK');