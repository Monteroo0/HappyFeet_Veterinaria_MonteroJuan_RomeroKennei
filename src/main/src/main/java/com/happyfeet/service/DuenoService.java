package com.happyfeet.service;

import com.happyfeet.dao.DuenoDAO;
import com.happyfeet.model.Dueno;
import java.util.List;

public class DuenoService {
    private final DuenoDAO duenoDAO = new DuenoDAO();

    public void registrarDueno(Dueno dueno) {
        duenoDAO.guardar(dueno);
    }

    public List<Dueno> listarDuenos() {
        return duenoDAO.listar();
    }
}
