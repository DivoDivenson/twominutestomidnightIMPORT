
CREATE TABLE docket(
	Docket_Number INTEGER PRIMARY KEY AUTOINCREMENT,
	Equipment TEXT  NULL,
	Customer TEXT NULL,
	Seal TEXT  NULL,
	Description TEXT  NULL,
	Berth TEXT  NULL,
	Return_Empty TEXT NULL,
	Weight TEXT  NULL,
	Size_ TEXT  NULL,
	Deliver_to INTEGER NULL,
	Collect_from INTEGER NULL,
	Haz INTEGER NULL,
	Date_ LONG NOT NULL
) 
;

CREATE TABLE addresses(
	ID INTEGER PRIMARY KEY AUTOINCREMENT,
	Address TEXT NULL,
	weight INTEGER NOT NULL
	
) 
;

CREATE TABLE haz(
	ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	Name TEXT  NULL,
	UN_Number TEXT  NULL,
	Packing_Group TEXT NULL,
	Primary_Class TEXT  NULL,
	Secondary_Class TEXT  NULL,
	Tunnel_code TEXT  NULL
	
) 
;

/*insert into docket (Date_) values ('1');*/

insert into docket (Equipment, Customer, Seal, Description, Berth, Weight, Size_, Return_Empty, Deliver_to, Collect_from, Haz, Date_) values ('123d', 'Trim', '11bd4', 'Copper sulphate', '44d', '1000kg', '45ft', 'to yard', 2, 3, 1, 222221);

insert into docket (Equipment, Customer, Seal, Description, Berth, Weight, Size_, Return_Empty, Deliver_to, Collect_from, Date_) values ('ryjhrys', 'Sharp', '11wgd4', 'Copper sulphate', '44d', '1000kg', '45ft', 'to heatons', 3, 2, 23322);

insert into addresses (address,weight) values ('', '1');

insert into addresses (Address,weight) values ('14 Strand Road\nSutton\nDublin 13' , '1');

insert into addresses (Address,weight) values ('123 Fakte st.\nNo road\nDublin 111' , '1');

insert into haz (Name, UN_Number, Packing_Group, Primary_Class, Tunnel_code) values ('Kero', '1223', 'III', '3', 'C/D');
INSERT INTO haz (Name, UN_Number, Packing_Group, Primary_Class, Secondary_Class, Tunnel_code) values ('wrvqwe', 'vqve', 'III', '4.1', '(4.3)', 'vea');


	
