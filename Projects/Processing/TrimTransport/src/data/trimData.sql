CREATE DATABASE IF NOT EXISTS trimtransport;

USE trimtransport;
 
DROP TABLE IF EXISTS docket;
DROP TABLE IF EXISTS addresses;
DROP TABLE IF EXISTS haz;

CREATE TABLE docket(
	Docket_Number INT NOT NULL AUTO_INCREMENT,
	Equipment varchar (30) NULL,
	Customer varchar(30) NULL,
	Seal varchar (20) NULL,
	Description varchar (200) NULL,
	Berth varchar (10) NULL,
	Return_Empty boolean NOT NULL,
	Weight varchar (10) NULL,
	Size_ varchar (10) NULL,
	Deliver_to INT NULL,
	Collect_from INT NULL,
	Haz INT NULL,
	PRIMARY KEY (Docket_Number)
) TYPE=INNODB
;

CREATE TABLE addresses(
	ID INT NOT NULL AUTO_INCREMENT,
	Address varchar(200) NULL,
	PRIMARY KEY (ID)
) TYPE=INNODB
;

CREATE TABLE haz(
	ID INT NOT NULL AUTO_INCREMENT,
	Name varchar (30) NULL,
	UN_Number varchar (6) NULL,
	Packing_Group varchar(3) NULL,
	Primary_Class varchar (5) NULL,
	Secondary_Class varchar (5) NULL,
	Tunnel_code varchar (7) NULL,
	PRIMARY KEY (ID)
) TYPE=INNODB
;

insert into docket (Equipment, Customer, Seal, Description, Berth, Weight, Size_, Return_Empty, Deliver_to, Collect_from, Haz) values ('123d', 'Trim', '11bd4', 'Copper sulphate', '44d', '1000kg', '45ft', 'f', 1, 2, 1);

insert into docket (Equipment, Customer, Seal, Description, Berth, Weight, Size_, Return_Empty, Deliver_to, Collect_from) values ('ryjhrys', 'Sharp', '11wgd4', 'Copper sulphate', '44d', '1000kg', '45ft', FALSE, 2, 1);

insert into addresses (Address) values ('14 Strand Road\nSutton\nDublin 13');

insert into addresses (Address) values ('123 Fakte st.\nNo road\nDublin 111');

insert into haz (Name, UN_Number, Packing_Group, Primary_Class, Tunnel_code) values ('Kero', '1223', 'III', '3', 'C/D');

	
