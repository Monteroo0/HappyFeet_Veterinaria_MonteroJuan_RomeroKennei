package com.happyfeet.service;

import com.happyfeet.model.ClubPuntos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClubService {
    private List<ClubPuntos> miembros = new ArrayList<>();
    private int siguienteId = 1;

    // Tabla de beneficios por nivel
    private static final Map<String, String> BENEFICIOS_POR_NIVEL = Map.of(
            "BRONCE", "Descuento 5% en consultas",
            "PLATA", "Descuento 10% en consultas + Vacuna anual gratis",
            "ORO", "Descuento 15% en consultas + Vacuna anual gratis + Descuento 10% en productos",
            "PLATINO", "Descuento 20% en consultas + Vacuna anual gratis + Descuento 15% en productos + Consulta de emergencia gratis"
    );

    public void registrarMiembro(int duenoId, String nombreDueno) {
        // Verificar si ya está registrado
        if (miembros.stream().anyMatch(m -> m.getDuenoId() == duenoId)) {
            System.out.println("❌ El cliente ya está registrado en el club");
            return;
        }

        ClubPuntos nuevoMiembro = new ClubPuntos();
        nuevoMiembro.setId(siguienteId++);
        nuevoMiembro.setDuenoId(duenoId);
        nuevoMiembro.setNombreDueno(nombreDueno);
        nuevoMiembro.setPuntosAcumulados(50); // Puntos de bienvenida
        nuevoMiembro.setFechaUltimaActividad(LocalDate.now());

        miembros.add(nuevoMiembro);
        System.out.println("✅ Miembro registrado exitosamente con 50 puntos de bienvenida");
        System.out.println("Nivel actual: " + nuevoMiembro.getNivelMembresía());
    }

    public void otorgarPuntos(int duenoId, int puntos, String motivo) {
        ClubPuntos miembro = buscarMiembroPorDueno(duenoId);
        if (miembro == null) {
            System.out.println("❌ El cliente no está registrado en el club");
            return;
        }

        String nivelAnterior = miembro.getNivelMembresía();
        miembro.setPuntosAcumulados(miembro.getPuntosAcumulados() + puntos);
        miembro.setFechaUltimaActividad(LocalDate.now());
        String nivelNuevo = miembro.getNivelMembresía();

        System.out.println("✅ " + puntos + " puntos otorgados por: " + motivo);
        System.out.println("Puntos totales: " + miembro.getPuntosAcumulados());

        // Verificar si subió de nivel
        if (!nivelAnterior.equals(nivelNuevo)) {
            System.out.println("🎉 ¡Felicidades! Has subido al nivel " + nivelNuevo);
            mostrarBeneficios(nivelNuevo);
        }
    }

    public boolean canjearPuntos(int duenoId, int puntosRequeridos, String beneficio) {
        ClubPuntos miembro = buscarMiembroPorDueno(duenoId);
        if (miembro == null) {
            System.out.println("❌ El cliente no está registrado en el club");
            return false;
        }

        if (!miembro.puedeCanjenar(puntosRequeridos)) {
            System.out.println("❌ Puntos insuficientes. Disponibles: " + miembro.getPuntosAcumulados() +
                    ", Requeridos: " + puntosRequeridos);
            return false;
        }

        miembro.setPuntosAcumulados(miembro.getPuntosAcumulados() - puntosRequeridos);
        miembro.setFechaUltimaActividad(LocalDate.now());

        System.out.println("✅ Canje exitoso: " + beneficio);
        System.out.println("Puntos restantes: " + miembro.getPuntosAcumulados());
        return true;
    }

    public List<ClubPuntos> listarMiembros() {
        return new ArrayList<>(miembros);
    }

    public ClubPuntos buscarMiembroPorDueno(int duenoId) {
        return miembros.stream()
                .filter(m -> m.getDuenoId() == duenoId)
                .findFirst()
                .orElse(null);
    }

    public void mostrarCatalogoBeneficios() {
        System.out.println("\n=== 🎁 CATÁLOGO DE BENEFICIOS CANJEABLES ===");
        System.out.println("50 puntos  - Descuento 10% en próxima consulta");
        System.out.println("100 puntos - Baño y corte de uñas gratis");
        System.out.println("200 puntos - Consulta general gratis");
        System.out.println("300 puntos - Desparasitación gratis");
        System.out.println("500 puntos - Vacuna triple gratis");
        System.out.println("800 puntos - Chequeo completo con exámenes");
        System.out.println("1000 puntos - Cirugía menor (esterilización)");
        System.out.println("\n=== BENEFICIOS AUTOMÁTICOS POR NIVEL ===");
        BENEFICIOS_POR_NIVEL.forEach((nivel, beneficio) ->
                System.out.println(nivel + ": " + beneficio));
    }

    private void mostrarBeneficios(String nivel) {
        System.out.println("Beneficios del nivel " + nivel + ":");
        System.out.println("• " + BENEFICIOS_POR_NIVEL.get(nivel));
    }

    public void procesarCompra(int duenoId, double montoCompra) {
        // Otorgar 1 punto por cada $1000 pesos colombianos
        int puntosGanados = (int) (montoCompra / 1000);
        if (puntosGanados > 0) {
            otorgarPuntos(duenoId, puntosGanados, "Compra por $" + String.format("%.2f", montoCompra));
        }
    }

    public void generarReporteClub() {
        System.out.println("\n=== 📊 REPORTE DEL CLUB DE PUNTOS ===");

        if (miembros.isEmpty()) {
            System.out.println("No hay miembros registrados en el club.");
            return;
        }

        System.out.println("Total de miembros: " + miembros.size());

        // Estadísticas por nivel
        Map<String, Long> miembrosPorNivel = miembros.stream()
                .collect(Collectors.groupingBy(ClubPuntos::getNivelMembresía, Collectors.counting()));

        System.out.println("\nMiembros por nivel:");
        miembrosPorNivel.forEach((nivel, cantidad) ->
                System.out.println("  • " + nivel + ": " + cantidad));

        // Top 5 miembros con más puntos
        System.out.println("\n--- Top 5 Miembros ---");
        miembros.stream()
                .sorted((m1, m2) -> Integer.compare(m2.getPuntosAcumulados(), m1.getPuntosAcumulados()))
                .limit(5)
                .forEach(m -> System.out.println("  • " + m.getNombreDueno() +
                        " - " + m.getPuntosAcumulados() + " puntos (" + m.getNivelMembresía() + ")"));

        // Promedio de puntos
        double promedioPuntos = miembros.stream()
                .mapToInt(ClubPuntos::getPuntosAcumulados)
                .average()
                .orElse(0.0);

        System.out.println("\nPromedio de puntos por miembro: " + String.format("%.1f", promedioPuntos));

        // Miembros inactivos (más de 6 meses sin actividad)
        LocalDate fechaLimite = LocalDate.now().minusMonths(6);
        long miembrosInactivos = miembros.stream()
                .filter(m -> m.getFechaUltimaActividad().isBefore(fechaLimite))
                .count();

        if (miembrosInactivos > 0) {
            System.out.println("⚠️  Miembros inactivos (>6 meses): " + miembrosInactivos);
        }
    }
}