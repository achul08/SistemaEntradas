package entidades;


//HACER ESTA ENTIDAD 2DA



public class Usuario {
    private int idUsuario;
    private String nombre;
    private String apellido;
    private String username;
    private String password;
    private String rol;


    //constructores

    public Usuario() {
    }

    public Usuario(String nombre, String apellido, String username, String password, String rol) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    //getters y setters
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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }


    //toString
    @Override
    public String toString() {
        return "Usuario\n" +
                "id del usuario: " + idUsuario +
                "\nNombre: " + nombre +
                "\nApellido: " + apellido +
                "\nUsername: " + username +
                "\nPassword: " + password +
                "\nRol: " + rol;
    }
}
