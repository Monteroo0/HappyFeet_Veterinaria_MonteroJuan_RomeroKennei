package com.happyfeet.view;

import com.happyfeet.model.*;
import com.happyfeet.service.AdopcionService;
import com.happyfeet.service.JornadaService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Vista del Módulo de Actividades Especiales
 * Maneja adopciones y jornadas de vacunación
 */
public class ActividadesView {

    private final Scanner sc;
    private final AdopcionService adopcionService;
    private final JornadaService jornadaService;

    public ActividadesView(Scanner sc) {
        this.sc = sc;
        this.adopcionService = new AdopcionService();
        this.jornadaService = new JornadaService();
    }

    public void mostrarMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("   🎪 MÓDULO: ACTIVIDADES ESPECIALES");
            System.out.println("=".repeat(50));
            System.out.println("1. 🏠 Gestión de Adopciones");
            System.out.println("2. 💉 Jornadas de Vacunación");
            System.out.println("0. Volver al menú principal");
            System.out.println("=".repeat(50));

            int opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> menuAdopciones();
                case 2 -> menuJornadas();
                case 0 -> {
                    return;
                }
                default -> System.out.println("❌ Opción no válida");
            }
        }
    }

    private void menuAdopciones() {
        while (true) {
            System.out.println("\n=== 🏠 GESTIÓN DE ADOPCIONES ===");
            System.out.println("1. Registrar mascota para adopción");
            System.out.println("2. Ver mascotas disponibles");
            System.out.println("3. Procesar adopción");
            System.out.println("4. Ver todas las mascotas");
            System.out.println("5. Ver contratos de adopción");
            System.out.println("6. Estadísticas de adopción");
            System.out.println("0. Volver");

            int opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> registrarMascotaAdopcion();
                case 2 -> verMascotasDisponibles();
                case 3 -> procesarAdopcion();
                case 4 -> verTodasMascotasAdopcion();
                case 5 -> verContratosAdopcion();
                case 6 -> adopcionService.mostrarEstadisticasAdopcion();
                case 0 -> {
                    return;
                }
                default -> System.out.println("❌ Opción no válida");
            }
        }
    }

    private void registrarMascotaAdopcion() {
        System.out.println("\n--- Registrar Mascota para Adopción ---");
        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim();
        if (nombre.isEmpty()) {
            System.out.println("❌ El nombre no puede estar vacío");
            return;
        }

        System.out.print("Edad estimada: ");
        String edad = sc.nextLine().trim();
        System.out.print("Sexo (Macho/Hembra): ");
        String sexo = sc.nextLine();
        System.out.print("Descripción: ");
        String descripcion = sc.nextLine();
        System.out.print("URL de foto (opcional): ");
        String foto = sc.nextLine();

        MascotaAdopcion mascota = new MascotaAdopcion();
        mascota.setNombre(nombre);
        mascota.setEdadEstimada(edad);
        mascota.setSexo(sexo);
        mascota.setDescripcion(descripcion);
        mascota.setUrlFoto(foto.isEmpty() ? null : foto);

        adopcionService.registrarMascotaParaAdopcion(mascota);
    }

    private void verMascotasDisponibles() {
        System.out.println("\n--- Mascotas Disponibles para Adopción ---");
        List<MascotaAdopcion> mascotas = adopcionService.listarMascotasDisponibles();
        if (mascotas.isEmpty()) {
            System.out.println("No hay mascotas disponibles para adopción.");
        } else {
            mascotas.forEach(System.out::println);
        }
    }

    private void procesarAdopcion() {
        List<MascotaAdopcion> disponibles = adopcionService.listarMascotasDisponibles();
        if (disponibles.isEmpty()) {
            System.out.println("❌ No hay mascotas disponibles para adopción.");
            return;
        }

        System.out.println("Mascotas disponibles:");
        disponibles.forEach(m -> System.out.println(m.getId() + ". " + m.getNombre()));

        int mascotaId = leerEntero("ID de la mascota: ");

        // Validar que la mascota existe y está disponible
        if (disponibles.stream().noneMatch(m -> m.getId() == mascotaId)) {
            System.out.println("❌ ID de mascota inválido o no disponible");
            return;
        }

        System.out.print("Nombre del adoptante: ");
        String nombre = sc.nextLine().trim();
        if (nombre.isEmpty()) {
            System.out.println("❌ El nombre del adoptante no puede estar vacío");
            return;
        }

        System.out.print("Documento: ");
        String documento = sc.nextLine().trim();
        if (documento.isEmpty()) {
            System.out.println("❌ El documento no puede estar vacío");
            return;
        }
        System.out.print("Teléfono: ");
        String telefono = sc.nextLine();
        System.out.print("Dirección: ");
        String direccion = sc.nextLine();

        adopcionService.procesarAdopcion(mascotaId, nombre, documento, telefono, direccion);
    }

    private void verTodasMascotasAdopcion() {
        System.out.println("\n--- Todas las Mascotas de Adopción ---");
        List<MascotaAdopcion> mascotas = adopcionService.listarTodasLasMascotas();
        if (mascotas.isEmpty()) {
            System.out.println("No hay mascotas registradas para adopción.");
        } else {
            mascotas.forEach(System.out::println);
        }
    }

    private void verContratosAdopcion() {
        System.out.println("\n--- Contratos de Adopción ---");
        List<ContratoAdopcion> contratos = adopcionService.listarContratos();
        if (contratos.isEmpty()) {
            System.out.println("No hay contratos de adopción registrados.");
        } else {
            contratos.forEach(System.out::println);
        }
    }

    private void menuJornadas() {
        while (true) {
            System.out.println("\n=== 💉 JORNADAS DE VACUNACIÓN ===");
            System.out.println("1. Crear jornada");
            System.out.println("2. Listar jornadas");
            System.out.println("3. Registrar mascota en jornada");
            System.out.println("4. Ver registros de jornada");
            System.out.println("5. Finalizar jornada");
            System.out.println("6. Estadísticas de jornada");
            System.out.println("7. Reporte general");
            System.out.println("0. Volver");

            int opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> crearJornada();
                case 2 -> listarJornadas();
                case 3 -> registrarEnJornada();
                case 4 -> verRegistrosJornada();
                case 5 -> finalizarJornada();
                case 6 -> estadisticasJornada();
                case 7 -> jornadaService.generarReporteTodasLasJornadas();
                case 0 -> {
                    return;
                }
                default -> System.out.println("❌ Opción no válida");
            }
        }
    }

    private void crearJornada() {
        System.out.println("\n--- Crear Jornada de Vacunación ---");
        System.out.print("Nombre de la jornada: ");
        String nombre = sc.nextLine().trim();
        if (nombre.isEmpty()) {
            System.out.println("❌ El nombre de la jornada no puede estar vacío");
            return;
        }

        LocalDate fechaInicio = leerFecha("Fecha de inicio (YYYY-MM-DD): ");
        LocalDate fechaFin = leerFecha("Fecha de fin (YYYY-MM-DD): ");

        if (fechaFin.isBefore(fechaInicio)) {
            System.out.println("❌ La fecha de fin no puede ser anterior a la fecha de inicio");
            return;
        }

        System.out.print("Descripción: ");
        String descripcion = sc.nextLine();

        JornadaVacunacion jornada = new JornadaVacunacion();
        jornada.setNombre(nombre);
        jornada.setFechaInicio(fechaInicio);
        jornada.setFechaFin(fechaFin);
        jornada.setDescripcion(descripcion);

        jornadaService.crearJornada(jornada);
    }

    private void listarJornadas() {
        System.out.println("\n--- Lista de Jornadas ---");
        List<JornadaVacunacion> jornadas = jornadaService.listarJornadas();
        if (jornadas.isEmpty()) {
            System.out.println("No hay jornadas registradas.");
        } else {
            jornadas.forEach(System.out::println);
        }
    }

    private void registrarEnJornada() {
        List<JornadaVacunacion> activas = jornadaService.listarJornadasActivas();
        if (activas.isEmpty()) {
            System.out.println("❌ No hay jornadas activas.");
            return;
        }

        System.out.println("Jornadas activas:");
        activas.forEach(j -> System.out.println(j.getId() + ". " + j.getNombre()));

        int jornadaId = leerEntero("ID de la jornada: ");

        // Validar que la jornada existe y está activa
        if (activas.stream().noneMatch(j -> j.getId() == jornadaId)) {
            System.out.println("❌ ID de jornada inválido o inactiva");
            return;
        }

        System.out.print("Nombre de la mascota: ");
        String mascotaNombre = sc.nextLine().trim();
        if (mascotaNombre.isEmpty()) {
            System.out.println("❌ El nombre de la mascota no puede estar vacío");
            return;
        }

        System.out.print("Nombre del dueño: ");
        String duenoNombre = sc.nextLine().trim();
        if (duenoNombre.isEmpty()) {
            System.out.println("❌ El nombre del dueño no puede estar vacío");
            return;
        }

        System.out.print("Teléfono del dueño: ");
        String telefono = sc.nextLine().trim();
        System.out.print("Vacuna aplicada: ");
        String vacuna = sc.nextLine().trim();
        if (vacuna.isEmpty()) {
            System.out.println("❌ La vacuna aplicada no puede estar vacía");
            return;
        }

        jornadaService.registrarMascotaEnJornada(jornadaId, mascotaNombre, duenoNombre, telefono, vacuna);
    }

    private void verRegistrosJornada() {
        int jornadaId = leerEntero("ID de la jornada: ");
        List<RegistroJornada> registros = jornadaService.listarRegistrosPorJornada(jornadaId);

        if (registros.isEmpty()) {
            System.out.println("No hay registros para esta jornada.");
        } else {
            System.out.println("\n--- Registros de la Jornada ---");
            registros.forEach(System.out::println);
        }
    }

    private void finalizarJornada() {
        int jornadaId = leerEntero("ID de la jornada a finalizar: ");
        jornadaService.finalizarJornada(jornadaId);
    }

    private void estadisticasJornada() {
        int jornadaId = leerEntero("ID de la jornada: ");
        jornadaService.mostrarEstadisticasJornada(jornadaId);
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