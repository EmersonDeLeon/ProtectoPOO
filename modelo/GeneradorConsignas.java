package modelo;

import java.util.Random;

public class GeneradorConsignas {

    public static final int MINIMO_POR_DEFECTO = 1;
    public static final int MAXIMO_POR_DEFECTO = ConsignaBinaria.VALOR_MAXIMO;

    private final int minimo;
    private final int maximo;
    private final Random aleatorio;

    public GeneradorConsignas() {
        this(MINIMO_POR_DEFECTO, MAXIMO_POR_DEFECTO);
    }

    public GeneradorConsignas(int minimo, int maximo) {
        if (minimo < 0 || maximo > ConsignaBinaria.VALOR_MAXIMO
                || minimo > maximo) {
            throw new IllegalArgumentException(
                    "El rango debe cumplir 0 <= mínimo <= máximo <= "
                    + ConsignaBinaria.VALOR_MAXIMO + "."
            );
        }

        this.minimo = minimo;
        this.maximo = maximo;
        this.aleatorio = new Random();
    }

    public ConsignaBinaria generar() {
        int numero = aleatorio.nextInt(maximo - minimo + 1) + minimo;
        return new ConsignaBinaria(numero);
    }

    public int getMinimo() {
        return minimo;
    }

    public int getMaximo() {
        return maximo;
    }
}
