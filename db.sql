-- MySQL dump 10.13  Distrib 8.0.35, for Win64 (x86_64)
--
-- Host: localhost    Database: chat_project
-- ------------------------------------------------------
-- Server version	8.0.35

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
-- Table structure for table `admin_users`
--

DROP TABLE IF EXISTS `admin_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin_users` (
                               `id` bigint NOT NULL,
                               `name` varchar(20) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'admin',
                               `user_id` bigint DEFAULT NULL,
                               PRIMARY KEY (`id`),
                               KEY `user_id` (`user_id`),
                               CONSTRAINT `admin_users_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_users`
--

LOCK TABLES `admin_users` WRITE;
/*!40000 ALTER TABLE `admin_users` DISABLE KEYS */;
/*!40000 ALTER TABLE `admin_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ai_chats`
--

DROP TABLE IF EXISTS `ai_chats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_chats` (
                            `id` bigint NOT NULL,
                            `ai_player_id` bigint DEFAULT NULL,
                            `tokens` bigint NOT NULL DEFAULT '0',
                            `chat_id` bigint DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            KEY `ai_player_id` (`ai_player_id`),
                            KEY `chat_id` (`chat_id`),
                            CONSTRAINT `ai_chats_ibfk_1` FOREIGN KEY (`ai_player_id`) REFERENCES `ai_players` (`id`),
                            CONSTRAINT `ai_chats_ibfk_2` FOREIGN KEY (`chat_id`) REFERENCES `chats` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_chats`
--

LOCK TABLES `ai_chats` WRITE;
/*!40000 ALTER TABLE `ai_chats` DISABLE KEYS */;
/*!40000 ALTER TABLE `ai_chats` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ai_players`
--

DROP TABLE IF EXISTS `ai_players`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_players` (
                              `id` bigint NOT NULL,
                              `player_id` bigint DEFAULT NULL,
                              `role_id` bigint DEFAULT NULL,
                              PRIMARY KEY (`id`),
                              KEY `player_id` (`player_id`),
                              KEY `role` (`role_id`),
                              CONSTRAINT `ai_players_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`),
                              CONSTRAINT `ai_players_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `ai_roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_players`
--

LOCK TABLES `ai_players` WRITE;
/*!40000 ALTER TABLE `ai_players` DISABLE KEYS */;
/*!40000 ALTER TABLE `ai_players` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ai_roles`
--

DROP TABLE IF EXISTS `ai_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_roles` (
                            `id` bigint NOT NULL,
                            `instruction` text COLLATE utf8mb4_general_ci,
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_roles`
--

LOCK TABLES `ai_roles` WRITE;
/*!40000 ALTER TABLE `ai_roles` DISABLE KEYS */;
/*!40000 ALTER TABLE `ai_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chats`
--

DROP TABLE IF EXISTS `chats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chats` (
                         `id` bigint NOT NULL,
                         `type` enum('AI','USER') COLLATE utf8mb4_general_ci NOT NULL,
                         `room_id` bigint DEFAULT NULL,
                         `player_id` bigint DEFAULT NULL,
                         `message` text COLLATE utf8mb4_general_ci NOT NULL,
                         `created_at` timestamp NULL DEFAULT NULL,
                         PRIMARY KEY (`id`),
                         KEY `chats_index_2` (`room_id`),
                         KEY `chats_index_3` (`created_at`),
                         KEY `player_id` (`player_id`),
                         CONSTRAINT `chats_ibfk_1` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`id`),
                         CONSTRAINT `chats_ibfk_2` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chats`
--

LOCK TABLES `chats` WRITE;
/*!40000 ALTER TABLE `chats` DISABLE KEYS */;
/*!40000 ALTER TABLE `chats` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group_rooms`
--

DROP TABLE IF EXISTS `group_rooms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `group_rooms` (
                               `id` bigint NOT NULL,
                               `invite_code` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
                               `room_id` bigint DEFAULT NULL,
                               PRIMARY KEY (`id`),
                               UNIQUE KEY `idx_group_room_invite_code` (`invite_code`) COMMENT '초대 코드 인덱싱',
                               KEY `group_rooms_rooms_id_fk` (`room_id`),
                               CONSTRAINT `group_rooms_rooms_id_fk` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_rooms`
--

LOCK TABLES `group_rooms` WRITE;
/*!40000 ALTER TABLE `group_rooms` DISABLE KEYS */;
/*!40000 ALTER TABLE `group_rooms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `membership_users`
--

DROP TABLE IF EXISTS `membership_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `membership_users` (
                                    `id` bigint NOT NULL,
                                    `bankType` enum('KOOKMIN','SHINHAN','HANA','IBK','NH','WOORI','KAKAOBANK','TOSSBANK') COLLATE utf8mb4_general_ci NOT NULL,
                                    `accountNumber` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
                                    `accountHolder` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
                                    `credit` double NOT NULL DEFAULT '0',
                                    `user_id` bigint DEFAULT NULL,
                                    PRIMARY KEY (`id`),
                                    KEY `user_id` (`user_id`),
                                    CONSTRAINT `membership_users_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `membership_users`
--

LOCK TABLES `membership_users` WRITE;
/*!40000 ALTER TABLE `membership_users` DISABLE KEYS */;
/*!40000 ALTER TABLE `membership_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `players`
--

DROP TABLE IF EXISTS `players`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `players` (
                           `id` bigint NOT NULL,
                           `type` enum('AI','USER') COLLATE utf8mb4_general_ci NOT NULL,
                           `room_id` bigint DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           KEY `room_id` (`room_id`),
                           CONSTRAINT `players_ibfk_1` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `players`
--

LOCK TABLES `players` WRITE;
/*!40000 ALTER TABLE `players` DISABLE KEYS */;
/*!40000 ALTER TABLE `players` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rooms`
--

DROP TABLE IF EXISTS `rooms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rooms` (
                         `id` bigint NOT NULL,
                         `is_private` tinyint(1) NOT NULL,
                         `type` enum('SINGLE','GROUP') COLLATE utf8mb4_general_ci NOT NULL,
                         `owner_id` bigint DEFAULT NULL,
                         `status` enum('OPEN','CLOSED') COLLATE utf8mb4_general_ci NOT NULL,
                         `created_at` timestamp NULL DEFAULT NULL,
                         `updated_at` timestamp NULL DEFAULT NULL,
                         `deleted_at` timestamp NULL DEFAULT NULL,
                         `max_players` int NOT NULL,
                         PRIMARY KEY (`id`),
                         KEY `rooms_index_1` (`created_at`),
                         KEY `owner_id` (`owner_id`),
                         CONSTRAINT `rooms_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rooms`
--

LOCK TABLES `rooms` WRITE;
/*!40000 ALTER TABLE `rooms` DISABLE KEYS */;
/*!40000 ALTER TABLE `rooms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `single_rooms`
--

DROP TABLE IF EXISTS `single_rooms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `single_rooms` (
                                `id` bigint NOT NULL,
                                `expected_cost` double NOT NULL,
                                `room_id` bigint DEFAULT NULL,
                                PRIMARY KEY (`id`),
                                KEY `single_rooms_rooms_id_fk` (`room_id`),
                                CONSTRAINT `single_rooms_rooms_id_fk` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `single_rooms`
--

LOCK TABLES `single_rooms` WRITE;
/*!40000 ALTER TABLE `single_rooms` DISABLE KEYS */;
/*!40000 ALTER TABLE `single_rooms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_chats`
--

DROP TABLE IF EXISTS `user_chats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_chats` (
                              `id` bigint NOT NULL,
                              `user_player_id` bigint DEFAULT NULL,
                              `chat_id` bigint DEFAULT NULL,
                              PRIMARY KEY (`id`),
                              KEY `user_player_id` (`user_player_id`),
                              KEY `chat_id` (`chat_id`),
                              CONSTRAINT `user_chats_ibfk_1` FOREIGN KEY (`user_player_id`) REFERENCES `user_players` (`id`),
                              CONSTRAINT `user_chats_ibfk_2` FOREIGN KEY (`chat_id`) REFERENCES `chats` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_chats`
--

LOCK TABLES `user_chats` WRITE;
/*!40000 ALTER TABLE `user_chats` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_chats` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_players`
--

DROP TABLE IF EXISTS `user_players`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_players` (
                                `id` bigint NOT NULL,
                                `user_id` bigint DEFAULT NULL,
                                `player_id` bigint DEFAULT NULL,
                                PRIMARY KEY (`id`),
                                KEY `user_id` (`user_id`),
                                KEY `player_id` (`player_id`),
                                CONSTRAINT `user_players_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
                                CONSTRAINT `user_players_ibfk_2` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_players`
--

LOCK TABLES `user_players` WRITE;
/*!40000 ALTER TABLE `user_players` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_players` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
                         `id` bigint NOT NULL,
                         `username` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
                         `nickname` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
                         `password` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
                         `role` enum('USER','ADMIN') COLLATE utf8mb4_general_ci NOT NULL,
                         `created_at` timestamp NULL DEFAULT NULL,
                         `updated_at` timestamp NULL DEFAULT NULL,
                         `deleted_at` timestamp NULL DEFAULT NULL,
                         'name' varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
                         `email` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
                         `picture` TEXT COLLATE utf8mb4_general_ci DEFAULT NULL,
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `users_index_0` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'chat_project'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-15 22:33:46
