-- Xóa database hiện có nếu tồn tại
DROP DATABASE IF EXISTS PhoneCardStoreDB;
CREATE DATABASE PhoneCardStoreDB;
USE PhoneCardStoreDB;

-- Bảng Roles để lưu trữ chức năng của người dùng
CREATE TABLE Roles (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    `Name` VARCHAR(50)
);

-- Tạo bảng Users
CREATE TABLE Users (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(50) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL,
    Fullname VARCHAR(100),
    Image varchar(255),
    Email VARCHAR(100) NOT NULL UNIQUE,
    Phone VARCHAR(20),
    Createdat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Roleid INT,
    `Status` bool,
    FOREIGN KEY (Roleid) REFERENCES Roles(ID)
);

-- Bảng ProductType để lưu trữ loại sản phẩm
CREATE TABLE Product (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    `Name` VARCHAR(100) NOT NULL,
	Image varchar(255),
    price int,
    `Describe` TEXT,
    Sold INT,
    `Status` bool
);

CREATE TABLE Productdetail (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    Productid int,
    Seri varchar(255),
    `Code` varchar(255),
    `Status` bool,	
    FOREIGN KEY (Productid) REFERENCES Product(ID)
);
CREATE TABLE Purchasehistory (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    Productid int,
	price int,
    `Date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Accountid int,
    FOREIGN KEY (Productid) REFERENCES Product(ID),
    FOREIGN KEY (Accountid) REFERENCES Users(ID)
);
CREATE TABLE Purchasehistorydetail (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    Purchasehistoryid int,
	Seri varchar(255),
    `Code` varchar(255),
    FOREIGN KEY (Purchasehistoryid) REFERENCES Purchasehistory(ID)
);
Create table Cart(
	ID INT AUTO_INCREMENT PRIMARY KEY,
	Productid int,
    Accountid int,
    Quantity int,
    FOREIGN KEY (Productid) REFERENCES Product(ID),
    FOREIGN KEY (Accountid) REFERENCES Users(ID)
);
Create table Wallet(
	ID INT AUTO_INCREMENT PRIMARY KEY,
    Accountid int,
	Wallet int,
    FOREIGN KEY (Accountid) REFERENCES Users(ID)
);

Create table Wallethistory(
	ID INT AUTO_INCREMENT PRIMARY KEY,
    Walletid int,
	Money int,
    `Date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `Describ` TEXT,
    FOREIGN KEY (Walletid) REFERENCES Wallet(ID)
    
);
create table blogs(
	ID int,
    `Name` varchar(255),
    `Text` text,
	`Date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
-- insert
INSERT INTO `phonecardstoredb`.`roles` (`Name`)	VALUES('ADMIN'),('USER');
INSERT INTO Users (Username, Password, Fullname, Email, Phone, Roleid,`Status`) 
VALUES 
    ('user1', '$2a$10$/fjstiJrV5sZ78E37kRILeAAwbpHY.TO6hIEwUyA7oSsmYtMNh8Vq', 'User One', 'user1@example.com', '1234567890', 2,1),
    ('user2', '$2a$10$/fjstiJrV5sZ78E37kRILeAAwbpHY.TO6hIEwUyA7oSsmYtMNh8Vq', 'User Two', 'user2@example.com', '1234567891', 2,1),
    ('user3', '$2a$10$/fjstiJrV5sZ78E37kRILeAAwbpHY.TO6hIEwUyA7oSsmYtMNh8Vq', 'User Three', 'user3@example.com', '1234567892', 2,1),
    ('user4', '$2a$10$/fjstiJrV5sZ78E37kRILeAAwbpHY.TO6hIEwUyA7oSsmYtMNh8Vq', 'User 4', 'user4@example.com', '1234567892', 2,0),
    ('admin', '$2a$10$/fjstiJrV5sZ78E37kRILeAAwbpHY.TO6hIEwUyA7oSsmYtMNh8Vq', 'Admin', 'admin@example.com', '1234567892', 1,1);
    
INSERT INTO Product (`Name`, price, `Describe`, Sold,`Status`) VALUES
('Netflix', 50000, 'Netflix accounts include gmail and password', 10,1),

('Viettel', 20000, 'Viettel Card For Mobile Services', 2,1),
('Viettel', 500000, 'Viettel Card For Mobile Services', 0,1),
('Viettel', 100000, 'Viettel Card For Mobile Services', 0,1),
('Viettel', 200000, 'Viettel Card For Mobile Services', 0,1),
('Viettel', 500000, 'Viettel Card For Mobile Services', 0,1),

('Vinaphone', 20000, 'Vinaphone Card For Mobile Services', 0,1),
('Vinaphone', 500000, 'Vinaphone Card For Mobile Services', 0,1),
('Vinaphone', 100000, 'Vinaphone Card For Mobile Services', 0,1),
('Vinaphone', 200000, 'Vinaphone Card For Mobile Services', 0,1),
('Vinaphone', 500000, 'Vinaphone Card For Mobile Services', 0,1),

('Mobilephone', 20000, 'Mobilephone Card For Mobile Services', 0,1),
('Mobilephone', 500000, 'Mobilephone Card For Mobile Services', 0,1),
('Mobilephone', 100000, 'Mobilephone Card For Mobile Services', 0,1),
('Mobilephone', 200000, 'Mobilephone Card For Mobile Services', 0,1),
('Mobilephone', 500000, 'Mobilephone Card For Mobile Services', 0,1),

('Quizzlet', 10000, 'Quizzlet and Quizzlet Plus Account', 0,1);
INSERT INTO `phonecardstoredb`.`productdetail`(`Productid`,`Seri`,`Code`,`Status`)
VALUES
(2,'00000','123456',1),(2,'00001','123456',1),(2,'00002','123456',1),(2,'00003','123456',1),(2,'00004','123456',1),
(3,'10000','123456',1),(3,'10001','123456',1),(3,'10002','123456',1),(3,'10003','123456',1),(3,'10004','123456',1),
(4,'20000','123456',1),(4,'20001','123456',1),(4,'20002','123456',1),(4,'20003','123456',1),(4,'20004','123456',1),
(5,'30000','123456',1),(5,'30001','123456',1),(5,'30002','123456',1),(5,'30003','123456',1),(5,'30004','123456',1),
(6,'40000','123456',1),(6,'40001','123456',1),(6,'40002','123456',1),(6,'40003','123456',1),(6,'40004','123456',1),
(7,'50000','123456',1),(7,'50001','123456',1),(7,'50002','123456',1),(7,'50003','123456',1),(7,'50004','123456',1),
(8,'60000','123456',1),(8,'60001','123456',1),(8,'60002','123456',1),(8,'60003','123456',1),(8,'60004','123456',1),
(9,'70000','123456',1),(9,'70001','123456',1),(9,'70002','123456',1),(9,'70003','123456',1),(9,'70004','123456',1),
(10,'80000','123456',1),(10,'80001','123456',1),(10,'80002','123456',1),(10,'80003','123456',1),(10,'80004','123456',1),
(11,'90000','123456',1),(11,'90001','123456',1),(11,'90002','123456',1),(11,'90003','123456',1),(11,'90004','123456',1),
(12,'100000','123456',1),(12,'100001','123456',1),(12,'100002','123456',1),(12,'100003','123456',1),(12,'100004','123456',1),
(13,'110000','123456',1),(13,'110001','123456',1),(13,'110002','123456',1),(13,'110003','123456',1),(13,'110004','123456',1),
(14,'120000','123456',1),(14,'120001','123456',1),(14,'120002','123456',1),(14,'120003','123456',1),(14,'120004','123456',1),
(15,'130000','123456',1),(15,'130001','123456',1),(15,'130002','123456',1),(15,'130003','123456',1),(15,'130004','123456',1),
(16,'140000','123456',1),(16,'140001','123456',1),(16,'140002','123456',1),(16,'140003','123456',1),(16,'140004','123456',1);

INSERT INTO `phonecardstoredb`.`wallet`(`Accountid`,`Wallet`) VALUES (1,500000),(2,0),(3,0),(4,0);

INSERT INTO `phonecardstoredb`.`wallethistory` (`Walletid`,`Money`,`Describe`) VALUES (1,20000,'VNPAY'),(1,100000,'VNPAY'),(1,10000,'VNPAY'),(1,20000,'VNPAY'),(1,50000,'VNPAY'),(1,200000,'VNPAY'),(1,100000,'VNPAY');

INSERT INTO `phonecardstoredb`.`purchasehistory` (`Productid`,`price`,`Accountid`) VALUES(2,40000,1);

INSERT INTO `phonecardstoredb`.`purchasehistorydetail`
(`Purchasehistoryid`,`Seri`,`Code`)
VALUES
(1,'000010','123456'),(1,'000020','123456');

SELECT * from purchasehistory
                    WHERE Accountid = 1;

SELECT purchasehistory.*,Product.`Name` FROM purchasehistory
                    JOIN Product ON purchasehistory.productid = Product.ID
                    WHERE Product.`Name` like "%%" AND purchasehistory.Accountid = 1
                    order by purchasehistory.`Date` desc;
                    
Select * from Wallet where Accountid = 1;
select * from Wallethistory where walletid = 1 AND `describe` = 'VNPAY';
