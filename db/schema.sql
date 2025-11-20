-- Database setup for Car Dealership Project
CREATE DATABASE IF NOT EXISTS cars_db;
USE cars_db;

DROP TABLE IF EXISTS `cars`;

CREATE TABLE `cars` (
  `car_id` int NOT NULL AUTO_INCREMENT,
  `make` varchar(50) NOT NULL,
  `model` varchar(50) NOT NULL,
  `year` int DEFAULT NULL,
  `car_condition` varchar(50) NOT NULL,
  `price` int DEFAULT NULL,
  `picture_path` varchar(255) DEFAULT NULL,
  `VIN` varchar(17) DEFAULT NULL,
  `Mileage` int NOT NULL,
  PRIMARY KEY (`car_id`),
  UNIQUE KEY `VIN` (`VIN`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Insert sample data
INSERT INTO `cars` (`car_id`, `make`, `model`, `year`, `car_condition`, `price`, `picture_path`, `VIN`, `Mileage`) VALUES
(1,'Subaru','BRZ',2018,'used',30000,'images/2018_subaru_brz.jpeg','1HGCM82633A004352',32000),
(2,'Honda','Accord 2.0t',2021,'used',25000,'images/2021_honda_accord20t.jpeg','2T1BR32E54C917284',18000),
(4,'Chevrolet','Silverado',2021,'used',25000,'images/2021_chevy_silverado.jpeg','4T1BE46K07U739521',72000),
(5,'Chevrolet','Corvette',2006,'used',52000,'images/2006_chevy_vette.jpeg','5NPEB4AC9CH278461',1200),
(6,'Ford','Mustang GT',2025,'new',48000,'images/2025_mustang_gt.jpeg','1C4RJEBG8FC739204',165000),
(7,'Toyota','Camry',1993,'used',4200,'images/1993_toyota_camry.jpeg','1FTFW1E55AFB39216',88000),
(8,'Toyota','Corolla',2014,'used',5000,'images/2014_toyota_corolla.jpeg','2C3CDZAG7EH219374',0),
(9,'Chevrolet','Camaro ZL1',2025,'new',106000,'images/2025_chevy_camaroZL1.jpeg','3GNEC16T51G241985',500),
(10,'Audi','R8 V10+',2012,'used',138000,'images/2012_audi_r8V10+.jpeg','5FNYF4H98FB032871',15000),
(11,'Audi','R8 V8',2015,'used',100000,'images/audi_r8V8.jpeg','JN1AZ4EH7DM894251',28000),
(12,'Nissan','R32 GTR',1995,'used',78500,'images/1995_nissan_r32.jpeg','JHMGE8G30DC041572',95000),
(13,'Nissan','Sentra',2012,'used',6500,'images/2012_nissan_sentra.jpeg','SALVP2BG0EH938421',74000),
(14,'Mitsubishi','Lancer Evolution FE',2015,'used',32000,'images/2018_evox.jpeg','WAUGFAFC6DN037251',62000),
(15,'Jeep','Wrangler',1978,'used',5500,'images/1978_CJ5.jpeg','WBA3A9C57DF479825',85000),
(16,'Jeep','Gladiator',2021,'used',43200,'images/2021_jeep_gladiator.jpeg','YS3FD49Y671027351',35000),
(17,'Honda','Civic Type R',2021,'used',43200,'images/2021_civic_typer.jpeg','ZFF65TJA0E0194827',22000);
