package repositorio;

<<<<<<< HEAD
import java.io.IOException;
import java.nio.charset.StandardCharsets;
=======
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
>>>>>>> f40dc91d0fe34d72c02c74f3110bd5b2c8b64661
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
<<<<<<< HEAD
=======
import java.time.format.DateTimeParseException;
>>>>>>> f40dc91d0fe34d72c02c74f3110bd5b2c8b64661
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import modelo.ResultadoTetris;

<<<<<<< HEAD
/** Guarda y consulta las calificaciones del juego en formato CSV. */
public class RepositorioPuntuaciones {

    private static final int MAX_RESULTADOS = 100000;
    private static final String ENCABEZADO = "Nombre,Puntaje";
    private static final Comparator<ResultadoTetris> ORDEN_PUNTAJE =
            Comparator.comparingInt(ResultadoTetris::getPuntuacion)
                    .reversed()
                    .thenComparing(ResultadoTetris::getFecha);
=======
public class RepositorioPuntuaciones {

    private static final int MARCA_ARCHIVO = 0x54545253;
    private static final int VERSION_ARCHIVO = 1;
    private static final int MAX_RESULTADOS = 100000;
>>>>>>> f40dc91d0fe34d72c02c74f3110bd5b2c8b64661

    private final Path rutaArchivo;
    private List<ResultadoTetris> resultados = new ArrayList<>();

    public RepositorioPuntuaciones() {
<<<<<<< HEAD
        this(Paths.get("datos", "calificaciones.csv"));
=======
        this(Paths.get("datos", "puntuaciones.dat"));
>>>>>>> f40dc91d0fe34d72c02c74f3110bd5b2c8b64661
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

<<<<<<< HEAD
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

    /** Guarda los resultados actuales en el archivo CSV. */
=======
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
>>>>>>> f40dc91d0fe34d72c02c74f3110bd5b2c8b64661
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
<<<<<<< HEAD
        actualizados.sort(ORDEN_PUNTAJE);
=======
>>>>>>> f40dc91d0fe34d72c02c74f3110bd5b2c8b64661
        guardarLista(actualizados);
        resultados = actualizados;
    }

    public List<ResultadoTetris> obtenerResultados() {
<<<<<<< HEAD
        return Collections.unmodifiableList(new ArrayList<>(resultados));
=======
        return Collections.unmodifiableList(
                new ArrayList<>(resultados)
        );
>>>>>>> f40dc91d0fe34d72c02c74f3110bd5b2c8b64661
    }

    public List<ResultadoTetris> obtenerRanking(int limite) {
        if (limite < 0) {
            throw new IllegalArgumentException(
                    "El límite no puede ser negativo."
            );
        }

        List<ResultadoTetris> ordenados = new ArrayList<>(resultados);
<<<<<<< HEAD
        ordenados.sort(ORDEN_PUNTAJE);
=======
        ordenados.sort(
                Comparator.comparingInt(ResultadoTetris::getPuntuacion)
                        .reversed()
                        .thenComparing(ResultadoTetris::getFecha)
        );

>>>>>>> f40dc91d0fe34d72c02c74f3110bd5b2c8b64661
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
<<<<<<< HEAD
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
=======

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
>>>>>>> f40dc91d0fe34d72c02c74f3110bd5b2c8b64661
            }
        } finally {
            Files.deleteIfExists(temporal);
        }
    }
<<<<<<< HEAD

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
=======
>>>>>>> f40dc91d0fe34d72c02c74f3110bd5b2c8b64661
}
