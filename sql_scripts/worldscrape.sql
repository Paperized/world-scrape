-- MySQL dump 10.13  Distrib 8.0.33, for Linux (x86_64)
--
-- Host: localhost    Database: worldscrape
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (25);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` bigint NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uc_role_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (2,'ROLE_ADMIN'),(1,'ROLE_USER');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scraper_file_configurations`
--

DROP TABLE IF EXISTS `scraper_file_configurations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `scraper_file_configurations` (
  `id` bigint NOT NULL,
  `configuration_url` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `policy` varchar(255) NOT NULL,
  `url_style` varchar(255) NOT NULL,
  `created_by_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKewo8nxdnp9mr6su681bhn2yj5` (`created_by_id`),
  CONSTRAINT `FKewo8nxdnp9mr6su681bhn2yj5` FOREIGN KEY (`created_by_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scraper_file_configurations`
--

LOCK TABLES `scraper_file_configurations` WRITE;
/*!40000 ALTER TABLE `scraper_file_configurations` DISABLE KEYS */;
INSERT INTO `scraper_file_configurations` VALUES (1,'https://s3.eu-central-1.amazonaws.com/world-scrape/1.yaml',NULL,'Trip Advisor Hotel','PUBLIC','https://www.tripadvisor.it/Hotel_%continue_url%',1),(2,'https://s3.eu-central-1.amazonaws.com/world-scrape/2.yaml',NULL,'Trip Advisor Restaurant','PUBLIC','https://www.tripadvisor.com/Restaurant_%continue_url%',1),(3,'https://s3.eu-central-1.amazonaws.com/world-scrape/3.yaml',NULL,'Booking.com Hotel','PUBLIC','https://www.booking.com/hotel/it/%continue_url%',1);
/*!40000 ALTER TABLE `scraper_file_configurations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scraper_file_parameters`
--

DROP TABLE IF EXISTS `scraper_file_parameters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `scraper_file_parameters` (
  `id` bigint NOT NULL,
  `default_value` varchar(255) DEFAULT NULL,
  `file_param_type` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `file_configuration_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnbx22v68hjaqoo94gms064k9g` (`file_configuration_id`),
  CONSTRAINT `FKnbx22v68hjaqoo94gms064k9g` FOREIGN KEY (`file_configuration_id`) REFERENCES `scraper_file_configurations` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scraper_file_parameters`
--

LOCK TABLES `scraper_file_parameters` WRITE;
/*!40000 ALTER TABLE `scraper_file_parameters` DISABLE KEYS */;
INSERT INTO `scraper_file_parameters` VALUES (1,'5','number','reviews_count',1),(2,'10','number','images_count',1),(3,'false','bool','upload_images',1),(4,'10','number','reviews_count',2),(5,'10','number','images_count',2),(6,'false','bool','upload_images',2),(7,'10','number','reviews_count',3),(8,'10','number','images_count',3),(9,'false','bool','upload_images',3);
/*!40000 ALTER TABLE `scraper_file_parameters` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_roles` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  KEY `fk_userroles_role_id` (`role_id`),
  KEY `fk_userroles_user_id` (`user_id`),
  CONSTRAINT `fk_userroles_role_id` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `fk_userroles_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
INSERT INTO `user_roles` VALUES (1,2),(1,2),(1,2),(1,2),(1,2);
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL,
  `creation_time` datetime(6) NOT NULL,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `is_enabled` bit(1) NOT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(16) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uc_user_username` (`username`),
  UNIQUE KEY `uc_user_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'2023-06-12 11:29:37.000000','admin@worldscrape.com',NULL,_binary '',NULL,'$2a$10$QYQs0DIa5LHl23zzuzIbOu5R/86H3PGW9l6mbC2GncTWEFsdDuTZi','WorldScrape');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-06-13 18:48:37
