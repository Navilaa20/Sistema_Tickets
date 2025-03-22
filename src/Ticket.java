public class Ticket {

    private int id;
    private String description;
    private String estado;
    private String prioridad;

    public Ticket(int id, String description, String estado, String prioridad) {
        this.id = id;
        this.description = description;
        this.estado = estado;
        this.prioridad = prioridad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public void historial (){

    }

}
