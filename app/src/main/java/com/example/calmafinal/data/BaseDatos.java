package com.example.calmafinal.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Usuario.class}, version = 2, exportSchema = false)
public abstract class BaseDatos extends RoomDatabase {

    private static volatile BaseDatos INSTANCE;

    public abstract UsuarioDao usuarioDao();

    public static BaseDatos getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BaseDatos.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    BaseDatos.class, "calma_db")
                            //.fallbackToDestructiveMigration() eliminar base de datos al cerrar el programa
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
