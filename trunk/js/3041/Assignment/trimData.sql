CREATE DATABASE IF NOT EXISTS CS3041;

USE CS3041;
 
DROP TABLE IF EXISTS docket;
DROP TABLE IF EXISTS address;
DROP TABLE IF EXISTS haz;
DROP TABLE IF EXISTS driver;
DROP TABLE IF EXISTS cargo;
DROP TABLE IF EXISTS truck;


CREATE TABLE docket(
	Docket_Number INT NOT NULL AUTO_INCREMENT,
	Equipment varchar (30) NULL,
	Customer varchar(30) NULL,
	Seal varchar (20) NULL,
	Deliver_to INT NULL,
	Collect_from INT NULL,
	Haz INT NULL,
	Date_ DATE NOT NULL,
   Driver_ INT NULL,
   Truck varchar(10) NULL,
	PRIMARY KEY (Docket_Number)
) TYPE=INNODB /*MYSQL thing, will have to change for oracle*/
;

CREATE TABLE address(
	ID INT NOT NULL AUTO_INCREMENT,
	Street varchar(200) NULL,
   City varchar(30) NULL,
   County varchar(20) NULL,
   Site boolean NULL,
	weight INT NOT NULL,
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

CREATE TABLE driver(
   ID INT NOT NULL AUTO_INCREMENT,
   FName varchar (15) NULL,
   SNname varchar (15) NULL,
   Residence INT NULL,
   ADR boolean NULL,
   SafePass boolean NULL,
   PRIMARY KEY (ID)
) TYPE=INNODB
;

CREATE TABLE cargo(
   Seal varchar(10) NOT NULL,
   Description varchar(200) NULL,
   Return_Empty boolean NULL,
   Weight INT NULL,
   Size_ varchar(20) NULL,
   Haz INT NULL, 
   Crane boolean NULL,
   PRIMARY KEY (Seal)
) TYPE=INNODB
;

CREATE TABLE truck(
   Reg varchar(10) NOT NULL,
   Crane boolean NULL,  /* Is the truck equipped to control the special trailer with a crane attatched to it */
   ADR boolean NULL,    /* Is the truck certified to haul hazardous goods */
   Road boolean NULL,   /* Is the truck road legal or not (ie, just used for shunting around the yard */
   Last_test Date NULL,
   PRIMARY KEY (Reg)
) TYPE=INNODB
;
/*insert into docket (Date_) values ('1');

insert into docket (Equipment, Customer, Seal, Description, Berth, Weight, Size_, Return_Empty, Deliver_to, Collect_from, Haz, Date_) values ('123d', 'Trim', '11bd4', 'Copper sulphate', '44d', '1000kg', '45ft', 'to yard', 2, 3, 1, 222221);

insert into docket (Equipment, Customer, Seal, Description, Berth, Weight, Size_, Return_Empty, Deliver_to, Collect_from, Date_) values ('ryjhrys', 'Sharp', '11wgd4', 'Copper sulphate', '44d', '1000kg', '45ft', 'to heatons', 3, 2, 23322);

insert into addresses (address,weight) values ('', '1');

insert into addresses (Address,weight) values ('14 Strand Road\nSutton\nDublin 13' , '1');

insert into addresses (Address,weight) values ('123 Fakte st.\nNo road\nDublin 111' , '1');

insert into haz (Name, UN_Number, Packing_Group, Primary_Class, Tunnel_code) values ('Kero', '1223', 'III', '3', 'C/D');
INSERT INTO haz (Name, UN_Number, Packing_Group, Primary_Class, Secondary_Class, Tunnel_code) values ('wrvqwe', 'vqve', 'III', '4.1', '(4.3)', 'vea');

*/
	
