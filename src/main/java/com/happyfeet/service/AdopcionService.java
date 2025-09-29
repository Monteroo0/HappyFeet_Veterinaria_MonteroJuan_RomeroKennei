package com.happyfeet.service;

import com.happyfeet.model.MascotaAdopcion;
import com.happyfeet.model.ContratoAdopcion;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AdopcionService {
    private List<MascotaAdopcion> mascotasAdopcion = new ArrayList<>();
    private List<ContratoAdopcion> contratos = new ArrayList<>();
    private int siguienteId = 1;
    private int siguienteContratoId = 1;

    public void registrarMascotaParaAdopcion(MascotaAdopcion mascota) {
        mascota.setId(siguienteId++);
        mascota.setFechaIngreso(LocalDate.now());
        mascota.setEstado("Disponible");
        mascotasAdopcion.add(mascota);
        System.out.println("✅ Mascota registrada para adopción exitosamente");
    }

    public List<MascotaAdopcion> listarMascotasDisponibles() {
        return mascotasAdopcion.stream()
                .filter(MascotaAdopcion::estaDisponible)
                .toList();
    }

    public List<MascotaAdopcion> listarTodasLasMascotas() {
        return new ArrayList<>(mascotasAdopcion);
    }

    public void procesarAdopcion(int mascotaId, String adoptanteNombre, String adoptanteDocumento,
                                 String adoptanteTelefono, String adoptanteDireccion) {

        MascotaAdopcion mascota = mascotasAdopcion.stream()
                .filter(m -> m.getId() == mascotaId && m.estaDisponible())
                .findFirst()
                .orElse(null);

        if (mascota == null) {
            System.out.println("❌ Mascota no encontrada o no disponible para adopción");
            return;
        }

        // Cambiar estado de la mascota
        mascota.setEstado("Adoptada");

        // Generar contrato
        ContratoAdopcion contrato = new ContratoAdopcion();
        contrato.setId(siguienteContratoId++);
        contrato.setMascotaAdopcionId(mascotaId);
        contrato.setAdoptanteNombre(adoptanteNombre);
        contrato.setAdoptanteDocumento(adoptanteDocumento);
        contrato.setAdoptanteTelefono(adoptanteTelefono);
        contrato.setAdoptanteDireccion(adoptanteDireccion);
        contrato.setFechaAdopcion(LocalDate.now());
        contrato.setNombreMascota(mascota.getNombre());

        // Generar texto del contrato
        String contratoTexto = generarContratoTexto(mascota, contrato);
        contrato.setContratoTexto(contratoTexto);

        contratos.add(contrato);

        System.out.println("✅ Adopción procesada exitosamente");
        System.out.println("\n" + contratoTexto);
    }

    private String generarContratoTexto(MascotaAdopcion mascota, ContratoAdopcion contrato) {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        sb.append("===============================================\n");
        sb.append("           CONTRATO DE ADOPCIÓN                \n");
        sb.append("        CLÍNICA VETERINARIA HAPPYFEET          \n");
        sb.append("===============================================\n\n");

        sb.append("FECHA: ").append(contrato.getFechaAdopcion().format(formatter)).append("\n");
        sb.append("CONTRATO N°: ").append(String.format("%06d", contrato.getId())).append("\n\n");

        sb.append("INFORMACIÓN DE LA MASCOTA:\n");
        sb.append("Nombre: ").append(mascota.getNombre()).append("\n");
        sb.append("Raza: ").append(mascota.getNombreRaza() != null ? mascota.getNombreRaza() : "Mestizo").append("\n");
        sb.append("Sexo: ").append(mascota.getSexo()).append("\n");
        sb.append("Edad estimada: ").append(mascota.getEdadEstimada()).append("\n");
        if (mascota.getDescripcion() != null) {
            sb.append("Descripción: ").append(mascota.getDescripcion()).append("\n");
        }
        sb.append("\n");

        sb.append("INFORMACIÓN DEL ADOPTANTE:\n");
        sb.append("Nombre: ").append(contrato.getAdoptanteNombre()).append("\n");
        sb.append("Documento: ").append(contrato.getAdoptanteDocumento()).append("\n");
        sb.append("Teléfono: ").append(contrato.getAdoptanteTelefono()).append("\n");
        sb.append("Dirección: ").append(contrato.getAdoptanteDireccion()).append("\n\n");

        sb.append("TÉRMINOS Y CONDICIONES:\n");
        sb.append("1. El adoptante se compromete a brindar cuidado, alimentación\n");
        sb.append("   y atención veterinaria adecuada a la mascota.\n");
        sb.append("2. La mascota no podrá ser vendida, regalada o abandonada.\n");
        sb.append("3. El adoptante permite visitas de seguimiento por parte\n");
        sb.append("   de la clínica durante los primeros 6 meses.\n");
        sb.append("4. En caso de no poder cuidar la mascota, debe ser devuelta\n");
        sb.append("   a la clínica.\n");
        sb.append("5. La clínica se reserva el derecho de recuperar la mascota\n");
        sb.append("   en caso de maltrato o negligencia.\n\n");

        sb.append("El adoptante declara haber leído y aceptado todos los\n");
        sb.append("términos de este contrato.\n\n");

        sb.append("_________________________    _________________________\n");
        sb.append("  Firma del Adoptante         Firma de la Clínica\n\n");

        sb.append("===============================================\n");

        return sb.toString();
    }

    public List<ContratoAdopcion> listarContratos() {
        return new ArrayList<>(contratos);
    }

    public void mostrarEstadisticasAdopcion() {
        System.out.println("\n=== 📊 ESTADÍSTICAS DE ADOPCIÓN ===");

        long disponibles = mascotasAdopcion.stream().filter(MascotaAdopcion::estaDisponible).count();
        long adoptadas = mascotasAdopcion.stream().filter(m -> "Adoptada".equals(m.getEstado())).count();
        long enProceso = mascotasAdopcion.stream().filter(m -> "En Proceso".equals(m.getEstado())).count();

        System.out.println("Mascotas disponibles: " + disponibles);
        System.out.println("Mascotas adoptadas: " + adoptadas);
        System.out.println("Adopciones en proceso: " + enProceso);
        System.out.println("Total de contratos: " + contratos.size());

        if (!mascotasAdopcion.isEmpty()) {
            double tasaAdopcion = (double) adoptadas / mascotasAdopcion.size() * 100;
            System.out.println("Tasa de adopción: " + String.format("%.1f%%", tasaAdopcion));
        }
    }
}