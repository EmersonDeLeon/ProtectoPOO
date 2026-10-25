package modelo;

public final class ConsignaBinaria {

    public static final int CANTIDAD_BITS = 8;
    public static final int VALOR_MAXIMO = (1 << CANTIDAD_BITS) - 1;

    private final int numeroDecimal;

    public ConsignaBinaria(int numeroDecimal) {
        if (numeroDecimal < 0 || numeroDecimal > VALOR_MAXIMO) {
            throw new IllegalArgumentException(
                    "La consigna debe estar entre 0 y " + VALOR_MAXIMO + "."
            );
        }
        this.numeroDecimal = numeroDecimal;
    }

    public int getNumeroDecimal() {
        return numeroDecimal;
    }

    public String getRepresentacionBinaria() {
        String binario = Integer.toBinaryString(numeroDecimal);
        StringBuilder resultado = new StringBuilder(CANTIDAD_BITS);
        for (int i = binario.length(); i < CANTIDAD_BITS; i++) {
            resultado.append('0');
        }
        return resultado.append(binario).toString();
    }

    public int[] getBitsObjetivo() {
        int[] bits = new int[CANTIDAD_BITS];
        String binario = getRepresentacionBinaria();
        for (int i = 0; i < CANTIDAD_BITS; i++) {
            bits[i] = binario.charAt(i) - '0';
        }
        return bits;
    }

    @Override
    public String toString() {
        return numeroDecimal + " (" + getRepresentacionBinaria() + ")";
    }
}
