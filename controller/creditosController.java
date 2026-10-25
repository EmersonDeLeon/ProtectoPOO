package controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import modelo.Credito;
import view.PanelCreditos;
import view.VentanaPrincipal;

public class creditosController {

    private final GestorPantallas gestorPantallas;
    private final PanelCreditos panel;
    private final List<Credito> creditos;

    public creditosController(
            VentanaPrincipal ventanaPrincipal,
            GestorPantallas gestorPantallas
    ) {
        if (ventanaPrincipal == null || gestorPantallas == null) {
            throw new IllegalArgumentException(
                    "La ventana y el gestor de pantallas son obligatorios."
            );
        }
        this.gestorPantallas = gestorPantallas;
        this.panel = ventanaPrincipal.getPanelCreditos();
        this.creditos = Collections.unmodifiableList(Arrays.asList(
                new Credito("Justin Paz", "Integrante", "Proyecto de POO"),
                new Credito("Emerson de León", "Integrante", "Proyecto de POO")
        ));

        ventanaPrincipal.getPanelMenu().getBotonCreditos()
                .addActionListener(e -> mostrarCreditos());
        panel.getBotonVolver().addActionListener(e -> volverAlMenu());
        panel.mostrarCreditos(creditos);
    }

    public void mostrarCreditos() {
        panel.mostrarCreditos(creditos);
        gestorPantallas.mostrarCreditos();
    }

    public void volverAlMenu() {
        gestorPantallas.volverAlMenu();
    }

    public List<Credito> getCreditos() {
        return creditos;
    }
}
