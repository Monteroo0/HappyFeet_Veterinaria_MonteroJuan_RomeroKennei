package com.happyfeet.service;

import com.happyfeet.dao.MascotaDAO;
import com.happyfeet.model.Mascota;
import java.util.List;

public class MascotaService {
    private final MascotaDAO mascotaDAO = new MascotaDAO();

    public void registrarMascota(Mascota mascota) {
        mascotaDAO.guardar(mascota);
    }

    public List<Mascota> listarMascotas() {
        return mascotaDAO.listar();
    }
}
