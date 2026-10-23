package com.example.calmafinal;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class homeActivity extends AppCompatActivity {

    private Button btnMusica, btnRespiracion, btnContacto, btnInformacion;
    private Button btnSubirBrillo, btnBajarBrillo;
    private float brilloActual = 0.5f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // 1. Inicializar botones de navegación
        btnMusica = findViewById(R.id.btn_musica);
        btnRespiracion = findViewById(R.id.btn_respiracion);
        btnContacto = findViewById(R.id.btn_contacto);
        btnInformacion = findViewById(R.id.btn_informacion);

        // 2. Inicializar botones de brillo
        btnSubirBrillo = findViewById(R.id.btnSubirBrillo);
        btnBajarBrillo = findViewById(R.id.btnBajarBrillo);

        // Configurar navegación de los módulos
        btnMusica.setOnClickListener(v -> {
            startActivity(new Intent(homeActivity.this, MusicaActivity.class));
        });

        btnRespiracion.setOnClickListener(v -> {
            startActivity(new Intent(homeActivity.this, RespiracionActivity.class));
        });

        btnContacto.setOnClickListener(v -> {
            // Aquí abres tu vista previa de WhatsApp
            startActivity(new Intent(homeActivity.this, PreviewWhatsAppActivity.class));
        });

        btnInformacion.setOnClickListener(v -> {
            Toast.makeText(this, "Próximamente: Información sobre el estrés", Toast.LENGTH_SHORT).show();
        });

        // Obtener brillo inicial de la ventana
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        if (layoutParams.screenBrightness >= 0) {
            brilloActual = layoutParams.screenBrightness;
        }

        // Configurar control de brillo (+)
        btnSubirBrillo.setOnClickListener(v -> {
            brilloActual += 0.2f;
            if (brilloActual > 1.0f) {
                brilloActual = 1.0f;
                Toast.makeText(this, "Brillo al máximo ☀️", Toast.LENGTH_SHORT).show();
            }
            cambiarBrillo(brilloActual);
        });

        // Configurar control de brillo (-)
        btnBajarBrillo.setOnClickListener(v -> {
            brilloActual -= 0.2f;
            if (brilloActual < 0.1f) {
                brilloActual = 0.1f;
                Toast.makeText(this, "Brillo al mínimo 🌙", Toast.LENGTH_SHORT).show();
            }
            cambiarBrillo(brilloActual);
        });
    }

    private void cambiarBrillo(float valorBrillo) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = valorBrillo;
        getWindow().setAttributes(layoutParams);
    }
}