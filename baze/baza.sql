-- MySQL dump 10.13  Distrib 8.0.22, for Win64 (x86_64)
--
-- Host: localhost    Database: podsistem1
-- ------------------------------------------------------
-- Server version	8.0.22

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `korisnik`
--

DROP TABLE IF EXISTS `korisnik`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `korisnik` (
  `IdKor` int NOT NULL AUTO_INCREMENT,
  `Ime` varchar(255) NOT NULL,
  `Email` varchar(255) NOT NULL,
  `Godiste` int DEFAULT NULL,
  `Pol` varchar(45) DEFAULT NULL,
  `IdMes` int NOT NULL,
  PRIMARY KEY (`IdKor`),
  UNIQUE KEY `Email_UNIQUE` (`Email`),
  KEY `FK_IdMes_Korisnik_idx` (`IdMes`),
  CONSTRAINT `FK_IdMes_Korisnik` FOREIGN KEY (`IdMes`) REFERENCES `mesto` (`IdMes`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `korisnik`
--

LOCK TABLES `korisnik` WRITE;
/*!40000 ALTER TABLE `korisnik` DISABLE KEYS */;
INSERT INTO `korisnik` VALUES (1,'Veljko Matic','veljkomatic0@gmail.com',2003,'Muski',1),(2,'Petar Mihajlovic','cope@gmail.com',2003,'Muski',5),(3,'Cope','cope123@yahoo.com',2003,'Muski',2),(4,'Vuk','vuk@gmail.com',2003,'Muski',1),(5,'Jasna Matic','jasna@gmail.com',1977,'Zenski',1),(6,'LeBron James','legoat@gmail.com',1984,'Muski',7),(7,'Milos-Cerovic','cerovic@gmail.com',2004,'Muski',1),(8,'Nikola Cerovic','nidza.cerovic@gmail.com',2002,'Muski',1),(9,'The Weeknd','abel@spotify.com',1990,'Muski',8),(10,'Drake','drake@spotify.com',1987,'Muski',8),(11,'Frank Ocean','frank@spotify.com',1990,'Muski',8);
/*!40000 ALTER TABLE `korisnik` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mesto`
--

DROP TABLE IF EXISTS `mesto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mesto` (
  `IdMes` int NOT NULL AUTO_INCREMENT,
  `Naziv` varchar(255) NOT NULL,
  PRIMARY KEY (`IdMes`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mesto`
--

LOCK TABLES `mesto` WRITE;
/*!40000 ALTER TABLE `mesto` DISABLE KEYS */;
INSERT INTO `mesto` VALUES (1,'Cuprija'),(2,'Beograd'),(3,'Novi Sad'),(4,'Nis'),(5,'Kragujevac'),(6,'Jagodina'),(7,'Akron,Ohio'),(8,'Los Angeles,California'),(9,'Krusevac'),(10,'Pozarevac'),(11,'Paracin');
/*!40000 ALTER TABLE `mesto` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-30 13:40:05
-- MySQL dump 10.13  Distrib 8.0.22, for Win64 (x86_64)
--
-- Host: localhost    Database: podsistem2
-- ------------------------------------------------------
-- Server version	8.0.22

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `audiosnimak`
--

DROP TABLE IF EXISTS `audiosnimak`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `audiosnimak` (
  `IdAud` int NOT NULL AUTO_INCREMENT,
  `Naziv` varchar(255) NOT NULL,
  `Trajanje` int NOT NULL,
  `Datum` timestamp NOT NULL,
  `IdVlasnik` int NOT NULL,
  PRIMARY KEY (`IdAud`),
  KEY `FK_IdVlasnik_AudioSnimak_idx` (`IdVlasnik`),
  CONSTRAINT `FK_IdVlasnik_AudioSnimak` FOREIGN KEY (`IdVlasnik`) REFERENCES `podsistem1`.`korisnik` (`IdKor`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audiosnimak`
--

LOCK TABLES `audiosnimak` WRITE;
/*!40000 ALTER TABLE `audiosnimak` DISABLE KEYS */;
INSERT INTO `audiosnimak` VALUES (5,'White Ferrari',204,'2024-12-18 21:27:53',11),(6,'Tralala',204,'2024-12-20 12:34:46',2),(8,'Timeless',306,'2025-04-01 14:25:31',9),(9,'RaceMyMind',348,'2025-07-15 01:09:42',10),(10,'Tralalero tralala',348,'2025-07-15 11:44:42',2);
/*!40000 ALTER TABLE `audiosnimak` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kategorija`
--

DROP TABLE IF EXISTS `kategorija`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kategorija` (
  `IdKat` int NOT NULL AUTO_INCREMENT,
  `Naziv` varchar(255) NOT NULL,
  PRIMARY KEY (`IdKat`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kategorija`
--

LOCK TABLES `kategorija` WRITE;
/*!40000 ALTER TABLE `kategorija` DISABLE KEYS */;
INSERT INTO `kategorija` VALUES (1,'drama'),(2,'horor'),(3,'pop'),(4,'rnb'),(5,'hiphop'),(6,'folk'),(7,'turbofolk'),(8,'indie');
/*!40000 ALTER TABLE `kategorija` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pripada`
--

DROP TABLE IF EXISTS `pripada`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pripada` (
  `IdPri` int NOT NULL AUTO_INCREMENT,
  `IdKat` int NOT NULL,
  `IdAud` int NOT NULL,
  PRIMARY KEY (`IdPri`),
  KEY `FK_IdKat_Pripada_idx` (`IdKat`),
  KEY `FK_IdAud_Pripada_idx` (`IdAud`),
  CONSTRAINT `FK_IdAud_Pripada` FOREIGN KEY (`IdAud`) REFERENCES `audiosnimak` (`IdAud`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_IdKat_Pripada` FOREIGN KEY (`IdKat`) REFERENCES `kategorija` (`IdKat`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pripada`
--

LOCK TABLES `pripada` WRITE;
/*!40000 ALTER TABLE `pripada` DISABLE KEYS */;
INSERT INTO `pripada` VALUES (3,4,5),(8,5,8),(9,5,5),(10,3,5),(11,8,5),(12,5,9);
/*!40000 ALTER TABLE `pripada` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-30 13:40:05
-- MySQL dump 10.13  Distrib 8.0.22, for Win64 (x86_64)
--
-- Host: localhost    Database: podsistem3
-- ------------------------------------------------------
-- Server version	8.0.22

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `favouritelist`
--

DROP TABLE IF EXISTS `favouritelist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `favouritelist` (
  `IdKor` int NOT NULL,
  `IdAud` int NOT NULL,
  PRIMARY KEY (`IdKor`,`IdAud`),
  KEY `FK_IdAud_FavouriteList_idx` (`IdAud`),
  CONSTRAINT `FK_IdAud_FavouriteList` FOREIGN KEY (`IdAud`) REFERENCES `podsistem2`.`audiosnimak` (`IdAud`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_IdKor_FavouriteList` FOREIGN KEY (`IdKor`) REFERENCES `podsistem1`.`korisnik` (`IdKor`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `favouritelist`
--

LOCK TABLES `favouritelist` WRITE;
/*!40000 ALTER TABLE `favouritelist` DISABLE KEYS */;
INSERT INTO `favouritelist` VALUES (1,5),(4,5),(1,8),(5,8),(1,9),(7,9);
/*!40000 ALTER TABLE `favouritelist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ocena`
--

DROP TABLE IF EXISTS `ocena`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ocena` (
  `IdOce` int NOT NULL AUTO_INCREMENT,
  `Ocena` int NOT NULL,
  `Datum` timestamp NOT NULL,
  `IdKor` int NOT NULL,
  `IdAud` int NOT NULL,
  PRIMARY KEY (`IdOce`),
  KEY `FK_IdKor_Ocena_idx` (`IdKor`),
  KEY `FK_IdAud_Ocena_idx` (`IdAud`),
  CONSTRAINT `FK_IdAud_Ocena` FOREIGN KEY (`IdAud`) REFERENCES `podsistem2`.`audiosnimak` (`IdAud`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_IdKor_Ocena` FOREIGN KEY (`IdKor`) REFERENCES `podsistem1`.`korisnik` (`IdKor`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ocena`
--

LOCK TABLES `ocena` WRITE;
/*!40000 ALTER TABLE `ocena` DISABLE KEYS */;
INSERT INTO `ocena` VALUES (2,10,'2024-12-20 13:20:37',1,5),(3,10,'2024-12-20 14:53:19',1,6),(4,9,'2024-12-20 14:53:29',4,6),(6,10,'2024-12-20 14:53:51',3,6),(7,7,'2025-07-16 00:48:12',1,8);
/*!40000 ALTER TABLE `ocena` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paket`
--

DROP TABLE IF EXISTS `paket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `paket` (
  `IdPak` int NOT NULL AUTO_INCREMENT,
  `Naziv` varchar(255) NOT NULL,
  `Cena` float NOT NULL,
  PRIMARY KEY (`IdPak`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paket`
--

LOCK TABLES `paket` WRITE;
/*!40000 ALTER TABLE `paket` DISABLE KEYS */;
INSERT INTO `paket` VALUES (5,'premium',1200),(6,'family',2000),(7,'deluxe',2400),(8,'student',600),(9,'duo',1500);
/*!40000 ALTER TABLE `paket` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pretplata`
--

DROP TABLE IF EXISTS `pretplata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pretplata` (
  `IdPre` int NOT NULL AUTO_INCREMENT,
  `Cena` float NOT NULL,
  `DatumPocetka` timestamp NOT NULL,
  `IdPak` int NOT NULL,
  `IdKor` int NOT NULL,
  PRIMARY KEY (`IdPre`),
  KEY `FK_IdPak_Pretplata_idx` (`IdPak`),
  KEY `FK_IdKor_Pretplata_idx` (`IdKor`),
  CONSTRAINT `FK_IdKor_Pretplata` FOREIGN KEY (`IdKor`) REFERENCES `podsistem1`.`korisnik` (`IdKor`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_IdPak_Pretplata` FOREIGN KEY (`IdPak`) REFERENCES `paket` (`IdPak`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pretplata`
--

LOCK TABLES `pretplata` WRITE;
/*!40000 ALTER TABLE `pretplata` DISABLE KEYS */;
INSERT INTO `pretplata` VALUES (8,2000,'2024-11-14 23:00:00',6,2),(9,2400,'2022-01-14 23:00:00',7,2),(10,2400,'2024-12-20 14:34:11',7,2),(11,1500,'2025-07-16 00:19:37',9,1),(12,1500,'2025-08-17 22:00:00',9,7),(13,1250,'2025-09-17 22:00:00',7,7);
/*!40000 ALTER TABLE `pretplata` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `slusanje`
--

DROP TABLE IF EXISTS `slusanje`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `slusanje` (
  `IdSlu` int NOT NULL AUTO_INCREMENT,
  `Datum` timestamp NOT NULL,
  `PocetakSek` int DEFAULT NULL,
  `VremeSlusanja` int DEFAULT NULL,
  `IdKor` int NOT NULL,
  `IdAud` int NOT NULL,
  PRIMARY KEY (`IdSlu`),
  KEY `FK_IdAud_Slusanje_idx` (`IdAud`),
  KEY `FK_IdKor_Slusanje_idx` (`IdKor`),
  CONSTRAINT `FK_IdAud_Slusanje` FOREIGN KEY (`IdAud`) REFERENCES `podsistem2`.`audiosnimak` (`IdAud`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_IdKor_Slusanje` FOREIGN KEY (`IdKor`) REFERENCES `podsistem1`.`korisnik` (`IdKor`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `slusanje`
--

LOCK TABLES `slusanje` WRITE;
/*!40000 ALTER TABLE `slusanje` DISABLE KEYS */;
INSERT INTO `slusanje` VALUES (1,'2024-12-20 12:28:38',0,200,2,5),(2,'2024-12-20 12:29:09',15,100,2,5),(3,'2024-12-20 12:29:35',15,100,1,5),(4,'2024-12-20 12:35:06',15,100,4,6),(5,'2024-12-20 12:45:35',15,100,3,6),(6,'2025-07-16 00:53:07',0,347,5,9);
/*!40000 ALTER TABLE `slusanje` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-30 13:40:05
