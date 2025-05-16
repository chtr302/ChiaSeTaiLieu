CREATE DATABASE  IF NOT EXISTS `ChiaSeTaiLieu` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `ChiaSeTaiLieu`;
-- MySQL dump 10.13  Distrib 8.0.42, for macos15 (arm64)
--
-- Host: localhost    Database: ChiaSeTaiLieu
-- ------------------------------------------------------
-- Server version	8.0.42

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
-- Table structure for table `BaoCao`
--

DROP TABLE IF EXISTS `BaoCao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `BaoCao` (
  `MaBaoCao` int NOT NULL AUTO_INCREMENT,
  `NoiDungBaoCao` varchar(45) NOT NULL,
  `NgayBaoCao` date NOT NULL,
  `TrangThai` bit(1) NOT NULL DEFAULT b'0',
  `MaTaiLieu` int NOT NULL,
  `MaSinhVien` varchar(10) NOT NULL,
  PRIMARY KEY (`MaBaoCao`),
  KEY `FK_BaoCao_TaiLieu_idx` (`MaTaiLieu`),
  KEY `FK_BaoCao_SinhVien_idx` (`MaSinhVien`),
  CONSTRAINT `FK_BaoCao_SinhVien` FOREIGN KEY (`MaSinhVien`) REFERENCES `SinhVien` (`MaSinhVien`) ON DELETE CASCADE,
  CONSTRAINT `FK_BaoCao_TaiLieu` FOREIGN KEY (`MaTaiLieu`) REFERENCES `TaiLieu` (`MaTaiLieu`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `BinhLuan`
--

DROP TABLE IF EXISTS `BinhLuan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `BinhLuan` (
  `MaBinhLuan` int NOT NULL AUTO_INCREMENT,
  `NoiDung` text NOT NULL,
  `NgayBinhLuan` date NOT NULL,
  `MaTaiLieu` int NOT NULL,
  `MaSinhVien` varchar(10) NOT NULL,
  PRIMARY KEY (`MaBinhLuan`),
  KEY `FK_BinhLuan_TaiLieu_idx` (`MaTaiLieu`),
  KEY `FK_BinhLuan_SinhVien_idx` (`MaSinhVien`),
  CONSTRAINT `FK_BinhLuan_SinhVien` FOREIGN KEY (`MaSinhVien`) REFERENCES `SinhVien` (`MaSinhVien`) ON DELETE CASCADE,
  CONSTRAINT `FK_BinhLuan_TaiLieu` FOREIGN KEY (`MaTaiLieu`) REFERENCES `TaiLieu` (`MaTaiLieu`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `File`
--

DROP TABLE IF EXISTS `File`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `File` (
  `MaFile` int NOT NULL AUTO_INCREMENT,
  `TenFile` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `DuongDanFile` varchar(100) DEFAULT NULL,
  `DinhDang` varchar(10) DEFAULT NULL,
  `KichThuoc` bigint DEFAULT NULL,
  `NgayTaiLen` date DEFAULT NULL,
  `Thumbnail` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`MaFile`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `LoaiTaiLieu`
--

DROP TABLE IF EXISTS `LoaiTaiLieu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `LoaiTaiLieu` (
  `MaLoai` varchar(10) NOT NULL,
  `TenLoai` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`MaLoai`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MonHoc`
--

DROP TABLE IF EXISTS `MonHoc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MonHoc` (
  `MaMon` varchar(15) NOT NULL,
  `TenMon` varchar(45) NOT NULL,
  PRIMARY KEY (`MaMon`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SinhVien`
--

DROP TABLE IF EXISTS `SinhVien`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SinhVien` (
  `MaSinhVien` varchar(10) NOT NULL,
  `HoVaTen` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `Email` varchar(50) NOT NULL,
  `HinhAnh` varchar(200) NOT NULL,
  `NgayTao` date NOT NULL,
  `LastLogin` date NOT NULL,
  PRIMARY KEY (`MaSinhVien`),
  UNIQUE KEY `Email_UNIQUE` (`Email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TaiLieu`
--

DROP TABLE IF EXISTS `TaiLieu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TaiLieu` (
  `MaTaiLieu` int NOT NULL AUTO_INCREMENT,
  `TieuDe` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `MoTa` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `LuotXem` int DEFAULT '0',
  `NgayDang` date NOT NULL,
  `UpVote` int DEFAULT '0',
  `DownVote` int DEFAULT '0',
  `Tags` varchar(100) DEFAULT NULL,
  `MonHoc` varchar(15) NOT NULL,
  `MaLoai` varchar(10) NOT NULL,
  `MaSinhVien` varchar(10) NOT NULL,
  `MaFile` int NOT NULL,
  PRIMARY KEY (`MaTaiLieu`),
  KEY `FK_TaiLieu_File_idx` (`MaFile`),
  KEY `FK_Tailieu_LoaiTaiLieu_idx` (`MaLoai`),
  KEY `FK_TaiLieu_MonHoc_idx` (`MonHoc`),
  KEY `FK_TaiLieu_SinhVien_idx` (`MaSinhVien`),
  CONSTRAINT `FK_TaiLieu_File` FOREIGN KEY (`MaFile`) REFERENCES `File` (`MaFile`) ON UPDATE CASCADE,
  CONSTRAINT `FK_Tailieu_LoaiTaiLieu` FOREIGN KEY (`MaLoai`) REFERENCES `LoaiTaiLieu` (`MaLoai`) ON UPDATE CASCADE,
  CONSTRAINT `FK_TaiLieu_MonHoc` FOREIGN KEY (`MonHoc`) REFERENCES `MonHoc` (`MaMon`) ON UPDATE CASCADE,
  CONSTRAINT `FK_TaiLieu_SinhVien` FOREIGN KEY (`MaSinhVien`) REFERENCES `SinhVien` (`MaSinhVien`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TaiLieu_TuKhoa`
--

DROP TABLE IF EXISTS `TaiLieu_TuKhoa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TaiLieu_TuKhoa` (
  `MaTaiLieu` int NOT NULL,
  `MaTuKhoa` int NOT NULL,
  PRIMARY KEY (`MaTaiLieu`,`MaTuKhoa`),
  KEY `FK_TuKhoa_idx` (`MaTuKhoa`),
  CONSTRAINT `FK_TaiLieu` FOREIGN KEY (`MaTaiLieu`) REFERENCES `TaiLieu` (`MaTaiLieu`) ON UPDATE CASCADE,
  CONSTRAINT `FK_TuKhoa` FOREIGN KEY (`MaTuKhoa`) REFERENCES `TuKhoa` (`MaTuKhoa`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TuKhoa`
--

DROP TABLE IF EXISTS `TuKhoa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TuKhoa` (
  `MaTuKhoa` int NOT NULL AUTO_INCREMENT,
  `NoiDung` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`MaTuKhoa`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `vw_TaiLieu`
--

DROP TABLE IF EXISTS `vw_TaiLieu`;
/*!50001 DROP VIEW IF EXISTS `vw_TaiLieu`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `vw_TaiLieu` AS SELECT 
 1 AS `TieuDe`,
 1 AS `LuotXem`,
 1 AS `TenMon`,
 1 AS `TenLoai`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `vw_TaiLieu`
--

/*!50001 DROP VIEW IF EXISTS `vw_TaiLieu`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `vw_TaiLieu` AS select `tl`.`TieuDe` AS `TieuDe`,`tl`.`LuotXem` AS `LuotXem`,`mh`.`TenMon` AS `TenMon`,`l`.`TenLoai` AS `TenLoai` from ((`TaiLieu` `tl` left join `MonHoc` `mh` on((`tl`.`MonHoc` = `mh`.`MaMon`))) left join `LoaiTaiLieu` `l` on((`tl`.`MaLoai` = `l`.`MaLoai`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-25 22:13:22
