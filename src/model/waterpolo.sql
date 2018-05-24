CREATE TABLE equipo (
   id INT AUTO_INCREMENT PRIMARY KEY,
   nombre VARCHAR(30),
   ciudad VARCHAR(30),
   pais VARCHAR(30)
);
CREATE TABLE jugador (
   id INT AUTO_INCREMENT PRIMARY KEY,
   nombre VARCHAR(30),
   apellidos VARCHAR(30),
   edad INT,
   idequipo int NOT NULL,
   FOREIGN KEY (idequipo) REFERENCES equipo(id) ON DELETE CASCADE ON UPDATE CASCADE
)