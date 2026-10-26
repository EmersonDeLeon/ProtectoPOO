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
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import modelo.Usuario;

public class RepositorioUsuarios {

    private static final int MARCA_ARCHIVO = 0x55535253;
    private static final int VERSION_ARCHIVO = 1;
    private static final int MAX_USUARIOS = 100000;

    private final Path rutaArchivo;
    private List<Usuario> usuarios = new ArrayList<>();
    private int siguienteId = 1;

    public RepositorioUsuarios() {
        this(Paths.get("datos", "usuarios.dat"));
    }

    public RepositorioUsuarios(Path rutaArchivo) {
        if (rutaArchivo == null) {
            throw new IllegalArgumentException(
                    "La ruta del archivo no puede ser nula."
            );
        }
        this.rutaArchivo = rutaArchivo;
    }

    public Usuario registrarUsuario(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre del usuario no puede estar vacío."
            );
        }

        String nombreNormalizado = nombre.trim();
        if (existeNombre(nombreNormalizado)) {
            throw new IllegalArgumentException(
                    "Ya existe un usuario con ese nombre."
            );
        }
        if (usuarios.size() >= MAX_USUARIOS) {
            throw new IllegalStateException(
                    "Se alcanzó el límite de usuarios registrados."
            );
        }
        if (siguienteId <= 0 || siguienteId == Integer.MAX_VALUE) {
            throw new IllegalStateException(
                    "No quedan identificadores disponibles para usuarios."
            );
        }

        Usuario usuario = new Usuario(siguienteId++, nombreNormalizado);
        usuarios.add(usuario);
        return usuario;
    }

    public Usuario buscarPorId(int id) {
        for (Usuario usuario : usuarios) {
            if (usuario.getId() == id) {
                return usuario;
            }
        }
        return null;
    }

    public List<Usuario> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String consulta = nombre.trim().toLowerCase(Locale.ROOT);
        List<Usuario> encontrados = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            if (usuario.getNombre().toLowerCase(Locale.ROOT)
                    .contains(consulta)) {
                encontrados.add(usuario);
            }
        }
        return Collections.unmodifiableList(encontrados);
    }

    public List<Usuario> obtenerUsuarios() {
        return Collections.unmodifiableList(new ArrayList<>(usuarios));
    }

    public boolean existeNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }

        String buscado = nombre.trim();
        for (Usuario usuario : usuarios) {
            if (usuario.getNombre().equalsIgnoreCase(buscado)) {
                return true;
            }
        }
        return false;
    }

    public void guardarUsuarios() throws IOException {
        Path carpeta = rutaArchivo.toAbsolutePath().getParent();
        if (carpeta != null) {
            Files.createDirectories(carpeta);
        }

        Path temporal = Files.createTempFile(
                carpeta == null ? Paths.get(".") : carpeta,
                "usuarios-",
                ".tmp"
        );

        try {
            try (DataOutputStream salida = new DataOutputStream(
                    Files.newOutputStream(temporal))) {
                salida.writeInt(MARCA_ARCHIVO);
                salida.writeInt(VERSION_ARCHIVO);
                salida.writeInt(siguienteId);
                salida.writeInt(usuarios.size());

                for (Usuario usuario : usuarios) {
                    salida.writeInt(usuario.getId());
                    salida.writeUTF(usuario.getNombre());
                    salida.writeUTF(usuario.getFechaRegistro().toString());
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

    public void cargarUsuarios() throws IOException {
        if (!Files.exists(rutaArchivo)) {
            usuarios = new ArrayList<>();
            siguienteId = 1;
            return;
        }

        List<Usuario> cargados = new ArrayList<>();
        Set<Integer> ids = new HashSet<>();
        Set<String> nombres = new HashSet<>();
        int siguienteIdGuardado;

        try (DataInputStream entrada = new DataInputStream(
                Files.newInputStream(rutaArchivo))) {
            int marca = entrada.readInt();
            int version = entrada.readInt();
            siguienteIdGuardado = entrada.readInt();
            int cantidad = entrada.readInt();

            if (marca != MARCA_ARCHIVO) {
                throw new IOException("El archivo de usuarios no es válido.");
            }
            if (version != VERSION_ARCHIVO) {
                throw new IOException(
                        "Versión de usuarios no compatible: " + version
                );
            }
            if (siguienteIdGuardado <= 0
                    || cantidad < 0 || cantidad > MAX_USUARIOS) {
                throw new IOException(
                        "Los datos de control del archivo no son válidos."
                );
            }

            int idMayor = 0;
            for (int i = 0; i < cantidad; i++) {
                int id = entrada.readInt();
                String nombre = entrada.readUTF();
                String fechaTexto = entrada.readUTF();

                try {
                    LocalDateTime fecha = LocalDateTime.parse(fechaTexto);
                    Usuario usuario = new Usuario(id, nombre, fecha);
                    String nombreClave = usuario.getNombre()
                            .toLowerCase(Locale.ROOT);

                    if (!ids.add(id) || !nombres.add(nombreClave)) {
                        throw new IOException(
                                "Hay IDs o nombres duplicados en el archivo."
                        );

                    }
                    cargados.add(usuario);
                    idMayor = Math.max(idMayor, id);
                } catch (DateTimeParseException | IllegalArgumentException e) {
                    throw new IOException(
                            "El usuario número " + (i + 1)
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
            if (siguienteIdGuardado <= idMayor) {
                throw new IOException(
                        "El siguiente ID guardado no es válido."
                );
            }
        } catch (EOFException e) {
            throw new IOException("El archivo de usuarios está incompleto.", e);
        }

        usuarios = cargados;
        siguienteId = siguienteIdGuardado;
    }

    public Path getRutaArchivo() {
        return rutaArchivo;
    }
}
