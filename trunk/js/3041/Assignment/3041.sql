
ALTER TABLE cargo DROP CONSTRAINT fk_coloumn_cargo;
ALTER TABLE driver DROP CONSTRAINT fk_coloumn_address;

DROP TABLE docket;
DROP TABLE address;
DROP TABLE haz;
DROP TABLE driver;
DROP TABLE cargo;
DROP TABLE truck;

DROP sequence address_increment;
DROP trigger address_incr_trigger;

drop sequence haz_increment;
drop trigger haz_inc_trigger;

drop sequence driver_increment;
drop trigger driver_incr_trigger;

drop sequence docket_increment;
drop trigger docket_incr_inrement;

CREATE TABLE address(
	ID INT,
	Street varchar(100) NULL,
  	City varchar(30) NULL,
  	County varchar(20) NULL,
  	Site char(1) NULL,
	weight INT NOT NULL,
	PRIMARY KEY (ID)
)
;

create sequence address_increment start with 1;

create trigger address_inrc_trigger
before insert on address
for each row
begin
select address_increment.nextval into :new.ID from dual;
end;
/

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
	FName varchar (15) ,
	SName varchar (15) ,
	Residence INT ,
	ADR char(1) ,
	SafePass char(1) ,
	PRIMARY KEY (ID),
	Constraint fk_coloumn_address
	FOREIGN KEY(Residence) References address
)
;

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
	Return_Empty char(1) NULL,
	Weight INT NULL,
	Size_ varchar(20) NULL,
	Haz INT NULL, 
	Crane char(1) NULL,
	PRIMARY KEY (Seal),
	Constraint fk_coloumn_cargo
	FOREIGN KEY(Haz) References haz
)
;

CREATE TABLE truck(
   Reg varchar(10),
   Crane char(1) ,  /* Is the truck equipped to control the special trailer with a crane attatched to it */
   ADR char(1) ,    /* Is the truck certified to haul hazardous goods */
   Road char(1) ,   /* Is the truck road legal or not (ie, just used for shunting around the yard */
   Last_test Date ,
   PRIMARY KEY (Reg)
)
;

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

column Site format a4
column Return_Empty format a6
column Crane format a5
column Street format a10
column Description format a15
column Name format a10

drop view docketNoHaz;
drop view docketHaz;
drop view docketNoHazTest;
drop view docketHazTest;

create view docketNoHaz (Docket_Number, Date_, Del_Street, Del_City, Del_County, Del_Site, Customer, Col_Street, Col_City, Col_Count, Col_Site, Equipment,
       Seal, Description, Return_Empty, Weight, Size_, Crane) AS
select doc.Docket_Number, doc.Date_, a.Street, a.City, a.County, a.Site, doc.Customer, a2.Street, a2.City, a2.County, a2.Site , doc.Equipment_No, doc.Seal,
       c.Description, c.Return_Empty, c.Weight, c.Size_, c.Crane
       from docket doc, address a, address a2, cargo c
       where doc.Deliver_to = a.ID AND doc.Collect_from = a2.ID AND doc.Seal = c.Seal AND c.Haz IS NULL
       order by doc.Docket_Number;

create view docketHaz (Docket_Number, Date_, Del_Street, Del_City, Del_County, Del_Site, Customer, Col_Street, Col_City, Col_Count, Col_Site, Equipment,
       Seal, Description, Return_Empty, Weight, Size_, Crane, Haz_Name, UN_No, PG, Primary, Secondary, Tunnel) AS
select doc.Docket_Number, doc.Date_, a.Street, a.City, a.County, a.Site, doc.Customer, a2.Street, a2.City, a2.County, a2.Site , doc.Equipment_No, doc.Seal,
       c.Description, c.Return_Empty, c.Weight, c.Size_, c.Crane, h.Name, h.UN_Number, h.Packing_Group, h.Primary_Class, h.Secondary_Class, h.Tunnel_Code
       from docket doc, address a, address a2, cargo c, haz h
       where doc.Deliver_to = a.ID AND doc.Collect_from = a2.ID AND doc.Seal = c.Seal AND c.Haz = h.ID
       order by doc.Docket_Number;


select doc.Docket_Number,  a.Street, c.Description
       from docket doc, address a,  cargo c
       where doc.Deliver_to = a.ID AND  doc.Seal = c.Seal
       order by doc.Docket_Number;


create view docketNoHazTest (Docket_Number, Del_Street, Description) AS
select doc.Docket_Number, a.Street,  c.Description
       from docket doc, address a, cargo c
       where doc.Deliver_to = a.ID AND doc.Seal = c.Seal AND c.Haz IS NULL
       order by doc.Docket_Number;

create view docketHazTest (Docket_Number, Del_Street, Description, Haz_Name) AS
select doc.Docket_Number, a.Street, c.Description, h.Name
       from docket doc, address a, cargo c, haz h
       where doc.Deliver_to = a.ID AND  doc.Seal = c.Seal AND c.Haz = h.ID
       order by doc.Docket_Number;

column Del_Street format a10;
column Description format a10;
column Haz_Name format a10;
column Docket_Number format a10;

(select * from docketNoHazTest) UNION (select * from docketHazTest);
