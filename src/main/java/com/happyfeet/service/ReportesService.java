package com.happyfeet.service;

import com.happyfeet.dao.FacturaDAO;
import com.happyfeet.dao.HistorialMedicoDAO;
import com.happyfeet.dao.CitaDAO;
import com.happyfeet.model.Factura;
import com.happyfeet.model.HistorialMedico;
import com.happyfeet.model.Cita;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportesService {
    private final FacturaDAO facturaDAO = new FacturaDAO();
    private final HistorialMedicoDAO historialDAO = new HistorialMedicoDAO();
    private final CitaDAO citaDAO = new CitaDAO();
    private final AlertasService alertasService = new AlertasService();

    public void generarReporteServicios() {
        System.out.println("\n=== 📊 REPORTE DE SERVICIOS MÁS SOLICITADOS ===");

        List<HistorialMedico> historiales = historialDAO.listar();

        if (historiales.isEmpty()) {
            System.out.println("No hay registros médicos para generar el reporte.");
            return;
        }

        // Agrupar por tipo de evento y contar
        Map<String, Long> servicios = historiales.stream()
                .collect(Collectors.groupingBy(
                        HistorialMedico::getTipoEvento,
                        Collectors.counting()));

        System.out.println("Servicios realizados:");
        servicios.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(entry ->
                        System.out.println("  • " + entry.getKey() + ": " + entry.getValue() + " servicios"));

        // Mostrar el más solicitado
        String servicioMasSolicitado = servicios.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");

        System.out.println("\n🏆 Servicio más solicitado: " + servicioMasSolicitado);
    }

    public void generarReporteFacturacion() {
        System.out.println("\n=== 💰 REPORTE DE FACTURACIÓN ===");

        List<Factura> facturas = facturaDAO.listar();

        if (facturas.isEmpty()) {
            System.out.println("No hay facturas registradas.");
            return;
        }

        // Calcular totales
        BigDecimal totalFacturado = facturas.stream()
                .map(Factura::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("Total de facturas: " + facturas.size());
        System.out.println("Total facturado: $" + String.format("%.2f", totalFacturado.doubleValue()));

        // Promedio por factura
        BigDecimal promedio = totalFacturado.divide(BigDecimal.valueOf(facturas.size()), 2, BigDecimal.ROUND_HALF_UP);
        System.out.println("Promedio por factura: $" + String.format("%.2f", promedio.doubleValue()));

        // Facturación por cliente (top 5)
        Map<String, BigDecimal> facturacionPorCliente = facturas.stream()
                .collect(Collectors.groupingBy(
                        Factura::getNombreDueno,
                        Collectors.reducing(BigDecimal.ZERO, Factura::getTotal, BigDecimal::add)));

        System.out.println("\n--- Top 5 Clientes por Facturación ---");
        facturacionPorCliente.entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .limit(5)
                .forEach(entry ->
                        System.out.println("  • " + entry.getKey() + ": $" +
                                String.format("%.2f", entry.getValue().doubleValue())));
    }

    public void generarReporteCitas() {
        System.out.println("\n=== 📅 REPORTE DE CITAS ===");

        List<Cita> citas = citaDAO.listar();

        if (citas.isEmpty()) {
            System.out.println("No hay citas registradas.");
            return;
        }

        // Agrupar por estado
        Map<String, Long> citasPorEstado = citas.stream()
                .collect(Collectors.groupingBy(
                        Cita::getEstadoNombre,
                        Collectors.counting()));

        System.out.println("Citas por estado:");
        citasPorEstado.forEach((estado, cantidad) ->
                System.out.println("  • " + estado + ": " + cantidad));

        // Citas del mes actual
        long citasEsteMes = citas.stream()
                .filter(cita -> cita.getFechaHora().getMonth() == LocalDate.now().getMonth() &&
                        cita.getFechaHora().getYear() == LocalDate.now().getYear())
                .count();

        System.out.println("\nCitas este mes: " + citasEsteMes);

        // Próximas citas (siguientes 7 días)
        long proximasCitas = citas.stream()
                .filter(cita -> cita.getFechaHora().toLocalDate().isAfter(LocalDate.now()) &&
                        cita.getFechaHora().toLocalDate().isBefore(LocalDate.now().plusDays(8)))
                .count();

        System.out.println("Próximas citas (7 días): " + proximasCitas);
    }

    public void generarReporteCompleto() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("             REPORTE GERENCIAL COMPLETO");
        System.out.println("        Fecha: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("=".repeat(60));

        // Servicios más solicitados
        generarReporteServicios();

        // Facturación
        generarReporteFacturacion();

        // Estado de citas
        generarReporteCitas();

        // Estado del inventario
        alertasService.generarReporteInventario();

        // Resumen de alertas
        int totalAlertas = alertasService.contarAlertas();
        System.out.println("\n=== 🔔 RESUMEN DE ALERTAS ===");
        if (totalAlertas > 0) {
            System.out.println("⚠️  Alertas pendientes: " + totalAlertas);
            System.out.println("   (Revisar módulo de inventario para detalles)");
        } else {
            System.out.println("✅ No hay alertas pendientes");
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("           FIN DEL REPORTE GERENCIAL");
        System.out.println("=".repeat(60));
    }

    public void generarReportePorPeriodo(LocalDate fechaInicio, LocalDate fechaFin) {
        System.out.println("\n=== 📈 REPORTE POR PERÍODO ===");
        System.out.println("Período: " + fechaInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                " - " + fechaFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        List<Factura> facturas = facturaDAO.listar();

        // Filtrar facturas por período
        List<Factura> facturasPeriodo = facturas.stream()
                .filter(f -> {
                    LocalDate fechaFactura = f.getFechaEmision().toLocalDate();
                    return !fechaFactura.isBefore(fechaInicio) && !fechaFactura.isAfter(fechaFin);
                })
                .collect(Collectors.toList());

        if (facturasPeriodo.isEmpty()) {
            System.out.println("No hay facturas en el período seleccionado.");
            return;
        }

        BigDecimal totalPeriodo = facturasPeriodo.stream()
                .map(Factura::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("Facturas en el período: " + facturasPeriodo.size());
        System.out.println("Total facturado: $" + String.format("%.2f", totalPeriodo.doubleValue()));

        // Facturación por día
        Map<LocalDate, BigDecimal> facturacionPorDia = facturasPeriodo.stream()
                .collect(Collectors.groupingBy(
                        f -> f.getFechaEmision().toLocalDate(),
                        Collectors.reducing(BigDecimal.ZERO, Factura::getTotal, BigDecimal::add)));

        System.out.println("\n--- Facturación por día ---");
        facturacionPorDia.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry ->
                        System.out.println(entry.getKey().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                                ": $" + String.format("%.2f", entry.getValue().doubleValue())));
    }
}