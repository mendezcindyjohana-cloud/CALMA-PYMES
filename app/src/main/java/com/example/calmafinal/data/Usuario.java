package com.example.calmafinal.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "usuario")
public class Usuario {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nombreUsuario;
    private String correo;
    private String password;

    // 🔹 Constructor vacío (Room lo necesita)
    public Usuario() {}

    // 🔹 Constructor con parámetros (para crear usuarios)
    public Usuario(String nombreUsuario, String correo, String password) {
        this.nombreUsuario = nombreUsuario;
        this.correo = correo;
        this.password = password;
    }

    // ====== GETTERS Y SETTERS ======

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

