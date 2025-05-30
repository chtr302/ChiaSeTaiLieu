CREATE DATABASE  IF NOT EXISTS `chiasetailieu` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `chiasetailieu`;
-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: chiasetailieu
-- ------------------------------------------------------
-- Server version	8.0.41

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
-- Table structure for table `baocao`
--

DROP TABLE IF EXISTS `baocao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `baocao` (
  `MaBaoCao` int NOT NULL AUTO_INCREMENT,
  `NoiDungBaoCao` varchar(45) NOT NULL,
  `NgayBaoCao` date NOT NULL,
  `TrangThai` bit(1) NOT NULL DEFAULT b'0',
  `MaTaiLieu` int NOT NULL,
  `MaSinhVien` varchar(10) NOT NULL,
  `TieuDeBaoCao` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`MaBaoCao`),
  KEY `FK_BaoCao_TaiLieu_idx` (`MaTaiLieu`),
  KEY `FK_BaoCao_SinhVien_idx` (`MaSinhVien`),
  CONSTRAINT `FK_BaoCao_SinhVien` FOREIGN KEY (`MaSinhVien`) REFERENCES `sinhvien` (`MaSinhVien`) ON DELETE CASCADE,
  CONSTRAINT `FK_BaoCao_TaiLieu` FOREIGN KEY (`MaTaiLieu`) REFERENCES `tailieu` (`MaTaiLieu`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `baocao`
--

LOCK TABLES `baocao` WRITE;
/*!40000 ALTER TABLE `baocao` DISABLE KEYS */;
INSERT INTO `baocao` VALUES (1,'Bình luận ID #1: aaa','2025-05-17',_binary '\0',6,'n22dcvt099','Báo cáo bình luận: harassment'),(2,'Bình luận ID #1: aaaa','2025-05-18',_binary '\0',6,'n22dcvt099','Báo cáo bình luận: harassment');
/*!40000 ALTER TABLE `baocao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `binhluan`
--

DROP TABLE IF EXISTS `binhluan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `binhluan` (
  `MaBinhLuan` int NOT NULL AUTO_INCREMENT,
  `NoiDung` text NOT NULL,
  `NgayBinhLuan` date NOT NULL,
  `MaTaiLieu` int NOT NULL,
  `MaSinhVien` varchar(10) NOT NULL,
  PRIMARY KEY (`MaBinhLuan`),
  KEY `FK_BinhLuan_TaiLieu_idx` (`MaTaiLieu`),
  KEY `FK_BinhLuan_SinhVien_idx` (`MaSinhVien`),
  CONSTRAINT `FK_BinhLuan_SinhVien` FOREIGN KEY (`MaSinhVien`) REFERENCES `sinhvien` (`MaSinhVien`) ON DELETE CASCADE,
  CONSTRAINT `FK_BinhLuan_TaiLieu` FOREIGN KEY (`MaTaiLieu`) REFERENCES `tailieu` (`MaTaiLieu`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `binhluan`
--

LOCK TABLES `binhluan` WRITE;
/*!40000 ALTER TABLE `binhluan` DISABLE KEYS */;
INSERT INTO `binhluan` VALUES (1,'xin chào','2025-05-15',6,'n22dcvt099');
/*!40000 ALTER TABLE `binhluan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file`
--

DROP TABLE IF EXISTS `file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `file` (
  `MaFile` int NOT NULL AUTO_INCREMENT,
  `TenFile` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `DuongDanFile` varchar(100) DEFAULT NULL,
  `DinhDang` varchar(10) DEFAULT NULL,
  `KichThuoc` bigint DEFAULT NULL,
  `NgayTaiLen` date DEFAULT NULL,
  `Thumbnail` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`MaFile`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file`
--

LOCK TABLES `file` WRITE;
/*!40000 ALTER TABLE `file` DISABLE KEYS */;
INSERT INTO `file` VALUES (7,'UML DIAGRAM ARCHITECT.pdf','e5993d64-9259-4d9d-a578-9d1e66a8b08e.pdf','pdf',99216,'2025-05-13','44e03081-5765-461b-ac63-ddbdd2639cdc.png'),(8,'TTNT 02.pdf','be70b142-8529-4c46-ba75-21bdb76c8d55.pdf','pdf',896945,'2025-05-14','a04d69c2-6b4d-4762-afd5-62e5bc7b2741.png'),(9,'UML DIAGRAM ARCHITECT.pdf','12747ca4-5890-47fd-8448-64807cabb43a.pdf','pdf',99216,'2025-05-14','54298334-c1fb-4943-9b6a-8e415b9d6d37.png'),(10,'INTRODUCTION TO SOFTWARE ENGINEERING.pdf','4a5deda4-4c3a-4532-a337-0c4924e9bc95.pdf','pdf',196414,'2025-05-15','78bce549-4e00-4c58-9a5b-485192e10a1f.png'),(11,'ngan-hang-trac-nghiem-chu-nghia-xa-hoi-khoa-hoc.pdf','d367d1a4-f7ed-496d-b7ca-c1b74f5f8144.pdf','pdf',3413882,'2025-05-15','3f617d11-3600-4fea-ac8d-2e9bcb739aa2.png'),(12,'2.SWE-Lectures v1.0 En(buoi 4).pdf','c8f66964-4bd0-4696-a5ac-d7e6c6ded6be.pdf','pdf',5309161,'2025-05-15','d1928080-34c1-4283-8adb-b0e2332e6b90.png'),(13,'HOEMWORK(INT1340).pdf','bdfbba75-c7b0-4a76-9bc6-24cc839cb314.pdf','pdf',265416,'2025-05-16','21d5aa09-cf55-4530-bd18-06ea7dd17c50.png');
/*!40000 ALTER TABLE `file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `loaitailieu`
--

DROP TABLE IF EXISTS `loaitailieu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `loaitailieu` (
  `MaLoai` varchar(10) NOT NULL,
  `TenLoai` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`MaLoai`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `loaitailieu`
--

LOCK TABLES `loaitailieu` WRITE;
/*!40000 ALTER TABLE `loaitailieu` DISABLE KEYS */;
INSERT INTO `loaitailieu` VALUES ('1','Bài giảng'),('2','Bài tập'),('3','Đề thi'),('4','Tài liệu tham khảo'),('5','Slide');
/*!40000 ALTER TABLE `loaitailieu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `luutailieu`
--

DROP TABLE IF EXISTS `luutailieu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `luutailieu` (
  `MaTaiLieu` int NOT NULL,
  `MaSinhVien` varchar(10) NOT NULL,
  `TrangThaiLuu` bit(1) DEFAULT NULL,
  `NgayLuu` date DEFAULT NULL,
  PRIMARY KEY (`MaTaiLieu`,`MaSinhVien`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `luutailieu`
--

LOCK TABLES `luutailieu` WRITE;
/*!40000 ALTER TABLE `luutailieu` DISABLE KEYS */;
INSERT INTO `luutailieu` VALUES (6,'n22dcvt099',_binary '\0',NULL),(7,'n22dcvt099',_binary '\0',NULL);
/*!40000 ALTER TABLE `luutailieu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `monhoc`
--

DROP TABLE IF EXISTS `monhoc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `monhoc` (
  `MaMon` varchar(15) NOT NULL,
  `TenMon` varchar(45) NOT NULL,
  PRIMARY KEY (`MaMon`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `monhoc`
--

LOCK TABLES `monhoc` WRITE;
/*!40000 ALTER TABLE `monhoc` DISABLE KEYS */;
INSERT INTO `monhoc` VALUES ('1','Lập trình cơ bản'),('2','Cấu trúc dữ liệu'),('3','Cơ sở dữ liệu'),('4','Lập trình Web'),('5','Giải tích 1');
/*!40000 ALTER TABLE `monhoc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sinhvien`
--

DROP TABLE IF EXISTS `sinhvien`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sinhvien` (
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
-- Dumping data for table `sinhvien`
--

LOCK TABLES `sinhvien` WRITE;
/*!40000 ALTER TABLE `sinhvien` DISABLE KEYS */;
INSERT INTO `sinhvien` VALUES ('N22DCVT099',' VU PHAM MINH THUC','n22dcvt099@student.ptithcm.edu.vn','https://lh3.googleusercontent.com/a/ACg8ocKUCnvWqDXmzehaixKf4qAtvgiMbprh1-RtwKmL_2ONXvDz9w=s96-c','2025-05-13','2025-05-19');
/*!40000 ALTER TABLE `sinhvien` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tailieu`
--

DROP TABLE IF EXISTS `tailieu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tailieu` (
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
  CONSTRAINT `FK_TaiLieu_File` FOREIGN KEY (`MaFile`) REFERENCES `file` (`MaFile`) ON UPDATE CASCADE,
  CONSTRAINT `FK_Tailieu_LoaiTaiLieu` FOREIGN KEY (`MaLoai`) REFERENCES `loaitailieu` (`MaLoai`) ON UPDATE CASCADE,
  CONSTRAINT `FK_TaiLieu_MonHoc` FOREIGN KEY (`MonHoc`) REFERENCES `monhoc` (`MaMon`) ON UPDATE CASCADE,
  CONSTRAINT `FK_TaiLieu_SinhVien` FOREIGN KEY (`MaSinhVien`) REFERENCES `sinhvien` (`MaSinhVien`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tailieu`
--

LOCK TABLES `tailieu` WRITE;
/*!40000 ALTER TABLE `tailieu` DISABLE KEYS */;
INSERT INTO `tailieu` VALUES (6,'UML DIAGRAM ARCHITECT','aaa',43,'2025-05-13',0,0,NULL,'1','1','n22dcvt099',7),(7,'TTNT 02','aaa',17,'2025-05-14',0,0,NULL,'1','2','n22dcvt099',8),(8,'UML DIAGRAM ARCHITECT','asd',0,'2025-05-14',0,0,NULL,'4','3','n22dcvt099',9),(9,'INTRODUCTION TO SOFTWARE ENGINEERING','aaa',0,'2025-05-15',0,0,NULL,'1','2','n22dcvt099',10),(10,'ngan-hang-trac-nghiem-chu-nghia-xa-hoi-khoa-hoc','aaa',0,'2025-05-15',0,0,NULL,'5','2','n22dcvt099',11),(11,'2.SWE-Lectures v1.0 En(buoi 4)','aaa',0,'2025-05-15',0,0,NULL,'3','4','n22dcvt099',12),(12,'HOEMWORK(INT1340)','aaaa',0,'2025-05-16',0,0,NULL,'2','3','n22dcvt099',13);
/*!40000 ALTER TABLE `tailieu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tailieu_tukhoa`
--

DROP TABLE IF EXISTS `tailieu_tukhoa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tailieu_tukhoa` (
  `MaTaiLieu` int NOT NULL,
  `MaTuKhoa` int NOT NULL,
  PRIMARY KEY (`MaTaiLieu`,`MaTuKhoa`),
  KEY `FK_TuKhoa_idx` (`MaTuKhoa`),
  CONSTRAINT `FK_TaiLieu` FOREIGN KEY (`MaTaiLieu`) REFERENCES `tailieu` (`MaTaiLieu`) ON UPDATE CASCADE,
  CONSTRAINT `FK_TuKhoa` FOREIGN KEY (`MaTuKhoa`) REFERENCES `tukhoa` (`MaTuKhoa`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tailieu_tukhoa`
--

LOCK TABLES `tailieu_tukhoa` WRITE;
/*!40000 ALTER TABLE `tailieu_tukhoa` DISABLE KEYS */;
/*!40000 ALTER TABLE `tailieu_tukhoa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `theodoi`
--

DROP TABLE IF EXISTS `theodoi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `theodoi` (
  `MaTD` int NOT NULL AUTO_INCREMENT,
  `NguoiDung` varchar(10) NOT NULL,
  `DangTheoDoi` varchar(10) NOT NULL,
  `TrangThai` bit(1) NOT NULL,
  PRIMARY KEY (`MaTD`),
  UNIQUE KEY `NguoiDung_DangTheoDoi` (`NguoiDung`,`DangTheoDoi`),
  KEY `TheoDoi_NguoiDung_idx` (`NguoiDung`),
  KEY `TheoDoi_DangTheoDoi_idx` (`DangTheoDoi`),
  CONSTRAINT `TheoDoi_DangTheoDoi` FOREIGN KEY (`DangTheoDoi`) REFERENCES `sinhvien` (`MaSinhVien`) ON UPDATE CASCADE,
  CONSTRAINT `TheoDoi_NguoiDung` FOREIGN KEY (`NguoiDung`) REFERENCES `sinhvien` (`MaSinhVien`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `theodoi`
--

LOCK TABLES `theodoi` WRITE;
/*!40000 ALTER TABLE `theodoi` DISABLE KEYS */;
INSERT INTO `theodoi` VALUES (1,'n22dcci010','N22DCDT002',_binary '\0'),(2,'n22dcci010','N22DCVT099',_binary '\0');
/*!40000 ALTER TABLE `theodoi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `traloibinhluan`
--

DROP TABLE IF EXISTS `traloibinhluan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `traloibinhluan` (
  `MaTraLoi` int NOT NULL AUTO_INCREMENT,
  `NoiDung` text NOT NULL,
  `NgayTraLoi` date NOT NULL,
  `MaSinhVien` varchar(10) NOT NULL,
  `MaBinhLuan` int NOT NULL,
  PRIMARY KEY (`MaTraLoi`),
  KEY `FK_TraLoi_SinhVien_idx` (`MaSinhVien`),
  KEY `FK_TraLoi_BinhLuan_idx` (`MaBinhLuan`),
  CONSTRAINT `FK_TraLoi_BinhLuan` FOREIGN KEY (`MaBinhLuan`) REFERENCES `binhluan` (`MaBinhLuan`) ON DELETE CASCADE,
  CONSTRAINT `FK_TraLoi_SinhVien` FOREIGN KEY (`MaSinhVien`) REFERENCES `sinhvien` (`MaSinhVien`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `traloibinhluan`
--

LOCK TABLES `traloibinhluan` WRITE;
/*!40000 ALTER TABLE `traloibinhluan` DISABLE KEYS */;
INSERT INTO `traloibinhluan` VALUES (1,'aaa','2025-05-18','n22dcvt099',1);
/*!40000 ALTER TABLE `traloibinhluan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tukhoa`
--

DROP TABLE IF EXISTS `tukhoa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tukhoa` (
  `MaTuKhoa` int NOT NULL AUTO_INCREMENT,
  `NoiDung` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`MaTuKhoa`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tukhoa`
--

LOCK TABLES `tukhoa` WRITE;
/*!40000 ALTER TABLE `tukhoa` DISABLE KEYS */;
/*!40000 ALTER TABLE `tukhoa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `vw_binhluan`
--

DROP TABLE IF EXISTS `vw_binhluan`;
/*!50001 DROP VIEW IF EXISTS `vw_binhluan`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `vw_binhluan` AS SELECT 
 1 AS `MaBinhLuan`,
 1 AS `NoiDung`,
 1 AS `NgayBinhLuan`,
 1 AS `HoVaTen`,
 1 AS `HinhAnh`,
 1 AS `MaTaiLieu`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vw_follow`
--

DROP TABLE IF EXISTS `vw_follow`;
/*!50001 DROP VIEW IF EXISTS `vw_follow`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `vw_follow` AS SELECT 
 1 AS `MaTD`,
 1 AS `NguoiDung`,
 1 AS `DangTheoDoi`,
 1 AS `HoTenND`,
 1 AS `HinhAnhND`,
 1 AS `HoTenDTD`,
 1 AS `HinhAnhDTD`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vw_monhoc`
--

DROP TABLE IF EXISTS `vw_monhoc`;
/*!50001 DROP VIEW IF EXISTS `vw_monhoc`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `vw_monhoc` AS SELECT 
 1 AS `MaMon`,
 1 AS `TenMon`,
 1 AS `TaiLieu`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vw_tailieu`
--

-- Drop the existing view if it exists
DROP VIEW IF EXISTS `vw_tailieu`;

-- Recreate the view with student information
CREATE VIEW `vw_tailieu` AS 
SELECT 
    `tl`.`MaTaiLieu` AS `MaTaiLieu`,
    `tl`.`TieuDe` AS `TieuDe`,
    `tl`.`MoTa` AS `MoTa`,
    `tl`.`LuotXem` AS `LuotXem`,
    `tl`.`MaSinhVien` AS `MaSinhVien`,
    `sv`.`HoVaTen` AS `TenSinhVien`,
    `sv`.`HinhAnh` AS `HinhAnhSV`,
    `mh`.`TenMon` AS `TenMon`,
    `l`.`TenLoai` AS `TenLoai`,
    (COALESCE(`tl`.`UpVote`,0) - COALESCE(`tl`.`DownVote`,0)) AS `DanhGia`,
    COUNT(`bl`.`MaBinhLuan`) AS `SoLuongBL`,
    `f`.`Thumbnail` AS `Thumbnail`,
    `f`.`DuongDanFile` AS `DuongDanFile`
FROM 
    `tailieu` `tl`
    LEFT JOIN `monhoc` `mh` ON (`tl`.`MonHoc` = `mh`.`MaMon`)
    LEFT JOIN `loaitailieu` `l` ON (`tl`.`MaLoai` = `l`.`MaLoai`)
    LEFT JOIN `binhluan` `bl` ON (`tl`.`MaTaiLieu` = `bl`.`MaTaiLieu`)
    LEFT JOIN `file` `f` ON (`tl`.`MaFile` = `f`.`MaFile`)
    LEFT JOIN `sinhvien` `sv` ON (`tl`.`MaSinhVien` = `sv`.`MaSinhVien`)
GROUP BY 
    `tl`.`MaTaiLieu`,
    `tl`.`TieuDe`,
    `tl`.`MoTa`,
    `tl`.`LuotXem`,
    `tl`.`MaSinhVien`,
    `sv`.`HoVaTen`,
    `sv`.`HinhAnh`,
    `tl`.`UpVote`,
    `tl`.`DownVote`,
    `mh`.`TenMon`,
    `l`.`TenLoai`,
    `f`.`Thumbnail`,
    `f`.`DuongDanFile`;
--
-- Temporary view structure for view `vw_thongkefollow`
--

DROP TABLE IF EXISTS `vw_thongkefollow`;
/*!50001 DROP VIEW IF EXISTS `vw_thongkefollow`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `vw_thongkefollow` AS SELECT 
 1 AS `MaSinhVien`,
 1 AS `HoVaTen`,
 1 AS `HinhAnh`,
 1 AS `Follow_ing`,
 1 AS `Follower`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vw_traloibinhluan`
--

DROP TABLE IF EXISTS `vw_traloibinhluan`;
/*!50001 DROP VIEW IF EXISTS `vw_traloibinhluan`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `vw_traloibinhluan` AS SELECT 
 1 AS `MaTraLoi`,
 1 AS `NoiDung`,
 1 AS `NgayTraLoi`,
 1 AS `MaSinhVien`,
 1 AS `MaBinhLuan`,
 1 AS `HoVaTen`,
 1 AS `HinhAnh`*/;
SET character_set_client = @saved_cs_client;

--
-- Dumping events for database 'chiasetailieu'
--

--
-- Dumping routines for database 'chiasetailieu'
--

--
-- Final view structure for view `vw_binhluan`
--

/*!50001 DROP VIEW IF EXISTS `vw_binhluan`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `vw_binhluan` AS select `bl`.`MaBinhLuan` AS `MaBinhLuan`,`bl`.`NoiDung` AS `NoiDung`,`bl`.`NgayBinhLuan` AS `NgayBinhLuan`,`sv`.`HoVaTen` AS `HoVaTen`,`sv`.`HinhAnh` AS `HinhAnh`,`bl`.`MaTaiLieu` AS `MaTaiLieu` from (`binhluan` `bl` left join `sinhvien` `sv` on((`bl`.`MaSinhVien` = `sv`.`MaSinhVien`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vw_follow`
--

/*!50001 DROP VIEW IF EXISTS `vw_follow`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `vw_follow` AS select `td`.`MaTD` AS `MaTD`,`td`.`NguoiDung` AS `NguoiDung`,`td`.`DangTheoDoi` AS `DangTheoDoi`,`sv1`.`HoVaTen` AS `HoTenND`,`sv1`.`HinhAnh` AS `HinhAnhND`,`sv2`.`HoVaTen` AS `HoTenDTD`,`sv2`.`HinhAnh` AS `HinhAnhDTD` from ((`theodoi` `td` join `sinhvien` `sv1` on((`td`.`NguoiDung` = `sv1`.`MaSinhVien`))) join `sinhvien` `sv2` on((`td`.`DangTheoDoi` = `sv2`.`MaSinhVien`))) where (`td`.`TrangThai` = 0) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vw_monhoc`
--

/*!50001 DROP VIEW IF EXISTS `vw_monhoc`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `vw_monhoc` AS select `mh`.`MaMon` AS `MaMon`,`mh`.`TenMon` AS `TenMon`,count(`tl`.`MaTaiLieu`) AS `TaiLieu` from (`monhoc` `mh` left join `tailieu` `tl` on((`mh`.`MaMon` = `tl`.`MonHoc`))) group by `mh`.`MaMon`,`mh`.`TenMon` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vw_tailieu`
--

/*!50001 DROP VIEW IF EXISTS `vw_tailieu`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `vw_tailieu` AS select `tl`.`MaTaiLieu` AS `MaTaiLieu`,`tl`.`TieuDe` AS `TieuDe`,`tl`.`MoTa` AS `MoTa`,`tl`.`LuotXem` AS `LuotXem`,`tl`.`MaSinhVien` AS `MaSinhVien`,`mh`.`TenMon` AS `TenMon`,`l`.`TenLoai` AS `TenLoai`,(coalesce(`tl`.`UpVote`,0) - coalesce(`tl`.`DownVote`,0)) AS `DanhGia`,count(`bl`.`MaBinhLuan`) AS `SoLuongBL`,`f`.`Thumbnail` AS `Thumbnail`,`f`.`DuongDanFile` AS `DuongDanFile` from ((((`tailieu` `tl` left join `monhoc` `mh` on((`tl`.`MonHoc` = `mh`.`MaMon`))) left join `loaitailieu` `l` on((`tl`.`MaLoai` = `l`.`MaLoai`))) left join `binhluan` `bl` on((`tl`.`MaTaiLieu` = `bl`.`MaTaiLieu`))) left join `file` `f` on((`tl`.`MaFile` = `f`.`MaFile`))) group by `tl`.`MaTaiLieu`,`tl`.`TieuDe`,`tl`.`MoTa`,`tl`.`LuotXem`,`tl`.`MaSinhVien`,`tl`.`UpVote`,`tl`.`DownVote`,`mh`.`TenMon`,`l`.`TenLoai`,`f`.`Thumbnail`,`f`.`DuongDanFile` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vw_thongkefollow`
--

/*!50001 DROP VIEW IF EXISTS `vw_thongkefollow`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `vw_thongkefollow` AS select `sv`.`MaSinhVien` AS `MaSinhVien`,`sv`.`HoVaTen` AS `HoVaTen`,`sv`.`HinhAnh` AS `HinhAnh`,(select count(0) from `theodoi` where ((`theodoi`.`NguoiDung` = `sv`.`MaSinhVien`) and (`theodoi`.`TrangThai` = 0))) AS `Follow_ing`,(select count(0) from `theodoi` where ((`theodoi`.`DangTheoDoi` = `sv`.`MaSinhVien`) and (`theodoi`.`TrangThai` = 0))) AS `Follower` from `sinhvien` `sv` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vw_traloibinhluan`
--

/*!50001 DROP VIEW IF EXISTS `vw_traloibinhluan`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `vw_traloibinhluan` AS select `tl`.`MaTraLoi` AS `MaTraLoi`,`tl`.`NoiDung` AS `NoiDung`,`tl`.`NgayTraLoi` AS `NgayTraLoi`,`tl`.`MaSinhVien` AS `MaSinhVien`,`tl`.`MaBinhLuan` AS `MaBinhLuan`,`sv`.`HoVaTen` AS `HoVaTen`,`sv`.`HinhAnh` AS `HinhAnh` from (`traloibinhluan` `tl` left join `sinhvien` `sv` on((`tl`.`MaSinhVien` = `sv`.`MaSinhVien`))) */;
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

SET SQL_SAFE_UPDATES = 0;
DELETE FROM file;
DELETE FROM tailieu;
-- Dump completed on 2025-05-19 16:58:57
