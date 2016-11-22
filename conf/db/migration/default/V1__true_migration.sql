CREATE DOMAIN RATING_TYPE INTEGER CHECK (VALUE >= 0 AND VALUE <= 10);

CREATE DOMAIN REQUEST_STATUS_TYPE INTEGER CHECK (VALUE >= 0 AND VALUE <= 4);

CREATE TABLE Service_Request (
  id SERIAL,
  rating RATING_TYPE,
  description TEXT,
  creation_date DATE,
  status REQUEST_STATUS_TYPE,
  PRIMARY KEY (id)
);

CREATE TABLE Building (
  cladr_id INT,
  building_id INT,
  PRIMARY KEY (cladr_id, building_id)
);

CREATE TABLE Person (
  person_id SERIAL,
  name VARCHAR,
  surname VARCHAR,
  paternal_name VARCHAR,
  registration_date DATE,
  email VARCHAR NOT NULL DEFAULT '' UNIQUE,
  password_hash VARCHAR NOT NULL DEFAULT '',
  is_admin BOOLEAN NOT NULL DEFAULT false,
  PRIMARY KEY (person_id)
);

CREATE TABLE Meter_Unit (
  meter_unit_id SERIAL,
  description VARCHAR,
  PRIMARY KEY (meter_unit_id)
);

CREATE TABLE Rate (
  rate_id SERIAL,
  value DECIMAL,
  date_from TIMESTAMP,
  date_to TIMESTAMP,
  meter_unit_id INTEGER  NOT NULL,
  PRIMARY KEY (rate_id),
  FOREIGN KEY (meter_unit_id) REFERENCES Meter_Unit (meter_unit_id)
);

CREATE TABLE Flat (
  flat_id SERIAL,
  area REAL,
  flat_number INT,
  balance REAL,
  cladr_id INT NOT NULL,
  building_id INT NOT NULL,
  PRIMARY KEY (flat_id),
  FOREIGN KEY (cladr_id, building_id) REFERENCES Building(cladr_id, building_id)
);

CREATE TABLE Request_To_Flat (
  request_id INTEGER,
  flat_id INTEGER,
  PRIMARY KEY (request_id, flat_id),
  FOREIGN KEY (request_id) REFERENCES Service_Request,
  FOREIGN KEY (flat_id) REFERENCES Flat
);

CREATE TABLE Commented (
  id SERIAL,
  request_id INTEGER,
  person_id INTEGER,
  text VARCHAR,
  date DATE,
  PRIMARY KEY (id, request_id, person_id),
  FOREIGN KEY (request_id) REFERENCES Service_Request,
  FOREIGN KEY (person_id) REFERENCES Person
);

CREATE TABLE Visited (
  visit_id SERIAL,
  service_requset_id INTEGER,
  schedule_time DATE,
  out_time DATE,
  start_time DATE,
  end_time DATE,
  costs REAL,
  PRIMARY KEY (visit_id),
  FOREIGN KEY (service_requset_id) REFERENCES Service_Request
);

CREATE TABLE Dweller (
  person_id SERIAL,
  PRIMARY KEY (person_id),
  FOREIGN KEY (person_id) REFERENCES Person(person_id)
);

CREATE TABLE Technician (
  person_id INTEGER,
  rating RATING_TYPE,
  salary REAL,
  PRIMARY KEY (person_id),
  FOREIGN KEY (person_id) REFERENCES Person(person_id)
);

CREATE TABLE Assigned (
  visit_id INTEGER,
  technician_id INTEGER,
  PRIMARY KEY (visit_id, technician_id),
  FOREIGN KEY (visit_id) REFERENCES Visited (visit_id),
  FOREIGN KEY (technician_id) REFERENCES Technician (person_id)
);

CREATE TABLE Dweller_Lives_In_Flat (
  person_id INTEGER,
  flat_id INTEGER,
  PRIMARY KEY (person_id, flat_id),
  FOREIGN KEY (person_id) REFERENCES Person(person_id),
  FOREIGN KEY (flat_id) REFERENCES Flat(flat_id)
);

CREATE TABLE Meter(
  meter_id SERIAL,
  installation_date TIMESTAMP,
  type VARCHAR,
  title VARCHAR,
  meter_unit_id INTEGER NOT NULL,
  active BOOLEAN,
  flat_id INTEGER NOT NULL,
  PRIMARY KEY (meter_id),
  FOREIGN KEY (meter_unit_id) REFERENCES Meter_Unit (meter_unit_id),
  FOREIGN KEY (flat_id) REFERENCES Flat (flat_id)
);

CREATE TABLE Meter_Reading(
  meter_reading_id SERIAL,
  value DECIMAL,
  date TIMESTAMP,
  paid BOOLEAN,
  meter_id INTEGER NOT NULL,
  PRIMARY KEY (meter_reading_id),
  FOREIGN KEY (meter_id) REFERENCES Meter (meter_id)
);

drop table if exists company;
create sequence company_id_seq start with 1;
create table company (
  id bigint not null default nextval('company_id_seq') primary key,
  name varchar(255) not null,
  url varchar(255),
  created_at timestamp not null,
  deleted_at timestamp
);

insert into company (name, url, created_at) values ('Typesafe', 'http://typesafe.com/', current_timestamp);
insert into company (name, url, created_at) values ('Oracle', 'http://www.oracle.com/', current_timestamp);
insert into company (name, url, created_at) values ('Google', 'http://www.google.com/', current_timestamp);
insert into company (name, url, created_at) values ('Microsoft', 'http://www.microsoft.com/', current_timestamp);