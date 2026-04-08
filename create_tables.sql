-- Créer la base de données si elle n'existe pas
CREATE DATABASE IF NOT EXISTS `esport-db`;
USE `esport-db`;

-- Table test
CREATE TABLE IF NOT EXISTS test (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Table jeu
CREATE TABLE IF NOT EXISTS jeu (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    genre VARCHAR(100),
    plateforme VARCHAR(100),
    description TEXT,
    statut VARCHAR(50)
);

-- Table tournoi
CREATE TABLE IF NOT EXISTS tournoi (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    date_debut DATE,
    date_fin DATE,
    statut VARCHAR(50),
    type VARCHAR(100),
    max_participants INT,
    cagnotte DOUBLE,
    date_inscription_limite DATE,
    frais_inscription DOUBLE,
    description TEXT,
    jeu_id INT,
    FOREIGN KEY (jeu_id) REFERENCES jeu(id)
);

-- Table profiling
CREATE TABLE IF NOT EXISTS profiling (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    genre VARCHAR(100),
    plateforme VARCHAR(100),
    description TEXT,
    statut VARCHAR(50)
);
