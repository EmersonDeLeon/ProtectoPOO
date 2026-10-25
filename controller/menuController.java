package controller;

import view.PanelMenu;

public class MenuController {

    private final PanelMenu panel;
    private final GestorPantallas gestorPantallas;

    public MenuController(PanelMenu panel, GestorPantallas gestorPantallas) {
        if (panel == null || gestorPantallas == null) {
            throw new IllegalArgumentException(
                    "El panel del menú y el gestor de pantallas son obligatorios."
            );
        }
        this.panel = panel;
        this.gestorPantallas = gestorPantallas;

        panel.getBotonJugar().addActionListener(e -> mostrarTetris());
        panel.getBotonPuntuaciones().addActionListener(
                e -> mostrarPuntuaciones()
        );
        panel.getBotonCreditos().addActionListener(e -> mostrarCreditos());
    }

    public void mostrarTetris() {
        gestorPantallas.mostrarTetris();
    }

    public void mostrarPuntuaciones() {
        gestorPantallas.mostrarPuntuaciones();
    }

    public void mostrarCreditos() {
        gestorPantallas.mostrarCreditos();
    }
}
