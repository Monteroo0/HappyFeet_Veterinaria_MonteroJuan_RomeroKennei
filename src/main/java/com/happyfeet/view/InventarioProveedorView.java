package com.happyfeet.view;

import com.happyfeet.model.Inventario;
import com.happyfeet.model.Proveedor;
import com.happyfeet.model.entities.InventarioProveedor;
import com.happyfeet.service.InventarioProveedorService;
import com.happyfeet.service.InventarioService;
import com.happyfeet.service.ProveedorService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

/**
 * Vista para la gestión de la relación Inventario-Proveedores
 */
public class InventarioProveedorView {

    private final Scanner sc;
    private final InventarioProveedorService inventarioProveedorService;
    private final InventarioService inventarioService;
    private final ProveedorService proveedorService;

    public InventarioProveedorView(Scanner sc) {
        this.sc = sc;
        this.inventarioProveedorService = new InventarioProveedorService();
        this.inventarioService = new InventarioService();
        this.proveedorService = new ProveedorService();
    }

    public void mostrarMenu() {
        while (true) {
            System.out.println("\n=== GESTIÓN INVENTARIO-PROVEEDORES ===");
            System.out.println("1. Asociar producto con proveedor");
            System.out.println("2. Ver proveedores de un producto");
            System.out.println("3. Ver productos de un proveedor");
            System.out.println("4. Establecer proveedor principal");
            System.out.println("5. Reporte de proveedores por producto");
            System.out.println("6. Reporte de productos por proveedor");
            System.out.println("7. Listar todas las relaciones");
            System.out.println("0. Volver");

            int opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> asociarProductoConProveedor();
                case 2 -> verProveedoresDeProducto();
                case 3 -> verProductosDeProveedor();
                case 4 -> establecerProveedorPrincipal();
                case 5 -> inventarioProveedorService.generarReporteProveedoresPorProducto();
                case 6 -> inventarioProveedorService.generarReporteProductosPorProveedor();
                case 7 -> listarTodasRelaciones();
                case 0 -> { return; }
                default -> System.out.println("❌ Opción no válida");
            }
        }
    }

    private void asociarProductoConProveedor() {
        System.out.println("\n--- Asociar Producto con Proveedor ---");

        List<Inventario> productos = inventarioService.listarInventario();
        if (productos.isEmpty()) {
            System.out.println("❌ No hay productos en inventario.");
            return;
        }

        System.out.println("Productos disponibles:");
        productos.forEach(p -> System.out.println(p.getId() + ". " + p.getNombreProducto()));

        int inventarioId = leerEntero("ID del producto: ");

        // Validar que el producto existe
        if (productos.stream().noneMatch(p -> p.getId() == inventarioId)) {
            System.out.println("❌ ID de producto inválido");
            return;
        }

        List<Proveedor> proveedores = proveedorService.listarProveedores();
        if (proveedores.isEmpty()) {
            System.out.println("❌ No hay proveedores registrados.");
            return;
        }

        System.out.println("\nProveedores disponibles:");
        proveedores.forEach(p -> System.out.println(p.getId() + ". " + p.getNombre()));

        int proveedorId = leerEntero("ID del proveedor: ");

        // Validar que el proveedor existe
        if (proveedores.stream().noneMatch(p -> p.getId() == proveedorId)) {
            System.out.println("❌ ID de proveedor inválido");
            return;
        }

        System.out.print("¿Es proveedor principal? (S/N): ");
        boolean esPrincipal = sc.nextLine().trim().equalsIgnoreCase("S");

        BigDecimal precioCompra = leerDecimal("Precio de compra (0 para omitir): ");
        if (precioCompra.compareTo(BigDecimal.ZERO) == 0) {
            precioCompra = null;
        }

        int tiempoEntrega = leerEntero("Tiempo de entrega en días (0 para omitir): ");
        Integer tiempoEntregaFinal = tiempoEntrega > 0 ? tiempoEntrega : null;

        inventarioProveedorService.asociarProductoConProveedor(
                inventarioId, proveedorId, esPrincipal, precioCompra, tiempoEntregaFinal
        );
    }

    private void verProveedoresDeProducto() {
        int inventarioId = leerEntero("ID del producto: ");
        List<InventarioProveedor> proveedores = inventarioProveedorService.listarProveedoresDeProducto(inventarioId);

        if (proveedores.isEmpty()) {
            System.out.println("Este producto no tiene proveedores asociados.");
        } else {
            System.out.println("\n--- Proveedores del Producto ---");
            proveedores.forEach(System.out::println);
        }
    }

    private void verProductosDeProveedor() {
        int proveedorId = leerEntero("ID del proveedor: ");
        List<InventarioProveedor> productos = inventarioProveedorService.listarProductosDeProveedor(proveedorId);

        if (productos.isEmpty()) {
            System.out.println("Este proveedor no tiene productos asociados.");
        } else {
            System.out.println("\n--- Productos del Proveedor ---");
            productos.forEach(System.out::println);
        }
    }

    private void establecerProveedorPrincipal() {
        int inventarioId = leerEntero("ID del producto: ");

        List<InventarioProveedor> proveedores = inventarioProveedorService.listarProveedoresDeProducto(inventarioId);
        if (proveedores.isEmpty()) {
            System.out.println("❌ Este producto no tiene proveedores asociados.");
            return;
        }

        System.out.println("\nProveedores actuales:");
        proveedores.forEach(p -> System.out.println(p.getProveedorId() + ". " + p.getNombreProveedor() +
                (p.isEsProveedorPrincipal() ? " [PRINCIPAL]" : "")));

        int proveedorId = leerEntero("ID del proveedor que será principal: ");

        inventarioProveedorService.establecerProveedorPrincipal(inventarioId, proveedorId);
    }

    private void listarTodasRelaciones() {
        List<InventarioProveedor> relaciones = inventarioProveedorService.listarTodas();

        if (relaciones.isEmpty()) {
            System.out.println("No hay relaciones registradas.");
        } else {
            System.out.println("\n--- Todas las Relaciones Inventario-Proveedor ---");
            relaciones.forEach(System.out::println);
        }
    }

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