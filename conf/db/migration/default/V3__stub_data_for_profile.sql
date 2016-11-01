
INSERT INTO person(person_id, name, surname, paternal_name, registration_date)
VALUES (1, 'Anton', 'Kiselev', '', '01/01/2012');

INSERT INTO dweller(person_id)
VALUES (1);

INSERT INTO dweller_lives_in_flat(person_id, flat_id)
VALUES (1, 1);
