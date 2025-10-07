package com.happyfeet.view;

import com.happyfeet.model.Cita;
import com.happyfeet.model.HistorialMedico;
import com.happyfeet.model.Mascota;
import com.happyfeet.service.CitaService;
import com.happyfeet.service.HistorialMedicoService;
import com.happyfeet.service.MascotaService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Vista del Módulo de Servicios Médicos y Citas
 * Maneja todas las funcionalidades relacionadas con servicios médicos
 */
public class ServiciosMedicosView {

    private final Scanner sc;
    private final CitaService citaService;
    private final HistorialMedicoService historialService;
    private final MascotaService mascotaService;

    public ServiciosMedicosView(Scanner sc) {
        this.sc = sc;
        this.citaService = new CitaService();
        this.historialService = new HistorialMedicoService();
        this.mascotaService = new MascotaService();
    }

    /**
     * Muestra el menú principal del módulo de servicios médicos
     */
    public void mostrarMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("   🏥 MÓDULO: SERVICIOS MÉDICOS Y CITAS");
            System.out.println("=".repeat(50));
            System.out.println("1. Agendar cita");
            System.out.println("2. Listar citas");
            System.out.println("3. Cambiar estado de cita");
            System.out.println("4. Registrar consulta médica");
            System.out.println("5. Ver historial médico");
            System.out.println("6. Buscar citas por mascota");
            System.out.println("7. Procedimientos Especiales (Cirugías)");
            System.out.println("0. Volver al menú principal");
            System.out.println("=".repeat(50));

            int opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> agendarCita();
                case 2 -> listarCitas();
                case 3 -> cambiarEstadoCita();
                case 4 -> registrarConsulta();
                case 5 -> verHistorialMedico();
                case 6 -> buscarCitasPorMascota();
                case 7 -> gestionarProcedimientosEspeciales();
                case 0 -> {
                    return; // Volver al menú principal
                }
                default -> System.out.println("❌ Opción no válida");
            }
        }
    }

    // ==================== FUNCIONES DEL MÓDULO ====================

    private void agendarCita() {
        System.out.println("\n--- Agendar Cita ---");

        List<Mascota> mascotas = mascotaService.listarMascotas();
        if (mascotas.isEmpty()) {
            System.out.println("❌ No hay mascotas registradas.");
            return;
        }

        System.out.println("Mascotas disponibles:");
        mascotas.forEach(m -> System.out.println(m.getId() + ". " + m.getNombre()));

        int mascotaId = leerEntero("ID de la mascota: ");

        // Validar que la mascota existe
        if (mascotas.stream().noneMatch(m -> m.getId() == mascotaId)) {
            System.out.println("❌ ID de mascota inválido");
            return;
        }

        System.out.print("Fecha y hora (YYYY-MM-DD HH:MM): ");
        String fechaHoraStr = sc.nextLine().trim();
        System.out.print("Motivo de la cita: ");
        String motivo = sc.nextLine().trim();
        if (motivo.isEmpty()) {
            System.out.println("❌ El motivo no puede estar vacío");
            return;
        }

        try {
            LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraStr,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            Cita cita = new Cita(0, mascotaId, fechaHora, motivo, 1);
            citaService.agendarCita(cita);
        } catch (DateTimeParseException e) {
            System.out.println("❌ Formato de fecha inválido. Use YYYY-MM-DD HH:MM");
        }
    }

    private void listarCitas() {
        System.out.println("\n--- Lista de Citas ---");
        List<Cita> citas = citaService.listarCitas();
        if (citas.isEmpty()) {
            System.out.println("No hay citas registradas.");
        } else {
            citas.forEach(System.out::println);
        }
    }

    private void cambiarEstadoCita() {
        int citaId = leerEntero("ID de la cita: ");
        citaService.mostrarEstadosDisponibles();
        int nuevoEstado = leerEntero("Nuevo estado: ");
        citaService.cambiarEstadoCita(citaId, nuevoEstado);
    }

    private void registrarConsulta() {
        System.out.println("\n--- Registrar Consulta Médica ---");

        List<Mascota> mascotas = mascotaService.listarMascotas();
        if (mascotas.isEmpty()) {
            System.out.println("❌ No hay mascotas registradas.");
            return;
        }

        System.out.println("Mascotas disponibles:");
        mascotas.forEach(m -> System.out.println(m.getId() + ". " + m.getNombre()));

        int mascotaId = leerEntero("ID de la mascota: ");

        // Validar que la mascota existe
        if (mascotas.stream().noneMatch(m -> m.getId() == mascotaId)) {
            System.out.println("❌ ID de mascota inválido");
            return;
        }

        System.out.print("Descripción de la consulta: ");
        String descripcion = sc.nextLine().trim();
        if (descripcion.isEmpty()) {
            System.out.println("❌ La descripción no puede estar vacía");
            return;
        }

        System.out.print("Diagnóstico: ");
        String diagnostico = sc.nextLine().trim();
        System.out.print("Tratamiento recomendado: ");
        String tratamiento = sc.nextLine().trim();

        historialService.mostrarTiposEventoDisponibles();
        int tipoEvento = leerEntero("Tipo de evento: ");

        System.out.print("Medicamento prescrito (opcional): ");
        String medicamento = sc.nextLine();
        int cantidad = 0;
        if (!medicamento.isEmpty()) {
            cantidad = leerEntero("Cantidad: ");
        }

        historialService.registrarConsultaCompleta(mascotaId, descripcion, diagnostico,
                                                  tratamiento, tipoEvento, medicamento, cantidad);
    }

    private void verHistorialMedico() {
        System.out.println("\n--- Historial Médico General ---");
        List<HistorialMedico> historial = historialService.listarHistorial();
        if (historial.isEmpty()) {
            System.out.println("No hay registros médicos.");
        } else {
            historial.forEach(System.out::println);
        }
    }

    private void buscarCitasPorMascota() {
        int mascotaId = leerEntero("ID de la mascota: ");
        List<Cita> citas = citaService.buscarCitasPorMascota(mascotaId);

        if (citas.isEmpty()) {
            System.out.println("No hay citas para esta mascota.");
        } else {
            System.out.println("\n--- Citas de la Mascota ---");
            citas.forEach(System.out::println);
        }
    }

    private void gestionarProcedimientosEspeciales() {
        // Delegar a la vista especializada de procedimientos
        ProcedimientoEspecialView procedimientoView = new ProcedimientoEspecialView(sc);
        procedimientoView.mostrarMenu();
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
}