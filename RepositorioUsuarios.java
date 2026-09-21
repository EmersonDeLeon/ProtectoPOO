private List<Usuario> usuarios;
private int siguienteId;

public Usuario registrarUsuario(String nombre);
public Usuario buscarPorId(int id);
public List<Usuario> buscarPorNombre(String nombre);
public List<Usuario> obtenerUsuarios();
public boolean existeNombre(String nombre);
public void guardarUsuarios();
public void cargarUsuarios();