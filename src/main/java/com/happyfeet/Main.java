package com.happyfeet;

import com.happyfeet.model.Dueno;
import com.happyfeet.model.Mascota;
import com.happyfeet.service.DuenoService;
import com.happyfeet.service.MascotaService;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DuenoService duenoService = new DuenoService();
        MascotaService mascotaService = new MascotaService();

        while (true) {
            System.out.println("\n=== Menú Gestión de Pacientes ===");
            System.out.println("1. Registrar dueño");
            System.out.println("2. Registrar mascota");
            System.out.println("3. Listar dueños");
            System.out.println("4. Listar mascotas");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1 -> {
                    System.out.print("Nombre completo: ");
                    String nombre = sc.nextLine();
                    System.out.print("Documento: ");
                    String doc = sc.nextLine();
                    System.out.print("Dirección: ");
                    String dir = sc.nextLine();
                    System.out.print("Teléfono: ");
                    String tel = sc.nextLine();
                    System.out.print("Email: ");
                    String email = sc.nextLine();

                    Dueno dueno = new Dueno(0, nombre, doc, dir, tel, email);
                    duenoService.registrarDueno(dueno);
                }
                case 2 -> {
                    System.out.print("Nombre mascota: ");
                    String nombreMascota = sc.nextLine();
                    System.out.print("ID dueño: ");
                    int duenoId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Raza: ");
                    String raza = sc.nextLine();
                    System.out.print("Sexo (Macho/Hembra): ");
                    String sexo = sc.nextLine();

                    Mascota mascota = new Mascota(0, duenoId, nombreMascota, raza, null, sexo, null);
                    mascotaService.registrarMascota(mascota);
                }
                case 3 -> {
                    List<Dueno> duenos = duenoService.listarDuenos();
                    duenos.forEach(System.out::println);
                }
                case 4 -> {
                    List<Mascota> mascotas = mascotaService.listarMascotas();
                    mascotas.forEach(System.out::println);
                }
                case 0 -> {
                    System.out.println("Saliendo...");
                    return;
                }
                default -> System.out.println("❌ Opción no válida");
            }
        }
    }
}
