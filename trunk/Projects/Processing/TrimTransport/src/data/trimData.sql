CREATE DATABASE IF NOT EXISTS trimtransport;

USE trimtransport;
 
DROP TABLE IF EXISTS docket;
DROP TABLE IF EXISTS addresses;
DROP TABLE IF EXISTS haz;

CREATE TABLE docket(
	docketNo INT NOT NULL AUTO_INCREMENT,
	equip varchar (30) NULL,
	customerRef varchar(30) NULL,
	sealNo varchar (20) NULL,
	description varchar (200) NULL,
	berth varchar (10) NULL,
	returnEmpty boolean NOT NULL,
	weight varchar (10) NULL,
	sizex varchar (10) NULL,
	deliver INT NULL,
	collectFrom INT NULL,
	haz INT NULL,
	PRIMARY KEY (docketNo)
) TYPE=INNODB
;

CREATE TABLE addresses(
	ID INT NOT NULL AUTO_INCREMENT,
	address varchar(200) NULL,
	PRIMARY KEY (ID)
) TYPE=INNODB
;

CREATE TABLE haz(
	ID INT NOT NULL AUTO_INCREMENT,
	name varchar (30) NULL,
	unNumber varchar (6) NULL,
	pg varchar(3) NULL,
	primeClass varchar (5) NULL,
	secClass varchar (5) NULL,
	tunnel varchar (7) NULL,
	PRIMARY KEY (ID)
) TYPE=INNODB
;

insert into docket (equip, customerRef, sealNo, description, berth, weight, sizex, returnEmpty, deliver, collectFrom, haz) values ('123d', 'Trim', '11bd4', 'Copper sulphate', '44d', '1000kg', '45ft', 'f', 1, 2, 1);

insert into docket (equip, customerRef, sealNo, description, berth, weight, sizex, returnEmpty, deliver, collectFrom) values ('ryjhrys', 'Sharp', '11wgd4', 'Copper sulphate', '44d', '1000kg', '45ft', FALSE, 2, 1);

insert into addresses (address) values ('14 Strand Road\nSutton\nDublin 13');

insert into addresses (address) values ('123 Fakte st.\nNo road\nDublin 111');

insert into haz (name, unNumber, pg, primeClass, tunnel) values ('Kero', '1223', 'III', '3', 'C/D');

	
