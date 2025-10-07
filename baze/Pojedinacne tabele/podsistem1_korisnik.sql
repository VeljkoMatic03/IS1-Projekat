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
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-30  1:40:45
