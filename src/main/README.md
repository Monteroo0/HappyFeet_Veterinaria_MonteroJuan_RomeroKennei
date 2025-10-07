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

## 📊 Estado Actual del Proyecto

### ✨ Funcionalidades Implementadas

El sistema HappyFeet está **completamente funcional** y cubre todos los módulos requeridos para la gestión integral de una clínica veterinaria. A continuación se detalla el análisis completo del estado actual:

#### 1. **Módulo de Gestión de Pacientes** ✅
**Ubicación:** `src/main/java/com/happyfeet/view/PacienteView.java`

**Funcionalidades:**
- Registro completo de dueños con validación de datos
- Registro de mascotas vinculadas a sus dueños
- Consulta de historial médico por mascota
- Registro de dueños en el club de puntos
- Sistema de transferencia de propiedad de mascotas

**Ejemplo de ejecución en consola:**
```
👥 MÓDULO: GESTIÓN DE PACIENTES
1. Registrar dueño
> Nombre completo: María García
> Documento: 1234567890
> Teléfono: 3001234567
✅ Dueño registrado exitosamente

2. Registrar mascota
> Seleccione dueño: 1. María García
> Nombre: Luna
> Raza: Labrador
> Sexo: Hembra
✅ Mascota registrada exitosamente
```

#### 2. **Módulo de Servicios Médicos y Citas** ✅
**Ubicación:** `src/main/java/com/happyfeet/view/ServiciosMedicosView.java`

**Funcionalidades:**
- Programación de citas con validación de fechas futuras
- Gestión de estados de citas (Programada, En Proceso, Finalizada, Cancelada)
- Registro de consultas médicas con diagnóstico y tratamiento
- Deducción automática de inventario al prescribir medicamentos

**Cómo funciona:**
Al prescribir un medicamento durante una consulta, el sistema automáticamente:
1. Verifica disponibilidad en inventario
2. Deduce la cantidad prescrita
3. Registra el evento en el historial médico
4. Actualiza los niveles de stock

#### 3. **Módulo de Inventario y Farmacia** ✅
**Ubicación:** `src/main/java/com/happyfeet/view/InventarioView.java`

**Funcionalidades:**
- Control completo de stock con niveles mínimos
- Sistema de alertas inteligentes (stock bajo, productos próximos a vencer, vencidos)
- Gestión de proveedores con múltiples proveedores por producto
- Relación inventario-proveedor con precio de compra y tiempos de entrega
- Validación automática de productos vencidos

**Ejemplo de alertas:**
```
🔔 ALERTAS DEL SISTEMA
⚠️  PRODUCTOS CON STOCK BAJO:
   • Vacuna Triple Felina - Stock: 3 (Mínimo: 5)
📅 PRODUCTOS PRÓXIMOS A VENCER (30 días):
   • Antibiótico XYZ - Vence: 2025-10-15
```

#### 4. **Módulo de Facturación** ✅
**Ubicación:** `src/main/java/com/happyfeet/view/FacturacionView.java`

**Funcionalidades:**
- Generación de facturas en formato texto plano profesional
- Inclusión de servicios y productos en una misma factura
- Cálculo automático de totales
- Integración con el club de puntos (otorgamiento automático)
- Registro de todas las transacciones

**Proceso de facturación:**
1. Seleccionar cliente
2. Agregar servicios (consultas, cirugías, etc.)
3. Agregar productos del inventario
4. Sistema calcula total automáticamente
5. Genera factura en formato profesional
6. Otorga puntos al cliente (1 punto por cada $1000 COP)

#### 5. **Módulo de Actividades Especiales** ✅
**Ubicación:** `src/main/java/com/happyfeet/view/ActividadesView.java`

**Funcionalidades completas:**

**a) Días de Adopción:**
- Registro de mascotas disponibles para adopción
- Estados: Disponible, En Proceso, Adoptada
- Generación automática de contratos de adopción
- Seguimiento completo del proceso

**b) Jornadas de Vacunación:**
- Creación y gestión de jornadas masivas
- Registro rápido de mascotas y aplicación de vacunas
- Deducción automática del inventario
- Estadísticas por jornada

**c) Club de Mascotas Frecuentes:**
- Sistema de puntos automático
- 4 niveles: Bronce (0-499), Plata (500-999), Oro (1000-1999), Platino (2000+)
- Catálogo de beneficios canjeables
- Historial de transacciones

#### 6. **Módulo de Reportes Gerenciales** ✅
**Ubicación:** `src/main/java/com/happyfeet/service/ReportesService.java`

**Reportes disponibles:**
- **Servicios más solicitados:** Análisis de consultas, vacunaciones, cirugías
- **Análisis de facturación:** Total facturado, promedios, top 5 clientes
- **Estado de citas:** Distribución por estado, citas del mes, próximas citas
- **Estado del inventario:** Valor total, productos por tipo, estado general
- **Reportes por período:** Facturación personalizada por rango de fechas

**Ejemplo de reporte:**
```
📊 REPORTE GERENCIAL COMPLETO
Fecha: 28/09/2025

💰 REPORTE DE FACTURACIÓN
Total de facturas: 45
Total facturado: $12,450,000.00
Promedio por factura: $276,666.67

Top 5 Clientes:
  • Carlos Rueda: $3,200,000.00
  • Ana Pérez: $2,100,000.00
```

### 🆕 Mejoras y Cambios Recientes

Basado en el análisis de los commits recientes, se han implementado las siguientes mejoras:

#### **Commit e9fbbde** - Documentación
- Agregado archivo README.md con documentación completa
- Descripción detallada de módulos y funcionalidades

#### **Commit c1bb375** - Módulo de Inventario Completo
- Implementación del sistema de relación inventario-proveedores
- Nuevas entidades: `InventarioProveedor`
- Sistema de proveedores principales por producto
- Cálculo de precios promedio de compra
- Reportes de productos por proveedor

#### **Commit 698df60 y 2c9214c** - Módulo de Actividades Especiales
- Implementación completa de procedimientos especiales médicos
- Nueva entidad: `ProcedimientoEspecial` para cirugías y tratamientos complejos
- Sistema de seguimiento pre y post operatorio
- Estados de recuperación: En Recuperación, Seguimiento, Alta Médica
- Sistema de transferencia de propiedad con historial completo
- Patrón Observer para notificaciones de transferencias

#### **Commit 2f4799c** - Módulo de Facturas
- Sistema completo de facturación con formato profesional
- Integración automática con club de puntos
- Inclusión de servicios y productos en facturas

#### **Commit 95e6d7f y 50d348c** - Módulo de Servicios Médicos
- Sistema completo de gestión de citas
- Deducción automática de inventario al prescribir
- Validaciones de fechas y estados

#### **Commit 869a0c2** - Módulo de Pacientes
- Sistema completo de gestión de dueños y mascotas
- Historial médico detallado
- Contactos de emergencia

### 🛠️ Tecnologías y Patrones Implementados

**Patrones de Diseño:**
- **DAO (Data Access Object):** Separación completa de lógica de acceso a datos
- **Service Layer:** Capa de servicios para lógica de negocio
- **MVC adaptado:** Modelo-Vista-Servicio para aplicaciones de consola
- **Observer:** Implementado en `TransferenciaPropiedadService` para notificaciones
- **Strategy (implícito):** En el manejo de diferentes tipos de procedimientos

**Programación Funcional:**
El proyecto hace uso extensivo de **Java Streams** y **expresiones lambda**:
- `InventarioProveedorService`: Uso de `stream()`, `filter()`, `collect()`, `groupingBy()`
- `ProcedimientoEspecialService`: Filtrado funcional por estado y fechas
- `ReportesService`: Agrupación y análisis de datos con streams
- `TransferenciaPropiedadService`: Filtrado por fechas y ordenamiento funcional

**Ejemplo de programación funcional:**
```java
// Cálculo de precio promedio usando streams
public BigDecimal calcularPrecioPromedioCompra(int inventarioId) {
    return proveedores.stream()
        .map(InventarioProveedor::getPrecioCompra)
        .filter(precio -> precio != null)
        .reduce(BigDecimal.ZERO, BigDecimal::add)
        .divide(BigDecimal.valueOf(proveedores.size()));
}
```

### 🎯 Estado de Estabilidad

**Errores Corregidos:**
- ✅ Validación de fechas en citas (no permite fechas pasadas)
- ✅ Validación de productos vencidos (no permite agregar al inventario)
- ✅ Manejo de excepciones en entrada de datos
- ✅ Validación de stock antes de deducir
- ✅ Integridad referencial en todas las tablas

**Estado Actual:**
- ✅ **Sistema 100% funcional** - Todos los módulos operativos
- ✅ **Base de datos robusta** - 17 tablas con relaciones bien definidas
- ✅ **Validaciones completas** - Todos los flujos críticos validados
- ✅ **Sin bugs conocidos** - Sistema estable para uso en producción
- ✅ **Código limpio** - Separación de responsabilidades clara

### 🚀 Posibles Mejoras Futuras

Aunque el sistema está completo y funcional, estas son algunas mejoras potenciales:

1. **Interfaz Gráfica:**
   - Migración a JavaFX o Swing para una interfaz visual
   - Dashboard con gráficos y estadísticas en tiempo real

2. **Seguridad:**
   - Sistema de autenticación de usuarios
   - Roles y permisos (administrador, veterinario, recepcionista)
   - Encriptación de datos sensibles

3. **Reportes Avanzados:**
   - Exportación de reportes a PDF
   - Gráficos estadísticos
   - Envío de reportes por email

4. **Notificaciones:**
   - Sistema de recordatorios de citas por SMS/email
   - Alertas automáticas de vencimientos a proveedores
   - Notificaciones de stock bajo

5. **Respaldos Automáticos:**
   - Sistema de backup automático de base de datos
   - Recuperación ante desastres

6. **API REST:**
   - Exposición de funcionalidades vía API
   - Integración con aplicaciones móviles
   - Sincronización multi-sucursal

### 📈 Casos de Uso Principales

#### Caso 1: Consulta Veterinaria Completa
```
1. Usuario accede al módulo de Servicios Médicos
2. Agenda cita para mascota "Luna" (validación de fecha futura)
3. Sistema registra cita en estado "Programada"
4. Durante consulta, cambia estado a "En Proceso"
5. Veterinario registra diagnóstico y prescribe medicamento
6. Sistema deduce automáticamente del inventario
7. Finaliza consulta, estado cambia a "Finalizada"
8. Sistema genera factura con servicio + medicamento
9. Cliente acumula puntos automáticamente
```

#### Caso 2: Jornada de Vacunación Masiva
```
1. Usuario crea jornada "Antirrábica 2025"
2. Define fechas y descripción
3. Registra mascotas participantes rápidamente
4. Por cada registro, sistema deduce vacuna del inventario
5. Al final, consulta estadísticas de la jornada
6. Sistema muestra total de mascotas vacunadas y vacunas utilizadas
```

#### Caso 3: Gestión de Inventario con Proveedores
```
1. Usuario registra producto "Vacuna Triple Felina"
2. Asocia múltiples proveedores con precios diferentes
3. Marca un proveedor como principal
4. Sistema calcula precio promedio de compra
5. Cuando stock llega al mínimo, genera alerta
6. Usuario consulta proveedor más rápido en entregas
7. Realiza pedido al proveedor óptimo
```

## 👥 Desarrolladores

- **Juan Montero**
- **Kennei Romero**

## 📄 Licencia

Este proyecto fue desarrollado como proyecto de Java en Campuslands.

---
