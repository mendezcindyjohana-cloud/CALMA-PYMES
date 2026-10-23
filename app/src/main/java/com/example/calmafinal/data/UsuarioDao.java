package com.example.calmafinal.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UsuarioDao {

    @Insert
    long insert(Usuario usuario);

    @Query("SELECT * FROM Usuario WHERE correo = :correo AND password = :password LIMIT 1")
    Usuario login(String correo, String password);

    // ✨ Método que busca por nombre de usuario o correo para el registro
    @Query("SELECT * FROM Usuario WHERE nombreUsuario = :username OR correo = :username LIMIT 1")
    Usuario findByUsername(String username);

    @Query("SELECT * FROM Usuario WHERE correo = :correo LIMIT 1")
    Usuario obtenerUsuarioPorCorreo(String correo);

    @Query("UPDATE Usuario SET password = :nuevaPassword WHERE correo = :correo")
    void actualizarPassword(String correo, String nuevaPassword);
}