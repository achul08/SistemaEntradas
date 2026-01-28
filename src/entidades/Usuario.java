package entidades;

public abstract class Usuario {
    //atributos comunes a todos los usuarios
    private int idUsuario;
    private String nombre;
    private String apellido;
    private String username;
    private String password;


    public Usuario() {
    }

    public Usuario(String nombre, String apellido, String username, String password) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.username = username;
        this.password = password;
    }


    //GETTERS Y SETTERS ------
    public int getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }


    public abstract String getRol();


    //TOSTRING ------
    @Override
    public String toString() {
        return "Usuario\n" +
                "id del usuario: " + idUsuario +
                "\nNombre: " + nombre +
                "\nApellido: " + apellido +
                "\nUsername: " + username +
                "\nPassword: " + password +
                "\nRol: " + getRol(); //llama al método de la clase hija
    }
}