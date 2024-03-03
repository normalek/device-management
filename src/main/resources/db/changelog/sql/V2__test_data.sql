INSERT INTO device_type(name) VALUES ('Samsung Galaxy S9');
INSERT INTO device_type(name) VALUES ('Samsung Galaxy S8');
INSERT INTO device_type(name) VALUES ('Motorola Nexus 6');
INSERT INTO device_type(name) VALUES ('Oneplus 9');
INSERT INTO device_type(name) VALUES ('Apple iPhone 13');
INSERT INTO device_type(name) VALUES ('Apple iPhone 12');
INSERT INTO device_type(name) VALUES ('iPhone X');
INSERT INTO device_type(name) VALUES ('Nokia 3310');

INSERT INTO device(device_type) SELECT id from device_type where name = 'Samsung Galaxy S9';
INSERT INTO device(device_type) SELECT id from device_type where name = 'Samsung Galaxy S8';
INSERT INTO device(device_type) SELECT id from device_type where name = 'Samsung Galaxy S8';
INSERT INTO device(device_type) SELECT id from device_type where name = 'Motorola Nexus 6';
INSERT INTO device(device_type) SELECT id from device_type where name = 'Oneplus 9';
INSERT INTO device(device_type) SELECT id from device_type where name = 'Apple iPhone 13';
INSERT INTO device(device_type) SELECT id from device_type where name = 'Apple iPhone 12';
INSERT INTO device(device_type) SELECT id from device_type where name = 'iPhone X';
INSERT INTO device(device_type) SELECT id from device_type where name = 'Nokia 3310';