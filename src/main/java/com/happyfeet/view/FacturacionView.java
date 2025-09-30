package com.happyfeet.view;

import com.happyfeet.model.ClubPuntos;
import com.happyfeet.model.Dueno;
import com.happyfeet.model.Factura;
import com.happyfeet.model.ItemFactura;
import com.happyfeet.service.ClubService;
import com.happyfeet.service.DuenoService;
import com.happyfeet.service.FacturacionService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

/**
 * Vista del Módulo de Facturación
 * Maneja todas las funcionalidades relacionadas con facturación y club de puntos
 */
public class FacturacionView {

    private final Scanner sc;
    private final FacturacionService facturacionService;
    private final DuenoService duenoService;
    private final ClubService clubService;

    public FacturacionView(Scanner sc) {
        this.sc = sc;
        this.facturacionService = new FacturacionService();
        this.duenoService = new DuenoService();
        this.clubService = new ClubService();
    }

    /**
     * Muestra el menú principal del módulo de facturación
     */
    public void mostrarMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("   💰 MÓDULO: FACTURACIÓN");
            System.out.println("=".repeat(50));
            System.out.println("1. Generar factura");
            System.out.println("2. Listar facturas");
            System.out.println("3. Imprimir factura");
            System.out.println("4. Buscar facturas por cliente");
            System.out.println("5. Gestionar club de puntos");
            System.out.println("0. Volver al menú principal");
            System.out.println("=".repeat(50));

            int opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> generarFactura();
                case 2 -> listarFacturas();
                case 3 -> imprimirFactura();
                case 4 -> buscarFacturasPorCliente();
                case 5 -> menuClubPuntos();
                case 0 -> {
                    return; // Volver al menú principal
                }
                default -> System.out.println("❌ Opción no válida");
            }
        }
    }

    // ==================== FUNCIONES DEL MÓDULO ====================

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

        // Validar que el dueño existe
        if (duenos.stream().noneMatch(d -> d.getId() == duenoId)) {
            System.out.println("❌ ID de cliente inválido");
            return;
        }

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
        String nombreProducto = sc.nextLine().trim();
        if (nombreProducto.isEmpty()) {
            System.out.println("❌ El nombre del producto no puede estar vacío");
            return;
        }

        int cantidad = leerEntero("Cantidad: ");
        if (cantidad <= 0) {
            System.out.println("❌ La cantidad debe ser mayor a 0");
            return;
        }

        BigDecimal precio = leerDecimal("Precio unitario: ");
        if (precio.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("❌ El precio debe ser mayor a 0");
            return;
        }

        ItemFactura item = facturacionService.crearItemProducto(nombreProducto, cantidad, precio);
        factura.agregarItem(item);
        System.out.println("✅ Producto agregado a la factura");
    }

    private void agregarServicioAFactura(Factura factura) {
        System.out.print("Descripción del servicio: ");
        String descripcion = sc.nextLine().trim();
        if (descripcion.isEmpty()) {
            System.out.println("❌ La descripción del servicio no puede estar vacía");
            return;
        }

        int cantidad = leerEntero("Cantidad: ");
        if (cantidad <= 0) {
            System.out.println("❌ La cantidad debe ser mayor a 0");
            return;
        }

        BigDecimal precio = leerDecimal("Precio unitario: ");
        if (precio.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("❌ El precio debe ser mayor a 0");
            return;
        }

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
                case 0 -> {
                    return;
                }
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

    // ==================== MÉTODOS AUXILIARES ====================

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
}