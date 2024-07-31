
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
                       `point` int,
                       Roleid INT,
                       `Status` bool,
                       FOREIGN KEY (Roleid) REFERENCES Roles(ID)
);

-- Bảng Product để lưu trữ loại sản phẩm
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
CREATE TABLE cart_items (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            product_id INT NOT NULL,
                            user_id INT NOT NULL,
                            quantity INT NOT NULL,
                            FOREIGN KEY (product_id) REFERENCES Product(ID),
                            FOREIGN KEY (user_id) REFERENCES Users(ID)
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
                              `Describe` TEXT,
                              FOREIGN KEY (Walletid) REFERENCES Wallet(ID)

);
CREATE TABLE Blogs (
                       ID INT AUTO_INCREMENT PRIMARY KEY,
                       Name VARCHAR(255) NOT NULL,
                       Text TEXT NOT NULL,
                       image_url VARCHAR(255),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE Table Rewards(
	ID INT AUTO_INCREMENT PRIMARY KEY,
    `type` varchar(255),
    `value` double,
    Name varchar(255),
    percent double
    );
create table Rewardslist(
	ID INT AUTO_INCREMENT PRIMARY KEY,
    Accountid int,
	name varchar(255),
    Createdat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (Accountid) REFERENCES Users(ID)
);
-- insert
INSERT INTO `phonecardstoredb`.`roles` (`Name`)	VALUES('ADMIN'),('USER');
INSERT INTO Users (Username, Password, Fullname, Email, Phone, Roleid,`Status`,`point`)
VALUES
    ('quan', '$2a$10$/fjstiJrV5sZ78E37kRILeAAwbpHY.TO6hIEwUyA7oSsmYtMNh8Vq', 'Nguyen Tat Quan', 'tatquan1803@gmail.com', '0837931504', 2,0,0),
    ('quannt', '$2a$10$/fjstiJrV5sZ78E37kRILeAAwbpHY.TO6hIEwUyA7oSsmYtMNh8Vq', 'Quannt47', 'quannthe187203@fpt.edu.vn', '0829505619', 2,0,0),
    ('duc', '$2a$10$/fjstiJrV5sZ78E37kRILeAAwbpHY.TO6hIEwUyA7oSsmYtMNh8Vq', 'Minh Duc', 'duchmhe173344@fpt.edu.vn', '1234567892', 2,0,0),
    ('user4', '$2a$10$/fjstiJrV5sZ78E37kRILeAAwbpHY.TO6hIEwUyA7oSsmYtMNh8Vq', 'User 4', 'user4@example.com', '1234567892', 2,1,0),
    ('admin1', '123456', 'Quan1_Admin', 'quan1@example.com', '1234567892', 1,0,0),
    ('admin2', '123456', 'Quan2_Admin', 'quan2@example.com', '1234567892', 1,0,0),
    ('user', '$2a$10$/fjstiJrV5sZ78E37kRILeAAwbpHY.TO6hIEwUyA7oSsmYtMNh8Vq', 'Quannt', 'quan3@example.com', '0123456789', 2,0,0),
    ('admin', '$2a$10$/fjstiJrV5sZ78E37kRILeAAwbpHY.TO6hIEwUyA7oSsmYtMNh8Vq', 'Admin', 'admin@example.com', '1234567892', 1,0,1000);
INSERT INTO Product (Name,Image, price, `Describe`, Sold,`Status`) VALUES
('Quizzlet','1.jpg', 10000, 'Quizzlet and Quizzlet Plus Account', 0,1),
('Viettel','2.jpg', 20000, 'Viettel Card For Mobile Services', 2,1),
('Vinaphone','3.jpg', 20000, 'Vinaphone Card For Mobile Services', 0,1),
('Mobilephone','4.jpg', 20000, 'Mobilephone Card For Mobile Services', 0,1),
('Netflix','5.jpg', 50000, 'Netflix accounts include gmail and password', 10,1),
('Viettel','6.jpg', 100000, 'Viettel Card For Mobile Services', 0,1),
('Vinaphone','7.jpg', 100000, 'Vinaphone Card For Mobile Services', 0,1),
('Mobilephone','8.jpg', 100000, 'Mobilephone Card For Mobile Services', 0,1),
('Viettel','9.jpg', 200000, 'Viettel Card For Mobile Services', 0,1),
('Vinaphone','10.jpg', 200000, 'Vinaphone Card For Mobile Services', 0,1),
('Mobilephone','11.jpg', 200000, 'Mobilephone Card For Mobile Services', 0,1),
('Viettel','12.jpg', 500000, 'Viettel Card For Mobile Services', 0,1),
('Vinaphone','13.jpg', 500000, 'Vinaphone Card For Mobile Services', 0,1),
('Mobilephone','14.jpg', 500000, 'Mobilephone Card For Mobile Services', 0,1);

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
    (14,'120000','123456',1),(14,'120001','123456',1),(14,'120002','123456',1),(14,'120003','123456',1),(14,'120004','123456',1);
    
    
INSERT INTO `phonecardstoredb`.`wallet`(`Accountid`,`Wallet`) VALUES (1,500000),(2,0),(3,0),(4,0),(5,0),(6,0),(6,0),(7,0),(8,0);

INSERT INTO `phonecardstoredb`.`wallethistory` (`Walletid`,`Money`,`Describe`) VALUES (1,20000,'VNPAY'),(1,100000,'VNPAY'),(1,10000,'VNPAY'),(1,20000,'VNPAY'),(1,50000,'VNPAY'),(1,200000,'VNPAY'),(1,100000,'VNPAY');

INSERT INTO `phonecardstoredb`.`purchasehistory` (`Productid`,`price`,`Accountid`) VALUES(2,40000,1);

INSERT INTO `phonecardstoredb`.`purchasehistorydetail`
(`Purchasehistoryid`,`Seri`,`Code`)
VALUES
    (1,'000010','123456'),(1,'000020','123456');
    
INSERT INTO Blogs (Name, Text) 
VALUES 
('Discount 15%', 'Discount 15% for phone card'),
('Discount 30%', 'Discount 30% for phone card'),
('Discount 10%', 'Discount 10% for phone card');

-- SELECT * from purchasehistorydetail WHERE Accountid = 7;
--                     

-- SELECT purchasehistory.*,Product.`Name` FROM purchasehistory
--                     JOIN Product ON purchasehistory.productid = Product.ID
--                     WHERE Product.`Name` like "%%" AND purchasehistory.Accountid = 1
--                     order by purchasehistory.`Date` desc;
-- SELECT * FROM cart_items;
-- SELECT * FROM users;
-- SELECT * FROM product;
-- SELECT * FROM productdetail;
-- SELECT * FROM Wallet;
-- SELECT * FROM users u JOIN Wallet w WHERE u.id = 7 AND w.Accountid = 7;
-- SELECT COUNT(*) FROM ProductDetail p WHERE p.productid = 5  AND p.status = true;
-- SELECT * FROM ProductDetail pd WHERE pd.productid = 5 AND pd.status = true LIMIT 3;
-- SELECT w.wallet FROM Wallet w WHERE w.accountId = 7;
-- UPDATE Product p SET p.sold = 5 WHERE p.ID = 5;
-- SELECT p.id FROM Product p JOIN Productdetail pd ON p.id = pd.productid WHERE pd.id = 17

CREATE TABLE Discount (
                          ID INT AUTO_INCREMENT PRIMARY KEY,
                          UserID INT,
                          CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (UserID) REFERENCES Users(ID)
);

CREATE TABLE DiscountDetail (
                                ID INT AUTO_INCREMENT PRIMARY KEY,
                                DiscountID INT,
                                Quantity INT NOT NULL,
                                Value VARCHAR(10) NOT NULL,
                                `Status` BOOL,
                                FOREIGN KEY (DiscountID) REFERENCES Discount(ID)
);
ALTER TABLE DiscountDetail AUTO_INCREMENT = 1;


INSERT INTO Discount (UserID)
VALUES
    (1), -- ID 1
    (2), -- ID 2
    (5), -- ID 3
    (7); -- ID 4


INSERT INTO DiscountDetail (DiscountID, Quantity, Value, `Status`)
VALUES
    (1, 1, '10%', 1),
    (4, 5, '20%', 1),
    (1, 2, '30%', 1),
    (4, 4,'50%', 1),
    (3, 3, '70%', 1),
    (4, 6, '100%', 1);
-- SELECT d.Id FROM discount d where d.userId = 2 join discountdetail dd on dd.discountId = d.Id;
-- SELECT * FROM discountdetail dt join discount d on dt.discountid = d.id ;
-- SELECT d.id FROM Discount d where d.userId = 2;
-- SELECT * FROM Discountdetail d where d.discountid = 2;
-- SELECT * FROM discount d join discountdetail dd on d.id = dd.discountId order by d.userId asc;
-- SELECT * FROM discountdetail;

select * from purchasehistory;
select * from purchasehistorydetail;

INSERT INTO purchasehistory (`Productid`,`price`,`Accountid`,`Date`) VALUES(1,20000,3,'2024-05-21 09:12:12'),
(2,20000,2,'2024-06-21 09:12:12'),
(2,20000,1,'2024-06-21 09:12:12'),
(2,20000,3,'2024-06-21 09:12:12');

INSERT INTO purchasehistorydetail
(`Purchasehistoryid`,`Seri`,`Code`)
VALUES
    (2,'nghia@gmail.com','123456'),(2,'asdqi@lookout.com','123456'),
    (3,'912313','123456'),
    (4,'111123','123456'),
    (5,'123177','123456');
INSERT INTO Rewards (`type`, `value` ,Name , percent) 
VALUES 
('Voucher',10,'Voucher 10%',0),
('Voucher',30,'Voucher 30%',0),
('Voucher',50,'Voucher 50%',0),
('Money',0,'0 VND',100),
('Money',1000,'1000 VND',0),
('Money',50000,'50000 VND',0);


select * from Users;
select `point` from Users where ID = 1;
select * from wallet;
select sum(percent) from Rewards ;
select * from RewardsList where Accountid = 8
    