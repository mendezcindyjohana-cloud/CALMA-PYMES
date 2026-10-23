package com.example.calmafinal;

import androidx.appcompat.app.AlertDialog; // <-- 1. Importación necesaria para la alerta
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast; // <-- Opcional por si deseas mostrar un mensaje al cancelar

public class MusicaActivity extends AppCompatActivity {

    private static final String PIXABAY_URL = "https://pixabay.com/es/sound-effects/search/relajante/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musica);

        Button btnIrMusicaRelajante = findViewById(R.id.btn_ir_youtube);

        btnIrMusicaRelajante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llamamos al diálogo de advertencia en lugar de abrir directamente
                mostrarAdvertenciaSalida();
            }
        });
    }

    // 2. Nuevo método que muestra la alerta antes de salir de la app
    private void mostrarAdvertenciaSalida() {
        new AlertDialog.Builder(MusicaActivity.this)
                .setTitle("🎵 Saliendo de CALMA+")
                .setMessage("Estás a punto de salir de la aplicación para abrir una fuente externa de sonidos relajantes en Pixabay. ¿Deseas continuar?")
                .setPositiveButton("Sí, continuar", (dialog, which) -> {
                    // Si el usuario acepta, ejecutamos la apertura del enlace
                    openMusicSource();
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    // Si cancela, solo cerramos el cuadro de diálogo
                    dialog.dismiss();
                })
                .show();
    }

    private void openMusicSource() {
        // 1. Crear un Intent con la acción ACTION_VIEW
        Intent intent = new Intent(Intent.ACTION_VIEW);

        // 2. Establecer los datos (Data) del Intent con la URI de Pixabay
        intent.setData(Uri.parse(PIXABAY_URL));

        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "No se pudo abrir la aplicación externa.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}