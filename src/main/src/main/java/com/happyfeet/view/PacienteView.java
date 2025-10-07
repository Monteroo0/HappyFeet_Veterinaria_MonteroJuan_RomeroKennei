package com.happyfeet.view;

import com.happyfeet.model.Dueno;
import com.happyfeet.model.HistorialMedico;
import com.happyfeet.model.Mascota;
import com.happyfeet.service.ClubService;
import com.happyfeet.service.DuenoService;
import com.happyfeet.service.HistorialMedicoService;
import com.happyfeet.service.MascotaService;

import java.util.List;
import java.util.Scanner;

/**
 * Vista del Módulo de Gestión de Pacientes (Dueños y Mascotas)
 * Maneja todas las funcionalidades relacionadas con pacientes
 */
public class PacienteView {

    private final Scanner sc;
    private final DuenoService duenoService;
    private final MascotaService mascotaService;
    private final HistorialMedicoService historialService;
    private final ClubService clubService;

    public PacienteView(Scanner sc) {
        this.sc = sc;
        this.duenoService = new DuenoService();
        this.mascotaService = new MascotaService();
        this.historialService = new HistorialMedicoService();
        this.clubService = new ClubService();
    }

    /**
     * Muestra el menú principal del módulo de pacientes
     */
    public void mostrarMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("   👥 MÓDULO: GESTIÓN DE PACIENTES");
            System.out.println("=".repeat(50));
            System.out.println("1. Registrar dueño");
            System.out.println("2. Registrar mascota");
            System.out.println("3. Listar dueños");
            System.out.println("4. Listar mascotas");
            System.out.println("5. Buscar historial médico de mascota");
            System.out.println("6. Registrar en club de puntos");
            System.out.println("7. Transferir propiedad de mascota");
            System.out.println("0. Volver al menú principal");
            System.out.println("=".repeat(50));

            int opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> registrarDueno();
                case 2 -> registrarMascota();
                case 3 -> listarDuenos();
                case 4 -> listarMascotas();
                case 5 -> buscarHistorialMascota();
                case 6 -> registrarEnClub();
                case 7 -> transferirPropiedad();
                case 0 -> {
                    return; // Volver al menú principal
                }
                default -> System.out.println("❌ Opción no válida");
            }
        }
    }

    // ==================== FUNCIONES DEL MÓDULO ====================

    private void registrarDueno() {
        System.out.println("\n--- Registro de Dueño ---");
        System.out.print("Nombre completo: ");
        String nombre = sc.nextLine().trim();
        if (nombre.isEmpty()) {
            System.out.println("❌ El nombre no puede estar vacío");
            return;
        }

        System.out.print("Documento: ");
        String doc = sc.nextLine().trim();
        if (doc.isEmpty()) {
            System.out.println("❌ El documento no puede estar vacío");
            return;
        }

        System.out.print("Dirección: ");
        String dir = sc.nextLine().trim();
        System.out.print("Teléfono: ");
        String tel = sc.nextLine().trim();
        System.out.print("Email: ");
        String email = sc.nextLine().trim();

        Dueno dueno = new Dueno(0, nombre, doc, dir, tel, email);
        duenoService.registrarDueno(dueno);
    }

    private void registrarMascota() {
        System.out.println("\n--- Registro de Mascota ---");

        // Mostrar dueños disponibles
        List<Dueno> duenos = duenoService.listarDuenos();
        if (duenos.isEmpty()) {
            System.out.println("❌ No hay dueños registrados. Registre un dueño primero.");
            return;
        }

        System.out.println("Dueños disponibles:");
        duenos.forEach(d -> System.out.println(d.getId() + ". " + d.getNombreCompleto()));

        int duenoId = leerEntero("ID del dueño: ");

        // Validar que el dueño existe
        if (duenos.stream().noneMatch(d -> d.getId() == duenoId)) {
            System.out.println("❌ ID de dueño inválido");
            return;
        }

        System.out.print("Nombre de la mascota: ");
        String nombreMascota = sc.nextLine().trim();
        if (nombreMascota.isEmpty()) {
            System.out.println("❌ El nombre de la mascota no puede estar vacío");
            return;
        }

        System.out.print("Raza: ");
        String raza = sc.nextLine().trim();
        System.out.print("Fecha de nacimiento (YYYY-MM-DD, opcional): ");
        String fechaNac = sc.nextLine();
        System.out.print("Sexo (Macho/Hembra): ");
        String sexo = sc.nextLine();
        System.out.print("URL de foto (opcional): ");
        String foto = sc.nextLine();

        Mascota mascota = new Mascota(0, duenoId, nombreMascota, raza,
                                     fechaNac.isEmpty() ? null : fechaNac, sexo,
                                     foto.isEmpty() ? null : foto);
        mascotaService.registrarMascota(mascota);
    }

    private void listarDuenos() {
        System.out.println("\n--- Lista de Dueños ---");
        List<Dueno> duenos = duenoService.listarDuenos();
        if (duenos.isEmpty()) {
            System.out.println("No hay dueños registrados.");
        } else {
            duenos.forEach(System.out::println);
        }
    }

    private void listarMascotas() {
        System.out.println("\n--- Lista de Mascotas ---");
        List<Mascota> mascotas = mascotaService.listarMascotas();
        if (mascotas.isEmpty()) {
            System.out.println("No hay mascotas registradas.");
        } else {
            mascotas.forEach(System.out::println);
        }
    }

    private void buscarHistorialMascota() {
        int mascotaId = leerEntero("ID de la mascota: ");
        List<HistorialMedico> historial = historialService.buscarHistorialPorMascota(mascotaId);

        if (historial.isEmpty()) {
            System.out.println("No hay historial médico para esta mascota.");
        } else {
            System.out.println("\n--- Historial Médico ---");
            historial.forEach(System.out::println);
        }
    }

    private void registrarEnClub() {
        List<Dueno> duenos = duenoService.listarDuenos();
        if (duenos.isEmpty()) {
            System.out.println("❌ No hay dueños registrados.");
            return;
        }

        System.out.println("Dueños disponibles:");
        duenos.forEach(d -> System.out.println(d.getId() + ". " + d.getNombreCompleto()));

        int duenoId = leerEntero("ID del dueño: ");
        Dueno dueno = duenos.stream().filter(d -> d.getId() == duenoId).findFirst().orElse(null);

        if (dueno != null) {
            clubService.registrarMiembro(duenoId, dueno.getNombreCompleto());
        } else {
            System.out.println("❌ Dueño no encontrado");
        }
    }

    private void transferirPropiedad() {
        // Delegar a la vista especializada de transferencias
        TransferenciaView transferenciaView = new TransferenciaView(sc);
        transferenciaView.mostrarMenu();
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