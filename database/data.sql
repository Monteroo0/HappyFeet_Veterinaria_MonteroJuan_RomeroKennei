-- ===========================
-- POBLADO DE TABLAS MAESTRAS
-- ===========================

-- Especies
INSERT INTO especies (nombre) VALUES
('Perro'),
('Gato'),
('Ave'),
('Conejo'),
('Reptil');

-- Razas
INSERT INTO razas (especie_id, nombre) VALUES
(1, 'Labrador Retriever'),
(1, 'Bulldog Francés'),
(1, 'Pastor Alemán'),
(2, 'Persa'),
(2, 'Siames'),
(3, 'Canario'),
(3, 'Periquito'),
(4, 'Conejo Enano'),
(5, 'Camaleón'),
(5, 'Iguana'),
(5, 'Serpiente');

-- Tipos de productos
INSERT INTO producto_tipos (nombre) VALUES
('Medicamento'),
('Vacuna'),
('Insumo Médico'),
('Alimento');

-- Tipos de eventos
INSERT INTO evento_tipos (nombre) VALUES
('Vacunación'),
('Consulta'),
('Cirugía'),
('Desparasitación');

-- Estados de cita
INSERT INTO cita_estados (nombre) VALUES
('Programada'),
('En Proceso'),
('Finalizada'),
('Cancelada');

-- ===========================
-- TABLAS OPERACIONALES
-- ===========================

-- Dueños
INSERT INTO duenos (nombre_completo, documento_identidad, direccion, telefono, email) VALUES
('Carlos Rueda', '1234567890', 'Carrera 27 #45-12, Bucaramanga', '6071234567', 'carlos.perez@email.com'),
('María López', '2345678901', 'Calle 36 #15-20, Bucaramanga', '6072345678', 'maria.lopez@email.com'),
('Ana Torres', '3456789012', 'Avenida Quebrada Seca #9-85, Bucaramanga', '6073456789', 'ana.torres@email.com');

-- Mascotas
INSERT INTO mascotas (dueno_id, nombre, raza_id, fecha_nacimiento, sexo, url_foto) VALUES
(1, 'Firulais', 1, '2021-05-15', 'Macho', 'https://cdn.discordapp.com/attachments/708676160208240721/1419796648787185734/image.png?ex=68d3102b&is=68d1beab&hm=6fcabc95b26a39f19ce3fac615e0dd717df441299f0df0b34d6d291753a611b4&'),
(1, 'Michi', 4, '2020-03-22', 'Hembra', 'https://cdn.discordapp.com/attachments/708676160208240721/1419798499310043166/D_NQ_NP_718744-MCO77474695098_072024-O.png?ex=68d311e4&is=68d1c064&hm=25682d82a42799bd75b5a3b3ca5f22b7c17d1a49854fd2c0493fe1b84763dd21&'),
(2, 'Luna', 2, '2022-01-10', 'Hembra', 'https://cdn.discordapp.com/attachments/708676160208240721/1419799105781366805/BULLDOG-FRANCC389S.png?ex=68d31275&is=68d1c0f5&hm=c0b3b705e752f450b251140ac4f3b9f2a89ac5977d203772a94b3968e5c322f3&'),
(2, 'Paco Amoroso', 9, '2023-07-08', 'Macho', 'https://cdn.discordapp.com/attachments/708676160208240721/1419801713988206752/images.png?ex=68d314e3&is=68d1c363&hm=dd5888bcf3f2e3dda089b7d188d9d9ca91b6358430a1d3a6ef58adafaae2efe0&'),
(3, 'Rodolfo', 3, '2019-08-08', 'Macho', 'https://cdn.discordapp.com/attachments/708676160208240721/1419799600067510302/images.png?ex=68d312eb&is=68d1c16b&hm=a1f9d6cd035d1b62f59ac66eb08443cb700aed2e22ee22311f81709172537bb6&'),
(3, 'Copito', 8, '2023-04-12', 'Macho', 'https://cdn.discordapp.com/attachments/708676160208240721/1419799765649981513/toy-o-enano_341_0_600.png?ex=68d31312&is=68d1c192&hm=6addbe628219f37aab61cde76941267e518b82a212718cfd499029195922291e&');


-- Historial médico (fechas dentro del rango)
INSERT INTO historial_medico (mascota_id, fecha_evento, evento_tipo_id, descripcion, diagnostico, tratamiento_recomendado) VALUES
(1, '2025-01-15', 2, 'Revisión general', 'Saludable', 'Revisión anual'),
(2, '2025-03-10', 1, 'Aplicación de vacuna triple felina', 'N/A', 'Refuerzo en 1 año'),
(3, '2025-05-05', 4, 'Tratamiento antiparasitario', 'Presencia de parásitos', 'Continuar tratamiento por 3 meses'),
(4, '2025-07-18', 3, 'Cirugía de esterilización', 'N/A', 'Revisión post-operatoria'),
(5, '2025-09-10', 2, 'Consulta por inapetencia', 'Gastritis leve', 'Dieta blanda + medicación');

-- Inventario (valores en COP, fechas válidas)
INSERT INTO inventario (nombre_producto, producto_tipo_id, descripcion, fabricante, lote, cantidad_stock, stock_minimo, fecha_vencimiento, precio_venta) VALUES
('Antiparasitario X', 1, 'Tabletas antiparasitarias para perros', 'VetPharma', 'L001', 50, 10, '2025-09-20', 85000.00),
('Vacuna Triple Felina', 2, 'Protección contra rinotraqueitis, calicivirus y panleucopenia', 'CatHealth', 'L002', 30, 5, '2025-09-15', 120000.00),
('Guantes quirúrgicos', 3, 'Guantes de látex estériles', 'MediSafe', 'L003', 200, 50, '2025-09-22', 1500.00),
('Alimento Premium Perro 10kg', 4, 'Bolsa de 10kg', 'NutriPets', 'L004', 100, 20, '2025-09-22', 180000.00);

-- Citas (fechas en rango)
INSERT INTO citas (mascota_id, fecha_hora, motivo, estado_id) VALUES
(1, '2025-02-15 10:00:00', 'Vacunación anual', 1),
(2, '2025-04-10 15:30:00', 'Consulta de control', 2),
(3, '2025-06-05 09:00:00', 'Cirugía programada', 1),
(4, '2025-08-18 11:00:00', 'Desparasitación', 3),
(5, '2025-09-20 14:00:00', 'Chequeo general', 4);

-- Facturas
INSERT INTO facturas (dueno_id, fecha_emision, total) VALUES
(1, '2025-02-15 11:00:00', 175000.00),
(2, '2025-04-10 16:00:00', 120000.00),
(3, '2025-06-05 12:00:00', 180000.00);

-- Ítems de factura (COP)
INSERT INTO items_factura (factura_id, producto_id, servicio_descripcion, cantidad, precio_unitario, subtotal) VALUES
(1, 1, NULL, 2, 85000.00, 170000.00),
(1, NULL, 'Consulta general', 1, 5000.00, 5000.00),
(2, 2, NULL, 1, 120000.00, 120000.00),
(3, 4, NULL, 1, 180000.00, 180000.00);

