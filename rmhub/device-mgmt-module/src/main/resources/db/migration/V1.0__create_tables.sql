--------------------------------------------------------------------------------------------------------------------------------------------
-- re-create sequence

DROP SEQUENCE IF EXISTS hibernate_sequence;
CREATE SEQUENCE hibernate_sequence;

--------------------------------------------------------------------------------------------------------------------------------------------
-- drop tables in order

DROP TABLE IF EXISTS channel_physical_device_mapping; -- fk on physical_device and channel
DROP TABLE IF EXISTS channel_mesure_mapping; -- fk on mesure_config and channel
DROP TABLE IF EXISTS channel; -- fk on pool
DROP TABLE IF EXISTS physical_device; -- fk on device_type
DROP TABLE IF EXISTS device_type;
DROP TABLE IF EXISTS nature;
DROP TABLE IF EXISTS pool;
DROP TABLE IF EXISTS mesure_config;
DROP TABLE IF EXISTS "period";

--------------------------------------------------------------------------------------------------------------------------------------------
-- device_type

CREATE TABLE device_type (
	id int8 NOT NULL,
	"name" varchar(255) NULL,
	CONSTRAINT device_type_pkey PRIMARY KEY (id)
);

-- Permissions

ALTER TABLE device_type OWNER TO rmhub;
GRANT ALL ON TABLE device_type TO rmhub;

--------------------------------------------------------------------------------------------------------------------------------------------
-- nature

CREATE TABLE nature (
	id int8 NOT NULL,
	"name" varchar(255) NULL,
	status int4 NULL,
	value varchar(255) NULL,
	CONSTRAINT nature_pkey PRIMARY KEY (id)
);

-- Permissions

ALTER TABLE nature OWNER TO rmhub;
GRANT ALL ON TABLE nature TO rmhub;

--------------------------------------------------------------------------------------------------------------------------------------------
-- pool

CREATE TABLE pool (
	id int8 NOT NULL,
	"name" varchar(255) NULL,
	status int4 NULL,
	value varchar(255) NULL,
	CONSTRAINT pool_pkey PRIMARY KEY (id)
);

-- Permissions

ALTER TABLE pool OWNER TO rmhub;
GRANT ALL ON TABLE pool TO rmhub;

--------------------------------------------------------------------------------------------------------------------------------------------
-- mesure_config

CREATE TABLE mesure_config (
	id int8 NOT NULL,
	is_enable bool NULL,
	mesure_id varchar(255) NULL,
	mesure_libelle varchar(255) NULL,
	mesure_type varchar(255) NULL,
	nature_id int8 NULL,
	period_id int8 NULL,
	status int4 NULL,
	value varchar(255) NULL,
	CONSTRAINT mesure_config_pkey PRIMARY KEY (id)
);

-- Permissions

ALTER TABLE mesure_config OWNER TO rmhub;
GRANT ALL ON TABLE mesure_config TO rmhub;

--------------------------------------------------------------------------------------------------------------------------------------------
-- period

CREATE TABLE "period" (
	id int8 NOT NULL,
	"name" varchar(255) NULL,
	status int4 NULL,
	value varchar(255) NULL,
	CONSTRAINT period_pkey PRIMARY KEY (id)
);

-- Permissions

ALTER TABLE "period" OWNER TO rmhub;
GRANT ALL ON TABLE "period" TO rmhub;

--------------------------------------------------------------------------------------------------------------------------------------------
-- physical_device

CREATE TABLE physical_device (
	id int8 NOT NULL,
	at_kilometers int4 NULL,
	at_meters int4 NULL,
	cmc_device_id int8 NULL,
	connection_info_id int8 NULL,
	context varchar(255) NULL,
	deployment_id int8 NULL,
	description varchar(255) NULL,
	external_id varchar(255) NOT NULL,
	installed_date timestamp NULL,
	is_registered bool NULL,
	last_modified_by int8 NULL,
	last_modified_date timestamp NULL,
	latitude float8 NULL,
	longitude float8 NULL,
	mapping_info_id int8 NULL,
	"name" varchar(255) NULL,
	status int4 NULL,
	device_type_id int8 NULL,
	CONSTRAINT physical_device_pkey PRIMARY KEY (id),
	CONSTRAINT uk_external_id UNIQUE (external_id),
	CONSTRAINT fk_device_type FOREIGN KEY (device_type_id) REFERENCES device_type(id)
);

-- Permissions

ALTER TABLE physical_device OWNER TO rmhub;
GRANT ALL ON TABLE physical_device TO rmhub;

--------------------------------------------------------------------------------------------------------------------------------------------
-- channel

CREATE TABLE channel (
	id int8 NOT NULL,
	device_type_id int8 NULL,
	name varchar(255) NOT NULL,
	status int4 NULL,
	value varchar(255) NULL,
	pool_id int8 NOT NULL,
	CONSTRAINT channel_pkey PRIMARY KEY (id),
	CONSTRAINT fk_pool FOREIGN KEY (pool_id) REFERENCES pool(id)
);

-- Permissions

ALTER TABLE channel OWNER TO rmhub;
GRANT ALL ON TABLE channel TO rmhub;

--------------------------------------------------------------------------------------------------------------------------------------------
-- channel_mesure_mapping

CREATE TABLE channel_mesure_mapping (
	id int8 NOT NULL,
	status int4 NULL,
	channel_id int8 NULL,
	mesure_config_id int8 NULL,
	CONSTRAINT channel_mesure_mapping_pkey PRIMARY KEY (id),
	CONSTRAINT fk_mesure_config FOREIGN KEY (mesure_config_id) REFERENCES mesure_config(id),
	CONSTRAINT fk_channel FOREIGN KEY (channel_id) REFERENCES channel(id)
);

-- Permissions

ALTER TABLE channel_mesure_mapping OWNER TO rmhub;
GRANT ALL ON TABLE channel_mesure_mapping TO rmhub;

--------------------------------------------------------------------------------------------------------------------------------------------
-- channel_physical_device_mapping

CREATE TABLE channel_physical_device_mapping (
	id int8 NOT NULL,
	status int4 NULL,
	channel_id int8 NOT NULL,
	physical_device_id int8 NOT NULL,
	CONSTRAINT channel_physical_device_mapping_pkey PRIMARY KEY (id),
	CONSTRAINT fk_physical_device FOREIGN KEY (physical_device_id) REFERENCES physical_device(id),
	CONSTRAINT fk_channel FOREIGN KEY (channel_id) REFERENCES channel(id)
);

-- Permissions

ALTER TABLE channel_physical_device_mapping OWNER TO rmhub;
GRANT ALL ON TABLE channel_physical_device_mapping TO rmhub;
