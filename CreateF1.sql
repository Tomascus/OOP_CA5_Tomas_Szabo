CREATE DATABASE F1;

USE F1;

CREATE TABLE Circuits (
    id INT AUTO_INCREMENT PRIMARY KEY,
    circuit_name VARCHAR(255),
    country VARCHAR(255),
    length FLOAT,
    turns INT
);

INSERT INTO Circuits (circuit_name, country, length, turns)
VALUES 
('Bahrain International Circuit', 'Bahrain', 5.4, 23),
('Jeddah Corniche Circuit', 'Saudi Arabia', 6.1, 27),
('Melbourne Grand Prix Circuit', 'Australia', 5.2, 14),
('Baku City Circuit', 'Azerbaijan', 6.0, 20),
('Miami International Autodrome', 'United States', 5.4, 19),
('Autodromo Enzo e Dino Ferrar', 'Italy', 4.9, 22),
('Circuit de Monaco', 'Monaco', 3.3 , 12),
('Circuit de Barcelona-Catalunya', 'Spain', 4.6, 16),
('Circuit Gilles Villeneuve', 'Canada', 4.3, 14),
('Red Bull Ring', 'Austria', 4.3, 10);