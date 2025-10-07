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
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-30  1:40:46
