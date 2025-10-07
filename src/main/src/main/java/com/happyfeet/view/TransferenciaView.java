package com.happyfeet.view;

import com.happyfeet.model.Dueno;
import com.happyfeet.model.Mascota;
import com.happyfeet.model.entities.TransferenciaPropiedad;
import com.happyfeet.service.DuenoService;
import com.happyfeet.service.MascotaService;
import com.happyfeet.service.TransferenciaPropiedadService;

import java.util.List;
import java.util.Scanner;

/**
 * Vista para el módulo de Transferencias de Propiedad
 */
public class TransferenciaView {

    private final Scanner sc;
    private final TransferenciaPropiedadService transferenciaService;
    private final MascotaService mascotaService;
    private final DuenoService duenoService;

    public TransferenciaView(Scanner sc) {
        this.sc = sc;
        this.transferenciaService = new TransferenciaPropiedadService();
        this.mascotaService = new MascotaService();
        this.duenoService = new DuenoService();
    }

    public void mostrarMenu() {
        while (true) {
            System.out.println("\n=== TRANSFERENCIAS DE PROPIEDAD ===");
            System.out.println("1. Transferir mascota a nuevo dueño");
            System.out.println("2. Ver historial de transferencias de mascota");
            System.out.println("3. Listar todas las transferencias");
            System.out.println("4. Ver transferencias recientes");
            System.out.println("5. Reporte por motivo");
            System.out.println("0. Volver");

            int opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> transferirMascota();
                case 2 -> verHistorialMascota();
                case 3 -> listarTodas();
                case 4 -> verTransferenciasRecientes();
                case 5 -> transferenciaService.generarReportePorMotivo();
                case 0 -> { return; }
                default -> System.out.println("❌ Opción no válida");
            }
        }
    }

    private void transferirMascota() {
        System.out.println("\n--- Transferir Propiedad de Mascota ---");

        List<Mascota> mascotas = mascotaService.listarMascotas();
        if (mascotas.isEmpty()) {
            System.out.println("❌ No hay mascotas registradas.");
            return;
        }

        System.out.println("Mascotas disponibles:");
        mascotas.forEach(m -> System.out.println(m.getId() + ". " + m.getNombre() + " (Dueño ID: " + m.getDuenoId() + ")"));

        int mascotaId = leerEntero("ID de la mascota: ");

        // Validar que la mascota existe
        if (mascotas.stream().noneMatch(m -> m.getId() == mascotaId)) {
            System.out.println("❌ ID de mascota inválido");
            return;
        }

        List<Dueno> duenos = duenoService.listarDuenos();
        if (duenos.isEmpty()) {
            System.out.println("❌ No hay dueños registrados.");
            return;
        }

        System.out.println("\nDueños disponibles:");
        duenos.forEach(d -> System.out.println(d.getId() + ". " + d.getNombreCompleto()));

        int duenoNuevoId = leerEntero("ID del nuevo dueño: ");

        // Validar que el dueño existe
        if (duenos.stream().noneMatch(d -> d.getId() == duenoNuevoId)) {
            System.out.println("❌ ID de dueño inválido");
            return;
        }

        System.out.print("Motivo de la transferencia: ");
        String motivo = sc.nextLine().trim();

        System.out.print("Observaciones: ");
        String observaciones = sc.nextLine();

        transferenciaService.transferirPropiedad(mascotaId, duenoNuevoId, motivo, observaciones);
    }

    private void verHistorialMascota() {
        int mascotaId = leerEntero("ID de la mascota: ");
        transferenciaService.mostrarHistorialMascota(mascotaId);
    }

    private void listarTodas() {
        List<TransferenciaPropiedad> transferencias = transferenciaService.listarTodas();

        if (transferencias.isEmpty()) {
            System.out.println("No hay transferencias registradas.");
        } else {
            System.out.println("\n--- Todas las Transferencias ---");
            transferencias.forEach(System.out::println);
        }
    }

    private void verTransferenciasRecientes() {
        int dias = leerEntero("Ver transferencias de los últimos N días: ");
        if (dias <= 0) {
            System.out.println("❌ El número de días debe ser mayor a 0");
            return;
        }
        List<TransferenciaPropiedad> transferencias = transferenciaService.listarTransferenciasRecientes(dias);

        if (transferencias.isEmpty()) {
            System.out.println("No hay transferencias en ese período.");
        } else {
            System.out.println("\n--- Transferencias Recientes ---");
            transferencias.forEach(System.out::println);
        }
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