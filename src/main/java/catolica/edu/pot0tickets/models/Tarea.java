package catolica.edu.pot0tickets.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Tareas")
public class Tarea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTarea;

    @NotNull
    @Size(max = 70)
    private String nombre;

    @NotNull
    @Size(max = 250)
    private String info;

    @NotNull
    @Size(max = 20)
    @Pattern(regexp = "^(BAJA|NORMAL|IMPORTANTE|CRÍTICA)$")
    private String prioridad;

    @NotNull
    private String estado;

    private boolean completada;

    @NotNull
    private int idTicket;

    private Integer idEncargado;

    // Constructor vacío
    public Tarea() {}

    // Constructor con todos los atributos
    public Tarea(int idTarea, String nombre, String info, String prioridad, String estado, boolean completada, int idTicket, Integer idEncargado) {
        this.idTarea = idTarea;
        this.nombre = nombre;
        this.info = info;
        this.prioridad = prioridad;
        this.estado = estado;
        this.completada = completada;
        this.idTicket = idTicket;
        this.idEncargado = idEncargado;
    }

    // Getters y Setters
    public int getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(int idTarea) {
        this.idTarea = idTarea;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    public int getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(int idTicket) {
        this.idTicket = idTicket;
    }

    public Integer getIdEncargado() {
        return idEncargado;
    }

    public void setIdEncargado(Integer idEncargado) {
        this.idEncargado = idEncargado;
    }
}
