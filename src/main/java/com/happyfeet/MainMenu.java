package com.happyfeet;

import com.happyfeet.model.*;
import com.happyfeet.service.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class MainMenu {
    private final Scanner sc = new Scanner(System.in);

    // Servicios de todos los módulos
    private final DuenoService duenoService = new DuenoService();
    private final MascotaService mascotaService = new MascotaService();
    private final CitaService citaService = new CitaService();
    private final HistorialMedicoService historialService = new HistorialMedicoService();
    private final InventarioService inventarioService = new InventarioService();
    private final ProveedorService proveedorService = new ProveedorService();
    private final AlertasService alertasService = new AlertasService();
    private final FacturacionService facturacionService = new FacturacionService();
    private final ReportesService reportesService = new ReportesService();
    private final AdopcionService adopcionService = new AdopcionService();
    private final JornadaService jornadaService = new JornadaService();
    private final ClubService clubService = new ClubService();

    public void iniciar() {
        while (true) {
            mostrarMenuPrincipal();
            int opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> menuPacientes();
                case 2 -> menuServicios();
                case 3 -> menuInventario();
                case 4 -> menuFacturacion();
                case 5 -> menuActividades();
                case 6 -> menuReportes();
                case 7 -> mostrarAlertas();
                case 0 -> {
                    System.out.println("¡Gracias por usar HappyFeet! 🐾");
                    return;
                }
                default -> System.out.println("❌ Opción no válida");
            }
        }
    }

    private void mostrarMenuPrincipal() {
        int alertas = alertasService.contarAlertas();
        String indicadorAlertas = alertas > 0 ? " 🔔(" + alertas + ")" : "";

        System.out.println("\n" + "=".repeat(50));
        System.out.println("     🐾 CLÍNICA VETERINARIA HAPPYFEET 🐾");
        System.out.println("=".repeat(50));
        System.out.println("1. 👥 Gestión de Pacientes (Dueños y Mascotas)");
        System.out.println("2. 🏥 Servicios Médicos y Citas");
        System.out.println("3. 📦 Inventario y Farmacia");
        System.out.println("4. 💰 Facturación");
        System.out.println("5. 🎪 Actividades Especiales");
        System.out.println("6. 📊 Reportes Gerenciales");
        System.out.println("7. 🔔 Ver Alertas" + indicadorAlertas);
        System.out.println("0. 🚪 Salir");
        System.out.println("=".repeat(50));
    }

    // === MÓDULO 1: GESTIÓN DE PACIENTES ===
    private void menuPacientes() {
        while (true) {
            System.out.println("\n=== 👥 GESTIÓN DE PACIENTES ===");
            System.out.println("1. Registrar dueño");
            System.out.println("2. Registrar mascota");
            System.out.println("3. Listar dueños");
            System.out.println("4. Listar mascotas");
            System.out.println("5. Buscar historial médico de mascota");
            System.out.println("6. Registrar en club de puntos");
            System.out.println("0. Volver al menú principal");

            int opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> registrarDueno();
                case 2 -> registrarMascota();
                case 3 -> listarDuenos();
                case 4 -> listarMascotas();
                case 5 -> buscarHistorialMascota();
                case 6 -> registrarEnClub();
                case 0 -> { return; }
                default -> System.out.println("❌ Opción no válida");
            }
        }
    }

    private void registrarDueno() {
        System.out.println("\n--- Registro de Dueño ---");
        System.out.print("Nombre completo: ");
        String nombre = sc.nextLine();
        System.out.print("Documento: ");
        String doc = sc.nextLine();
        System.out.print("Dirección: ");
        String dir = sc.nextLine();
        System.out.print("Teléfono: ");
        String tel = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();

        Dueno dueno = new Dueno(0, nombre, doc, dir, tel, email);
        duenoService.registrarDueno(dueno);
    }

    private void registrarMascota() {
        System.out.println("\n--- Registro de Mascota ---");

        // Mostrar dueños disponibles
        List<Dueno> duenos = duenoService.listarDuenos();
        if (duenos.isEmpty()) {
            System.out.println("❌ No hay dueños registrados. Registre un dueño primero.");
            return;
        }

        System.out.println("Dueños disponibles:");
        duenos.forEach(d -> System.out.println(d.getId() + ". " + d.getNombreCompleto()));

        int duenoId = leerEntero("ID del dueño: ");
        System.out.print("Nombre de la mascota: ");
        String nombreMascota = sc.nextLine();
        System.out.print("Raza: ");
        String raza = sc.nextLine();
        System.out.print("Fecha de nacimiento (YYYY-MM-DD, opcional): ");
        String fechaNac = sc.nextLine();
        System.out.print("Sexo (Macho/Hembra): ");
        String sexo = sc.nextLine();
        System.out.print("URL de foto (opcional): ");
        String foto = sc.nextLine();

        Mascota mascota = new Mascota(0, duenoId, nombreMascota, raza,
                                     fechaNac.isEmpty() ? null : fechaNac, sexo,
                                     foto.isEmpty() ? null : foto);
        mascotaService.registrarMascota(mascota);
    }

    private void listarDuenos() {
        System.out.println("\n--- Lista de Dueños ---");
        List<Dueno> duenos = duenoService.listarDuenos();
        if (duenos.isEmpty()) {
            System.out.println("No hay dueños registrados.");
        } else {
            duenos.forEach(System.out::println);
        }
    }

    private void listarMascotas() {
        System.out.println("\n--- Lista de Mascotas ---");
        List<Mascota> mascotas = mascotaService.listarMascotas();
        if (mascotas.isEmpty()) {
            System.out.println("No hay mascotas registradas.");
        } else {
            mascotas.forEach(System.out::println);
        }
    }

    private void buscarHistorialMascota() {
        int mascotaId = leerEntero("ID de la mascota: ");
        List<HistorialMedico> historial = historialService.buscarHistorialPorMascota(mascotaId);

        if (historial.isEmpty()) {
            System.out.println("No hay historial médico para esta mascota.");
        } else {
            System.out.println("\n--- Historial Médico ---");
            historial.forEach(System.out::println);
        }
    }

    private void registrarEnClub() {
        List<Dueno> duenos = duenoService.listarDuenos();
        if (duenos.isEmpty()) {
            System.out.println("❌ No hay dueños registrados.");
            return;
        }

        System.out.println("Dueños disponibles:");
        duenos.forEach(d -> System.out.println(d.getId() + ". " + d.getNombreCompleto()));

        int duenoId = leerEntero("ID del dueño: ");
        Dueno dueno = duenos.stream().filter(d -> d.getId() == duenoId).findFirst().orElse(null);

        if (dueno != null) {
            clubService.registrarMiembro(duenoId, dueno.getNombreCompleto());
        } else {
            System.out.println("❌ Dueño no encontrado");
        }
    }

    // === MÓDULO 2: SERVICIOS MÉDICOS ===
    private void menuServicios() {
        while (true) {
            System.out.println("\n=== 🏥 SERVICIOS MÉDICOS Y CITAS ===");
            System.out.println("1. Agendar cita");
            System.out.println("2. Listar citas");
            System.out.println("3. Cambiar estado de cita");
            System.out.println("4. Registrar consulta médica");
            System.out.println("5. Ver historial médico");
            System.out.println("6. Buscar citas por mascota");
            System.out.println("0. Volver al menú principal");

            int opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> agendarCita();
                case 2 -> listarCitas();
                case 3 -> cambiarEstadoCita();
                case 4 -> registrarConsulta();
                case 5 -> verHistorialMedico();
                case 6 -> buscarCitasPorMascota();
                case 0 -> { return; }
                default -> System.out.println("❌ Opción no válida");
            }
        }
    }

    private void agendarCita() {
        System.out.println("\n--- Agendar Cita ---");

        List<Mascota> mascotas = mascotaService.listarMascotas();
        if (mascotas.isEmpty()) {
            System.out.println("❌ No hay mascotas registradas.");
            return;
        }

        System.out.println("Mascotas disponibles:");
        mascotas.forEach(m -> System.out.println(m.getId() + ". " + m.getNombre()));

        int mascotaId = leerEntero("ID de la mascota: ");
        System.out.print("Fecha y hora (YYYY-MM-DD HH:MM): ");
        String fechaHoraStr = sc.nextLine();
        System.out.print("Motivo de la cita: ");
        String motivo = sc.nextLine();

        try {
            LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraStr,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            Cita cita = new Cita(0, mascotaId, fechaHora, motivo, 1);
            citaService.agendarCita(cita);
        } catch (DateTimeParseException e) {
            System.out.println("❌ Formato de fecha inválido. Use YYYY-MM-DD HH:MM");
        }
    }

    private void listarCitas() {
        System.out.println("\n--- Lista de Citas ---");
        List<Cita> citas = citaService.listarCitas();
        if (citas.isEmpty()) {
            System.out.println("No hay citas registradas.");
        } else {
            citas.forEach(System.out::println);
        }
    }

    private void cambiarEstadoCita() {
        int citaId = leerEntero("ID de la cita: ");
        citaService.mostrarEstadosDisponibles();
        int nuevoEstado = leerEntero("Nuevo estado: ");
        citaService.cambiarEstadoCita(citaId, nuevoEstado);
    }

    private void registrarConsulta() {
        System.out.println("\n--- Registrar Consulta Médica ---");

        List<Mascota> mascotas = mascotaService.listarMascotas();
        if (mascotas.isEmpty()) {
            System.out.println("❌ No hay mascotas registradas.");
            return;
        }

        System.out.println("Mascotas disponibles:");
        mascotas.forEach(m -> System.out.println(m.getId() + ". " + m.getNombre()));

        int mascotaId = leerEntero("ID de la mascota: ");
        System.out.print("Descripción de la consulta: ");
        String descripcion = sc.nextLine();
        System.out.print("Diagnóstico: ");
        String diagnostico = sc.nextLine();
        System.out.print("Tratamiento recomendado: ");
        String tratamiento = sc.nextLine();

        historialService.mostrarTiposEventoDisponibles();
        int tipoEvento = leerEntero("Tipo de evento: ");

        System.out.print("Medicamento prescrito (opcional): ");
        String medicamento = sc.nextLine();
        int cantidad = 0;
        if (!medicamento.isEmpty()) {
            cantidad = leerEntero("Cantidad: ");
        }

        historialService.registrarConsultaCompleta(mascotaId, descripcion, diagnostico,
                                                  tratamiento, tipoEvento, medicamento, cantidad);
    }

    private void verHistorialMedico() {
        System.out.println("\n--- Historial Médico General ---");
        List<HistorialMedico> historial = historialService.listarHistorial();
        if (historial.isEmpty()) {
            System.out.println("No hay registros médicos.");
        } else {
            historial.forEach(System.out::println);
        }
    }

    private void buscarCitasPorMascota() {
        int mascotaId = leerEntero("ID de la mascota: ");
        List<Cita> citas = citaService.buscarCitasPorMascota(mascotaId);

        if (citas.isEmpty()) {
            System.out.println("No hay citas para esta mascota.");
        } else {
            System.out.println("\n--- Citas de la Mascota ---");
            citas.forEach(System.out::println);
        }
    }

    // === MÓDULO 3: INVENTARIO ===
    private void menuInventario() {
        while (true) {
            System.out.println("\n=== 📦 INVENTARIO Y FARMACIA ===");
            System.out.println("1. Agregar producto");
            System.out.println("2. Listar inventario");
            System.out.println("3. Buscar producto");
            System.out.println("4. Actualizar stock");
            System.out.println("5. Gestionar proveedores");
            System.out.println("6. Ver alertas de inventario");
            System.out.println("7. Reporte de inventario");
            System.out.println("0. Volver al menú principal");

            int opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> agregarProducto();
                case 2 -> listarInventario();
                case 3 -> buscarProducto();
                case 4 -> actualizarStock();
                case 5 -> menuProveedores();
                case 6 -> alertasService.mostrarAlertasInventario();
                case 7 -> alertasService.generarReporteInventario();
                case 0 -> { return; }
                default -> System.out.println("❌ Opción no válida");
            }
        }
    }

    private void agregarProducto() {
        System.out.println("\n--- Agregar Producto al Inventario ---");
        System.out.print("Nombre del producto: ");
        String nombre = sc.nextLine();

        inventarioService.mostrarTiposProductoDisponibles();
        int tipoId = leerEntero("Tipo de producto: ");

        System.out.print("Descripción: ");
        String descripcion = sc.nextLine();
        System.out.print("Fabricante: ");
        String fabricante = sc.nextLine();
        System.out.print("Lote: ");
        String lote = sc.nextLine();

        int cantidad = leerEntero("Cantidad en stock: ");
        int stockMinimo = leerEntero("Stock mínimo: ");

        System.out.print("Fecha de vencimiento (YYYY-MM-DD, opcional): ");
        String fechaVencStr = sc.nextLine();
        LocalDate fechaVenc = null;
        if (!fechaVencStr.isEmpty()) {
            try {
                fechaVenc = LocalDate.parse(fechaVencStr);
            } catch (DateTimeParseException e) {
                System.out.println("❌ Formato de fecha inválido. Se guardará sin fecha de vencimiento.");
            }
        }

        BigDecimal precio = leerDecimal("Precio de venta: ");

        Inventario producto = new Inventario(0, nombre, tipoId, descripcion, fabricante,
                                           lote, cantidad, stockMinimo, fechaVenc, precio);
        inventarioService.agregarProducto(producto);
    }

    private void listarInventario() {
        System.out.println("\n--- Inventario Completo ---");
        List<Inventario> productos = inventarioService.listarInventario();
        if (productos.isEmpty()) {
            System.out.println("No hay productos en el inventario.");
        } else {
            productos.forEach(System.out::println);
        }
    }

    private void buscarProducto() {
        System.out.print("Nombre del producto a buscar: ");
        String nombre = sc.nextLine();
        List<Inventario> productos = inventarioService.buscarProducto(nombre);

        if (productos.isEmpty()) {
            System.out.println("No se encontraron productos.");
        } else {
            System.out.println("\n--- Resultados de la Búsqueda ---");
            productos.forEach(System.out::println);
        }
    }

    private void actualizarStock() {
        int productoId = leerEntero("ID del producto: ");
        int nuevaCantidad = leerEntero("Nueva cantidad: ");
        inventarioService.actualizarStock(productoId, nuevaCantidad);
    }

    private void menuProveedores() {
        while (true) {
            System.out.println("\n=== Gestión de Proveedores ===");
            System.out.println("1. Registrar proveedor");
            System.out.println("2. Listar proveedores");
            System.out.println("3. Buscar proveedor");
            System.out.println("0. Volver");

            int opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> registrarProveedor();
                case 2 -> listarProveedores();
                case 3 -> buscarProveedor();
                case 0 -> { return; }
                default -> System.out.println("❌ Opción no válida");
            }
        }
    }

    private void registrarProveedor() {
        System.out.println("\n--- Registrar Proveedor ---");
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Contacto: ");
        String contacto = sc.nextLine();
        System.out.print("Teléfono: ");
        String telefono = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Dirección: ");
        String direccion = sc.nextLine();

        Proveedor proveedor = new Proveedor(0, nombre, contacto, telefono, email, direccion);
        proveedorService.registrarProveedor(proveedor);
    }

    private void listarProveedores() {
        System.out.println("\n--- Lista de Proveedores ---");
        List<Proveedor> proveedores = proveedorService.listarProveedores();
        if (proveedores.isEmpty()) {
            System.out.println("No hay proveedores registrados.");
        } else {
            proveedores.forEach(System.out::println);
        }
    }

    private void buscarProveedor() {
        System.out.print("Nombre del proveedor: ");
        String nombre = sc.nextLine();
        List<Proveedor> proveedores = proveedorService.buscarProveedor(nombre);

        if (proveedores.isEmpty()) {
            System.out.println("No se encontraron proveedores.");
        } else {
            proveedores.forEach(System.out::println);
        }
    }

    // === MÓDULO 4: FACTURACIÓN ===
    private void menuFacturacion() {
        while (true) {
            System.out.println("\n=== 💰 FACTURACIÓN ===");
            System.out.println("1. Generar factura");
            System.out.println("2. Listar facturas");
            System.out.println("3. Imprimir factura");
            System.out.println("4. Buscar facturas por cliente");
            System.out.println("5. Gestionar club de puntos");
            System.out.println("0. Volver al menú principal");

            int opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> generarFactura();
                case 2 -> listarFacturas();
                case 3 -> imprimirFactura();
                case 4 -> buscarFacturasPorCliente();
                case 5 -> menuClubPuntos();
                case 0 -> { return; }
                default -> System.out.println("❌ Opción no válida");
            }
        }
    }

    private void generarFactura() {
        System.out.println("\n--- Generar Factura ---");

        List<Dueno> duenos = duenoService.listarDuenos();
        if (duenos.isEmpty()) {
            System.out.println("❌ No hay clientes registrados.");
            return;
        }

        System.out.println("Clientes disponibles:");
        duenos.forEach(d -> System.out.println(d.getId() + ". " + d.getNombreCompleto()));

        int duenoId = leerEntero("ID del cliente: ");

        Factura factura = new Factura();
        factura.setDuenoId(duenoId);
        factura.setFechaEmision(LocalDateTime.now());

        // Agregar items a la factura
        while (true) {
            System.out.println("\n1. Agregar producto");
            System.out.println("2. Agregar servicio");
            System.out.println("3. Finalizar factura");

            int opcion = leerEntero("Opción: ");

            if (opcion == 1) {
                agregarProductoAFactura(factura);
            } else if (opcion == 2) {
                agregarServicioAFactura(factura);
            } else if (opcion == 3) {
                break;
            }
        }

        if (!factura.getItems().isEmpty()) {
            int facturaId = facturacionService.generarFactura(factura);

            // Otorgar puntos del club
            if (facturaId > 0) {
                clubService.procesarCompra(duenoId, factura.getTotal().doubleValue());
            }
        } else {
            System.out.println("❌ No se puede generar una factura sin items");
        }
    }

    private void agregarProductoAFactura(Factura factura) {
        System.out.print("Nombre del producto: ");
        String nombreProducto = sc.nextLine();
        int cantidad = leerEntero("Cantidad: ");
        BigDecimal precio = leerDecimal("Precio unitario: ");

        ItemFactura item = facturacionService.crearItemProducto(nombreProducto, cantidad, precio);
        factura.agregarItem(item);
        System.out.println("✅ Producto agregado a la factura");
    }

    private void agregarServicioAFactura(Factura factura) {
        System.out.print("Descripción del servicio: ");
        String descripcion = sc.nextLine();
        int cantidad = leerEntero("Cantidad: ");
        BigDecimal precio = leerDecimal("Precio unitario: ");

        ItemFactura item = facturacionService.crearItemServicio(descripcion, cantidad, precio);
        factura.agregarItem(item);
        System.out.println("✅ Servicio agregado a la factura");
    }

    private void listarFacturas() {
        System.out.println("\n--- Lista de Facturas ---");
        List<Factura> facturas = facturacionService.listarFacturas();
        if (facturas.isEmpty()) {
            System.out.println("No hay facturas registradas.");
        } else {
            facturas.forEach(System.out::println);
        }
    }

    private void imprimirFactura() {
        int facturaId = leerEntero("ID de la factura: ");
        facturacionService.imprimirFactura(facturaId);
    }

    private void buscarFacturasPorCliente() {
        int duenoId = leerEntero("ID del cliente: ");
        List<Factura> facturas = facturacionService.buscarFacturasPorDueno(duenoId);

        if (facturas.isEmpty()) {
            System.out.println("No se encontraron facturas para este cliente.");
        } else {
            System.out.println("\n--- Facturas del Cliente ---");
            facturas.forEach(System.out::println);
        }
    }

    private void menuClubPuntos() {
        while (true) {
            System.out.println("\n=== 🎁 CLUB DE PUNTOS ===");
            System.out.println("1. Ver miembros del club");
            System.out.println("2. Otorgar puntos");
            System.out.println("3. Canjear puntos");
            System.out.println("4. Catálogo de beneficios");
            System.out.println("5. Reporte del club");
            System.out.println("0. Volver");

            int opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> verMiembrosClub();
                case 2 -> otorgarPuntos();
                case 3 -> canjearPuntos();
                case 4 -> clubService.mostrarCatalogoBeneficios();
                case 5 -> clubService.generarReporteClub();
                case 0 -> { return; }
                default -> System.out.println("❌ Opción no válida");
            }
        }
    }

    private void verMiembrosClub() {
        System.out.println("\n--- Miembros del Club de Puntos ---");
        List<ClubPuntos> miembros = clubService.listarMiembros();
        if (miembros.isEmpty()) {
            System.out.println("No hay miembros registrados en el club.");
        } else {
            miembros.forEach(System.out::println);
        }
    }

    private void otorgarPuntos() {
        int duenoId = leerEntero("ID del cliente: ");
        int puntos = leerEntero("Puntos a otorgar: ");
        System.out.print("Motivo: ");
        String motivo = sc.nextLine();

        clubService.otorgarPuntos(duenoId, puntos, motivo);
    }

    private void canjearPuntos() {
        int duenoId = leerEntero("ID del cliente: ");
        int puntos = leerEntero("Puntos a canjear: ");
        System.out.print("Beneficio: ");
        String beneficio = sc.nextLine();

        clubService.canjearPuntos(duenoId, puntos, beneficio);
    }

    // === MÓDULO 5: ACTIVIDADES ESPECIALES ===
    private void menuActividades() {
        while (true) {
            System.out.println("\n=== 🎪 ACTIVIDADES ESPECIALES ===");
            System.out.println("1. 🏠 Gestión de Adopciones");
            System.out.println("2. 💉 Jornadas de Vacunación");
            System.out.println("0. Volver al menú principal");

            int opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> menuAdopciones();
                case 2 -> menuJornadas();
                case 0 -> { return; }
                default -> System.out.println("❌ Opción no válida");
            }
        }
    }

    private void menuAdopciones() {
        while (true) {
            System.out.println("\n=== 🏠 GESTIÓN DE ADOPCIONES ===");
            System.out.println("1. Registrar mascota para adopción");
            System.out.println("2. Ver mascotas disponibles");
            System.out.println("3. Procesar adopción");
            System.out.println("4. Ver todas las mascotas");
            System.out.println("5. Ver contratos de adopción");
            System.out.println("6. Estadísticas de adopción");
            System.out.println("0. Volver");

            int opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> registrarMascotaAdopcion();
                case 2 -> verMascotasDisponibles();
                case 3 -> procesarAdopcion();
                case 4 -> verTodasMascotasAdopcion();
                case 5 -> verContratosAdopcion();
                case 6 -> adopcionService.mostrarEstadisticasAdopcion();
                case 0 -> { return; }
                default -> System.out.println("❌ Opción no válida");
            }
        }
    }

    private void registrarMascotaAdopcion() {
        System.out.println("\n--- Registrar Mascota para Adopción ---");
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Edad estimada: ");
        String edad = sc.nextLine();
        System.out.print("Sexo (Macho/Hembra): ");
        String sexo = sc.nextLine();
        System.out.print("Descripción: ");
        String descripcion = sc.nextLine();
        System.out.print("URL de foto (opcional): ");
        String foto = sc.nextLine();

        MascotaAdopcion mascota = new MascotaAdopcion();
        mascota.setNombre(nombre);
        mascota.setEdadEstimada(edad);
        mascota.setSexo(sexo);
        mascota.setDescripcion(descripcion);
        mascota.setUrlFoto(foto.isEmpty() ? null : foto);

        adopcionService.registrarMascotaParaAdopcion(mascota);
    }

    private void verMascotasDisponibles() {
        System.out.println("\n--- Mascotas Disponibles para Adopción ---");
        List<MascotaAdopcion> mascotas = adopcionService.listarMascotasDisponibles();
        if (mascotas.isEmpty()) {
            System.out.println("No hay mascotas disponibles para adopción.");
        } else {
            mascotas.forEach(System.out::println);
        }
    }

    private void procesarAdopcion() {
        List<MascotaAdopcion> disponibles = adopcionService.listarMascotasDisponibles();
        if (disponibles.isEmpty()) {
            System.out.println("❌ No hay mascotas disponibles para adopción.");
            return;
        }

        System.out.println("Mascotas disponibles:");
        disponibles.forEach(m -> System.out.println(m.getId() + ". " + m.getNombre()));

        int mascotaId = leerEntero("ID de la mascota: ");
        System.out.print("Nombre del adoptante: ");
        String nombre = sc.nextLine();
        System.out.print("Documento: ");
        String documento = sc.nextLine();
        System.out.print("Teléfono: ");
        String telefono = sc.nextLine();
        System.out.print("Dirección: ");
        String direccion = sc.nextLine();

        adopcionService.procesarAdopcion(mascotaId, nombre, documento, telefono, direccion);
    }

    private void verTodasMascotasAdopcion() {
        System.out.println("\n--- Todas las Mascotas de Adopción ---");
        List<MascotaAdopcion> mascotas = adopcionService.listarTodasLasMascotas();
        if (mascotas.isEmpty()) {
            System.out.println("No hay mascotas registradas para adopción.");
        } else {
            mascotas.forEach(System.out::println);
        }
    }

    private void verContratosAdopcion() {
        System.out.println("\n--- Contratos de Adopción ---");
        List<ContratoAdopcion> contratos = adopcionService.listarContratos();
        if (contratos.isEmpty()) {
            System.out.println("No hay contratos de adopción registrados.");
        } else {
            contratos.forEach(System.out::println);
        }
    }

    private void menuJornadas() {
        while (true) {
            System.out.println("\n=== 💉 JORNADAS DE VACUNACIÓN ===");
            System.out.println("1. Crear jornada");
            System.out.println("2. Listar jornadas");
            System.out.println("3. Registrar mascota en jornada");
            System.out.println("4. Ver registros de jornada");
            System.out.println("5. Finalizar jornada");
            System.out.println("6. Estadísticas de jornada");
            System.out.println("7. Reporte general");
            System.out.println("0. Volver");

            int opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> crearJornada();
                case 2 -> listarJornadas();
                case 3 -> registrarEnJornada();
                case 4 -> verRegistrosJornada();
                case 5 -> finalizarJornada();
                case 6 -> estadisticasJornada();
                case 7 -> jornadaService.generarReporteTodasLasJornadas();
                case 0 -> { return; }
                default -> System.out.println("❌ Opción no válida");
            }
        }
    }

    private void crearJornada() {
        System.out.println("\n--- Crear Jornada de Vacunación ---");
        System.out.print("Nombre de la jornada: ");
        String nombre = sc.nextLine();

        LocalDate fechaInicio = leerFecha("Fecha de inicio (YYYY-MM-DD): ");
        LocalDate fechaFin = leerFecha("Fecha de fin (YYYY-MM-DD): ");

        System.out.print("Descripción: ");
        String descripcion = sc.nextLine();

        JornadaVacunacion jornada = new JornadaVacunacion();
        jornada.setNombre(nombre);
        jornada.setFechaInicio(fechaInicio);
        jornada.setFechaFin(fechaFin);
        jornada.setDescripcion(descripcion);

        jornadaService.crearJornada(jornada);
    }

    private void listarJornadas() {
        System.out.println("\n--- Lista de Jornadas ---");
        List<JornadaVacunacion> jornadas = jornadaService.listarJornadas();
        if (jornadas.isEmpty()) {
            System.out.println("No hay jornadas registradas.");
        } else {
            jornadas.forEach(System.out::println);
        }
    }

    private void registrarEnJornada() {
        List<JornadaVacunacion> activas = jornadaService.listarJornadasActivas();
        if (activas.isEmpty()) {
            System.out.println("❌ No hay jornadas activas.");
            return;
        }

        System.out.println("Jornadas activas:");
        activas.forEach(j -> System.out.println(j.getId() + ". " + j.getNombre()));

        int jornadaId = leerEntero("ID de la jornada: ");
        System.out.print("Nombre de la mascota: ");
        String mascotaNombre = sc.nextLine();
        System.out.print("Nombre del dueño: ");
        String duenoNombre = sc.nextLine();
        System.out.print("Teléfono del dueño: ");
        String telefono = sc.nextLine();
        System.out.print("Vacuna aplicada: ");
        String vacuna = sc.nextLine();

        jornadaService.registrarMascotaEnJornada(jornadaId, mascotaNombre, duenoNombre, telefono, vacuna);
    }

    private void verRegistrosJornada() {
        int jornadaId = leerEntero("ID de la jornada: ");
        List<RegistroJornada> registros = jornadaService.listarRegistrosPorJornada(jornadaId);

        if (registros.isEmpty()) {
            System.out.println("No hay registros para esta jornada.");
        } else {
            System.out.println("\n--- Registros de la Jornada ---");
            registros.forEach(System.out::println);
        }
    }

    private void finalizarJornada() {
        int jornadaId = leerEntero("ID de la jornada a finalizar: ");
        jornadaService.finalizarJornada(jornadaId);
    }

    private void estadisticasJornada() {
        int jornadaId = leerEntero("ID de la jornada: ");
        jornadaService.mostrarEstadisticasJornada(jornadaId);
    }

    // === MÓDULO 6: REPORTES ===
    private void menuReportes() {
        while (true) {
            System.out.println("\n=== 📊 REPORTES GERENCIALES ===");
            System.out.println("1. Servicios más solicitados");
            System.out.println("2. Reporte de facturación");
            System.out.println("3. Estado de citas");
            System.out.println("4. Reporte de inventario");
            System.out.println("5. Reporte completo");
            System.out.println("6. Reporte por período");
            System.out.println("0. Volver al menú principal");

            int opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> reportesService.generarReporteServicios();
                case 2 -> reportesService.generarReporteFacturacion();
                case 3 -> reportesService.generarReporteCitas();
                case 4 -> alertasService.generarReporteInventario();
                case 5 -> reportesService.generarReporteCompleto();
                case 6 -> generarReportePorPeriodo();
                case 0 -> { return; }
                default -> System.out.println("❌ Opción no válida");
            }
        }
    }

    private void generarReportePorPeriodo() {
        System.out.println("\n--- Reporte por Período ---");
        LocalDate fechaInicio = leerFecha("Fecha de inicio (YYYY-MM-DD): ");
        LocalDate fechaFin = leerFecha("Fecha de fin (YYYY-MM-DD): ");

        reportesService.generarReportePorPeriodo(fechaInicio, fechaFin);
    }

    private void mostrarAlertas() {
        alertasService.mostrarAlertasInventario();
    }

    // === MÉTODOS AUXILIARES ===
    private int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Ingrese un número válido");
            }
        }
    }

    private BigDecimal leerDecimal(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return new BigDecimal(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Ingrese un número decimal válido");
            }
        }
    }

    private LocalDate leerFecha(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return LocalDate.parse(sc.nextLine());
            } catch (DateTimeParseException e) {
                System.out.println("❌ Formato de fecha inválido. Use YYYY-MM-DD");
            }
        }
    }

    public static void main(String[] args) {
        new MainMenu().iniciar();
    }
}