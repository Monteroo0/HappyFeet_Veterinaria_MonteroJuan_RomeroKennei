package com.happyfeet.service;

import com.happyfeet.model.entities.InventarioProveedor;
import com.happyfeet.repository.InventarioProveedorDAO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar la relación entre inventario y proveedores
 * Implementa programación funcional con streams
 */
public class InventarioProveedorService {

    private final InventarioProveedorDAO inventarioProveedorDAO;

    public InventarioProveedorService() {
        this.inventarioProveedorDAO = new InventarioProveedorDAO();
    }

    /**
     * Asocia un producto del inventario con un proveedor
     */
    public int asociarProductoConProveedor(int inventarioId, int proveedorId,
                                          boolean esPrincipal, BigDecimal precioCompra,
                                          Integer tiempoEntrega) {
        // Validaciones
        if (inventarioId <= 0 || proveedorId <= 0) {
            System.out.println("❌ IDs inválidos");
            return -1;
        }

        InventarioProveedor relacion = new InventarioProveedor(
            inventarioId, proveedorId, esPrincipal, precioCompra, tiempoEntrega
        );

        return inventarioProveedorDAO.asociar(relacion);
    }

    /**
     * Lista todos los proveedores de un producto
     */
    public List<InventarioProveedor> listarProveedoresDeProducto(int inventarioId) {
        return inventarioProveedorDAO.listarProveedoresPorProducto(inventarioId);
    }

    /**
     * Lista todos los productos de un proveedor
     */
    public List<InventarioProveedor> listarProductosDeProveedor(int proveedorId) {
        return inventarioProveedorDAO.listarProductosPorProveedor(proveedorId);
    }

    /**
     * Establece un proveedor como principal para un producto
     */
    public boolean establecerProveedorPrincipal(int inventarioId, int proveedorId) {
        return inventarioProveedorDAO.establecerProveedorPrincipal(inventarioId, proveedorId);
    }

    /**
     * Obtiene el proveedor principal de un producto (Programación Funcional)
     */
    public InventarioProveedor obtenerProveedorPrincipal(int inventarioId) {
        return inventarioProveedorDAO.listarProveedoresPorProducto(inventarioId)
                .stream()
                .filter(InventarioProveedor::isEsProveedorPrincipal)
                .findFirst()
                .orElse(null);
    }

    /**
     * Calcula el precio de compra promedio de un producto (Programación Funcional)
     */
    public BigDecimal calcularPrecioPromedioCompra(int inventarioId) {
        List<InventarioProveedor> proveedores = inventarioProveedorDAO.listarProveedoresPorProducto(inventarioId);

        return proveedores.stream()
                .map(InventarioProveedor::getPrecioCompra)
                .filter(precio -> precio != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(proveedores.size()), BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Obtiene el proveedor con mejor tiempo de entrega (Programación Funcional)
     */
    public InventarioProveedor obtenerProveedorMasRapido(int inventarioId) {
        return inventarioProveedorDAO.listarProveedoresPorProducto(inventarioId)
                .stream()
                .filter(p -> p.getTiempoEntregaDias() != null)
                .min((p1, p2) -> Integer.compare(p1.getTiempoEntregaDias(), p2.getTiempoEntregaDias()))
                .orElse(null);
    }

    /**
     * Genera reporte de proveedores por producto (Programación Funcional)
     */
    public void generarReporteProveedoresPorProducto() {
        System.out.println("\n=== REPORTE DE PROVEEDORES POR PRODUCTO ===");

        List<InventarioProveedor> todasRelaciones = inventarioProveedorDAO.listarTodas();

        if (todasRelaciones.isEmpty()) {
            System.out.println("No hay relaciones registradas.");
            return;
        }

        // Agrupar por producto
        Map<String, List<InventarioProveedor>> porProducto = todasRelaciones.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getNombreProducto() != null ? r.getNombreProducto() : "Producto ID:" + r.getInventarioId()
                ));

        porProducto.forEach((producto, relaciones) -> {
            System.out.println("\n" + producto + ":");
            System.out.println("  Total de proveedores: " + relaciones.size());

            relaciones.stream()
                    .filter(InventarioProveedor::isEsProveedorPrincipal)
                    .findFirst()
                    .ifPresent(principal ->
                            System.out.println("  Proveedor principal: " + principal.getNombreProveedor())
                    );

            // Precio promedio
            double precioPromedio = relaciones.stream()
                    .map(InventarioProveedor::getPrecioCompra)
                    .filter(precio -> precio != null)
                    .mapToDouble(BigDecimal::doubleValue)
                    .average()
                    .orElse(0.0);

            if (precioPromedio > 0) {
                System.out.printf("  Precio promedio de compra: $%.2f%n", precioPromedio);
            }

            // Mejor tiempo de entrega
            relaciones.stream()
                    .filter(r -> r.getTiempoEntregaDias() != null)
                    .min((r1, r2) -> Integer.compare(r1.getTiempoEntregaDias(), r2.getTiempoEntregaDias()))
                    .ifPresent(masRapido ->
                            System.out.println("  Entrega más rápida: " + masRapido.getTiempoEntregaDias() +
                                             " días (" + masRapido.getNombreProveedor() + ")")
                    );
        });
    }

    /**
     * Genera reporte de productos por proveedor (Programación Funcional)
     */
    public void generarReporteProductosPorProveedor() {
        System.out.println("\n=== REPORTE DE PRODUCTOS POR PROVEEDOR ===");

        List<InventarioProveedor> todasRelaciones = inventarioProveedorDAO.listarTodas();

        if (todasRelaciones.isEmpty()) {
            System.out.println("No hay relaciones registradas.");
            return;
        }

        // Agrupar por proveedor
        Map<String, List<InventarioProveedor>> porProveedor = todasRelaciones.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getNombreProveedor() != null ? r.getNombreProveedor() : "Proveedor ID:" + r.getProveedorId()
                ));

        porProveedor.forEach((proveedor, relaciones) -> {
            System.out.println("\n" + proveedor + ":");
            System.out.println("  Total de productos: " + relaciones.size());

            long productosPrincipales = relaciones.stream()
                    .filter(InventarioProveedor::isEsProveedorPrincipal)
                    .count();

            System.out.println("  Productos donde es proveedor principal: " + productosPrincipales);

            // Tiempo promedio de entrega
            double tiempoPromedio = relaciones.stream()
                    .map(InventarioProveedor::getTiempoEntregaDias)
                    .filter(tiempo -> tiempo != null)
                    .mapToInt(Integer::intValue)
                    .average()
                    .orElse(0.0);

            if (tiempoPromedio > 0) {
                System.out.printf("  Tiempo promedio de entrega: %.1f días%n", tiempoPromedio);
            }
        });
    }

    /**
     * Lista todas las relaciones
     */
    public List<InventarioProveedor> listarTodas() {
        return inventarioProveedorDAO.listarTodas();
    }
}