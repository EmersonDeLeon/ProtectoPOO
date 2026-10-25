package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TableroBinario {

    private final List<FilaBinaria> filas = new ArrayList<>();

    public TableroBinario(ConsignaBinaria consignaInicial) {
        if (consignaInicial == null) {
            throw new IllegalArgumentException(
                    "La consigna inicial no puede ser nula."
            );
        }
        filas.add(new FilaBinaria(consignaInicial.getNumeroDecimal()));
    }

    public FilaBinaria getFilaActiva() {
        if (filas.isEmpty()) {
            throw new IllegalStateException(
                    "La partida ya terminó; no hay una fila activa."
            );
        }
        return filas.get(0);
    }

    public List<FilaBinaria> getFilas() {
        return Collections.unmodifiableList(new ArrayList<>(filas));
    }

    public int getCantidadFilas() {
        return filas.size();
    }

    public boolean estaTerminada() {
        return filas.isEmpty();
    }

    public boolean colocarUno(int columna) {
        return getFilaActiva().colocarUno(columna);
    }

    public void agregarFila(ConsignaBinaria consigna) {
        if (consigna == null) {
            throw new IllegalArgumentException(
                    "La consigna no puede ser nula."
            );
        }
        if (filas.isEmpty()) {
            throw new IllegalStateException(
                    "No se puede agregar una fila después de terminar la partida."
            );
        }
        if (!filas.get(0).estaInfectada()) {
            throw new IllegalStateException(
                    "Solo se puede agregar una fila cuando la activa está infectada."
            );
        }

        filas.add(0, new FilaBinaria(consigna.getNumeroDecimal()));
    }

    public boolean resolverFilaActiva() {
        FilaBinaria activa = getFilaActiva();
        if (!activa.estaResuelta()) {
            throw new IllegalStateException(
                    "La fila activa debe estar completa y sin infección."
            );
        }

        filas.remove(0);

        if (!filas.isEmpty()) {
            filas.get(0).reiniciar();
        }

        return filas.isEmpty();
    }

    public void reiniciar(ConsignaBinaria consignaInicial) {
        if (consignaInicial == null) {
            throw new IllegalArgumentException(
                    "La consigna inicial no puede ser nula."
            );
        }
        filas.clear();
        filas.add(new FilaBinaria(consignaInicial.getNumeroDecimal()));
    }
}
