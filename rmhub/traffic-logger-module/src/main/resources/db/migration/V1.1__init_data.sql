--------------------------------------------------------------------------------------------------------------------------------------------
-- init traffic_alert_setting

INSERT INTO traffic_alert_setting
(id, color, created_date, description, last_modified_date, "level", max, min, status)
VALUES(1, '#A90B00', now(), 'Traffic Jam', now(), 1, 39, 0, true);

INSERT INTO traffic_alert_setting
(id, color, created_date, description, last_modified_date, "level", max, min, status)
VALUES(2, '#E77F20', now(), 'Slow Traffic', now(), 5, 69, 40, true);
