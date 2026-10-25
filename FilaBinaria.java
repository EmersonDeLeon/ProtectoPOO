package modelo;

import java.util.Arrays;

public class FilaBinaria {

    public static final int CANTIDAD_BITS = 8;
    public static final int VALOR_MAXIMO = (1 << CANTIDAD_BITS) - 1;

    private final int objetivo;
    private final int[] bits = new int[CANTIDAD_BITS];
    private boolean infectada;
    public FilaBinaria(int objetivo) {
        if (objetivo < 0 || objetivo > VALOR_MAXIMO) {
            throw new IllegalArgumentException(
                    "El objetivo debe estar entre 0 y " + VALOR_MAXIMO + "."
            );
        }
        this.objetivo = objetivo;
    }

    public int getObjetivo() {
        return objetivo;
    }

    public int[] getBits() {
        return bits.clone();
    }

    public boolean estaInfectada() {
        return infectada;
    }

    public boolean necesitaUnoEn(int columna) {
        validarColumna(columna);
        int peso = 1 << (CANTIDAD_BITS - 1 - columna);
        return (objetivo & peso) != 0;
    }

    public boolean colocarUno(int columna) {
        validarColumna(columna);

        if (infectada) {
            return false;
        }
        if (bits[columna] == 1) {
            throw new IllegalStateException(
                    "Ya hay un bloque en la columna " + columna + "."
            );
        }

        bits[columna] = 1;

        if (!necesitaUnoEn(columna)) {
            infectada = true;
            return false;
        }
        return true;
    }

    public int getValorDecimal() {
        int valor = 0;
        for (int bit : bits) {
            valor = valor * 2 + bit;
        }
        return valor;
    }

    public boolean estaResuelta() {
        return !infectada && getValorDecimal() == objetivo;
    }

    public void reiniciar() {
        Arrays.fill(bits, 0);
        infectada = false;
    }

    private static void validarColumna(int columna) {
        if (columna < 0 || columna >= CANTIDAD_BITS) {
            throw new IllegalArgumentException(
                    "La columna debe estar entre 0 y "
                    + (CANTIDAD_BITS - 1) + "."
            );
        }
    }
}
