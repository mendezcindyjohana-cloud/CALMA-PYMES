package com.example.calmafinal;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.view.View;

import com.example.calmafinal.data.BaseDatos;
import com.example.calmafinal.data.Usuario;
import com.example.calmafinal.data.UsuarioDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class loginActivity extends AppCompatActivity {

    private EditText etCorreoLogin, etPasswordLogin;
    private Button btnIniciarSesion;
    private TextView tvRegister, tvForgotPassword;

    private BaseDatos db;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = BaseDatos.getDatabase(getApplicationContext());

        // 1. Inicialización de Vistas
        etCorreoLogin = findViewById(R.id.etCorreoLogin);
        etPasswordLogin = findViewById(R.id.etPasswordLogin);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        tvRegister = findViewById(R.id.tvRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        // 2. Listener para Iniciar Sesión
        btnIniciarSesion.setOnClickListener(v -> iniciarSesion());

        // 3. HABILITACIÓN DEL BOTÓN DE REGISTRO
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });

        // 4. ✨ CORREGIDO: CONFIGURACIÓN DEL ENLACE DE RECUPERACIÓN DE CONTRASEÑA
        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(loginActivity.this, RecuperarActivity.class);
            startActivity(intent);
        });
    }

    private void iniciarSesion() {
        final String correo = etCorreoLogin.getText().toString().trim();
        final String password = etPasswordLogin.getText().toString().trim();

        if (correo.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Por favor, ingresa un correo válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        executorService.execute(() -> {
            UsuarioDao usuarioDao = db.usuarioDao();
            Usuario user = usuarioDao.login(correo, password);

            runOnUiThread(() -> {
                if (user != null) {
                    Toast.makeText(this, "¡Bienvenido/a, " + user.getNombreUsuario() + "!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, homeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Correo o contraseña incorrectos.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}