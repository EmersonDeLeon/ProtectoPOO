import controller.GestorPantallas;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import modelo.Usuario;
import repositorio.RepositorioPuntuaciones;
import repositorio.RepositorioUsuarios;
import view.Fuentes;
import view.VentanaPrincipal;
import controller.tetrisController;
import modelo.JuegoTetrisBinario;

public class Aplicacion {

    private Usuario usuarioActual;
    private final RepositorioUsuarios repositorioUsuarios;
    private final RepositorioPuntuaciones repositorioPuntuaciones;

    private GestorPantallas gestorPantallas;
    private VentanaPrincipal ventanaPrincipal;

    public Aplicacion() {
        this(new RepositorioUsuarios(), new RepositorioPuntuaciones());
    }

    public Aplicacion(
            RepositorioUsuarios repositorioUsuarios,
            RepositorioPuntuaciones repositorioPuntuaciones
    ) {
         if (repositorioUsuarios == null || repositorioPuntuaciones == null) {
            throw new IllegalArgumentException(
                    "Los repositorios de usuarios y puntuaciones son obligatorios."
            );
        }
        this.repositorioUsuarios = repositorioUsuarios;
        this.repositorioPuntuaciones = repositorioPuntuaciones;
    }

    public void iniciar() {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(this::iniciar);
            return;
        }
        if (ventanaPrincipal != null && ventanaPrincipal.isDisplayable()) {
            ventanaPrincipal.setVisible(true);
            return;
        }

        Fuentes.configurar();
        cargarRepositorios();

        ventanaPrincipal = new VentanaPrincipal();
        ventanaPrincipal.setDefaultCloseOperation(
                VentanaPrincipal.DO_NOTHING_ON_CLOSE
        );
        ventanaPrincipal.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrarAplicacion();
            }
        });

        gestorPantallas = new GestorPantallas(
        ventanaPrincipal,
        repositorioUsuarios
        );

        JuegoTetrisBinario juego = new JuegoTetrisBinario();
        new tetrisController(juego, ventanaPrincipal);

        gestorPantallas.mostrarMenu();
        ventanaPrincipal.setVisible(true);
        gestorPantallas.mostrarMenu();
        ventanaPrincipal.setVisible(true);
    }

    public Usuario registrarUsuario() {
        if (gestorPantallas == null) {
            throw new IllegalStateException(
                    "Primero debes iniciar la aplicación."
            );
        }
        Usuario registrado = gestorPantallas.mostrarRegistro();
        if (registrado != null) {
            setUsuarioActual(registrado);
        }
        return registrado;
    }

    public void cerrarAplicacion() {
        guardarRepositorios();
        if (ventanaPrincipal != null) {
            ventanaPrincipal.dispose();
            ventanaPrincipal = null;
            gestorPantallas = null;
        }
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuario) {
        usuarioActual = usuario;
    }

    public RepositorioUsuarios getRepositorioUsuarios() {
        return repositorioUsuarios;
    }

    public RepositorioPuntuaciones getRepositorioPuntuaciones() {
        return repositorioPuntuaciones;
    }

    public GestorPantallas getGestorPantallas() {
        return gestorPantallas;
    }

    private void cargarRepositorios() {
        try {
            repositorioUsuarios.cargarUsuarios();
        } catch (IOException e) {
            mostrarError("No se pudieron cargar los usuarios: " + e.getMessage());
        }

        try {
            repositorioPuntuaciones.cargar();
        } catch (IOException e) {
            mostrarError(
                    "No se pudieron cargar las puntuaciones: " + e.getMessage()
            );
        }
    }

    private void guardarRepositorios() {
        try {
            repositorioUsuarios.guardarUsuarios();
        } catch (IOException e) {
            mostrarError("No se pudieron guardar los usuarios: " + e.getMessage());
        }

        try {
            repositorioPuntuaciones.guardar();
        } catch (IOException e) {
            mostrarError(
                    "No se pudieron guardar las puntuaciones: " + e.getMessage()
            );
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                ventanaPrincipal,
                mensaje,
                "Aviso",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Aplicacion().iniciar());
    }
}
