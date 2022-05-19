CREATE TABLE `Bairro` (
	`id_bairro` int AUTO_INCREMENT,
	`nome` varchar(50) UNIQUE NOT NULL,
	PRIMARY KEY (`id_bairro`)
)

CREATE TABLE `Bandeira_Cartao` (
	`id_bandeira` int AUTO_INCREMENT,
	`nome` varchar(50) UNIQUE NOT NULL,
	PRIMARY KEY (`id_bandeira`)
)

CREATE TABLE `Cartao` (
	`id_cartao` int AUTO_INCREMENT,
	`numero` varchar(20) NOT NULL,
	`data_validade` date NOT NULL,
	`cvv` varchar(3) NOT NULL,
	`id_usuario` int NOT NULL,
	`id_bandeira` int NOT NULL,
	PRIMARY KEY (`id_cartao`),
	FOREIGN KEY (`id_usuario`) REFERENCES `Usuario` (`id_usuario`),
	FOREIGN KEY (`id_bandeira`) REFERENCES `Bandeira_Cartao` (`id_bandeira`)
)

CREATE TABLE `Cidade` (
	`id_cidade` int AUTO_INCREMENT,
	`nome` varchar(50) UNIQUE NOT NULL,
	PRIMARY KEY (`id_cidade`)
)

CREATE TABLE `Compra` (
	`id_compra` int AUTO_INCREMENT,
	`data_compra` datetime NOT NULL,
	`id_usuario` int,
	`id_farmacia` int,
	`id_valor` int,
	`id_entrega` int,
	`id_forma_pagamento` int,
	`id_status` int,
	PRIMARY KEY (`id_compra`),
	FOREIGN KEY (`id_usuario`) REFERENCES `Usuario` (`id_usuario`),
	FOREIGN KEY (`id_farmacia`) REFERENCES `Farmacia` (`id_farmacia`),
	FOREIGN KEY (`id_valor`) REFERENCES `Valor_Compra` (`id_valor`),
	FOREIGN KEY (`id_entrega`) REFERENCES `Entrega` (`id_entrega`),
	FOREIGN KEY (`id_forma_pagamento`) REFERENCES `Forma_Pagamento` (`id_forma_pagamento`),
	FOREIGN KEY (`id_status`) REFERENCES `Status` (`id_status`)
)

CREATE TABLE `Status` (
	`id_status` int AUTO_INCREMENT,
	`nome` varchar(50) NOT NULL,
	`ativo` boolean NOT NULL,
	PRIMARY KEY (`id_status`)
)

CREATE TABLE `Endereco` (
	`id_endereco` int AUTO_INCREMENT,
	`nome` varchar(50) NOT NULL,
	`numero` int NOT NULL,
	`complemento` varchar(50),
	`cep` varchar(9) NOT NULL,
	`lat` varchar(20) NOT NULL,
	`lon` varchar(20) NOT NULL,
	`id_cidade` int,
	`id_bairro` int,
	`id_estado` int,
	`id_tipo_endereco` int,
	PRIMARY KEY (`id_endereco`),
	FOREIGN KEY (`id_cidade`) REFERENCES `Cidade` (`id_cidade`),
	FOREIGN KEY (`id_bairro`) REFERENCES `Bairro` (`id_bairro`),
	FOREIGN KEY (`id_estado`) REFERENCES `Estado` (`id_estado`),
	FOREIGN KEY (`id_tipo_endereco`) REFERENCES `Tipo_Endereco` (`id_tipo_endereco`)
)

CREATE TABLE `Entrega` (
	`id_entrega` int AUTO_INCREMENT,
	`valor_entrega` float NOT NULL,
	`data_entrega` date NOT NULL,
	`hora_entrega` time NOT NULL,
	`id_entregador` int,
	PRIMARY KEY (`id_entrega`),
	FOREIGN KEY (`id_entregador`) REFERENCES `Entregador` (`id_entregador`)
)

CREATE TABLE `Entregador` (
	`id_entregador` int AUTO_INCREMENT,
	`nome` varchar(50) NOT NULL,
	`cpf` varchar(11) NOT NULL,
	`cnh` varchar(25) NOT NULL,
	`categoria_cnh` varchar(25) NOT NULL,
	`ativo` boolean NOT NULL,
	PRIMARY KEY (`id_entregador`)
)

CREATE TABLE `Estado` (
	`id_estado` int AUTO_INCREMENT,
	`nome` varchar(50) UNIQUE NOT NULL,
	`id_sigla_estado` int,
	PRIMARY KEY (`id_estado`),
	FOREIGN KEY (`id_sigla_estado`) REFERENCES `Sigla_Estado` (`id_sigla_estado`)
)

CREATE TABLE `Farmacia` (
	`id_farmacia` int AUTO_INCREMENT,
	`nome` varchar(50) NOT NULL,
	`cnpj` varchar(18) NOT NULL,
	`id_endereco` int,
	`ativo` boolean NOT NULL,
	PRIMARY KEY (`id_farmacia`),
	FOREIGN KEY (`id_endereco`) REFERENCES `Endereco` (`id_endereco`)
)

CREATE TABLE `Produto_Farmacia` (
	`id_produto_farmacia` int AUTO_INCREMENT,
	`estoque` int NOT NULL,
	`valor_unitario` float NOT NULL,
	`id_farmacia` int,
	`id_produto` int,
	PRIMARY KEY (`id_produto_farmacia`),
	FOREIGN KEY (`id_farmacia`) REFERENCES `Farmacia` (`id_farmacia`),
	FOREIGN KEY (`id_produto`) REFERENCES `Produto` (`id_produto`)
)

CREATE TABLE `Produto` (
	`id_produto` int AUTO_INCREMENT,
	`nome` varchar(50) NOT NULL,
	`ativo` boolean NOT NULL,
	`id_categoria` int,
	PRIMARY KEY (`id_produto`),
	FOREIGN KEY (`id_categoria`) REFERENCES `Categoria` (`id_categoria`)
)

CREATE TABLE `Categoria` (
	`id_categoria` int AUTO_INCREMENT,
	`nome` varchar(50) UNIQUE NOT NULL,
	`ativo` boolean NOT NULL,
	`id_subcategoria` int,
	PRIMARY KEY (`id_categoria`),
	FOREIGN KEY (`id_subcategoria`) REFERENCES `Sub_Categoria` (`id_subcategoria`)
)

CREATE TABLE `Sub_Categoria` (
	`id_subcategoria` int AUTO_INCREMENT,
	`nome` varchar(50) NOT NULL,
	`ativo` boolean NOT NULL,
	PRIMARY KEY (`id_subcategoria`)
)

CREATE TABLE `Compra_Itens` (
	`quantidade` int NOT NULL,
	`valor_total_compra_itens` float NOT NULL,
	`id_produto_farmacia` int,
	`id_compra` int,
	FOREIGN KEY (`id_produto_farmacia`) REFERENCES `Produto_Farmacia` (`id_produto_farmacia`),
	FOREIGN KEY (`id_compra`) REFERENCES `Compra` (`id_compra`)
)

CREATE TABLE `Forma_Pagamento` (
	`id_forma_pagamento` int AUTO_INCREMENT,
	`id_cartao` int,
	PRIMARY KEY (`id_forma_pagamento`),
	FOREIGN KEY (`id_cartao`) REFERENCES `Cartao` (`id_cartao`)
)

CREATE TABLE `Genero` (
	`id_genero` int AUTO_INCREMENT,
	`nome` varchar(50) UNIQUE NOT NULL,
	`sigla` char(10) UNIQUE NOT NULL,
	PRIMARY KEY (`id_genero`)
)

CREATE TABLE `Login_Sessao` (
	`id_login_sessao` int AUTO_INCREMENT,
	`id_sessao` varchar(250) NOT NULL,
	`id_usuario` int,
	`id_senha` int,
	PRIMARY KEY (`id_login_sessao`),
	FOREIGN KEY (`id_usuario`) REFERENCES `Usuario` (`id_usuario`),
	FOREIGN KEY (`id_senha`) REFERENCES `Senha` (`id_senha`)
)

CREATE TABLE `Rec_Senha` (
	`id_rec_senha` int AUTO_INCREMENT,
	`data_rec` date NOT NULL,
	`token_rec_senha` varchar(250) NOT NULL,
	PRIMARY KEY (`id_rec_senha`)
)

CREATE TABLE `Senha` (
	`id_senha` int AUTO_INCREMENT,
	`senha` varchar(25) NOT NULL,
	`data_criacao` date NOT NULL,
	`ativo` boolean NOT NULL,
	`id_usuario` int,
	`id_rec_senha` int,
	PRIMARY KEY (`id_senha`),
	FOREIGN KEY (`id_usuario`) REFERENCES `Usuario` (`id_usuario`),
	FOREIGN KEY (`id_rec_senha`) REFERENCES `Rec_Senha` (`id_rec_senha`)
)

CREATE TABLE `Sigla_Estado` (
	`id_sigla_estado` int AUTO_INCREMENT,
	`nome` varchar(2) UNIQUE NOT NULL,
	PRIMARY KEY (`id_sigla_estado`)
)

CREATE TABLE `Tipo_Endereco` (
	`id_tipo_endereco` int AUTO_INCREMENT,
	`nome` varchar(50) UNIQUE NOT NULL,
	PRIMARY KEY (`id_tipo_endereco`)
)

CREATE TABLE `Usuario` (
	`id_usuario` int AUTO_INCREMENT,
	`nome` varchar(50) NOT NULL,
	`email` varchar(60) UNIQUE NOT NULL,
	`cpf` varchar(11) UNIQUE NOT NULL,
	`ativo` tinyint(1) NOT NULL,
	`birth_date` date NOT NULL,
	`token_confirmacao_cadastro` varchar(250) NOT NULL,
	`id_genero` int,
	`id_endereco` int,
	PRIMARY KEY (`id_usuario`),
	FOREIGN KEY (`id_genero`) REFERENCES `Genero` (`id_genero`),
	FOREIGN KEY (`id_endereco`) REFERENCES `Endereco` (`id_endereco`)
)

CREATE TABLE `Valor_Compra` (
	`id_valor` int AUTO_INCREMENT,
	`valor_total` float NOT NULL,
	`valor_farmacia` float NOT NULL,
	`valor_entrega` float NOT NULL,
	`valor_receita` float NOT NULL,
	PRIMARY KEY (`id_valor`)
)