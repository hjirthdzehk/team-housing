-- From V2

INSERT INTO meter_unit(
  description)
VALUES ('cbm'), ('kw-hr');

INSERT INTO building(
cladr_id, building_id)
VALUES (1, 1);


INSERT INTO flat(
  flat_id, area, flat_number, balance, cladr_id, building_id)
VALUES (1, 153, 22, 228, 1, 1);


INSERT INTO meter(
  installation_date, type, title, meter_unit_id, active,
  flat_id)
VALUES ('01/01/2012', 'Water', 'Hot water#1 in kitchen', 1, true,
        1);


INSERT INTO meter(
  installation_date, type, title, meter_unit_id, active,
  flat_id)
VALUES ('01/01/2014', 'Water', 'Cold water in kitchen', 1, true,
        1);

INSERT INTO meter(
  installation_date, type, title, meter_unit_id, active,
  flat_id)
VALUES ('01/01/2012', 'Electricity', 'Electricity in hall', 2, true,
        1);

-- From V3

INSERT INTO person(person_id, name, surname, paternal_name, registration_date, email, password_hash, is_admin)
VALUES (1, 'test', 'test', '', '01/01/2012', 'test', '098f6bcd4621d373cade4e832627b4f6', true);

INSERT INTO dweller(person_id)
VALUES (1);

INSERT INTO dweller_lives_in_flat(person_id, flat_id)
VALUES (1, 1);

-- From V4

insert into rate (rate_id, value, date_from, date_to, meter_unit_id)
values (1, 228, '2016-10-02', '2016-11-30', 1);

insert into rate (rate_id, value, date_from, date_to, meter_unit_id)
values (2, 42, '2016-10-02', '2016-11-30', 2);

-- Rates

insert into rate (value, date_from, date_to, meter_unit_id) values(192, '2012-01-01', '2012-02-01', 1);
insert into rate (value, date_from, date_to, meter_unit_id) values(216, '2012-02-01', '2012-03-01', 1);
insert into rate (value, date_from, date_to, meter_unit_id) values(321, '2012-03-01', '2012-04-01', 1);
insert into rate (value, date_from, date_to, meter_unit_id) values(223, '2012-04-01', '2012-05-01', 1);
insert into rate (value, date_from, date_to, meter_unit_id) values(167, '2012-05-01', '2012-06-01', 1);

insert into rate (value, date_from, date_to, meter_unit_id) values(45, '2012-01-01', '2012-02-01', 2);
insert into rate (value, date_from, date_to, meter_unit_id) values(72, '2012-02-01', '2012-03-01', 2);
insert into rate (value, date_from, date_to, meter_unit_id) values(105, '2012-03-01', '2012-04-01', 2);
insert into rate (value, date_from, date_to, meter_unit_id) values(68, '2012-04-01', '2012-05-01', 2);
insert into rate (value, date_from, date_to, meter_unit_id) values(31, '2012-05-01', '2012-06-01', 2);