package com.happyfeet.service;

import com.happyfeet.dao.ProveedorDAO;
import com.happyfeet.model.Proveedor;

import java.util.List;

public class ProveedorService {
    private final ProveedorDAO proveedorDAO = new ProveedorDAO();

    public void registrarProveedor(Proveedor proveedor) {
        // Validaciones básicas
        if (proveedor.getNombre() == null || proveedor.getNombre().trim().isEmpty()) {
            System.out.println("❌ El nombre del proveedor es obligatorio");
            return;
        }

        proveedorDAO.guardar(proveedor);
    }

    public List<Proveedor> listarProveedores() {
        return proveedorDAO.listar();
    }

    public List<Proveedor> buscarProveedor(String nombre) {
        return proveedorDAO.buscarPorNombre(nombre);
    }

    public void actualizarProveedor(Proveedor proveedor) {
        if (proveedor.getId() <= 0) {
            System.out.println("❌ ID de proveedor inválido");
            return;
        }

        if (proveedor.getNombre() == null || proveedor.getNombre().trim().isEmpty()) {
            System.out.println("❌ El nombre del proveedor es obligatorio");
            return;
        }

        proveedorDAO.actualizar(proveedor);
    }
}