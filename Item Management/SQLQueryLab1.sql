CREATE database ItemManagement
GO

USE ItemManagement
GO

CREATE table tblUsers (
	userID varchar(10) PRIMARY KEY,
	fullName nvarchar(50),
	password varchar(50),
	status bit
	)

CREATE table tblSuppliers (
	supCode varchar(10) PRIMARY KEY,
	supName nvarchar(50),
	address nvarchar(50),
	collaborating bit
	)

CREATE table tblItems (
	itemCode varchar(10) PRIMARY KEY,
	itemName nvarchar(50),
	unit varchar(50),
	price float,
	supplying bit,
	supCode varchar(10),
	CONSTRAINT fk FOREIGN KEY (supCode) REFERENCES tblSuppliers(supCode)
	)


INSERT INTO dbo.tblUsers VALUES ('admin', 'Administrator', 'a', 1),
							('user', 'User ne', 'a', 0),
							('thanh', 'Cong Thanh - san', 'a', 1),
							('van', 'Co Van de thuong', 'a', 0)

INSERT INTO tblSuppliers VALUES ('sup1', 'Nike', 'USA', 1),
								('sup2', 'Adidas', 'Germany', 1),
								('sup3', 'Vans', 'USA', 1),
								('sup4', 'Converse', 'USA', 1),
								('sup5', 'MCQueen', 'England', 1)

INSERT INTO tblItems VALUES ('it1101', 'Nike Air Force 1', 'Pair', 2200000, 1, 'sup1'),
							('it1102', 'Nike Air Jordan 1', 'Pair', 3200000, 1, 'sup1'),
							('it1103', 'Adidas Stan Smith', 'Pair', 2200000, 1, 'sup2'),
							('it1104', 'Adidas SuperStart', 'Pair', 2100000, 1, 'sup2'),
							('it1105', 'Vans Old Skools', 'Pair', 1200000, 1, 'sup3'),
							('it1106', 'Vans Lowland', 'Pair', 1400000, 1, 'sup3'),
							('it1107', 'Converse Basic', 'Pair', 1200000, 1, 'sup4'),
							('it1108', 'Converse 1970s', 'Pair', 1300000, 1, 'sup4'),
							('it1109', 'Alexander MCQueen', 'Pair', 1000000, 1, 'sup5')
