package com.happyfeet.service;

import com.happyfeet.dao.InventarioDAO;
import com.happyfeet.model.Inventario;
import com.happyfeet.model.ProductoTipo;

import java.time.LocalDate;
import java.util.List;

public class InventarioService {
    private final InventarioDAO inventarioDAO = new InventarioDAO();

    public void agregarProducto(Inventario producto) {
        // Validaciones básicas
        if (producto.getCantidadStock() < 0) {
            System.out.println("❌ La cantidad de stock no puede ser negativa");
            return;
        }

        if (producto.getStockMinimo() < 0) {
            System.out.println("❌ El stock mínimo no puede ser negativo");
            return;
        }

        if (producto.getFechaVencimiento() != null && producto.getFechaVencimiento().isBefore(LocalDate.now())) {
            System.out.println("❌ No se puede agregar un producto ya vencido");
            return;
        }

        inventarioDAO.guardar(producto);
    }

    public List<Inventario> listarInventario() {
        return inventarioDAO.listar();
    }

    public List<Inventario> buscarProducto(String nombre) {
        return inventarioDAO.buscarPorNombre(nombre);
    }

    public boolean deducirStock(String nombreProducto, int cantidad) {
        return inventarioDAO.deducirStock(nombreProducto, cantidad);
    }

    public boolean actualizarStock(int productoId, int nuevaCantidad) {
        if (nuevaCantidad < 0) {
            System.out.println("❌ La cantidad no puede ser negativa");
            return false;
        }
        return inventarioDAO.actualizarStock(productoId, nuevaCantidad);
    }

    public List<ProductoTipo> listarTiposProducto() {
        return inventarioDAO.listarTiposProducto();
    }

    public void mostrarTiposProductoDisponibles() {
        System.out.println("\n=== Tipos de Producto Disponibles ===");
        List<ProductoTipo> tipos = listarTiposProducto();
        for (ProductoTipo tipo : tipos) {
            System.out.println(tipo.getId() + ". " + tipo.getNombre());
        }
    }
}