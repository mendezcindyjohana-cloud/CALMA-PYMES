package com.example.calmafinal;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PreviewWhatsAppActivity extends AppCompatActivity {

    private EditText etMensajePreview;
    private Button btnEnviarWhatsApp;

    // Número configurado de apoyo psicológico
    private static final String WHATSAPP_NUMBER = "573104678220";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_whatsapp);

        etMensajePreview = findViewById(R.id.etMensajePreview);
        btnEnviarWhatsApp = findViewById(R.id.btnEnviarWhatsApp);

        // Mensaje predeterminado con los saltos de línea codificados
        String mensajeInicial = "🌸 ¡Hola y bienvenid@ a CALMA+! 💚\n\n" +
                "Muchas gracias por contactarnos.\n" +
                "Entendemos que dar este paso es importante. Estamos aquí para ofrecerte un apoyo psicológico enfocado en tu bienestar y en ayudarte a manejar el estrés de forma efectiva.\n\n" +
                "Cuéntanos, ¿Cómo te sientes hoy? ¿En qué podemos acompañarte y ser de ayuda en este momento?";

        etMensajePreview.setText(mensajeInicial);

        btnEnviarWhatsApp.setOnClickListener(v -> {
            String mensajeFinal = etMensajePreview.getText().toString().trim();

            if (mensajeFinal.isEmpty()) {
                Toast.makeText(this, "El mensaje no puede estar vacío.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // Intent para abrir WhatsApp con el mensaje editado
                String url = "https://api.whatsapp.com/send?phone=" + WHATSAPP_NUMBER + "&text=" + Uri.encode(mensajeFinal);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "WhatsApp no está instalado en el dispositivo.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }
}