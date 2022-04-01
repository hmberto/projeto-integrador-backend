-- MySQL dump 10.13  Distrib 8.0.28, for Linux (x86_64)
--
-- Host: localhost    Database: pharmacy
-- ------------------------------------------------------
-- Server version	8.0.28-0ubuntu0.20.04.3

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
-- Table structure for table `Bairro`
--

DROP TABLE IF EXISTS `Bairro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Bairro` (
  `id_bairro` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  PRIMARY KEY (`id_bairro`),
  UNIQUE KEY `nome` (`nome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Bairro`
--

LOCK TABLES `Bairro` WRITE;
/*!40000 ALTER TABLE `Bairro` DISABLE KEYS */;
/*!40000 ALTER TABLE `Bairro` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Bandeira_Cartao`
--

DROP TABLE IF EXISTS `Bandeira_Cartao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Bandeira_Cartao` (
  `id_bandeira` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  PRIMARY KEY (`id_bandeira`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Bandeira_Cartao`
--

LOCK TABLES `Bandeira_Cartao` WRITE;
/*!40000 ALTER TABLE `Bandeira_Cartao` DISABLE KEYS */;
/*!40000 ALTER TABLE `Bandeira_Cartao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Cartao`
--

DROP TABLE IF EXISTS `Cartao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Cartao` (
  `id_cartao` int NOT NULL AUTO_INCREMENT,
  `numero` varchar(20) DEFAULT NULL,
  `data_validade` date NOT NULL,
  `cvv` varchar(3) NOT NULL,
  `id_usuario` int DEFAULT NULL,
  `id_bandeira` int DEFAULT NULL,
  PRIMARY KEY (`id_cartao`),
  KEY `id_usuario` (`id_usuario`),
  KEY `id_bandeira` (`id_bandeira`),
  CONSTRAINT `Cartao_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `Usuario` (`id_usuario`),
  CONSTRAINT `Cartao_ibfk_2` FOREIGN KEY (`id_bandeira`) REFERENCES `Bandeira_Cartao` (`id_bandeira`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Cartao`
--

LOCK TABLES `Cartao` WRITE;
/*!40000 ALTER TABLE `Cartao` DISABLE KEYS */;
/*!40000 ALTER TABLE `Cartao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Cidade`
--

DROP TABLE IF EXISTS `Cidade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Cidade` (
  `id_cidade` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  PRIMARY KEY (`id_cidade`),
  UNIQUE KEY `nome` (`nome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Cidade`
--

LOCK TABLES `Cidade` WRITE;
/*!40000 ALTER TABLE `Cidade` DISABLE KEYS */;
/*!40000 ALTER TABLE `Cidade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Compra`
--

DROP TABLE IF EXISTS `Compra`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Compra` (
  `id_compra` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int DEFAULT NULL,
  `id_farmacia` int DEFAULT NULL,
  `id_valor` int DEFAULT NULL,
  `id_entrega` int DEFAULT NULL,
  PRIMARY KEY (`id_compra`),
  KEY `id_usuario` (`id_usuario`),
  KEY `id_farmacia` (`id_farmacia`),
  KEY `id_valor` (`id_valor`),
  KEY `id_entrega` (`id_entrega`),
  CONSTRAINT `Compra_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `Usuario` (`id_usuario`),
  CONSTRAINT `Compra_ibfk_2` FOREIGN KEY (`id_farmacia`) REFERENCES `Farmacia` (`id_farmacia`),
  CONSTRAINT `Compra_ibfk_3` FOREIGN KEY (`id_valor`) REFERENCES `Valor_Compra` (`id_valor`),
  CONSTRAINT `Compra_ibfk_4` FOREIGN KEY (`id_entrega`) REFERENCES `Entrega` (`id_entrega`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Compra`
--

LOCK TABLES `Compra` WRITE;
/*!40000 ALTER TABLE `Compra` DISABLE KEYS */;
/*!40000 ALTER TABLE `Compra` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Emp_Entrega`
--

DROP TABLE IF EXISTS `Emp_Entrega`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Emp_Entrega` (
  `id_emp_entrega` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  `cnpj` varchar(18) NOT NULL,
  `id_endereco` int DEFAULT NULL,
  `ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`id_emp_entrega`),
  KEY `id_endereco` (`id_endereco`),
  CONSTRAINT `Emp_Entrega_ibfk_1` FOREIGN KEY (`id_endereco`) REFERENCES `Endereco` (`id_endereco`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Emp_Entrega`
--

LOCK TABLES `Emp_Entrega` WRITE;
/*!40000 ALTER TABLE `Emp_Entrega` DISABLE KEYS */;
/*!40000 ALTER TABLE `Emp_Entrega` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Endereco`
--

DROP TABLE IF EXISTS `Endereco`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Endereco` (
  `id_endereco` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  `numero` int NOT NULL,
  `complemento` varchar(50) DEFAULT NULL,
  `cep` varchar(9) NOT NULL,
  `lat` varchar(20) NOT NULL,
  `lon` varchar(20) NOT NULL,
  `id_cidade` int DEFAULT NULL,
  `id_bairro` int DEFAULT NULL,
  `id_estado` int DEFAULT NULL,
  `id_tipo_endereco` int DEFAULT NULL,
  PRIMARY KEY (`id_endereco`),
  KEY `id_cidade` (`id_cidade`),
  KEY `id_bairro` (`id_bairro`),
  KEY `id_estado` (`id_estado`),
  KEY `id_tipo_endereco` (`id_tipo_endereco`),
  CONSTRAINT `Endereco_ibfk_1` FOREIGN KEY (`id_cidade`) REFERENCES `Cidade` (`id_cidade`),
  CONSTRAINT `Endereco_ibfk_2` FOREIGN KEY (`id_bairro`) REFERENCES `Bairro` (`id_bairro`),
  CONSTRAINT `Endereco_ibfk_3` FOREIGN KEY (`id_estado`) REFERENCES `Estado` (`id_estado`),
  CONSTRAINT `Endereco_ibfk_4` FOREIGN KEY (`id_tipo_endereco`) REFERENCES `Tipo_Endereco` (`id_tipo_endereco`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Endereco`
--

LOCK TABLES `Endereco` WRITE;
/*!40000 ALTER TABLE `Endereco` DISABLE KEYS */;
/*!40000 ALTER TABLE `Endereco` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Entrega`
--

DROP TABLE IF EXISTS `Entrega`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Entrega` (
  `id_entrega` int NOT NULL AUTO_INCREMENT,
  `data_entrega` date NOT NULL,
  `hora_entrega` time NOT NULL,
  `id_entregador` int DEFAULT NULL,
  PRIMARY KEY (`id_entrega`),
  KEY `id_entregador` (`id_entregador`),
  CONSTRAINT `Entrega_ibfk_1` FOREIGN KEY (`id_entregador`) REFERENCES `Entregador` (`id_entregador`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Entrega`
--

LOCK TABLES `Entrega` WRITE;
/*!40000 ALTER TABLE `Entrega` DISABLE KEYS */;
/*!40000 ALTER TABLE `Entrega` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Entregador`
--

DROP TABLE IF EXISTS `Entregador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Entregador` (
  `id_entregador` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  `cpf` varchar(11) NOT NULL,
  `ativo` tinyint(1) NOT NULL,
  `id_emp_entrega` int DEFAULT NULL,
  PRIMARY KEY (`id_entregador`),
  KEY `id_emp_entrega` (`id_emp_entrega`),
  CONSTRAINT `Entregador_ibfk_1` FOREIGN KEY (`id_emp_entrega`) REFERENCES `Emp_Entrega` (`id_emp_entrega`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Entregador`
--

LOCK TABLES `Entregador` WRITE;
/*!40000 ALTER TABLE `Entregador` DISABLE KEYS */;
/*!40000 ALTER TABLE `Entregador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Estado`
--

DROP TABLE IF EXISTS `Estado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Estado` (
  `id_estado` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  `id_sigla_estado` int DEFAULT NULL,
  PRIMARY KEY (`id_estado`),
  UNIQUE KEY `nome` (`nome`),
  KEY `id_sigla_estado` (`id_sigla_estado`),
  CONSTRAINT `Estado_ibfk_1` FOREIGN KEY (`id_sigla_estado`) REFERENCES `Sigla_Estado` (`id_sigla_estado`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Estado`
--

LOCK TABLES `Estado` WRITE;
/*!40000 ALTER TABLE `Estado` DISABLE KEYS */;
/*!40000 ALTER TABLE `Estado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Farmacia`
--

DROP TABLE IF EXISTS `Farmacia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Farmacia` (
  `id_farmacia` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  `cnpj` varchar(18) NOT NULL,
  `id_endereco` int DEFAULT NULL,
  `ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`id_farmacia`),
  KEY `id_endereco` (`id_endereco`),
  CONSTRAINT `Farmacia_ibfk_1` FOREIGN KEY (`id_endereco`) REFERENCES `Endereco` (`id_endereco`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Farmacia`
--

LOCK TABLES `Farmacia` WRITE;
/*!40000 ALTER TABLE `Farmacia` DISABLE KEYS */;
/*!40000 ALTER TABLE `Farmacia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Forma_Pagamento`
--

DROP TABLE IF EXISTS `Forma_Pagamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Forma_Pagamento` (
  `id_forma_pagamento` int NOT NULL AUTO_INCREMENT,
  `id_cartao` int DEFAULT NULL,
  PRIMARY KEY (`id_forma_pagamento`),
  KEY `id_cartao` (`id_cartao`),
  CONSTRAINT `Forma_Pagamento_ibfk_1` FOREIGN KEY (`id_cartao`) REFERENCES `Cartao` (`id_cartao`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Forma_Pagamento`
--

LOCK TABLES `Forma_Pagamento` WRITE;
/*!40000 ALTER TABLE `Forma_Pagamento` DISABLE KEYS */;
/*!40000 ALTER TABLE `Forma_Pagamento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Genero`
--

DROP TABLE IF EXISTS `Genero`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Genero` (
  `id_genero` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  `sigla` char(10) NOT NULL,
  PRIMARY KEY (`id_genero`),
  UNIQUE KEY `nome` (`nome`),
  UNIQUE KEY `sigla` (`sigla`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Genero`
--

LOCK TABLES `Genero` WRITE;
/*!40000 ALTER TABLE `Genero` DISABLE KEYS */;
/*!40000 ALTER TABLE `Genero` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Login_Sessao`
--

DROP TABLE IF EXISTS `Login_Sessao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Login_Sessao` (
  `id_login_sessao` int NOT NULL AUTO_INCREMENT,
  `id_session` varchar(250) DEFAULT NULL,
  `id_usuario` int DEFAULT NULL,
  `id_senha` int DEFAULT NULL,
  PRIMARY KEY (`id_login_sessao`),
  KEY `id_usuario` (`id_usuario`),
  KEY `id_senha` (`id_senha`),
  CONSTRAINT `Login_Sessao_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `Usuario` (`id_usuario`),
  CONSTRAINT `Login_Sessao_ibfk_2` FOREIGN KEY (`id_senha`) REFERENCES `Senha` (`id_senha`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Login_Sessao`
--

LOCK TABLES `Login_Sessao` WRITE;
/*!40000 ALTER TABLE `Login_Sessao` DISABLE KEYS */;
/*!40000 ALTER TABLE `Login_Sessao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Rec_Senha`
--

DROP TABLE IF EXISTS `Rec_Senha`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Rec_Senha` (
  `id_rec_senha` int NOT NULL AUTO_INCREMENT,
  `data_rec` date NOT NULL,
  `token_rec_senha` varchar(250) NOT NULL,
  PRIMARY KEY (`id_rec_senha`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Rec_Senha`
--

LOCK TABLES `Rec_Senha` WRITE;
/*!40000 ALTER TABLE `Rec_Senha` DISABLE KEYS */;
/*!40000 ALTER TABLE `Rec_Senha` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Senha`
--

DROP TABLE IF EXISTS `Senha`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Senha` (
  `id_senha` int NOT NULL AUTO_INCREMENT,
  `senha` varchar(25) DEFAULT NULL,
  `data_criacao` date NOT NULL,
  `ativo` tinyint(1) NOT NULL,
  `id_usuario` int DEFAULT NULL,
  `id_rec_senha` int DEFAULT NULL,
  PRIMARY KEY (`id_senha`),
  KEY `id_usuario` (`id_usuario`),
  KEY `id_rec_senha` (`id_rec_senha`),
  CONSTRAINT `Senha_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `Usuario` (`id_usuario`),
  CONSTRAINT `Senha_ibfk_2` FOREIGN KEY (`id_rec_senha`) REFERENCES `Rec_Senha` (`id_rec_senha`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Senha`
--

LOCK TABLES `Senha` WRITE;
/*!40000 ALTER TABLE `Senha` DISABLE KEYS */;
/*!40000 ALTER TABLE `Senha` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Sigla_Estado`
--

DROP TABLE IF EXISTS `Sigla_Estado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Sigla_Estado` (
  `id_sigla_estado` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(2) NOT NULL,
  PRIMARY KEY (`id_sigla_estado`),
  UNIQUE KEY `nome` (`nome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Sigla_Estado`
--

LOCK TABLES `Sigla_Estado` WRITE;
/*!40000 ALTER TABLE `Sigla_Estado` DISABLE KEYS */;
/*!40000 ALTER TABLE `Sigla_Estado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Tipo_Endereco`
--

DROP TABLE IF EXISTS `Tipo_Endereco`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Tipo_Endereco` (
  `id_tipo_endereco` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  PRIMARY KEY (`id_tipo_endereco`),
  UNIQUE KEY `nome` (`nome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Tipo_Endereco`
--

LOCK TABLES `Tipo_Endereco` WRITE;
/*!40000 ALTER TABLE `Tipo_Endereco` DISABLE KEYS */;
/*!40000 ALTER TABLE `Tipo_Endereco` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Usuario`
--

DROP TABLE IF EXISTS `Usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Usuario` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  `email` varchar(60) NOT NULL,
  `cpf` varchar(11) NOT NULL,
  `ativo` tinyint(1) NOT NULL,
  `birth_date` date NOT NULL,
  `token_confirmacao_cadastro` varchar(250) DEFAULT NULL,
  `id_genero` int DEFAULT NULL,
  `id_endereco` int DEFAULT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `cpf` (`cpf`),
  KEY `id_genero` (`id_genero`),
  KEY `id_endereco` (`id_endereco`),
  CONSTRAINT `Usuario_ibfk_1` FOREIGN KEY (`id_genero`) REFERENCES `Genero` (`id_genero`),
  CONSTRAINT `Usuario_ibfk_2` FOREIGN KEY (`id_endereco`) REFERENCES `Endereco` (`id_endereco`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Usuario`
--

LOCK TABLES `Usuario` WRITE;
/*!40000 ALTER TABLE `Usuario` DISABLE KEYS */;
/*!40000 ALTER TABLE `Usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Valor_Compra`
--

DROP TABLE IF EXISTS `Valor_Compra`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Valor_Compra` (
  `id_valor` int NOT NULL AUTO_INCREMENT,
  `valor_total` float NOT NULL,
  `valor_farmacia` float NOT NULL,
  `valor_entrega` float NOT NULL,
  `valor_receita` float NOT NULL,
  PRIMARY KEY (`id_valor`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Valor_Compra`
--

LOCK TABLES `Valor_Compra` WRITE;
/*!40000 ALTER TABLE `Valor_Compra` DISABLE KEYS */;
/*!40000 ALTER TABLE `Valor_Compra` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-04-01 12:13:59
