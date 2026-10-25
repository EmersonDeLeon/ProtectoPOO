package controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import modelo.ResultadoTetris;
import repositorio.RepositorioPuntuaciones;
import view.PanelPuntuaciones;
import view.VentanaPrincipal;

public class RankingController {

    private static final int CANTIDAD_MOSTRADA = 10;

    private final RepositorioPuntuaciones repositorio;
    private final PanelPuntuaciones panel;
    private final GestorPantallas gestorPantallas;
    private List<ResultadoTetris> ranking = Collections.emptyList();
    private String errorCarga;

    public RankingController(
            RepositorioPuntuaciones repositorio,
            VentanaPrincipal ventanaPrincipal,
            GestorPantallas gestorPantallas
    ) {
        if (repositorio == null || ventanaPrincipal == null
                || gestorPantallas == null) {
            throw new IllegalArgumentException(
                    "El repositorio, la ventana y el gestor son obligatorios."
            );
        }

        this.repositorio = repositorio;
        this.panel = ventanaPrincipal.getPanelPuntuaciones();
        this.gestorPantallas = gestorPantallas;

        ventanaPrincipal.getPanelMenu().getBotonPuntuaciones()
                .addActionListener(e -> mostrarMejoresPuntuaciones());
        panel.getBotonVolver().addActionListener(e -> volverAlMenu());
    }

    public void cargarRanking() {
        errorCarga = null;
        try {
            repositorio.cargar();
            ranking = repositorio.obtenerRanking(CANTIDAD_MOSTRADA);
        } catch (IOException e) {
            ranking = Collections.emptyList();
            errorCarga = "No se pudieron cargar las puntuaciones: "
                    + e.getMessage();
        }
    }

    public void mostrarMejoresPuntuaciones() {
        gestorPantallas.mostrarPuntuaciones();
        panel.limpiarResultados();
        cargarRanking();

        if (errorCarga != null) {
            panel.mostrarError(errorCarga);
            return;
        }

        for (ResultadoTetris resultado : ranking) {
            panel.agregarResultado(
                    resultado.getNombreJugador(),
                    resultado.getPuntuacion()
            );
        }
    }

    public void volverAlMenu() {
        gestorPantallas.volverAlMenu();
    }

    public List<ResultadoTetris> getRanking() {
        return Collections.unmodifiableList(ranking);
    }
}