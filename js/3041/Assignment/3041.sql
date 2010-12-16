
ALTER TABLE cargo DROP CONSTRAINT fk_coloumn_cargo;
ALTER TABLE driver DROP CONSTRAINT fk_coloumn_address;

DROP TABLE docket;
DROP TABLE address;
DROP TABLE haz;
DROP TABLE driver;
DROP TABLE cargo;
DROP TABLE truck;

DROP sequence address_increment;

drop sequence haz_increment;

drop sequence driver_increment;

drop sequence docket_increment;

CREATE TABLE address(
	ID INT,
	Street varchar(100) NULL,
 	City varchar(30) NULL,
 	County varchar(20) NULL,
 	Site char(1) default 'N',
	weight INT NOT NULL,
	PRIMARY KEY (ID)
)
;

create sequence address_increment start with 1;

 
create or replace trigger address_inrc_trigger
before insert on address
for each row
begin
select address_increment.nextval into :new.ID from dual;
end;
/ 
/*
set serveroutput on;
create or replace  trigger address_weight
before insert on address
for each row
declare
  tempID INT;
begin
select count(ID) into tempID from address where Street = :new.Street;
dbms_output.put_line(tempID);
if tempID = 0 then
  select address_increment.nextval into :new.ID from dual;
  insert into address(ID, Street, City, County, Site) values(:new.ID, :new.Street, :new.City, :new.County, :new.Site);
else
  select ID into tempID from address where Street = :new.Street;
  update address SET weight = weight+1 where ID = tempID;
end if;
end;
/
/*Spent ~1 hour trying to get this to work. Come back to later*/



CREATE TABLE haz(
	ID INT,
	Name varchar (50) NULL,
	UN_Number INT NULL,
	Packing_Group varchar(3) NULL,
	Primary_Class number NULL,
	Secondary_Class number NULL,
	Tunnel_code varchar (7) NULL,
	PRIMARY KEY (ID)
)
;

create sequence haz_increment start with 1;

create trigger haz_inc_trigger
before insert on haz
for each row
begin
select haz_increment.nextval into :new.ID from dual;
end;
/

CREATE TABLE driver(
	ID INT,
	FName varchar (15) NOT NULL ,
	SName varchar (15) NOT NULL,
	Residence INT ,
	ADR char(1) default 'Y',
	SafePass char(1) default 'N',
	PRIMARY KEY (ID),
	Constraint fk_coloumn_address
	FOREIGN KEY(Residence) References address
)
;

alter table driver add constraint adr_bool CHECK(ADR IN ('Y','N'));
alter table driver add constraint safe_bool check (SafePass IN ('Y','N'));


create sequence driver_increment start with 1;

create trigger driver_incr_trigger
before insert on driver
for each row
begin
select driver_increment.nextval into :new.ID from dual;
end;
/

CREATE TABLE cargo(
	Seal varchar(10),
	Description varchar(200) NULL,
	Return_Empty char(1) default 'Y',
	Weight INT NULL,
	Size_ varchar(20) NULL,
	Haz INT NULL, 
	Crane char(1) NULL,
	PRIMARY KEY (Seal),
	Constraint fk_coloumn_cargo
	FOREIGN KEY(Haz) References haz
)
;

alter table cargo add constraint return_bool check (Return_Empty in('Y','N'));
alter table cargo add constraint crane_bool check (Crane in('Y','N'));

CREATE TABLE truck(
   Reg varchar(10),
   Crane char(1) NOT NULL,  /* Is the truck equipped to control the special trailer with a crane attatched to it */
   ADR char(1) NOT NULL,    /* Is the truck certified to haul hazardous goods */
   Road char(1) NOT NULL,   /* Is the truck road legal or not (ie, just used for shunting around the yard */
   Last_test Date NOT NULL,
   PRIMARY KEY (Reg)
)
;

alter table cargo add constraint crane_truck_bool check (Crane in ('Y','N'));
alter table cargo add constraint ADR_truck_bool check (ADR in ('Y','N'));
alter table cargo add constraint road_truck_bool check (Crane in ('Y','N'));


CREATE TABLE docket(
	Docket_Number INT,
	Equipment_No varchar (30) NULL,
	Customer varchar(30) NULL,
	Seal varchar (20) NULL,
	Deliver_to INT NULL,
	Collect_from INT NULL,
	Date_ DATE NOT NULL,
   	Driver_ INT NULL,
   	Truck varchar(10) NULL,
	PRIMARY KEY (Docket_Number),
	Constraint fk_column
	FOREIGN KEY(Deliver_to) References address,
	FOREIGN KEY(Collect_from) References address,
	FOREIGN KEY(Driver_) References driver,
	FOREIGN KEY(Truck) References truck,
	FOREIGN KEY(Seal) References cargo 
)
;

create sequence docket_increment start with 1;

create trigger docker_incr_trigger
before insert on docket
for each row
begin
select docket_increment.nextval into :new.Docket_Number from dual;
end;
/


insert into truck (Reg, Crane, ADR, Road, Last_test) values('00D3452', 'N', 'N', 'Y', '21-feb-09');
insert into truck (Reg, Crane, ADR, Road, Last_test) values('05D312', 'N', 'Y', 'Y', '21-feb-09');
insert into truck (Reg, Crane, ADR, Road, Last_test) values('07KE4738', 'Y', 'N', 'Y', '21-feb-09');
insert into truck (Reg, Crane, ADR, Road, Last_test) values('03D3119', 'Y', 'Y', 'Y', '21-feb-09');
insert into truck (Reg, Crane, ADR, Road, Last_test) values('03D5341', 'Y', 'Y', 'N', '11-nov-08');


insert into address (Street, City, County, Site, weight) values('23 Cove Drive', 'Howth', 'Dublin 13', 'N', 0);
insert into address (Street, City, County, Site, weight) values('56 Balkhill Park', 'Howth', 'Dublin 13', 'N', 0);
insert into address (Street, City, County, Site, weight) values('154 Bettyglen', 'Raheny', 'Dublin 5', 'N', 0);
insert into address (Street, City, County, Site, weight) values('67 Oak Avenue', 'Bray', 'Dublin, South', 'N', 0);

insert into address (Street, City, County, Site, weight) values('Maxol', 'Tolka Quay', 'Dublin 1', 'N', 0);
insert into address (Street, City, County, Site, weight) values('Indaver Ltd', 'Tolka Quay', 'Dublin 1', 'N', 0);
insert into address (Street, City, County, Site, weight) values('Unit 23 Rosemount Business Park', 'Tyrellstown', 'Dublin 11', 'Y', 0);
insert into address (Street, City, County, Site, weight) values('Heatons', 'East Wall Road', 'Dublin 1', 'N', 0);
insert into address (Street, City, County, Site, weight) values('P and O', 'Littlerath', 'Dublin 1', 'N', 0);
insert into address (Street, City, County, Site, weight) values('Trim Transport Ltd', 'Tolka Quay', 'Dublin 1', 'N', 0);

insert into haz (Name, UN_Number, Packing_Group, Primary_class, Tunnel_code) values('Petrol', 1203, 2, 3, 'D/E');
insert into haz (Name, UN_Number, Packing_Group, Primary_class, Secondary_class, Tunnel_code) values('Sodium Methylate', 1431, 2, 4.2, 8, 'D/E');
insert into haz (Name, UN_Number, Packing_Group, Primary_class, Tunnel_code) values('Triisopropyl Borate', 2616, 3, 3, 'D/E');
insert into haz (Name, UN_Number, Packing_Group, Primary_class, Tunnel_code) values('Organophosphorus Pesticide, Solid, Toxic', 2783, 3, 6.1, 'E');
insert into haz (Name, UN_Number, Packing_Group, Primary_class, Tunnel_code) values('Toxic Liquid, Organic, N.O.S', 2810, 2, 6.1, 'D/E');

insert into driver(FName, SName, Residence, ADR, SafePass) values('Eoin', 'McCarthy', 1, 'Y', 'Y');
insert into driver(FName, SName, Residence, ADR, SafePass) values('Paul', 'Murphy', 1, 'Y', 'N');
insert into driver(FName, SName, Residence, ADR, SafePass) values('Richard', 'Delaney', 2, 'N', 'Y');
insert into driver(FName, SName, Residence, ADR, SafePass) values('Conor', 'McEvoy', 3, 'N', 'N');
insert into driver(FName, SName, Residence, ADR, SafePass) values('Steven', 'Diviney', 4, 'Y', 'Y');

insert into cargo(Seal, Description, Return_Empty, Weight, Size_, Haz, Crane) values('u45bfg', '30 Drums of waste from Repo.', 'N', 15000, '45ft', 5, 'Y');
insert into cargo(Seal, Description, Return_Empty, Weight, Size_, Crane) values('35hgjk', 'Bags of cement', 'Y', 20000, '45ft', 'Y');
insert into cargo(Seal, Description, Return_Empty, Weight, Size_, Crane) values('hea454', 'Timber', 'N', 5000, '45ft Tautliner', 'N');
insert into cargo(Seal, Description, Return_Empty, Weight, Size_, Haz, Crane) values('4jkde3', 'Borate for China', 'N', 2000, '20ft', 3, 'N');
insert into cargo(Seal, Description, Return_Empty, Weight, Size_, Haz, Crane) values('435jn4', '30 Pallets', 'Y', 15000, '45ft', 2, 'Y');

insert into docket(Equipment_No, Customer, Date_, Seal, Deliver_to, Collect_from, Driver_, Truck) values('TRIM1', 'Heatons', '13-feb-10', 'hea454', 8, 10, 1, '00D3452'); 
insert into docket(Equipment_No, Customer, Date_, Seal, Deliver_to, Collect_from, Driver_, Truck) values('3562', 'Irish Concrete', '14-may-10', '35hgjk', 7, 9, 3, '07KE4738');
insert into docket(Equipment_No, Customer, Date_, Seal, Deliver_to, Collect_from, Driver_, Truck) values('2231', 'Indaver', '26-jul-10', 'u45bfg', 6, 10, 5, '03D3119'); 
insert into docket(Equipment_No, Customer, Date_, Seal, Deliver_to, Collect_from, Driver_, Truck) values('3321', 'Sas Cem.', '27-oct-10', '435jn4', 10, 5, 1, '03D3119'); 
insert into docket(Equipment_No, Customer, Date_, Seal, Deliver_to, Collect_from, Driver_, Truck) values('1224', 'P and O', '18-sep-10', '4jkde3', 9, 10, 2, '05D312'); 


create or replace view Docket_Summary(Docket_Number, Date_, Customer, Del_Street, Col_Street, Equipment, Seal, Size_, DriverS, Truck) AS
       select doc.Docket_Number, doc.date_, doc.Customer, a.Street, a2.Street, doc.Equipment_No, doc.Seal, c.Size_, driver.SName, doc.Truck
       from docket doc, address a, address a2, cargo c, driver
       where doc.Deliver_to = a.ID AND doc.Collect_from = a2.ID AND doc.Seal = c.Seal AND doc.Driver_ = driver.ID
       order by doc.Docket_Number;

create or replace view docketNoHaz (Docket_Number, Date_, Del_Street, Del_City, Del_County, Del_Site, Customer, Col_Street, Col_City, Col_Count, Col_Site, Equipment,
       Seal, Description, Return_Empty, Weight, Size_, Crane, DriverS, DriverF, Truck) AS
select doc.Docket_Number, doc.Date_, a.Street, a.City, a.County, a.Site, doc.Customer, a2.Street, a2.City, a2.County, a2.Site , doc.Equipment_No, doc.Seal,
       c.Description, c.Return_Empty, c.Weight, c.Size_, c.Crane, dr.SName, dr.FName, truck.Reg
       from docket doc, address a, address a2, cargo c, driver dr, truck
       where doc.Deliver_to = a.ID AND doc.Collect_from = a2.ID AND doc.Seal = c.Seal AND c.Haz IS NULL AND doc.Driver_ = dr.ID AND doc.Truck = truck.Reg
       order by doc.Docket_Number;

create or replace view docketHaz (Docket_Number, Date_, Del_Street, Del_City, Del_County, Del_Site, Customer, Col_Street, Col_City, Col_Count, Col_Site, Equipment,
       Seal, Description, Return_Empty, Weight, Size_, Crane, DriverS, DriverF, Truck, Haz_Name, UN_No, PG, Primary, Secondary, Tunnel) AS
select doc.Docket_Number, doc.Date_, a.Street, a.City, a.County, a.Site, doc.Customer, a2.Street, a2.City, a2.County, a2.Site , doc.Equipment_No, doc.Seal,
       c.Description, c.Return_Empty, c.Weight, c.Size_, c.Crane,  dr.SName, dr.FName, truck.Reg, h.Name, h.UN_Number, h.Packing_Group, 
       h.Primary_Class, h.Secondary_Class, h.Tunnel_Code
       from docket doc, address a, address a2, cargo c, haz h, driver dr, truck
       where doc.Deliver_to = a.ID AND doc.Collect_from = a2.ID AND doc.Seal = c.Seal AND c.Haz = h.ID AND doc.Driver_ = dr.ID AND doc.Truck = truck.Reg
       order by doc.Docket_Number;

-- Update the trucks after passing an NCT
update truck set LAST_TEST = '13-NOV-10' where LAST_TEST = '21-feb-09';

-- Paul has moved out of Eoins house to live in Richards house
update driver set Residence = 2 where SName = 'Murphy';

-- Paul was ment to deliver a load but Eoin had to instead
update docket set Driver_ = 1 where Driver_ = 2 AND Date_ = '18-sep-10';


declare
temp number;
begin
select count(ID) into temp from address;
dbms_output.put_line(temp);
end;
.
