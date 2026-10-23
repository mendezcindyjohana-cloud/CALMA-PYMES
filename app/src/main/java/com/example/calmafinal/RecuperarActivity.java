package com.example.calmafinal;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.calmafinal.data.BaseDatos;
import com.example.calmafinal.data.Usuario;
import com.example.calmafinal.data.UsuarioDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecuperarActivity extends AppCompatActivity {

    private EditText etCorreoRecuperar, etNuevaPassword;
    private Button btnVerificarCorreo, btnCambiarPassword;
    private LinearLayout layoutNuevaPassword;

    private BaseDatos db;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private String correoValidado = ""; // Almacena temporalmente el correo verificado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar);

        db = BaseDatos.getDatabase(getApplicationContext());

        // Inicializar vistas (Asegúrate de que tus IDs en el XML coincidan con estos)
        etCorreoRecuperar = findViewById(R.id.etCorreoRecuperar);
        etNuevaPassword = findViewById(R.id.etNuevaPassword); // Este campo lo agregaremos en el XML si no lo tienes
        btnVerificarCorreo = findViewById(R.id.btnEnviarCorreo); // Reutilizamos o nombramos tu botón
        btnCambiarPassword = findViewById(R.id.btnCambiarPassword); // Botón para guardar la nueva pass
        layoutNuevaPassword = findViewById(R.id.layoutNuevaPassword); // Contenedor del nuevo password

        // Ocultar el campo de nueva contraseña al iniciar la pantalla
        if (layoutNuevaPassword != null) {
            layoutNuevaPassword.setVisibility(View.GONE);
        }
        if (btnCambiarPassword != null) {
            btnCambiarPassword.setVisibility(View.GONE);
        }

        // Botón 1: Verificar si el correo existe en la BD
        btnVerificarCorreo.setOnClickListener(v -> verificarCorreoExistente());

        // Botón 2: Cambiar y guardar la contraseña
        if (btnCambiarPassword != null) {
            btnCambiarPassword.setOnClickListener(v -> actualizarContraseñaBD());
        }
    }

    private void verificarCorreoExistente() {
        final String correo = etCorreoRecuperar.getText().toString().trim();

        if (correo.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa tu correo electrónico.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Ingresa un formato de correo válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Consultar en la base de datos de forma asíncrona
        executorService.execute(() -> {
            UsuarioDao dao = db.usuarioDao();
            Usuario usuario = dao.obtenerUsuarioPorCorreo(correo);

            runOnUiThread(() -> {
                if (usuario != null) {
                    // ¡El correo existe en la base de datos!
                    correoValidado = correo;
                    Toast.makeText(this, "¡Correo verificado con éxito! Ingresa tu nueva contraseña.", Toast.LENGTH_LONG).show();

                    // Bloquear campo de correo y mostrar campos de nueva contraseña
                    etCorreoRecuperar.setEnabled(false);
                    btnVerificarCorreo.setVisibility(View.GONE);

                    if (layoutNuevaPassword != null) {
                        layoutNuevaPassword.setVisibility(View.VISIBLE);
                    }
                    if (btnCambiarPassword != null) {
                        btnCambiarPassword.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(this, "El correo ingresado no está registrado en CALMA+.", Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void actualizarContraseñaBD() {
        final String nuevaPassword = etNuevaPassword.getText().toString().trim();

        if (nuevaPassword.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa una nueva contraseña.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (nuevaPassword.length() < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show();
            return;
        }

        executorService.execute(() -> {
            UsuarioDao dao = db.usuarioDao();
            dao.actualizarPassword(correoValidado, nuevaPassword);

            runOnUiThread(() -> {
                Toast.makeText(this, "¡Contraseña actualizada correctamente!", Toast.LENGTH_SHORT).show();
                finish(); // Cierra la actividad y regresa al Login
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}