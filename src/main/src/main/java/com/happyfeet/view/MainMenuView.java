package com.happyfeet.view;

import java.util.Scanner;

/**
 * Vista Principal del Sistema HappyFeet
 *
 * FUNCIÓN: Selector de módulos principales
 * Esta clase NO contiene lógica de negocio ni funcionalidades internas.
 * Solo muestra el menú principal y redirige a las vistas correspondientes.
 *
 * FLUJO: Main.java → MainMenuView → Vista del Módulo → Funcionalidades
 */
public class MainMenuView {

    private final Scanner sc = new Scanner(System.in);

    /**
     * Inicia el menú principal del sistema
     */
    public void iniciar() {
        while (true) {
            mostrarMenuPrincipal();
            int opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> iniciarModuloPacientes();
                case 2 -> iniciarModuloServicios();
                case 3 -> iniciarModuloInventario();
                case 4 -> iniciarModuloFacturacion();
                case 5 -> iniciarModuloActividades();
                case 6 -> iniciarModuloReportes();
                case 7 -> IniciarReporteCompleto();
                case 0 -> {
                    System.out.println("\n¡Gracias por usar HappyFeet! 🐾");
                    System.out.println("Hasta pronto...\n");
                    return; // Salir del sistema
                }
                default -> System.out.println("❌ Opción no válida. Intente nuevamente.");
            }
        }
    }

    /**
     * Muestra el menú principal con los módulos disponibles
     */
    private void mostrarMenuPrincipal() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("           🐾 CLÍNICA VETERINARIA HAPPYFEET 🐾");
        System.out.println("=".repeat(60));
        System.out.println("  SELECCIONE EL MÓDULO AL QUE DESEA ACCEDER:");
        System.out.println();
        System.out.println("  1. 👥  Gestión de Pacientes (Dueños y Mascotas)");
        System.out.println("  2. 🏥  Servicios Médicos y Citas");
        System.out.println("  3. 📦  Inventario y Farmacia");
        System.out.println("  4. 💰  Facturación");
        System.out.println("  5. 🎪  Actividades Especiales");
        System.out.println("  6. 📊  Reportes Gerenciales");
        System.out.println("  7. 📊  Reporte Completo");
        System.out.println();
        System.out.println("  0. 🚪  Salir del Sistema");
        System.out.println("=".repeat(60));
    }

    // =====================================================================
    // ===== MÓDULO 1: GESTIÓN DE PACIENTES (Dueños y Mascotas) =====
    // =====================================================================
    /**
     * Redirige al módulo de gestión de pacientes
     * Delega toda la funcionalidad a PacienteView
     */
    private void iniciarModuloPacientes() {
        PacienteView pacienteView = new PacienteView(sc);
        pacienteView.mostrarMenu();
    }

    // =====================================================================
    // ===== MÓDULO 2: SERVICIOS MÉDICOS Y CITAS =====
    // =====================================================================
    /**
     * Redirige al módulo de servicios médicos
     * Delega toda la funcionalidad a ServiciosMedicosView
     */
    private void iniciarModuloServicios() {
        ServiciosMedicosView serviciosView = new ServiciosMedicosView(sc);
        serviciosView.mostrarMenu();
    }

    // =====================================================================
    // ===== MÓDULO 3: INVENTARIO Y FARMACIA =====
    // =====================================================================
    /**
     * Redirige al módulo de inventario y farmacia
     * Delega toda la funcionalidad a InventarioView
     */
    private void iniciarModuloInventario() {
        InventarioView inventarioView = new InventarioView(sc);
        inventarioView.mostrarMenu();
    }

    // =====================================================================
    // ===== MÓDULO 4: FACTURACIÓN =====
    // =====================================================================
    /**
     * Redirige al módulo de facturación
     * Delega toda la funcionalidad a FacturacionView
     */
    private void iniciarModuloFacturacion() {
        FacturacionView facturacionView = new FacturacionView(sc);
        facturacionView.mostrarMenu();
    }

    // =====================================================================
    // ===== MÓDULO 5: ACTIVIDADES ESPECIALES =====
    // =====================================================================
    /**
     * Redirige al módulo de actividades especiales
     * Delega toda la funcionalidad a ActividadesView
     */
    private void iniciarModuloActividades() {
        ActividadesView actividadesView = new ActividadesView(sc);
        actividadesView.mostrarMenu();
    }

    // =====================================================================
    // ===== MÓDULO 6: REPORTES GERENCIALES =====
    // =====================================================================
    /**
     * Redirige al módulo de reportes gerenciales
     * Delega toda la funcionalidad a ReportesView
     */
    private void iniciarModuloReportes() {
        ReportesView reportesView = new ReportesView(sc);
        reportesView.mostrarMenu();
    }
    
    private void IniciarReporteCompleto() {
        ReportesView reportesView = new ReportesView(sc);
        reportesView.mostrarMenu();
    }

    // =====================================================================
    // ===== MÉTODOS AUXILIARES =====
    // =====================================================================
    /**
     * Lee un número entero validado desde la consola
     */
    private int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Error: Ingrese un número válido");
            }
        }
    }
}