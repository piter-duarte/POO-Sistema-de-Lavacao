CREATE DATABASE IF NOT EXISTS db_lavacao;
USE db_lavacao;

CREATE TABLE IF NOT EXISTS cor(
   id int NOT NULL auto_increment,
   nome varchar(100),
   CONSTRAINT pk_cor
      PRIMARY KEY(id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS marca(
   id int NOT NULL auto_increment,
   nome  varchar(100) NOT NULL,
   CONSTRAINT pk_marca
      PRIMARY KEY(id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS modelo(
   id int NOT NULL auto_increment,
   descricao varchar(100),
   id_marca int NOT NULL ,
   categoria ENUM('PEQUENO', 'MEDIO', 'GRANDE', 'MOTO', 'PADRAO') NOT NULL DEFAULT 'PADRAO',
   CONSTRAINT pk_modelo
      PRIMARY KEY(id),
   CONSTRAINT fk_modelo_marca
      FOREIGN KEY(id_marca)
      REFERENCES marca(id)
) ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS motor(
   id_modelo INT NOT NULL REFERENCES modelo(id),
   potencia int NOT NULL DEFAULT 100,
   tipoCombustivel ENUM('GASOLINA', 'ETANOL', 'FLEX', 'GNV', 'OUTRO') NOT NULL DEFAULT 'GASOLINA',
	CONSTRAINT pk_motor PRIMARY KEY (id_modelo),
    CONSTRAINT fk_motor_modelo FOREIGN KEY (id_modelo) REFERENCES modelo(id) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS cliente(
   id int NOT NULL auto_increment,
   nome varchar(50) NOT NULL,
   celular varchar(50) NOT NULL,
   email varchar(100),
   endereco varchar(100),
   dataCadastro date,
   dataNascimento date,
   CONSTRAINT pk_cliente
      PRIMARY KEY(id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS pontuacao(
   id_cliente INT NOT NULL REFERENCES cliente(id),
   quantidade int,
   CONSTRAINT pk_pontuacao
      PRIMARY KEY(id_cliente),
      CONSTRAINT fk_pontuacao_cliente FOREIGN KEY ( id_cliente) 
      REFERENCES cliente(id) 
      ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS pessoa_fisica(
	id_cliente INT NOT NULL REFERENCES cliente(id),
	cpf varchar(100) NOT NULL,
    CONSTRAINT pk_pessoaFisica PRIMARY KEY (id_cliente),
    CONSTRAINT fk_pessoaFisica_cliente FOREIGN KEY (id_cliente) REFERENCES cliente(id) 
		ON DELETE CASCADE
        ON UPDATE CASCADE
)ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS pessoa_juridica(
	id_cliente INT NOT NULL REFERENCES cliente(id),
	cnpj varchar(100) NOT NULL,
    inscricaoEstadual varchar(100),
    CONSTRAINT pk_pessoaJuridica PRIMARY KEY (id_cliente),
    CONSTRAINT fk_pessoaJuridica_cliente FOREIGN KEY (id_cliente) REFERENCES cliente(id) 
		ON DELETE CASCADE
        ON UPDATE CASCADE
)ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS veiculo(
   id int NOT NULL auto_increment,
   placa varchar(100) NOT NULL,
   observacoes varchar(100) NOT NULL,
   id_modelo int NOT NULL,
   id_cor int NOT NULL,
   id_cliente int NOT NULL,
   CONSTRAINT pk_veiculo
      PRIMARY KEY(id),
   CONSTRAINT fk_veiculo_modelo
      FOREIGN KEY(id_modelo)
      REFERENCES modelo(id),
   CONSTRAINT fk_veiculo_cor
      FOREIGN KEY(id_cor)
      REFERENCES cor(id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS servico(
	id INT NOT NULL auto_increment,
    descricao varchar(100),
    valor double,
    pontos INT NOT NULL,
    categoria ENUM('PEQUENO', 'MEDIO', 'GRANDE', 'MOTO', 'PADRAO') NOT NULL DEFAULT 'PADRAO',
    CONSTRAINT pk_servico PRIMARY KEY (id)
)ENGINE = InnoDB;

/*
  TABELAS PARA IMPLEMENTAÇÃO DO CONCEITO DE CLASSES ASSOCIATIVAS OU CLASSES INTERMEDIÁRIAS, QUE 
  IMPLEMENTAM A RELAÇÃO DE MULTIPLICIDADE MUITOS PARA MUITOS (M:N)
  TABELAS: ordemServico, itemOS, veiculo e servico (estas duas últimas, já implementada)
*/
CREATE TABLE IF NOT EXISTS ordem_servico(
	numero int NOT NULL auto_increment,
    total decimal(10,2) NOT NULL,
    agenda DATE NOT NULL,
    desconto double,
    id_veiculo int NOT NULL,
    status ENUM('ABERTA', 'FECHADA', 'CANCELADA') NOT NULL DEFAULT 'ABERTA',
    CONSTRAINT pk_ordemServico 
		PRIMARY KEY (numero),
    CONSTRAINT fk_ordemServico_veiculo 
		FOREIGN KEY(id_veiculo) 
        REFERENCES veiculo(id)
)ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS item_OS(
	id int NOT NULL auto_increment,
	valorServico decimal(10,2),
    observacoes varchar(100),
    id_servico INT NOT NULL,
    id_ordem_servico INT NOT NULL,
	CONSTRAINT pk_item_OS 
		PRIMARY KEY (id),
    CONSTRAINT fk_item_OS_servico 
		FOREIGN KEY (id_servico) 
		REFERENCES servico(id), 
	CONSTRAINT fk_item_OS_ordem_servico 
		FOREIGN KEY (id_ordem_servico) 
		REFERENCES ordem_servico(numero)
        ON DELETE CASCADE
)ENGINE = InnoDB;

/*FIM DA IMPLEMENTAÇÃO DAS TABELAS M:N */

INSERT INTO marca(nome) VALUES('Chevrolet');
INSERT INTO marca(nome) VALUES('Fiat');
INSERT INTO marca(nome) VALUES('Toyota');
INSERT INTO marca(nome) VALUES('Foard');

INSERT INTO modelo(descricao, id_marca, categoria) VALUES('Equinox', 1, 'MEDIO');
INSERT INTO motor(id_modelo) (SELECT max(id) FROM modelo);
INSERT INTO modelo(descricao, id_marca, categoria) VALUES('Pulse', 2, 'MEDIO');
INSERT INTO motor(id_modelo) (SELECT max(id) FROM modelo);
INSERT INTO modelo(descricao, id_marca, categoria) VALUES('Corolla', 3, 'MEDIO');
INSERT INTO motor(id_modelo) (SELECT max(id) FROM modelo);
INSERT INTO modelo(descricao, id_marca, categoria) VALUES('Fusion', 4, 'MEDIO');
INSERT INTO motor(id_modelo) (SELECT max(id) FROM modelo);
INSERT INTO modelo(descricao, id_marca, categoria) VALUES('Corsa', 1, 'MEDIO');
INSERT INTO motor(id_modelo) (SELECT max(id) FROM modelo);

UPDATE motor SET potencia=172, tipoCombustivel='GASOLINA' WHERE id_modelo = 1;
UPDATE motor SET potencia=130, tipoCombustivel='ETANOL'   WHERE id_modelo = 2;
UPDATE motor SET potencia=177, tipoCombustivel='ETANOL'   WHERE id_modelo = 3;
UPDATE motor SET potencia=143, tipoCombustivel='FLEX'     WHERE id_modelo = 4;
UPDATE motor SET potencia=112, tipoCombustivel='GNV'      WHERE id_modelo = 5;

INSERT INTO cor(nome) VALUES('Preto');
INSERT INTO cor(nome) VALUES('Branco');
INSERT INTO cor(nome) VALUES('Azul');
INSERT INTO cor(nome) VALUES('Vermelho');
INSERT INTO cor(nome) VALUES('Cinza');

INSERT INTO cliente(nome, celular, email, endereco, dataCadastro, dataNascimento) VALUES('Edgar','(11) 1111-1111', 'edgar@ifsc.edu.br', 'Rua Wenceslau Corrêa de Souza', '1996-08-17', '1970-04-20');
INSERT INTO pessoa_fisica(id_cliente, cpf) VALUES((SELECT max(id) FROM cliente), '111.111.111-11');
INSERT INTO pontuacao(id_cliente, quantidade) VALUES((SELECT max(id) FROM cliente), 30);

INSERT INTO cliente(nome, celular, email, endereco, dataCadastro,  dataNascimento) VALUES('Marilene','(22) 2222-2121', 'marilene@ifsc.edu.br', 'Quadra 1007 Sul Alameda 1 A', '2003-06-02', '1979-10-18');
INSERT INTO pessoa_fisica(id_cliente, cpf) VALUES((SELECT max(id) FROM cliente), '222.222.222-22');
INSERT INTO pontuacao(id_cliente, quantidade) VALUES((SELECT max(id) FROM cliente), 0);

INSERT INTO cliente(nome, celular, email, endereco, dataCadastro, dataNascimento) VALUES('Carla','(33) 3333-3333', 'carla@ifsc.edu.br','Rua Santo Antônio', '1986-12-12', '1977-02-01');
INSERT INTO pessoa_juridica(id_cliente, cnpj, inscricaoEstadual) VALUES((SELECT max(id) FROM cliente), '11.719.809/0001-94', '040.240.270.481');
INSERT INTO pontuacao(id_cliente, quantidade) VALUES((SELECT max(id) FROM cliente), 150);

INSERT INTO cliente(nome, celular, email, endereco, dataCadastro, dataNascimento) VALUES('José','(44) 4444-4444', 'jose@ifsc.edu.br', 'Rua Isaura da Sílvia', '1968-03-07', '1975-08-27');
INSERT INTO pessoa_juridica(id_cliente, cnpj, inscricaoEstadual) VALUES((SELECT max(id) FROM cliente), '11.719.809/0001-94', '720.324.234.299');
INSERT INTO pontuacao(id_cliente, quantidade) VALUES((SELECT max(id) FROM cliente), 10);

INSERT INTO veiculo(placa, observacoes, id_modelo, id_cor, id_cliente) VALUES('KDC-0230', 'Carro 2010', 4, 1, 1);
INSERT INTO veiculo(placa, observacoes, id_modelo, id_cor, id_cliente) VALUES('NAJ-9579', 'Carro 2008', 5, 5, 2);
INSERT INTO veiculo(placa, observacoes, id_modelo, id_cor, id_cliente) VALUES('JVV-4555', 'Carro 2018', 3, 4, 3);
INSERT INTO veiculo(placa, observacoes, id_modelo, id_cor, id_cliente) VALUES('AZX-7081', 'Carro 2005', 1, 3, 4);

INSERT INTO servico(descricao, valor, pontos, categoria) VALUES('Lavação Pequena', 75.50, 10, 'PEQUENO');
INSERT INTO servico(descricao, valor, pontos, categoria) VALUES('Lavação Média', 115.25, 10, 'MEDIO');
INSERT INTO servico(descricao, valor, pontos, categoria) VALUES('Polimento Médio', 105.68, 10, 'MEDIO');
INSERT INTO servico(descricao, valor, pontos, categoria) VALUES('Lavação Grande', 180, 10, 'GRANDE');

INSERT INTO ordem_servico(total, agenda, desconto, id_veiculo, status) VALUES('190.75', '2022-11-20', '0.00', 1, 'FECHADA');
INSERT INTO item_os(valorServico, observacoes, id_servico, id_ordem_servico) VALUES(75.50, 'Lavação Bicicleta', 1, 1);
INSERT INTO item_os(valorServico, observacoes, id_servico, id_ordem_servico) VALUES(115.25, 'Lavação Mobi', 2, 1);