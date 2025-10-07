use happyfeet;

-- =========================================
-- TABLAS MAESTRAS
-- =========================================

CREATE TABLE especies (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE razas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    especie_id INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    FOREIGN KEY (especie_id) REFERENCES especies(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE producto_tipos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL -- 'Medicamento', 'Vacuna', 'Insumo Médico', 'Alimento'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE evento_tipos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL -- 'Vacunación', 'Consulta', 'Cirugía', 'Desparasitación'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE cita_estados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) UNIQUE NOT NULL -- 'Programada', 'En Proceso', 'Finalizada', 'Cancelada'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================================
-- TABLAS OPERACIONALES DEL NEGOCIO
-- =========================================

CREATE TABLE duenos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_completo VARCHAR(255) NOT NULL,
    documento_identidad VARCHAR(20) UNIQUE NOT NULL,
    direccion VARCHAR(255),
    telefono VARCHAR(20),
    email VARCHAR(100) UNIQUE NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE mascotas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dueno_id INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    raza_id INT,
    fecha_nacimiento DATE,
    sexo ENUM('Macho', 'Hembra'),
    url_foto VARCHAR(255),
    FOREIGN KEY (dueno_id) REFERENCES duenos(id)
        ON DELETE CASCADE,
    FOREIGN KEY (raza_id) REFERENCES razas(id)
        ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE historial_medico (
    id INT AUTO_INCREMENT PRIMARY KEY,
    mascota_id INT NOT NULL,
    fecha_evento DATE NOT NULL,
    evento_tipo_id INT NOT NULL,
    descripcion TEXT,
    diagnostico TEXT,
    tratamiento_recomendado TEXT,
    FOREIGN KEY (mascota_id) REFERENCES mascotas(id)
        ON DELETE CASCADE,
    FOREIGN KEY (evento_tipo_id) REFERENCES evento_tipos(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE inventario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_producto VARCHAR(255) NOT NULL,
    producto_tipo_id INT NOT NULL,
    descripcion TEXT,
    fabricante VARCHAR(100),
    lote VARCHAR(50),
    cantidad_stock INT NOT NULL,
    stock_minimo INT NOT NULL,
    fecha_vencimiento DATE,
    precio_venta DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (producto_tipo_id) REFERENCES producto_tipos(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE citas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    mascota_id INT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    motivo VARCHAR(255),
    estado_id INT NOT NULL,
    FOREIGN KEY (mascota_id) REFERENCES mascotas(id)
        ON DELETE CASCADE,
    FOREIGN KEY (estado_id) REFERENCES cita_estados(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE facturas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dueno_id INT NOT NULL,
    fecha_emision DATETIME NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (dueno_id) REFERENCES duenos(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE items_factura (
    id INT AUTO_INCREMENT PRIMARY KEY,
    factura_id INT NOT NULL,
    producto_id INT, -- Puede ser un producto del inventario
    servicio_descripcion VARCHAR(255), -- O un servicio
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (factura_id) REFERENCES facturas(id)
        ON DELETE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES inventario(id)
        ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================================
-- TABLAS PARA ACTIVIDADES ESPECIALES
-- =========================================

CREATE TABLE proveedores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    contacto VARCHAR(255),
    telefono VARCHAR(20),
    email VARCHAR(100),
    direccion VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE mascotas_adopcion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    raza_id INT,
    edad_estimada VARCHAR(50),
    sexo ENUM('Macho', 'Hembra'),
    descripcion TEXT,
    estado ENUM('Disponible', 'En Proceso', 'Adoptada') DEFAULT 'Disponible',
    fecha_ingreso DATE NOT NULL,
    url_foto VARCHAR(255),
    FOREIGN KEY (raza_id) REFERENCES razas(id)
        ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE contratos_adopcion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    mascota_adopcion_id INT NOT NULL,
    adoptante_nombre VARCHAR(255) NOT NULL,
    adoptante_documento VARCHAR(20) NOT NULL,
    adoptante_telefono VARCHAR(20),
    adoptante_direccion VARCHAR(255),
    fecha_adopcion DATE NOT NULL,
    contrato_texto TEXT NOT NULL,
    FOREIGN KEY (mascota_adopcion_id) REFERENCES mascotas_adopcion(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE jornadas_vacunacion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    descripcion TEXT,
    activa BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE registros_jornada (
    id INT AUTO_INCREMENT PRIMARY KEY,
    jornada_id INT NOT NULL,
    mascota_nombre VARCHAR(100) NOT NULL,
    dueno_nombre VARCHAR(255) NOT NULL,
    dueno_telefono VARCHAR(20),
    vacuna_aplicada VARCHAR(255) NOT NULL,
    fecha_aplicacion DATETIME NOT NULL,
    FOREIGN KEY (jornada_id) REFERENCES jornadas_vacunacion(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE club_puntos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dueno_id INT NOT NULL,
    puntos_acumulados INT DEFAULT 0,
    fecha_ultima_actividad DATE,
    FOREIGN KEY (dueno_id) REFERENCES duenos(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE transacciones_puntos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    club_puntos_id INT NOT NULL,
    tipo ENUM('Ganados', 'Canjeados') NOT NULL,
    puntos INT NOT NULL,
    descripcion VARCHAR(255),
    fecha_transaccion DATETIME NOT NULL,
    FOREIGN KEY (club_puntos_id) REFERENCES club_puntos(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE contactos_emergencia (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dueno_id INT NOT NULL,
    nombre_contacto VARCHAR(255) NOT NULL,
    telefono_contacto VARCHAR(20) NOT NULL,
    relacion VARCHAR(100),
    FOREIGN KEY (dueno_id) REFERENCES duenos(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================================
-- TABLA PARA PROCEDIMIENTOS ESPECIALES
-- =========================================

CREATE TABLE procedimientos_especiales (
    id INT AUTO_INCREMENT PRIMARY KEY,
    mascota_id INT NOT NULL,
    tipo_procedimiento ENUM('Cirugía', 'Tratamiento Complejo', 'Procedimiento Especializado') NOT NULL,
    fecha_procedimiento DATETIME NOT NULL,
    veterinario_responsable VARCHAR(255) NOT NULL,

    -- Información preoperatoria
    diagnostico_preoperatorio TEXT NOT NULL,
    analisis_previos TEXT,
    medicacion_previa TEXT,
    ayuno_requerido BOOLEAN DEFAULT FALSE,
    alergias_conocidas TEXT,

    -- Detalles del procedimiento
    descripcion_procedimiento TEXT NOT NULL,
    anestesia_utilizada VARCHAR(255),
    duracion_minutos INT,
    complicaciones TEXT,
    observaciones TEXT,

    -- Seguimiento postoperatorio
    medicacion_postoperatoria TEXT,
    cuidados_especiales TEXT NOT NULL,
    proxima_revision DATE,
    restricciones TEXT,
    estado_actual ENUM('En Recuperación', 'Seguimiento', 'Alta Médica') DEFAULT 'En Recuperación',

    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (mascota_id) REFERENCES mascotas(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================================
-- TABLA PARA HISTORIAL DE TRANSFERENCIAS
-- =========================================

CREATE TABLE transferencias_propiedad (
    id INT AUTO_INCREMENT PRIMARY KEY,
    mascota_id INT NOT NULL,
    dueno_anterior_id INT NOT NULL,
    dueno_nuevo_id INT NOT NULL,
    fecha_transferencia DATETIME NOT NULL,
    motivo VARCHAR(255),
    observaciones TEXT,
    FOREIGN KEY (mascota_id) REFERENCES mascotas(id)
        ON DELETE CASCADE,
    FOREIGN KEY (dueno_anterior_id) REFERENCES duenos(id)
        ON DELETE CASCADE,
    FOREIGN KEY (dueno_nuevo_id) REFERENCES duenos(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================================
-- TABLA PARA RELACIÓN INVENTARIO-PROVEEDORES
-- =========================================

CREATE TABLE inventario_proveedores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    inventario_id INT NOT NULL,
    proveedor_id INT NOT NULL,
    es_proveedor_principal BOOLEAN DEFAULT FALSE,
    precio_compra DECIMAL(10, 2),
    tiempo_entrega_dias INT,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (inventario_id) REFERENCES inventario(id)
        ON DELETE CASCADE,
    FOREIGN KEY (proveedor_id) REFERENCES proveedores(id)
        ON DELETE CASCADE,
    UNIQUE KEY unique_inventario_proveedor (inventario_id, proveedor_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;