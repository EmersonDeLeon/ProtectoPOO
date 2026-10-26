package repositorio;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import modelo.ResultadoTetris;

public class RepositorioPuntuaciones {

    private static final int MAX_RESULTADOS = 100000;
    private static final String ENCABEZADO = "Nombre,Puntaje";
    private static final Comparator<ResultadoTetris> ORDEN_PUNTAJE =
            Comparator.comparingInt(ResultadoTetris::getPuntuacion)
                    .reversed()
                    .thenComparing(ResultadoTetris::getFecha);

    private final Path rutaArchivo;
    private List<ResultadoTetris> resultados = new ArrayList<>();

    public RepositorioPuntuaciones() {
        this(Paths.get("datos", "calificaciones.csv"));
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

        List<List<String>> registros = parsearCsv(
                new String(Files.readAllBytes(rutaArchivo), StandardCharsets.UTF_8)
        );
        List<ResultadoTetris> cargados = new ArrayList<>();
        for (int i = 0; i < registros.size(); i++) {
            List<String> fila = registros.get(i);
            if (fila.size() == 2 && i == 0
                    && fila.get(0).equalsIgnoreCase("Nombre")
                    && fila.get(1).equalsIgnoreCase("Puntaje")) {
                continue;
            }
            if (fila.size() != 2) {
                throw new IOException(
                        "La fila " + (i + 1)
                        + " del CSV debe tener nombre y puntaje."
                );
            }

            try {
                int puntuacion = Integer.parseInt(fila.get(1).trim());
                cargados.add(new ResultadoTetris(
                        fila.get(0), puntuacion, LocalDateTime.now()
                ));
            } catch (IllegalArgumentException e) {
                throw new IOException(
                        "La fila " + (i + 1)
                        + " del CSV contiene un nombre o puntaje inválido.",
                        e
                );
            }
            if (cargados.size() > MAX_RESULTADOS) {
                throw new IOException("El CSV excede el límite de resultados.");
            }
        }

        cargados.sort(ORDEN_PUNTAJE);
        resultados = cargados;
    }

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
        actualizados.sort(ORDEN_PUNTAJE);
        guardarLista(actualizados);
        resultados = actualizados;
    }

    public List<ResultadoTetris> obtenerResultados() {
        return Collections.unmodifiableList(new ArrayList<>(resultados));
    }

    public List<ResultadoTetris> obtenerRanking(int limite) {
        if (limite < 0) {
            throw new IllegalArgumentException(
                    "El límite no puede ser negativo."
            );
        }

        List<ResultadoTetris> ordenados = new ArrayList<>(resultados);
        ordenados.sort(ORDEN_PUNTAJE);
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
        Path temporal = Files.createTempFile(carpeta, "calificaciones-", ".tmp");

        List<ResultadoTetris> ordenados = new ArrayList<>(datos);
        ordenados.sort(ORDEN_PUNTAJE);
        try {
            StringBuilder contenido = new StringBuilder(ENCABEZADO).append('\n');
            for (ResultadoTetris resultado : ordenados) {
                contenido.append(escaparCsv(resultado.getNombreJugador()))
                        .append(',')
                        .append(resultado.getPuntuacion())
                        .append('\n');
            }
            Files.write(temporal, contenido.toString().getBytes(StandardCharsets.UTF_8));
            try {
                Files.move(temporal, rutaArchivo,
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporal, rutaArchivo,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporal);
        }
    }

    private static String escaparCsv(String valor) {
        return "\"" + valor.replace("\"", "\"\"") + "\"";
    }

    private static List<List<String>> parsearCsv(String contenido)
            throws IOException {
        List<List<String>> registros = new ArrayList<>();
        List<String> registro = new ArrayList<>();
        StringBuilder campo = new StringBuilder();
        boolean entreComillas = false;
        boolean despuesDeComillas = false;

        if (!contenido.isEmpty() && contenido.charAt(0) == '\uFEFF') {
            contenido = contenido.substring(1);
        }

        for (int i = 0; i < contenido.length(); i++) {
            char actual = contenido.charAt(i);
            if (entreComillas) {
                if (actual == '"') {
                    if (i + 1 < contenido.length()
                            && contenido.charAt(i + 1) == '"') {
                        campo.append('"');
                        i++;
                    } else {
                        entreComillas = false;
                        despuesDeComillas = true;
                    }
                } else {
                    campo.append(actual);
                }
            } else if (despuesDeComillas) {
                if (actual == ',') {
                    registro.add(campo.toString());
                    campo.setLength(0);
                    despuesDeComillas = false;
                } else if (actual == '\n' || actual == '\r') {
                    registro.add(campo.toString());
                    registros.add(registro);
                    registro = new ArrayList<>();
                    campo.setLength(0);
                    despuesDeComillas = false;
                    if (actual == '\r' && i + 1 < contenido.length()
                            && contenido.charAt(i + 1) == '\n') {
                        i++;
                    }
                } else {
                    throw new IOException("El CSV tiene texto después de una comilla.");
                }
            } else if (actual == '"') {
                if (campo.length() != 0) {
                    throw new IOException("El CSV contiene comillas mal ubicadas.");
                }
                entreComillas = true;
            } else if (actual == ',') {
                registro.add(campo.toString());
                campo.setLength(0);
            } else if (actual == '\n' || actual == '\r') {
                registro.add(campo.toString());
                registros.add(registro);
                registro = new ArrayList<>();
                campo.setLength(0);
                if (actual == '\r' && i + 1 < contenido.length()
                        && contenido.charAt(i + 1) == '\n') {
                    i++;
                }
            } else {
                campo.append(actual);
            }
        }

        if (entreComillas) {
            throw new IOException("El CSV termina con una comilla sin cerrar.");
        }
        if (despuesDeComillas || campo.length() > 0 || !registro.isEmpty()) {
            registro.add(campo.toString());
            registros.add(registro);
        }
        return registros;
    }
}