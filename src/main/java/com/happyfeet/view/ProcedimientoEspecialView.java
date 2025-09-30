package com.happyfeet.view;

import com.happyfeet.model.Mascota;
import com.happyfeet.model.entities.ProcedimientoEspecial;
import com.happyfeet.service.MascotaService;
import com.happyfeet.service.ProcedimientoEspecialService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Vista para el módulo de Procedimientos Especiales
 */
public class ProcedimientoEspecialView {

    private final Scanner sc;
    private final ProcedimientoEspecialService procedimientoService;
    private final MascotaService mascotaService;

    public ProcedimientoEspecialView(Scanner sc) {
        this.sc = sc;
        this.procedimientoService = new ProcedimientoEspecialService();
        this.mascotaService = new MascotaService();
    }

    public void mostrarMenu() {
        while (true) {
            System.out.println("\n=== PROCEDIMIENTOS ESPECIALES ===");
            System.out.println("1. Registrar procedimiento");
            System.out.println("2. Listar procedimientos de una mascota");
            System.out.println("3. Listar procedimientos en recuperación");
            System.out.println("4. Ver próximas revisiones");
            System.out.println("5. Actualizar estado de procedimiento");
            System.out.println("6. Generar estadísticas");
            System.out.println("7. Reporte por tipo");
            System.out.println("0. Volver");

            int opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> registrarProcedimiento();
                case 2 -> listarPorMascota();
                case 3 -> listarEnRecuperacion();
                case 4 -> verProximasRevisiones();
                case 5 -> actualizarEstado();
                case 6 -> procedimientoService.generarEstadisticas();
                case 7 -> procedimientoService.generarReportePorTipo();
                case 0 -> { return; }
                default -> System.out.println("❌ Opción no válida");
            }
        }
    }

    private void registrarProcedimiento() {
        System.out.println("\n--- Registrar Procedimiento Especial ---");

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

        // Tipo de procedimiento
        procedimientoService.mostrarTiposDisponibles();
        int tipoOpcion = leerEntero("Tipo de procedimiento: ");
        ProcedimientoEspecial.TipoProcedimiento tipo = obtenerTipoProcedimiento(tipoOpcion);

        if (tipo == null) {
            System.out.println("❌ Tipo de procedimiento inválido");
            return;
        }

        System.out.print("Fecha y hora del procedimiento (YYYY-MM-DD HH:MM): ");
        String fechaStr = sc.nextLine();
        LocalDateTime fechaProcedimiento;

        try {
            fechaProcedimiento = LocalDateTime.parse(fechaStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (DateTimeParseException e) {
            System.out.println("❌ Formato de fecha inválido");
            return;
        }

        System.out.print("Veterinario responsable: ");
        String veterinario = sc.nextLine().trim();
        if (veterinario.isEmpty()) {
            System.out.println("❌ El veterinario responsable no puede estar vacío");
            return;
        }

        // Información preoperatoria
        System.out.println("\n--- Información Preoperatoria ---");
        System.out.print("Diagnóstico preoperatorio: ");
        String diagnostico = sc.nextLine().trim();
        if (diagnostico.isEmpty()) {
            System.out.println("❌ El diagnóstico no puede estar vacío");
            return;
        }

        System.out.print("Análisis previos realizados: ");
        String analisis = sc.nextLine();

        System.out.print("Medicación previa: ");
        String medicacionPrevia = sc.nextLine();

        System.out.print("¿Requiere ayuno? (S/N): ");
        boolean ayuno = sc.nextLine().trim().equalsIgnoreCase("S");

        System.out.print("Alergias conocidas: ");
        String alergias = sc.nextLine();

        // Detalles del procedimiento
        System.out.println("\n--- Detalles del Procedimiento ---");
        System.out.print("Descripción del procedimiento: ");
        String descripcion = sc.nextLine().trim();
        if (descripcion.isEmpty()) {
            System.out.println("❌ La descripción no puede estar vacía");
            return;
        }

        System.out.print("Anestesia utilizada: ");
        String anestesia = sc.nextLine();

        Integer duracion = null;
        int duracionTemp = leerEntero("Duración en minutos (opcional, 0 para omitir): ");
        if (duracionTemp > 0) {
            duracion = duracionTemp;
        }

        System.out.print("Complicaciones (opcional): ");
        String complicaciones = sc.nextLine();

        System.out.print("Observaciones adicionales: ");
        String observaciones = sc.nextLine();

        // Seguimiento postoperatorio
        System.out.println("\n--- Seguimiento Postoperatorio ---");
        System.out.print("Medicación postoperatoria: ");
        String medicacionPost = sc.nextLine();

        System.out.print("Cuidados especiales requeridos: ");
        String cuidados = sc.nextLine();

        System.out.print("Fecha de próxima revisión (YYYY-MM-DD, opcional): ");
        String proximaRevStr = sc.nextLine();
        LocalDate proximaRev = null;
        if (!proximaRevStr.trim().isEmpty()) {
            try {
                proximaRev = LocalDate.parse(proximaRevStr);
            } catch (DateTimeParseException e) {
                System.out.println("⚠ Formato de fecha inválido, se usará fecha por defecto (7 días)");
            }
        }

        System.out.print("Restricciones: ");
        String restricciones = sc.nextLine();

        // Crear el procedimiento
        ProcedimientoEspecial procedimiento = new ProcedimientoEspecial(
                mascotaId, tipo, fechaProcedimiento, veterinario,
                diagnostico, descripcion, cuidados
        );

        // Información preoperatoria
        procedimiento.setAnalisisPrevios(analisis);
        procedimiento.setMedicacionPrevia(medicacionPrevia);
        procedimiento.setAyunoRequerido(ayuno);
        procedimiento.setAlergiasConocidas(alergias);

        // Detalles
        procedimiento.setAnestesiaUtilizada(anestesia);
        procedimiento.setDuracionMinutos(duracion);
        procedimiento.setComplicaciones(complicaciones);
        procedimiento.setObservaciones(observaciones);

        // Postoperatorio
        procedimiento.setMedicacionPostoperatoria(medicacionPost);
        procedimiento.setProximaRevision(proximaRev);
        procedimiento.setRestricciones(restricciones);

        procedimientoService.registrarProcedimiento(procedimiento);
    }

    private void listarPorMascota() {
        int mascotaId = leerEntero("ID de la mascota: ");
        List<ProcedimientoEspecial> procedimientos = procedimientoService.listarPorMascota(mascotaId);

        if (procedimientos.isEmpty()) {
            System.out.println("No hay procedimientos registrados para esta mascota.");
        } else {
            System.out.println("\n--- Procedimientos Especiales ---");
            procedimientos.forEach(this::mostrarProcedimiento);
        }
    }

    private void listarEnRecuperacion() {
        List<ProcedimientoEspecial> procedimientos = procedimientoService.listarEnRecuperacion();

        if (procedimientos.isEmpty()) {
            System.out.println("No hay procedimientos en recuperación.");
        } else {
            System.out.println("\n--- Procedimientos en Recuperación ---");
            procedimientos.forEach(this::mostrarProcedimiento);
        }
    }

    private void verProximasRevisiones() {
        int dias = leerEntero("Ver revisiones de los próximos N días: ");
        if (dias <= 0) {
            System.out.println("❌ El número de días debe ser mayor a 0");
            return;
        }
        List<ProcedimientoEspecial> procedimientos = procedimientoService.listarProximasRevisiones(dias);

        if (procedimientos.isEmpty()) {
            System.out.println("No hay revisiones programadas en ese período.");
        } else {
            System.out.println("\n--- Próximas Revisiones ---");
            procedimientos.forEach(this::mostrarProcedimiento);
        }
    }

    private void actualizarEstado() {
        int id = leerEntero("ID del procedimiento: ");
        procedimientoService.mostrarEstadosDisponibles();
        int estadoOpcion = leerEntero("Nuevo estado: ");

        ProcedimientoEspecial.EstadoProcedimiento nuevoEstado = obtenerEstadoProcedimiento(estadoOpcion);

        if (nuevoEstado != null) {
            procedimientoService.actualizarEstado(id, nuevoEstado);
        } else {
            System.out.println("❌ Estado inválido");
        }
    }

    private void mostrarProcedimiento(ProcedimientoEspecial p) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        System.out.println("\n" + "-".repeat(50));
        System.out.println("ID: " + p.getId() + " | Mascota ID: " + p.getMascotaId());
        System.out.println("Tipo: " + p.getTipoProcedimiento().getDescripcion());
        System.out.println("Fecha: " + p.getFechaProcedimiento().format(formatter));
        System.out.println("Veterinario: " + p.getVeterinarioResponsable());
        System.out.println("Diagnóstico: " + p.getDiagnosticoPreoperatorio());
        System.out.println("Estado: " + p.getEstadoActual().getDescripcion());

        if (p.getProximaRevision() != null) {
            System.out.println("Próxima revisión: " + p.getProximaRevision());
        }
    }

    private ProcedimientoEspecial.TipoProcedimiento obtenerTipoProcedimiento(int opcion) {
        ProcedimientoEspecial.TipoProcedimiento[] tipos = ProcedimientoEspecial.TipoProcedimiento.values();
        if (opcion >= 1 && opcion <= tipos.length) {
            return tipos[opcion - 1];
        }
        return null;
    }

    private ProcedimientoEspecial.EstadoProcedimiento obtenerEstadoProcedimiento(int opcion) {
        ProcedimientoEspecial.EstadoProcedimiento[] estados = ProcedimientoEspecial.EstadoProcedimiento.values();
        if (opcion >= 1 && opcion <= estados.length) {
            return estados[opcion - 1];
        }
        return null;
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
}