CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE Tipos_Rol(
    id_tipo_rol SERIAL PRIMARY KEY,
    nombre VARCHAR(20) UNIQUE NOT NULL
);

INSERT INTO Tipos_Rol (nombre) VALUES ('Cliente'), ('Soporte'), ('Administrador');

CREATE TABLE Roles(
    id_rol SERIAL PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL,
    capacidad INT NOT NULL CHECK(capacidad > 0),
    tipo_rol INT NOT NULL,
    FOREIGN KEY (tipo_rol) REFERENCES Tipos_Rol(id_tipo_rol)
);

INSERT INTO Roles (nombre, capacidad, tipo_rol) VALUES
    ('Cliente', 50, 1), 
    ('Soporte Técnico', 20, 2), 
    ('Administrador', 4, 3);

CREATE TABLE Usuarios(
    id_usuario SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    telefono VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(250) UNIQUE NOT NULL,
    contrasena VARCHAR(260) NOT NULL,
    tel_contacto VARCHAR(20) NOT NULL,
    id_rol INT NOT NULL,
    FOREIGN KEY (id_rol) REFERENCES Roles(id_rol)
);
CREATE OR REPLACE FUNCTION encrypt_password()
RETURNS TRIGGER AS $$
BEGIN
    -- Encripta la contraseña usando crypt y el salt
    NEW.contrasena := crypt(NEW.contrasena, gen_salt('bf')); -- Usamos Blowfish (bf) como algoritmo de encriptación
    RETURN NEW;
END;
$$ LANGUAGE plpgsql

CREATE TRIGGER trigger_encrypt_password
BEFORE INSERT ON Usuarios
FOR EACH ROW
EXECUTE FUNCTION encrypt_password();

INSERT INTO Usuarios (nombre, apellido, telefono, email, contrasena, tel_contacto, id_rol) VALUES
    ('Xavier Alexander', 'Ávila Posada', '7780-1219', 'xavieravilaposada@gmail.com', 'admin', '2447-5225', 3);

CREATE TABLE Tickets(
    id_ticket SERIAL PRIMARY KEY,
    estado VARCHAR(35) CHECK (estado IN ('CREADO', 'ASIGNADO', 'EN ESPERA DE INFORMACIÓN', 'EN RESOLUCIÓN', 'RESUELTO')),
    descripcion TEXT NOT NULL,
    prioridad VARCHAR(20) CHECK (prioridad IN ('BAJA', 'NORMAL', 'IMPORTANTE', 'CRÍTICA')),
    servicio VARCHAR(250) NOT NULL,
    resuelta BOOLEAN DEFAULT FALSE,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_encargado INT,
    id_cliente INT NOT NULL,
    FOREIGN KEY (id_encargado) REFERENCES Usuarios(id_usuario),
    FOREIGN KEY (id_cliente) REFERENCES Usuarios(id_usuario)
);

CREATE TABLE Archivos(
    id_archivo SERIAL PRIMARY KEY,
    url VARCHAR(400) NOT NULL,
    id_ticket INT NOT NULL,
    FOREIGN KEY (id_ticket) REFERENCES Tickets(id_ticket)
);

CREATE TABLE Notificaciones(
    id_notificacion SERIAL PRIMARY KEY,
    dato VARCHAR(500) NOT NULL,
    url_archivo VARCHAR(400) NOT NULL,
    notificar_cliente BOOLEAN DEFAULT FALSE,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    autogenerada BOOLEAN DEFAULT TRUE,
    remitente INT,
    id_ticket INT NOT NULL,
    FOREIGN KEY (remitente) REFERENCES Usuarios(id_usuario),
    FOREIGN KEY (id_ticket) REFERENCES Tickets(id_ticket)
);

CREATE TABLE Tareas(
    id_tarea SERIAL PRIMARY KEY,
    nombre VARCHAR(70) NOT NULL,
    info VARCHAR(250) NOT NULL,
    prioridad VARCHAR(20) CHECK (prioridad IN ('BAJA', 'NORMAL', 'IMPORTANTE', 'CRÍTICA')),
    estado TEXT NOT NULL,
    completada BOOLEAN DEFAULT FALSE,
    id_ticket INT NOT NULL,
    id_encargado INT,
    FOREIGN KEY (id_encargado) REFERENCES Usuarios(id_usuario),
    FOREIGN KEY (id_ticket) REFERENCES Tickets(id_ticket)
);
