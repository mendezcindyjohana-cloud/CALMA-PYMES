package com.example.calmafinal;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import com.example.calmafinal.data.BaseDatos;
import com.example.calmafinal.data.Usuario;
import com.example.calmafinal.data.UsuarioDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegistroActivity extends AppCompatActivity {

    private EditText etNombre, etEmailRegistro, etPasswordRegistro;
    private Button btnRegistrarse;

    private BaseDatos db;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        db = BaseDatos.getDatabase(getApplicationContext());

        etNombre = findViewById(R.id.etNombre);
        etEmailRegistro = findViewById(R.id.etEmailRegistro);
        etPasswordRegistro = findViewById(R.id.etPasswordRegistro);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);

        btnRegistrarse.setOnClickListener(v -> registrarUsuario());
    }

    private void registrarUsuario() {
        final String nombre = etNombre.getText().toString().trim();
        final String email = etEmailRegistro.getText().toString().trim();
        final String password = etPasswordRegistro.getText().toString().trim();

        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        executorService.execute(() -> {
            UsuarioDao usuarioDao = db.usuarioDao();
            Usuario existente = usuarioDao.findByUsername(nombre);

            if (existente != null) {
                runOnUiThread(() ->
                        Toast.makeText(this, "El usuario ya existe.", Toast.LENGTH_SHORT).show());
            } else {
                Usuario nuevo = new Usuario(nombre, email, password);
                long id = usuarioDao.insert(nuevo);

                runOnUiThread(() -> {
                    if (id > 0) {
                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        // Redirigir al login
                        Intent intent = new Intent(this, loginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Error al registrar.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
