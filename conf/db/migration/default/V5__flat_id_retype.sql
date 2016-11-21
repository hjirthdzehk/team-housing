DROP TABLE flat CASCADE;

CREATE TABLE IF NOT EXISTS ServiceRequest (
  id SERIAL,
  rating RATING_TYPE,
  description TEXT,
  creation_date DATE,
  status REQUEST_STATUS_TYPE,
  PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS Building (
  cladr_id INT,
  building_id INT,
  PRIMARY KEY (cladr_id, building_id)
);

CREATE TABLE IF NOT EXISTS Person (
  person_id SERIAL,
  name VARCHAR,
  surname VARCHAR,
  paternal_name VARCHAR,
  registration_date DATE,
  PRIMARY KEY (person_id)
);

CREATE TABLE IF NOT EXISTS MeterUnit (
  meter_unit_id SERIAL,
  description VARCHAR,
  PRIMARY KEY (meter_unit_id)
);

CREATE TABLE IF NOT EXISTS Rate (
  rate_id SERIAL,
  value DECIMAL,
  date_from TIMESTAMP,
  date_to TIMESTAMP,
  meter_unit_id INTEGER  NOT NULL,
  PRIMARY KEY (rate_id),
  FOREIGN KEY (meter_unit_id) REFERENCES MeterUnit (meter_unit_id)
);

CREATE TABLE IF NOT EXISTS Flat (
  flat_id SERIAL,
  area REAL,
  flat_number INT,
  balance REAL,
  cladr_id INT NOT NULL,
  building_id INT NOT NULL,
  PRIMARY KEY (flat_id),
  FOREIGN KEY (cladr_id, building_id) REFERENCES Building(cladr_id, building_id)
);

CREATE TABLE IF NOT EXISTS RequestToFlat (
  request_id INTEGER,
  flat_id INTEGER,
  PRIMARY KEY (request_id, flat_id),
  FOREIGN KEY (request_id) REFERENCES ServiceRequest,
  FOREIGN KEY (flat_id) REFERENCES Flat
);

CREATE TABLE IF NOT EXISTS Commented (
  id SERIAL,
  request_id INTEGER,
  person_id INTEGER,
  text VARCHAR,
  date DATE,
  PRIMARY KEY (id, request_id, person_id),
  FOREIGN KEY (request_id) REFERENCES ServiceRequest,
  FOREIGN KEY (person_id) REFERENCES Person
);

CREATE TABLE IF NOT EXISTS Visited (
  visit_id SERIAL,
  service_requset_id INTEGER,
  schedule_time DATE,
  out_time DATE,
  start_time DATE,
  end_time DATE,
  costs REAL,
  PRIMARY KEY (visit_id),
  FOREIGN KEY (service_requset_id) REFERENCES ServiceRequest
);

CREATE TABLE IF NOT EXISTS Dweller (
  person_id SERIAL,
  PRIMARY KEY (person_id),
  FOREIGN KEY (person_id) REFERENCES Person(person_id)
);

CREATE TABLE IF NOT EXISTS Technician (
  person_id INTEGER,
  rating RATING_TYPE,
  salary REAL,
  PRIMARY KEY (person_id),
  FOREIGN KEY (person_id) REFERENCES Person(person_id)
);

CREATE TABLE IF NOT EXISTS Assigned (
  visit_id INTEGER,
  technician_id INTEGER,
  PRIMARY KEY (visit_id, technician_id),
  FOREIGN KEY (visit_id) REFERENCES Visited (visit_id),
  FOREIGN KEY (technician_id) REFERENCES Technician (person_id)
);

CREATE TABLE IF NOT EXISTS DwellerLivesInFlat (
  person_id INTEGER,
  flat_id INTEGER,
  PRIMARY KEY (person_id, flat_id),
  FOREIGN KEY (person_id) REFERENCES Person(person_id),
  FOREIGN KEY (flat_id) REFERENCES Flat(flat_id)
);

CREATE TABLE IF NOT EXISTS Meter(
  meter_id SERIAL,
  installation_date TIMESTAMP,
  type VARCHAR,
  title VARCHAR,
  meter_unit_id INTEGER NOT NULL,
  active BOOLEAN,
  flat_id INTEGER NOT NULL,
  PRIMARY KEY (meter_id),
  FOREIGN KEY (meter_unit_id) REFERENCES MeterUnit (meter_unit_id),
  FOREIGN KEY (flat_id) REFERENCES Flat (flat_id)
);

CREATE TABLE IF NOT EXISTS MeterReading(
  meter_reading_id SERIAL,
  value DECIMAL,
  date TIMESTAMP,
  paid BOOLEAN,
  meter_id INTEGER NOT NULL,
  PRIMARY KEY (meter_reading_id),
  FOREIGN KEY (meter_id) REFERENCES Meter (meter_id)
);


INSERT INTO meter_unit(
  description)
VALUES ('cbm'), ('kw-hr');

INSERT INTO flat(
  area, flat_number, balance, cladr_id, building_id)
VALUES (153, 22, 228, 1, 1);

INSERT INTO meter(installation_date, type, title, meter_unit_id, active, flat_id)
VALUES ('01/01/2012', 'Water', 'Hot water#1 in kitchen', 1, true, 1);

INSERT INTO meter(installation_date, type, title, meter_unit_id, active, flat_id)
VALUES ('01/01/2014', 'Water', 'Cold water in kitchen', 1, true, 1);

INSERT INTO meter(installation_date, type, title, meter_unit_id, active, flat_id)
VALUES ('01/01/2012', 'Electricity', 'Electricity in hall', 2, true, 1);