-- MySQL dump 10.13  Distrib 8.0.28, for Win64 (x86_64)
--
-- Host: localhost    Database: test_db
-- ------------------------------------------------------
-- Server version	8.0.28

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
-- Table structure for table `authors`
--

DROP TABLE IF EXISTS `authors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `authors` (
  `authorID` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `lastName` varchar(45) NOT NULL,
  PRIMARY KEY (`authorID`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authors`
--

LOCK TABLES `authors` WRITE;
/*!40000 ALTER TABLE `authors` DISABLE KEYS */;
INSERT INTO `authors` VALUES (1,'Erich','Maria Remarque'),(2,'Stendhal',''),(3,'Emil','Zola'),(4,'Oscar','Wilde'),(5,'Guy','de Maupassant'),(6,'Coleen','McCullough'),(7,'Arthur','Conan Doyle'),(8,'Agatha','Christie'),(9,'John','Galsworthy'),(10,'Margaret','Mitchell'),(11,'Louisa','May Alcott'),(12,'Arthur','Haley'),(13,'Jane','Austen'),(14,'Ian','McEwan'),(15,'Emily','Bronte'),(16,'Gabriel','Garcia Marquez'),(17,'Andre','Maurois'),(18,'Somerset','Maugham'),(19,'Francis','Scott Key Fitzgerald'),(20,'Jack','Paris'),(21,'Harper','Lee'),(22,'Ernest','Miller Hemingway');
/*!40000 ALTER TABLE `authors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `books`
--

DROP TABLE IF EXISTS `books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `books` (
  `bookID` int NOT NULL AUTO_INCREMENT,
  `book_name` varchar(80) NOT NULL,
  `book_authorID` int NOT NULL,
  `book_publish_year` varchar(4) NOT NULL,
  `book_in_stock` tinyint NOT NULL DEFAULT '1',
  `book_count` int NOT NULL,
  `book_ISBN` varchar(45) NOT NULL,
  PRIMARY KEY (`bookID`),
  UNIQUE KEY `book_cipher_UNIQUE` (`book_ISBN`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books`
--

LOCK TABLES `books` WRITE;
/*!40000 ALTER TABLE `books` DISABLE KEYS */;
INSERT INTO `books` VALUES (1,'The Night in Lisbon',1,'1961',1,2,'TN1'),(2,'The Red and The Black',2,'1830',1,2,'TR2'),(3,'Three Komrades',1,'1947',1,2,'TK3'),(4,'Arch of Triumph',1,'1946',1,3,'Ao4'),(5,'All Quiet on the Western Front',1,'1928',1,3,'AQ5'),(11,'Geborgtes Leben',1,'1959',1,2,'GL11'),(12,'Au Bonheur des Dames',3,'1883',1,2,'AB12'),(14,'The thorn birds',6,'1977',0,0,'Tt14'),(16,'The Sign of the Four',7,'1890',1,1,'TS16'),(17,'The Adventures of Sherlock Holmes',7,'1892',1,3,'TA17'),(18,'The Hound of the Baskervilles',7,'1902',1,2,'TH18'),(19,'Gone with the Wind',10,'1936',1,2,'Gw19'),(20,'Little women',11,'1868',1,2,'Lw20'),(21,'Airport',12,'1968',1,1,'Ai21'),(22,'Pride and prejudice',13,'1797',1,1,'Pa22'),(23,'The Atonement',14,'2001',1,2,'TA23'),(24,'Wuthering Heights',15,'1847',1,4,'WH24'),(25,'One hundred years of solitude',16,'1967',1,2,'Oh25'),(26,'Lettres a l`Inconnue',17,'1953',1,2,'La26'),(27,'Bel ami',5,'1838',1,3,'Ba27'),(28,'The Picture of Dorian Gray',4,'1890',1,3,'TP28'),(29,'Theatre',18,'1937',1,4,'Th29'),(30,'The Great Gatsby',19,'1925',1,4,'TG30'),(31,'Martin Iden',20,'1909',1,3,'MI31'),(32,'To Kill a Mockingbird',21,'1960',1,3,'TK32'),(33,'A Farewell to Arms',22,'1929',1,2,'AF36'),(35,'Canterville`s ghost',4,'1889',1,1,'Cg35'),(37,'Death on Nile',8,'1937',1,2,'Do37'),(38,'The Forsyte saga',9,'1922',1,2,'TF38');
/*!40000 ALTER TABLE `books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `data`
--

DROP TABLE IF EXISTS `data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `data` (
  `3` int DEFAULT NULL,
  `Coleen` text,
  `McCullough` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `data`
--

LOCK TABLES `data` WRITE;
/*!40000 ALTER TABLE `data` DISABLE KEYS */;
INSERT INTO `data` VALUES (4,'Oscar','Wilde'),(5,'Guy','de Maupassant');
/*!40000 ALTER TABLE `data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `leasing`
--

DROP TABLE IF EXISTS `leasing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `leasing` (
  `leaseID` int NOT NULL AUTO_INCREMENT,
  `lease_readerID` int NOT NULL,
  `lease_bookID` int NOT NULL,
  `lease_start_date` varchar(15) NOT NULL,
  `lease_finish_date` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`leaseID`)
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `leasing`
--

LOCK TABLES `leasing` WRITE;
/*!40000 ALTER TABLE `leasing` DISABLE KEYS */;
INSERT INTO `leasing` VALUES (61,1,3,'24-05-2022',NULL),(62,16,14,'24-05-2022','02-06-2022'),(65,1,10,'29-05-2022','29-05-2022'),(66,6,18,'29-05-2022',NULL),(69,23,3,'29-05-2022','02-06-2022'),(72,6,2,'30-05-2022',NULL),(73,28,33,'30-05-2022',NULL),(77,29,19,'31-05-2022',NULL),(78,37,21,'31-05-2022','31-05-2022'),(79,24,35,'02-06-2022',NULL),(80,35,14,'02-06-2022',NULL),(81,8,22,'02-06-2022',NULL),(82,8,14,'02-06-2022',NULL),(83,22,31,'05-06-2022','05-06-2022');
/*!40000 ALTER TABLE `leasing` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `readers`
--

DROP TABLE IF EXISTS `readers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `readers` (
  `readerID` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `lastName` varchar(45) NOT NULL,
  `reader_doc` int NOT NULL,
  PRIMARY KEY (`readerID`),
  UNIQUE KEY `reader_doc_UNIQUE` (`reader_doc`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `readers`
--

LOCK TABLES `readers` WRITE;
/*!40000 ALTER TABLE `readers` DISABLE KEYS */;
INSERT INTO `readers` VALUES (1,'Martin','Iden',123),(4,'Julien','Sorel',325),(5,'Louise','de Renale',532),(6,'Mathilde','de La Mole',163),(8,'Meggie','Cleary',25),(9,'Dorian','Gray',441),(10,'Joan','Madou',75),(12,'Ralph','de Bricassar',251),(15,'Luke','O`Neil',853),(16,'Julia','Lambert',328),(22,'Patricia','Holman',851),(23,'Robert','Lokamp',344),(24,'Gotfrid','Lenz',111),(27,'Georges','Duroy',751),(28,'Jay','Gatsby',372),(29,'Daisy','Buchanan',762),(30,'Otto','Kester',875),(31,'Ruth','Morse',653),(32,'Jean','Louise Finch',743),(33,'Dane','O`Neil',632),(34,'Justine','O`Neil',733),(35,'Rainer','Moerling Hartheim',864),(37,'Stepan','Shvets',1016);
/*!40000 ALTER TABLE `readers` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-06-05 16:22:52
