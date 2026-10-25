package repositorio;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import modelo.ResultadoTetris;

public class RepositorioPuntuaciones {

    private static final int MARCA_ARCHIVO = 0x54545253;
    private static final int VERSION_ARCHIVO = 1;
    private static final int MAX_RESULTADOS = 100000;

    private final Path rutaArchivo;
    private List<ResultadoTetris> resultados = new ArrayList<>();

    public RepositorioPuntuaciones() {
        this(Paths.get("datos", "puntuaciones.dat"));
    }

    public RepositorioPuntuaciones(Path rutaArchivo) {
        if (rutaArchivo == null) {
            throw new IllegalArgumentException(
                    "La ruta del archivo no puede ser nula."
            );
        }
        this.rutaArchivo = rutaArchivo;
    }

    public void cargar() throws IOException {
        if (!Files.exists(rutaArchivo)) {
            resultados = new ArrayList<>();
            return;
        }

        List<ResultadoTetris> cargados = new ArrayList<>();
        try (DataInputStream entrada = new DataInputStream(
                Files.newInputStream(rutaArchivo))) {
            int marca = entrada.readInt();
            int version = entrada.readInt();
            int cantidad = entrada.readInt();

            if (marca != MARCA_ARCHIVO) {
                throw new IOException("El archivo de puntuaciones no es válido.");
            }
            if (version != VERSION_ARCHIVO) {
                throw new IOException(
                        "Versión de puntuaciones no compatible: " + version
                );
            }
            if (cantidad < 0 || cantidad > MAX_RESULTADOS) {
                throw new IOException(
                        "La cantidad de resultados guardada no es válida."
                );
            }

            for (int i = 0; i < cantidad; i++) {
                String nombre = entrada.readUTF();
                int puntuacion = entrada.readInt();
                String fechaTexto = entrada.readUTF();

                try {
                    LocalDateTime fecha = LocalDateTime.parse(fechaTexto);
                    cargados.add(
                            new ResultadoTetris(nombre, puntuacion, fecha)
                    );
                } catch (DateTimeParseException | IllegalArgumentException e) {
                    throw new IOException(
                            "El resultado número " + (i + 1)
                            + " contiene datos inválidos.",
                            e
                    );
                }
            }

            if (entrada.read() != -1) {
                throw new IOException(
                        "El archivo contiene datos adicionales inesperados."
                );
            }
        } catch (EOFException e) {
            throw new IOException(
                    "El archivo de puntuaciones está incompleto.", e
            );
        }

        resultados = cargados;
    }

    /** Guarda los resultados actuales en el archivo local. */
    public void guardar() throws IOException {
        guardarLista(resultados);
    }

    public void registrarResultado(ResultadoTetris resultado)
            throws IOException {
        if (resultado == null) {
            throw new IllegalArgumentException(
                    "El resultado no puede ser nulo."
            );
        }
        if (resultados.size() >= MAX_RESULTADOS) {
            throw new IllegalStateException(
                    "Se alcanzó el límite de resultados guardados."
            );
        }

        List<ResultadoTetris> actualizados = new ArrayList<>(resultados);
        actualizados.add(resultado);
        guardarLista(actualizados);
        resultados = actualizados;
    }

    public List<ResultadoTetris> obtenerResultados() {
        return Collections.unmodifiableList(
                new ArrayList<>(resultados)
        );
    }

    public List<ResultadoTetris> obtenerRanking(int limite) {
        if (limite < 0) {
            throw new IllegalArgumentException(
                    "El límite no puede ser negativo."
            );
        }

        List<ResultadoTetris> ordenados = new ArrayList<>(resultados);
        ordenados.sort(
                Comparator.comparingInt(ResultadoTetris::getPuntuacion)
                        .reversed()
                        .thenComparing(ResultadoTetris::getFecha)
        );

        if (ordenados.size() > limite) {
            ordenados = new ArrayList<>(ordenados.subList(0, limite));
        }
        return Collections.unmodifiableList(ordenados);
    }

    public Path getRutaArchivo() {
        return rutaArchivo;
    }

    private void guardarLista(List<ResultadoTetris> datos)
            throws IOException {
        Path carpeta = rutaArchivo.toAbsolutePath().getParent();
        if (carpeta != null) {
            Files.createDirectories(carpeta);
        }

        Path baseTemporal = carpeta == null ? Paths.get(".") : carpeta;
        Path temporal = Files.createTempFile(
                baseTemporal, "puntuaciones-", ".tmp"
        );

        try {
            try (DataOutputStream salida = new DataOutputStream(
                    Files.newOutputStream(temporal))) {
                salida.writeInt(MARCA_ARCHIVO);
                salida.writeInt(VERSION_ARCHIVO);
                salida.writeInt(datos.size());

                for (ResultadoTetris resultado : datos) {
                    salida.writeUTF(resultado.getNombreJugador());
                    salida.writeInt(resultado.getPuntuacion());
                    salida.writeUTF(resultado.getFecha().toString());
                }
            }

            try {
                Files.move(
                        temporal,
                        rutaArchivo,
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE
                );
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(
                        temporal,
                        rutaArchivo,
                        StandardCopyOption.REPLACE_EXISTING
                );
            }
        } finally {
            Files.deleteIfExists(temporal);
        }
    }
}
