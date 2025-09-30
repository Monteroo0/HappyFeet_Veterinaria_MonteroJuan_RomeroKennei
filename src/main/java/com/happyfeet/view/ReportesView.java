package com.happyfeet.view;

import com.happyfeet.service.AlertasService;
import com.happyfeet.service.ReportesService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Vista del Módulo de Reportes Gerenciales
 * Maneja generación de reportes y alertas
 */
public class ReportesView {

    private final Scanner sc;
    private final ReportesService reportesService;
    private final AlertasService alertasService;

    public ReportesView(Scanner sc) {
        this.sc = sc;
        this.reportesService = new ReportesService();
        this.alertasService = new AlertasService();
    }

    public void mostrarMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("   📊 MÓDULO: REPORTES GERENCIALES");
            System.out.println("=".repeat(50));
            System.out.println("1. Servicios más solicitados");
            System.out.println("2. Reporte de facturación");
            System.out.println("3. Estado de citas");
            System.out.println("4. Reporte de inventario");
            System.out.println("5. Reporte completo");
            System.out.println("6. Reporte por período");
            System.out.println("7. Ver alertas del sistema");
            System.out.println("0. Volver al menú principal");
            System.out.println("=".repeat(50));

            int opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> reportesService.generarReporteServicios();
                case 2 -> reportesService.generarReporteFacturacion();
                case 3 -> reportesService.generarReporteCitas();
                case 4 -> alertasService.generarReporteInventario();
                case 5 -> reportesService.generarReporteCompleto();
                case 6 -> generarReportePorPeriodo();
                case 7 -> alertasService.mostrarAlertasInventario();
                case 0 -> {
                    return;
                }
                default -> System.out.println("❌ Opción no válida");
            }
        }
    }

    private void generarReportePorPeriodo() {
        System.out.println("\n--- Reporte por Período ---");
        LocalDate fechaInicio = leerFecha("Fecha de inicio (YYYY-MM-DD): ");
        LocalDate fechaFin = leerFecha("Fecha de fin (YYYY-MM-DD): ");

        if (fechaFin.isBefore(fechaInicio)) {
            System.out.println("❌ La fecha de fin no puede ser anterior a la fecha de inicio");
            return;
        }

        reportesService.generarReportePorPeriodo(fechaInicio, fechaFin);
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
}