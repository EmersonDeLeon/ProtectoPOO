public class Aplicacion extends Application

private Usuario usuarioActual;
private GestorPantallas gestorPantallas;
private RepositorioUsuarios repositorioUsuarios;
private RepositorioPuntuaciones repositorioPuntuaciones;

@Override
public void start(Stage stage);

public void iniciar();
public void cerrarAplicacion();

public Usuario getUsuarioActual();
public void setUsuarioActual(Usuario usuario);