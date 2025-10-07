package com.happyfeet.service;

import com.happyfeet.model.JornadaVacunacion;
import com.happyfeet.model.RegistroJornada;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JornadaService {
    private List<JornadaVacunacion> jornadas = new ArrayList<>();
    private List<RegistroJornada> registros = new ArrayList<>();
    private int siguienteId = 1;
    private int siguienteRegistroId = 1;
    private final InventarioService inventarioService = new InventarioService();

    public void crearJornada(JornadaVacunacion jornada) {
        // Validaciones
        if (jornada.getFechaInicio().isAfter(jornada.getFechaFin())) {
            System.out.println("❌ La fecha de inicio no puede ser posterior a la fecha de fin");
            return;
        }

        jornada.setId(siguienteId++);
        jornada.setActiva(true);
        jornadas.add(jornada);
        System.out.println("✅ Jornada de vacunación creada exitosamente");
    }

    public List<JornadaVacunacion> listarJornadas() {
        return new ArrayList<>(jornadas);
    }

    public List<JornadaVacunacion> listarJornadasActivas() {
        return jornadas.stream()
                .filter(JornadaVacunacion::isActiva)
                .toList();
    }

    public JornadaVacunacion buscarJornadaPorId(int jornadaId) {
        return jornadas.stream()
                .filter(j -> j.getId() == jornadaId)
                .findFirst()
                .orElse(null);
    }

    public void registrarMascotaEnJornada(int jornadaId, String mascotaNombre, String duenoNombre,
                                          String duenoTelefono, String vacunaAplicada) {

        JornadaVacunacion jornada = buscarJornadaPorId(jornadaId);
        if (jornada == null) {
            System.out.println("❌ Jornada no encontrada");
            return;
        }

        if (!jornada.estaEnCurso()) {
            System.out.println("❌ La jornada no está en curso actualmente");
            return;
        }

        // Crear registro
        RegistroJornada registro = new RegistroJornada();
        registro.setId(siguienteRegistroId++);
        registro.setJornadaId(jornadaId);
        registro.setMascotaNombre(mascotaNombre);
        registro.setDuenoNombre(duenoNombre);
        registro.setDuenoTelefono(duenoTelefono);
        registro.setVacunaAplicada(vacunaAplicada);
        registro.setFechaAplicacion(LocalDateTime.now());
        registro.setNombreJornada(jornada.getNombre());

        registros.add(registro);

        // Intentar deducir vacuna del inventario
        boolean deducido = inventarioService.deducirStock(vacunaAplicada, 1);
        if (deducido) {
            System.out.println("✅ Mascota registrada en jornada y vacuna deducida del inventario");
        } else {
            System.out.println("✅ Mascota registrada en jornada");
            System.out.println("⚠️  Advertencia: No se pudo deducir la vacuna del inventario");
        }
    }

    public List<RegistroJornada> listarRegistros() {
        return new ArrayList<>(registros);
    }

    public List<RegistroJornada> listarRegistrosPorJornada(int jornadaId) {
        return registros.stream()
                .filter(r -> r.getJornadaId() == jornadaId)
                .toList();
    }

    public void finalizarJornada(int jornadaId) {
        JornadaVacunacion jornada = buscarJornadaPorId(jornadaId);
        if (jornada == null) {
            System.out.println("❌ Jornada no encontrada");
            return;
        }

        jornada.setActiva(false);
        System.out.println("✅ Jornada finalizada exitosamente");

        // Mostrar estadísticas
        mostrarEstadisticasJornada(jornadaId);
    }

    public void mostrarEstadisticasJornada(int jornadaId) {
        JornadaVacunacion jornada = buscarJornadaPorId(jornadaId);
        if (jornada == null) {
            System.out.println("❌ Jornada no encontrada");
            return;
        }

        List<RegistroJornada> registrosJornada = listarRegistrosPorJornada(jornadaId);

        System.out.println("\n=== 📊 ESTADÍSTICAS DE JORNADA ===");
        System.out.println("Jornada: " + jornada.getNombre());
        System.out.println("Período: " + jornada.getFechaInicio() + " - " + jornada.getFechaFin());
        System.out.println("Total de mascotas atendidas: " + registrosJornada.size());

        if (!registrosJornada.isEmpty()) {
            // Contar vacunas aplicadas
            var vacunasCounts = registrosJornada.stream()
                    .collect(java.util.stream.Collectors.groupingBy(
                            RegistroJornada::getVacunaAplicada,
                            java.util.stream.Collectors.counting()));

            System.out.println("\nVacunas aplicadas:");
            vacunasCounts.forEach((vacuna, cantidad) ->
                    System.out.println("  • " + vacuna + ": " + cantidad));

            // Promedio diario
            long diasJornada = java.time.temporal.ChronoUnit.DAYS.between(
                    jornada.getFechaInicio(), jornada.getFechaFin()) + 1;
            double promedioDiario = (double) registrosJornada.size() / diasJornada;
            System.out.println("Promedio diario: " + String.format("%.1f", promedioDiario) + " mascotas");
        }
    }

    public void generarReporteTodasLasJornadas() {
        System.out.println("\n=== 📋 REPORTE GENERAL DE JORNADAS ===");

        if (jornadas.isEmpty()) {
            System.out.println("No se han creado jornadas de vacunación.");
            return;
        }

        System.out.println("Total de jornadas creadas: " + jornadas.size());

        long jornadasActivas = jornadas.stream().filter(JornadaVacunacion::isActiva).count();
        long jornadasFinalizadas = jornadas.size() - jornadasActivas;

        System.out.println("Jornadas activas: " + jornadasActivas);
        System.out.println("Jornadas finalizadas: " + jornadasFinalizadas);
        System.out.println("Total de registros: " + registros.size());

        if (!registros.isEmpty()) {
            // Jornada más exitosa
            var registrosPorJornada = registros.stream()
                    .collect(java.util.stream.Collectors.groupingBy(
                            RegistroJornada::getNombreJornada,
                            java.util.stream.Collectors.counting()));

            String jornadaMasExitosa = registrosPorJornada.entrySet().stream()
                    .max(java.util.Map.Entry.comparingByValue())
                    .map(java.util.Map.Entry::getKey)
                    .orElse("N/A");

            System.out.println("Jornada más exitosa: " + jornadaMasExitosa);
        }
    }
}