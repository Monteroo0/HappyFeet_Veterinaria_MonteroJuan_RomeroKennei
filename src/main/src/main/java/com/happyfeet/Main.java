package com.happyfeet;

import com.happyfeet.view.MainMenuView;

/**
 * Clase principal - Punto de entrada de la aplicación
 * Inicia el sistema de gestión de la clínica veterinaria HappyFeet
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("🐾 Iniciando Sistema HappyFeet Veterinaria 🐾");
        System.out.println("Versión: 1.0 - Sistema Completo");
        System.out.println("Desarrollado por: Juan Montero & Kennei Romero\n");

        // Iniciar el menú principal desde la vista
        MainMenuView menuPrincipal = new MainMenuView();
        menuPrincipal.iniciar();
    }
}
