--------------------------------------------------------------------------------------------------------------------------------------------
-- re-create sequence

DROP SEQUENCE IF EXISTS hibernate_sequence;
CREATE SEQUENCE hibernate_sequence;

--------------------------------------------------------------------------------------------------------------------------------------------
-- drop tables in order

DROP TABLE IF EXISTS traffic_measurement_detail; -- fk constraint
DROP TABLE IF EXISTS traffic_alert;
DROP TABLE IF EXISTS traffic_alert_setting;
DROP TABLE IF EXISTS traffic_measurement;
DROP TABLE IF EXISTS traffic_technical_data;

--------------------------------------------------------------------------------------------------------------------------------------------
-- traffic_alert_setting

CREATE TABLE traffic_alert_setting (
	id int8 NOT NULL,
	color varchar(255) NOT NULL,
	created_date timestamp NOT NULL,
	description varchar(255) NOT NULL,
	last_modified_date timestamp NULL,
	"level" int4 NOT NULL,
	max int4 NOT NULL,
	min int4 NOT NULL,
	status bool NOT NULL,
	CONSTRAINT traffic_alert_setting_pkey PRIMARY KEY (id)
);

-- Permissions

ALTER TABLE traffic_alert_setting OWNER TO rmhub;
GRANT ALL ON TABLE traffic_alert_setting TO rmhub;

--------------------------------------------------------------------------------------------------------------------------------------------
-- traffic_alert

CREATE TABLE traffic_alert (
	id int8 NOT NULL,
	cause varchar(255) NULL,
	created_date timestamp NOT NULL,
	deployment_id int4 NULL,
	description varchar(255) NULL,
	external_id varchar(255) NOT NULL,
	handled_at timestamp NULL,
	handled_by varchar(255) NULL,
	is_handled bool NULL,
	traffic_measurement_id int8 NULL,
	CONSTRAINT traffic_alert_pkey PRIMARY KEY (id)
);

-- Permissions

ALTER TABLE traffic_alert OWNER TO rmhub;
GRANT ALL ON TABLE traffic_alert TO rmhub;

--------------------------------------------------------------------------------------------------------------------------------------------
-- traffic_measurement

CREATE TABLE traffic_measurement (
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
	CONSTRAINT traffic_measurement_pkey PRIMARY KEY (id)
);

-- Permissions

ALTER TABLE traffic_measurement OWNER TO rmhub;
GRANT ALL ON TABLE traffic_measurement TO rmhub;

--------------------------------------------------------------------------------------------------------------------------------------------
-- traffic_technical_data

CREATE TABLE traffic_technical_data (
	id int8 NOT NULL,
	eqt_conf_version int4 NULL,
	etat_alim int4 NULL,
	etat_com int4 NULL,
	etat_date varchar(255) NULL,
	etat_sys int4 NULL,
	external_id varchar(255) NOT NULL,
	physical_device_id int8 NULL,
	CONSTRAINT traffic_technical_data_pkey PRIMARY KEY (id)
);

-- Permissions

ALTER TABLE traffic_technical_data OWNER TO rmhub;
GRANT ALL ON TABLE traffic_technical_data TO rmhub;

--------------------------------------------------------------------------------------------------------------------------------------------
-- traffic_measurement_detail

CREATE TABLE traffic_measurement_detail (
	id int8 NOT NULL,
	eqt_mes_klif varchar(255) NULL,
	eqt_mes_val varchar(255) NULL,
	"index" int4 NULL,
	tl_measurement_id int8 NOT NULL,
	CONSTRAINT traffic_measurement_detail_pkey PRIMARY KEY (id),
	CONSTRAINT fk_traffic_measurement FOREIGN KEY (tl_measurement_id) REFERENCES traffic_measurement(id)
);

-- Permissions

ALTER TABLE traffic_measurement_detail OWNER TO rmhub;
GRANT ALL ON TABLE traffic_measurement_detail TO rmhub;
