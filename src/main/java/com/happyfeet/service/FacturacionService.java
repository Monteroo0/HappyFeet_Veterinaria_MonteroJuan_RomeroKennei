package com.happyfeet.service;

import com.happyfeet.dao.FacturaDAO;
import com.happyfeet.model.Factura;
import com.happyfeet.model.ItemFactura;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FacturacionService {
    private final FacturaDAO facturaDAO = new FacturaDAO();
    private final InventarioService inventarioService = new InventarioService();

    public int generarFactura(Factura factura) {
        // Validaciones
        if (factura.getItems().isEmpty()) {
            System.out.println("❌ No se puede generar una factura sin items");
            return -1;
        }

        // Establecer fecha de emisión si no está definida
        if (factura.getFechaEmision() == null) {
            factura.setFechaEmision(LocalDateTime.now());
        }

        // Calcular total
        BigDecimal total = factura.calcularTotal();
        factura.setTotal(total);

        // Procesar items que son productos (deducir del inventario)
        for (ItemFactura item : factura.getItems()) {
            if (item.esProducto() && item.getNombreProducto() != null) {
                boolean deducido = inventarioService.deducirStock(item.getNombreProducto(), item.getCantidad());
                if (!deducido) {
                    System.out.println("⚠️  Advertencia: No se pudo deducir '" + item.getNombreProducto() + "' del inventario");
                }
            }
        }

        return facturaDAO.guardar(factura);
    }

    public List<Factura> listarFacturas() {
        return facturaDAO.listar();
    }

    public Factura obtenerFactura(int facturaId) {
        return facturaDAO.obtenerPorId(facturaId);
    }

    public List<Factura> buscarFacturasPorDueno(int duenoId) {
        return facturaDAO.buscarPorDueno(duenoId);
    }

    public void imprimirFactura(int facturaId) {
        Factura factura = facturaDAO.obtenerPorId(facturaId);
        if (factura == null) {
            System.out.println("❌ Factura no encontrada");
            return;
        }

        System.out.println(generarFacturaTextoPlano(factura));
    }

    public String generarFacturaTextoPlano(Factura factura) {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // Encabezado de la clínica
        sb.append("===============================================\n");
        sb.append("           CLÍNICA VETERINARIA HAPPYFEET       \n");
        sb.append("===============================================\n");
        sb.append("  Dirección: Calle Principal #123, Ciudad     \n");
        sb.append("  Teléfono: (607) 123-4567                    \n");
        sb.append("  Email: info@happyfeet.com                   \n");
        sb.append("===============================================\n\n");

        // Información de la factura
        sb.append("FACTURA N°: ").append(String.format("%06d", factura.getId())).append("\n");
        sb.append("Fecha: ").append(factura.getFechaEmision().format(formatter)).append("\n\n");

        // Información del cliente
        sb.append("CLIENTE:\n");
        sb.append("Nombre: ").append(factura.getNombreDueno()).append("\n");
        sb.append("Documento: ").append(factura.getDocumentoDueno()).append("\n\n");

        // Detalle de items
        sb.append("DETALLE DE SERVICIOS Y PRODUCTOS:\n");
        sb.append("-----------------------------------------------\n");
        sb.append(String.format("%-25s %5s %10s %10s\n", "DESCRIPCIÓN", "CANT", "PRECIO", "SUBTOTAL"));
        sb.append("-----------------------------------------------\n");

        for (ItemFactura item : factura.getItems()) {
            String descripcion = item.getDescripcionCompleta();
            if (descripcion.length() > 25) {
                descripcion = descripcion.substring(0, 22) + "...";
            }

            sb.append(String.format("%-25s %5d %10.2f %10.2f\n",
                    descripcion,
                    item.getCantidad(),
                    item.getPrecioUnitario().doubleValue(),
                    item.getSubtotal().doubleValue()));
        }

        sb.append("-----------------------------------------------\n");
        sb.append(String.format("%41s %10.2f\n", "TOTAL A PAGAR: $", factura.getTotal().doubleValue()));
        sb.append("===============================================\n\n");

        // Pie de página
        sb.append("        ¡Gracias por confiar en nosotros!     \n");
        sb.append("    Su mascota es nuestra responsabilidad     \n");
        sb.append("===============================================\n");

        return sb.toString();
    }

    public ItemFactura crearItemProducto(String nombreProducto, int cantidad, BigDecimal precio) {
        ItemFactura item = new ItemFactura();
        item.setNombreProducto(nombreProducto);
        item.setCantidad(cantidad);
        item.setPrecioUnitario(precio);
        item.setSubtotal(precio.multiply(BigDecimal.valueOf(cantidad)));
        return item;
    }

    public ItemFactura crearItemServicio(String descripcionServicio, int cantidad, BigDecimal precio) {
        ItemFactura item = new ItemFactura();
        item.setServicioDescripcion(descripcionServicio);
        item.setCantidad(cantidad);
        item.setPrecioUnitario(precio);
        item.setSubtotal(precio.multiply(BigDecimal.valueOf(cantidad)));
        return item;
    }
}