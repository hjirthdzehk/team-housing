ALTER TABLE servicerequest RENAME TO service_request;
ALTER TABLE requesttoflat RENAME TO request_to_flat;
ALTER TABLE meterunit RENAME TO meter_unit;
ALTER TABLE meterreading RENAME TO meter_reading;
ALTER TABLE dwellerlivesinflat RENAME TO dweller_lives_in_flat;

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