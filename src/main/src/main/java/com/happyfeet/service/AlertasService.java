package com.happyfeet.service;

import com.happyfeet.dao.InventarioDAO;
import com.happyfeet.model.Inventario;

import java.util.List;

public class AlertasService {
    private final InventarioDAO inventarioDAO = new InventarioDAO();

    public void mostrarAlertasInventario() {
        System.out.println("\n=== 🔔 ALERTAS DEL SISTEMA ===");

        // Productos con stock bajo
        List<Inventario> stockBajo = inventarioDAO.obtenerProductosBajoStock();
        if (!stockBajo.isEmpty()) {
            System.out.println("\n⚠️  PRODUCTOS CON STOCK BAJO:");
            for (Inventario producto : stockBajo) {
                System.out.println("   • " + producto.getNombreProducto() +
                        " - Stock actual: " + producto.getCantidadStock() +
                        " (Mínimo: " + producto.getStockMinimo() + ")");
            }
        }

        // Productos próximos a vencer (30 días)
        List<Inventario> proximosVencer = inventarioDAO.obtenerProductosProximosAVencer(30);
        if (!proximosVencer.isEmpty()) {
            System.out.println("\n📅 PRODUCTOS PRÓXIMOS A VENCER (30 días):");
            for (Inventario producto : proximosVencer) {
                System.out.println("   • " + producto.getNombreProducto() +
                        " - Vence: " + producto.getFechaVencimiento() +
                        " (Lote: " + producto.getLote() + ")");
            }
        }

        // Productos vencidos
        List<Inventario> todosProductos = inventarioDAO.listar();
        boolean hayVencidos = false;
        for (Inventario producto : todosProductos) {
            if (producto.estaVencido()) {
                if (!hayVencidos) {
                    System.out.println("\n❌ PRODUCTOS VENCIDOS:");
                    hayVencidos = true;
                }
                System.out.println("   • " + producto.getNombreProducto() +
                        " - Venció: " + producto.getFechaVencimiento() +
                        " (Stock: " + producto.getCantidadStock() + ")");
            }
        }

        if (stockBajo.isEmpty() && proximosVencer.isEmpty() && !hayVencidos) {
            System.out.println("✅ No hay alertas pendientes. Todo el inventario está en orden.");
        }
    }

    public int contarAlertas() {
        List<Inventario> stockBajo = inventarioDAO.obtenerProductosBajoStock();
        List<Inventario> proximosVencer = inventarioDAO.obtenerProductosProximosAVencer(30);
        List<Inventario> todosProductos = inventarioDAO.listar();

        int vencidos = 0;
        for (Inventario producto : todosProductos) {
            if (producto.estaVencido()) {
                vencidos++;
            }
        }

        return stockBajo.size() + proximosVencer.size() + vencidos;
    }

    public void generarReporteInventario() {
        System.out.println("\n=== 📊 REPORTE DE INVENTARIO ===");

        List<Inventario> productos = inventarioDAO.listar();

        if (productos.isEmpty()) {
            System.out.println("No hay productos en el inventario.");
            return;
        }

        // Estadísticas generales
        System.out.println("Total de productos: " + productos.size());

        // Agrupar por tipo
        System.out.println("\n--- Por Tipo de Producto ---");
        productos.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        Inventario::getTipoProducto,
                        java.util.stream.Collectors.counting()))
                .forEach((tipo, cantidad) ->
                        System.out.println(tipo + ": " + cantidad + " productos"));

        // Estado del inventario
        long enBuenEstado = productos.stream()
                .filter(p -> !p.estaVencido() && !p.necesitaReabastecimiento() && !p.venceProximamente(30))
                .count();

        long requierenAtencion = productos.size() - enBuenEstado;

        System.out.println("\n--- Estado General ---");
        System.out.println("En buen estado: " + enBuenEstado);
        System.out.println("Requieren atención: " + requierenAtencion);

        // Valor total del inventario
        double valorTotal = productos.stream()
                .mapToDouble(p -> p.getPrecioVenta().doubleValue() * p.getCantidadStock())
                .sum();

        System.out.println("Valor total del inventario: $" + String.format("%.2f", valorTotal));
    }
}