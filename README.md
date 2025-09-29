# 🐾 Sistema de Gestión Veterinaria HappyFeet

## Descripción del Proyecto

HappyFeet es un sistema integral de gestión para clínicas veterinarias desarrollado en Java que implementa todos los módulos requeridos para el proyecto final. El sistema maneja desde la gestión básica de pacientes hasta actividades especiales como adopciones y jornadas de vacunación.

## 🎯 Módulos Implementados

### 1. 👥 Módulo de Gestión de Pacientes (Mascotas y Dueños)
- ✅ Registro completo de mascotas y dueños
- ✅ Gestión de relaciones mascota-dueño
- ✅ Historial médico detallado
- ✅ Contactos de emergencia

### 2. 🏥 Módulo de Servicios Médicos y Citas
- ✅ Sistema de agenda de citas
- ✅ Gestión de estados de citas
- ✅ Registro de consultas médicas
- ✅ Deducción automática de inventario al prescribir

### 3. 📦 Módulo de Inventario y Farmacia
- ✅ Control completo de stock
- ✅ Sistema de alertas inteligentes
- ✅ Gestión de proveedores
- ✅ Validación de productos vencidos

### 4. 💰 Módulo de Facturación y Reportes
- ✅ Generación de facturas en texto plano
- ✅ Reportes gerenciales completos
- ✅ Análisis de desempeño
- ✅ Reportes por período

### 5. 🎪 Módulo de Actividades Especiales
- ✅ **Días de Adopción**: Registro de mascotas y contratos
- ✅ **Jornadas de Vacunación**: Registro masivo optimizado
- ✅ **Club de Mascotas Frecuentes**: Sistema de puntos y beneficios

## 🛠️ Tecnologías Utilizadas

- **Java 17+**
- **MySQL 8.0+**
- **JDBC** para conexión a base de datos
- **Arquitectura DAO/Service** para separación de responsabilidades

## 📋 Requisitos del Sistema

1. **Java Development Kit (JDK) 17 o superior**
2. **MySQL Server 8.0 o superior**
3. **MySQL Connector/J** (incluido en el proyecto)
4. **IDE** (recomendado: IntelliJ IDEA o Eclipse)

## 🚀 Instalación y Configuración

### 1. Configuración de la Base de Datos

```sql
-- Crear la base de datos
CREATE DATABASE happyfeet;

-- Ejecutar el script de esquema
mysql -u root -p happyfeet < database/schema.sql

-- Cargar datos de ejemplo
mysql -u root -p happyfeet < database/data.sql
```

### 2. Configuración de Conexión

Actualizar las credenciales en `DBConnection.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/happyfeet";
private static final String USER = "tu_usuario";
private static final String PASSWORD = "tu_contraseña";
```

### 3. Compilación y Ejecución

```bash
# Compilar el proyecto
javac -cp "lib/*" src/main/java/com/happyfeet/*.java src/main/java/com/happyfeet/*/*.java

# Ejecutar la aplicación
java -cp "lib/*:src/main/java" com.happyfeet.Main
```

## 📖 Guía de Uso

### Menú Principal

El sistema presenta un menú principal con los siguientes módulos:

```
🐾 CLÍNICA VETERINARIA HAPPYFEET 🐾
1. 👥 Gestión de Pacientes (Dueños y Mascotas)
2. 🏥 Servicios Médicos y Citas
3. 📦 Inventario y Farmacia
4. 💰 Facturación
5. 🎪 Actividades Especiales
6. 📊 Reportes Gerenciales
7. 🔔 Ver Alertas
0. 🚪 Salir
```

### Funcionalidades Destacadas

#### 🎁 Club de Puntos
- Los clientes acumulan puntos automáticamente
- 4 niveles de membresía: Bronce, Plata, Oro, Platino
- Catálogo de beneficios canjeables
- Descuentos automáticos por nivel

#### 🏠 Sistema de Adopciones
- Registro de mascotas para adopción
- Generación automática de contratos
- Seguimiento de estados de adopción
- Estadísticas de adopción

#### 💉 Jornadas de Vacunación
- Creación y gestión de jornadas
- Registro masivo y rápido
- Deducción automática de vacunas del inventario
- Estadísticas por jornada

#### 🔔 Sistema de Alertas
- Productos con stock bajo
- Productos próximos a vencer
- Productos vencidos
- Indicador visual en el menú principal

## 📊 Reportes Disponibles

### Reportes Gerenciales
- **Servicios más solicitados**
- **Análisis de facturación**
- **Estado de citas**
- **Estado del inventario**
- **Reporte completo**
- **Reportes por período personalizado**

### Ejemplo de Factura Generada

```
===============================================
           CLÍNICA VETERINARIA HAPPYFEET
===============================================
  Dirección: Calle Principal #123, Ciudad
  Teléfono: (607) 123-4567
  Email: info@happyfeet.com
===============================================

FACTURA N°: 000001
Fecha: 28/09/2025 14:30

CLIENTE:
Nombre: Carlos Rueda
Documento: 1234567890

DETALLE DE SERVICIOS Y PRODUCTOS:
-----------------------------------------------
DESCRIPCIÓN           CANT     PRECIO  SUBTOTAL
-----------------------------------------------
Consulta general         1    5000.00   5000.00
Vacuna Triple Felina     1  120000.00 120000.00
-----------------------------------------------
                        TOTAL A PAGAR: $ 125000.00
===============================================
```

## 🏗️ Arquitectura del Proyecto

```
src/main/java/com/happyfeet/
├── Main.java                 # Punto de entrada
├── MainMenu.java            # Menú principal unificado
├── model/                   # Modelos de datos
│   ├── Dueno.java
│   ├── Mascota.java
│   ├── Cita.java
│   ├── HistorialMedico.java
│   ├── Inventario.java
│   ├── Factura.java
│   ├── ItemFactura.java
│   ├── MascotaAdopcion.java
│   ├── JornadaVacunacion.java
│   └── ClubPuntos.java
├── dao/                     # Acceso a datos
│   ├── DuenoDAO.java
│   ├── MascotaDAO.java
│   ├── CitaDAO.java
│   ├── InventarioDAO.java
│   └── FacturaDAO.java
├── service/                 # Lógica de negocio
│   ├── DuenoService.java
│   ├── MascotaService.java
│   ├── CitaService.java
│   ├── InventarioService.java
│   ├── FacturacionService.java
│   ├── ReportesService.java
│   ├── AlertasService.java
│   ├── AdopcionService.java
│   ├── JornadaService.java
│   └── ClubService.java
└── util/
    └── DBConnection.java    # Conexión a BD
```

## 🎮 Datos de Prueba

El sistema incluye datos de ejemplo:
- 3 dueños registrados
- 6 mascotas con fotos
- Historial médico
- Productos en inventario
- Facturas de ejemplo
- Miembros del club de puntos

## 🔧 Lógica de Negocio Implementada

### Validaciones Automáticas
- ✅ No permitir citas en el pasado
- ✅ No agregar productos vencidos al inventario
- ✅ Validar stock antes de deducir
- ✅ Restricciones de integridad referencial

### Reglas de Negocio
- ✅ Deducción automática de inventario al prescribir
- ✅ Otorgamiento automático de puntos por compras (1 punto por $1000 COP)
- ✅ Aplicación automática de descuentos por nivel de membresía
- ✅ Alertas automáticas de stock bajo y vencimientos

## 👥 Desarrolladores

- **Juan Montero**
- **Kennei Romero**

## 📄 Licencia

Este proyecto fue desarrollado como proyecto de Java en campuslands.

---
