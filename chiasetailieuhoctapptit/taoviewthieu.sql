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
-- Temporary view structure for view `vw_BinhLuan`
--

DROP TABLE IF EXISTS `vw_BinhLuan`;
/*!50001 DROP VIEW IF EXISTS `vw_BinhLuan`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `vw_BinhLuan` AS SELECT 
 1 AS `MaBinhLuan`,
 1 AS `NoiDung`,
 1 AS `NgayBinhLuan`,
 1 AS `HoVaTen`,
 1 AS `HinhAnh`,
 1 AS `MaTaiLieu`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vw_MonHoc`
--

DROP TABLE IF EXISTS `vw_MonHoc`;
/*!50001 DROP VIEW IF EXISTS `vw_MonHoc`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `vw_MonHoc` AS SELECT 
 1 AS `MaMon`,
 1 AS `TenMon`,
 1 AS `TaiLieu`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vw_TaiLieu`
--

DROP TABLE IF EXISTS `vw_TaiLieu`;
/*!50001 DROP VIEW IF EXISTS `vw_TaiLieu`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `vw_TaiLieu` AS SELECT 
 1 AS `MaTaiLieu`,
 1 AS `TieuDe`,
 1 AS `LuotXem`,
 1 AS `TenMon`,
 1 AS `TenLoai`,
 1 AS `DanhGia`,
 1 AS `SoLuongBL`,
 1 AS `Thumbnail`,
 1 AS `DuongDanFile`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `vw_BinhLuan`
--

/*!50001 DROP VIEW IF EXISTS `vw_BinhLuan`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `vw_BinhLuan` AS select `bl`.`MaBinhLuan` AS `MaBinhLuan`,`bl`.`NoiDung` AS `NoiDung`,`bl`.`NgayBinhLuan` AS `NgayBinhLuan`,`sv`.`HoVaTen` AS `HoVaTen`,`sv`.`HinhAnh` AS `HinhAnh`,`bl`.`MaTaiLieu` AS `MaTaiLieu` from (`BinhLuan` `bl` left join `SinhVien` `sv` on((`bl`.`MaSinhVien` = `sv`.`MaSinhVien`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vw_MonHoc`
--

/*!50001 DROP VIEW IF EXISTS `vw_MonHoc`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`tch`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `vw_MonHoc` AS select `mh`.`MaMon` AS `MaMon`,`mh`.`TenMon` AS `TenMon`,count(`tl`.`MaTaiLieu`) AS `TaiLieu` from (`MonHoc` `mh` left join `TaiLieu` `tl` on((`mh`.`MaMon` = `tl`.`MonHoc`))) group by `mh`.`MaMon`,`mh`.`TenMon` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

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
/*!50001 VIEW `vw_TaiLieu` AS select `tl`.`MaTaiLieu` AS `MaTaiLieu`,`tl`.`TieuDe` AS `TieuDe`,`tl`.`LuotXem` AS `LuotXem`,`mh`.`TenMon` AS `TenMon`,`l`.`TenLoai` AS `TenLoai`,(coalesce(`tl`.`UpVote`,0) - coalesce(`tl`.`DownVote`,0)) AS `DanhGia`,count(`bl`.`MaBinhLuan`) AS `SoLuongBL`,`f`.`Thumbnail` AS `Thumbnail`,`f`.`DuongDanFile` AS `DuongDanFile` from ((((`TaiLieu` `tl` left join `MonHoc` `mh` on((`tl`.`MonHoc` = `mh`.`MaMon`))) left join `LoaiTaiLieu` `l` on((`tl`.`MaLoai` = `l`.`MaLoai`))) left join `BinhLuan` `bl` on((`tl`.`MaTaiLieu` = `bl`.`MaTaiLieu`))) left join `File` `f` on((`tl`.`MaFile` = `f`.`MaFile`))) group by `tl`.`MaTaiLieu`,`tl`.`TieuDe`,`tl`.`LuotXem`,`tl`.`UpVote`,`tl`.`DownVote`,`mh`.`TenMon`,`l`.`TenLoai`,`f`.`Thumbnail`,`f`.`DuongDanFile` */;
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

-- Dump completed on 2025-04-29 14:18:30
