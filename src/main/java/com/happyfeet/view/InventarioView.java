package com.happyfeet.view;

import com.happyfeet.model.Inventario;
import com.happyfeet.model.Proveedor;
import com.happyfeet.service.AlertasService;
import com.happyfeet.service.InventarioService;
import com.happyfeet.service.ProveedorService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Vista del Módulo de Inventario y Farmacia
 * Maneja todas las funcionalidades relacionadas con inventario y proveedores
 */
public class InventarioView {

    private final Scanner sc;
    private final InventarioService inventarioService;
    private final ProveedorService proveedorService;
    private final AlertasService alertasService;

    public InventarioView(Scanner sc) {
        this.sc = sc;
        this.inventarioService = new InventarioService();
        this.proveedorService = new ProveedorService();
        this.alertasService = new AlertasService();
    }

    /**
     * Muestra el menú principal del módulo de inventario
     */
    public void mostrarMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("   📦 MÓDULO: INVENTARIO Y FARMACIA");
            System.out.println("=".repeat(50));
            System.out.println("1. Agregar producto");
            System.out.println("2. Listar inventario");
            System.out.println("3. Buscar producto");
            System.out.println("4. Actualizar stock");
            System.out.println("5. Gestionar proveedores");
            System.out.println("6. Ver alertas de inventario");
            System.out.println("7. Reporte de inventario");
            System.out.println("8. Gestión Inventario-Proveedores");
            System.out.println("0. Volver al menú principal");
            System.out.println("=".repeat(50));

            int opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> agregarProducto();
                case 2 -> listarInventario();
                case 3 -> buscarProducto();
                case 4 -> actualizarStock();
                case 5 -> menuProveedores();
                case 6 -> alertasService.mostrarAlertasInventario();
                case 7 -> alertasService.generarReporteInventario();
                case 8 -> gestionarInventarioProveedores();
                case 0 -> {
                    return; // Volver al menú principal
                }
                default -> System.out.println("❌ Opción no válida");
            }
        }
    }

    // ==================== FUNCIONES DEL MÓDULO ====================

    private void agregarProducto() {
        System.out.println("\n--- Agregar Producto al Inventario ---");
        System.out.print("Nombre del producto: ");
        String nombre = sc.nextLine().trim();
        if (nombre.isEmpty()) {
            System.out.println("❌ El nombre del producto no puede estar vacío");
            return;
        }

        inventarioService.mostrarTiposProductoDisponibles();
        int tipoId = leerEntero("Tipo de producto: ");

        System.out.print("Descripción: ");
        String descripcion = sc.nextLine();
        System.out.print("Fabricante: ");
        String fabricante = sc.nextLine();
        System.out.print("Lote: ");
        String lote = sc.nextLine();

        int cantidad = leerEntero("Cantidad en stock: ");
        if (cantidad < 0) {
            System.out.println("❌ La cantidad no puede ser negativa");
            return;
        }

        int stockMinimo = leerEntero("Stock mínimo: ");
        if (stockMinimo < 0) {
            System.out.println("❌ El stock mínimo no puede ser negativo");
            return;
        }

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
        if (nuevaCantidad < 0) {
            System.out.println("❌ La cantidad no puede ser negativa");
            return;
        }
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
                case 0 -> {
                    return;
                }
                default -> System.out.println("❌ Opción no válida");
            }
        }
    }

    private void registrarProveedor() {
        System.out.println("\n--- Registrar Proveedor ---");
        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim();
        if (nombre.isEmpty()) {
            System.out.println("❌ El nombre del proveedor no puede estar vacío");
            return;
        }

        System.out.print("Contacto: ");
        String contacto = sc.nextLine().trim();
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

    private void gestionarInventarioProveedores() {
        // Delegar a la vista especializada de inventario-proveedores
        InventarioProveedorView inventarioProveedorView = new InventarioProveedorView(sc);
        inventarioProveedorView.mostrarMenu();
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