--------------------------------------------------------------------------------------------------------------------------------------------
-- re-create sequence

DROP SEQUENCE IF EXISTS hibernate_sequence;
CREATE SEQUENCE hibernate_sequence;

--------------------------------------------------------------------------------------------------------------------------------------------
-- drop tables in order

DROP TABLE IF EXISTS weather_measurement_detail; --fk on weather_measurement
DROP TABLE IF EXISTS weather_alert_setting;
DROP TABLE IF EXISTS weather_alert;
DROP TABLE IF EXISTS weather_measurement;

--------------------------------------------------------------------------------------------------------------------------------------------
-- weather_alert_setting

CREATE TABLE weather_alert_setting (
	id int8 NOT NULL,
	alert_code varchar(255) NULL,
	color varchar(255) NULL,
	"condition" int4 NULL,
	"content" varchar(255) NULL,
	deployment_id int4 NULL,
	"level" int4 NULL,
	status bool NULL,
	value int4 NULL,
	CONSTRAINT weather_alert_setting_pkey PRIMARY KEY (id),
	CONSTRAINT weather_alert_setting_value_check CHECK ((value <= 1000))
);

-- Permissions

ALTER TABLE weather_alert_setting OWNER TO rmhub;
GRANT ALL ON TABLE weather_alert_setting TO rmhub;

--------------------------------------------------------------------------------------------------------------------------------------------
-- weather_alert

CREATE TABLE weather_alert (
	id int8 NOT NULL,
	cause varchar(255) NULL,
	deployment_id int8 NULL,
	description varchar(255) NULL,
	external_id varchar(255) NOT NULL,
	handled_at timestamp NULL,
	handled_by int8 NULL,
	is_handled bool NULL,
	physical_device_id int8 NULL,
	"timestamp" timestamp NULL,
	weather_measurement_id int8 NULL,
	CONSTRAINT weather_alert_pkey PRIMARY KEY (id)
);

-- Permissions

ALTER TABLE weather_alert OWNER TO rmhub;
GRANT ALL ON TABLE weather_alert TO rmhub;

--------------------------------------------------------------------------------------------------------------------------------------------
-- weather_measurement

CREATE TABLE weather_measurement (
	id int8 NOT NULL,
	created_date timestamp NOT NULL,
	deployment_id int4 NULL,
	eqt_dt_mes varchar(255) NULL,
	eqt_mes_id varchar(255) NULL,
	eqt_mes_lg_id int4 NULL,
	eqt_mes_lg_type int4 NULL,
	eqt_mes_nb_val int4 NULL,
	eqt_mes_per int4 NULL,
	eqt_mes_type varchar(255) NULL,
	external_id varchar(255) NOT NULL,
	physical_device_id int8 NULL,
	CONSTRAINT weather_measurement_pkey PRIMARY KEY (id)
);

-- Permissions

ALTER TABLE weather_measurement OWNER TO rmhub;
GRANT ALL ON TABLE weather_measurement TO rmhub;

--------------------------------------------------------------------------------------------------------------------------------------------
-- weather_measurement_detail

CREATE TABLE weather_measurement_detail (
	id int8 NOT NULL,
	eqt_mes_klif varchar(255) NULL,
	eqt_mes_val varchar(255) NULL,
	"index" int4 NULL,
	ws_measurement_id int8 NOT NULL,
	CONSTRAINT weather_measurement_detail_pkey PRIMARY KEY (id),
	CONSTRAINT fk_weather_measurement FOREIGN KEY (ws_measurement_id) REFERENCES weather_measurement(id)
);

-- Permissions

ALTER TABLE weather_measurement_detail OWNER TO rmhub;
GRANT ALL ON TABLE weather_measurement_detail TO rmhub;
