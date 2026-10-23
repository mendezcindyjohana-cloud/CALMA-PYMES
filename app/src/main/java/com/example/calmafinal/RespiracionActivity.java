package com.example.calmafinal;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.graphics.Color;
import android.widget.Toast;

import java.util.Locale;

public class RespiracionActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private View circleView;
    private TextView tvInstruccion;
    private TextView tvContador;
    private Button btnIniciarCiclo;
    private AnimatorSet animatorSet;
    private CountDownTimer countDownTimer;
    private Vibrator vibrator;
    private TextToSpeech tts;

    private TextView tvContadorCiclos;
    private int currentCycle = 1;
    private static final int MAX_CYCLES = 4;

    private static final long DURATION_INHALAR = 4000; // 4 segundos
    private static final long DURATION_AGUANTAR = 7000; // 7 segundos
    private static final long DURATION_EXHALAR = 8000; // 8 segundos

    private static final float SCALE_MIN = 1.0f;
    private static final float SCALE_MAX = 2.0f;

    // Colores para las fases de relajación
    private static final int COLOR_INHALAR = Color.parseColor("#4DB6AC"); // Verde Menta
    private static final int COLOR_AGUANTAR = Color.parseColor("#64B5F6"); // Azul Relajante
    private static final int COLOR_EXHALAR = Color.parseColor("#81C784"); // Verde Claro

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respiracion);

        circleView = findViewById(R.id.circleView);
        tvInstruccion = findViewById(R.id.tvInstruccion);
        tvContador = findViewById(R.id.tvContador);
        tvContadorCiclos = findViewById(R.id.tvContadorCiclos);
        btnIniciarCiclo = findViewById(R.id.btnIniciarCiclo);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Inicializar TextToSpeech en español
        tts = new TextToSpeech(this, this);

        resetUI();

        btnIniciarCiclo.setOnClickListener(v -> iniciarCicloDesdeBoton());
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(new Locale("es", "ES"));
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "El idioma español no está soportado en este dispositivo", Toast.LENGTH_SHORT).show();
            } else {
                tts.setSpeechRate(0.85f); // Velocidad pausada y relajante

                // 🌟 Mensaje de bienvenida automático apenas entra a la actividad
                hablar("Vamos a relajarnos. Comencemos.");
            }
        } else {
            Toast.makeText(this, "Error al inicializar el servicio de voz", Toast.LENGTH_SHORT).show();
        }
    }

    private void hablar(String mensaje) {
        if (tts != null) {
            tts.speak(mensaje, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    private void iniciarCicloDesdeBoton() {
        btnIniciarCiclo.setEnabled(false);
        btnIniciarCiclo.setText("EN PROGRESO...");

        currentCycle = 1;
        resetUI();

        startBreathingCycle();
    }

    private void resetUI() {
        tvContadorCiclos.setText("Ciclo: " + currentCycle + "/" + MAX_CYCLES);
        tvInstruccion.setText("¡Prepárate!");
        tvContador.setText("4");

        circleView.setScaleX(SCALE_MIN);
        circleView.setScaleY(SCALE_MIN);
        cambiarColorCirculo(COLOR_INHALAR);
    }

    private void startBreathingCycle() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        hablar("Inhala profundo");

        // 1. 🌬️ INHALAR (4 segundos)
        tvInstruccion.setText("INHALA (4s) 🌬️");
        animarColorCirculo(COLOR_EXHALAR, COLOR_INHALAR, DURATION_INHALAR);
        iniciarTemporizadorFase(DURATION_INHALAR, 4, true, () -> {

            // 2. 🧘 AGUANTAR (7 segundos)
            tvInstruccion.setText("AGUANTA (7s) 🧘");
            hablar("Sostén el aire");
            animarColorCirculo(COLOR_INHALAR, COLOR_AGUANTAR, 1000);
            iniciarTemporizadorFase(DURATION_AGUANTAR, 7, false, () -> {

                // 3. 😌 EXHALAR (8 segundos)
                tvInstruccion.setText("EXHALA (8s) 😌");
                hablar("Exhala despacio");
                animarColorCirculo(COLOR_AGUANTAR, COLOR_EXHALAR, 1000);
                iniciarTemporizadorFase(DURATION_EXHALAR, 8, false, () -> {

                    currentCycle++;
                    if (currentCycle <= MAX_CYCLES) {
                        tvContadorCiclos.setText("Ciclo: " + currentCycle + "/" + MAX_CYCLES);
                        startBreathingCycle();
                    } else {
                        tvInstruccion.setText("¡Ciclo Completo! ✅");
                        tvContadorCiclos.setText("Ciclo: " + MAX_CYCLES + "/" + MAX_CYCLES);
                        tvContador.setText("✨");
                        hablar("¡Excelente trabajo! Ciclo completado.");
                        btnIniciarCiclo.setEnabled(true);
                        btnIniciarCiclo.setText("INICIAR CICLO");
                    }
                });
            });
        });

        // Sincronizar animación visual del círculo con las duraciones exactas
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(circleView, "scaleX", SCALE_MIN, SCALE_MAX);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(circleView, "scaleY", SCALE_MIN, SCALE_MAX);
        scaleUpX.setDuration(DURATION_INHALAR);
        scaleUpY.setDuration(DURATION_INHALAR);

        ObjectAnimator holdScaleX = ObjectAnimator.ofFloat(circleView, "scaleX", SCALE_MAX, SCALE_MAX);
        ObjectAnimator holdScaleY = ObjectAnimator.ofFloat(circleView, "scaleY", SCALE_MAX, SCALE_MAX);
        holdScaleX.setDuration(DURATION_AGUANTAR);
        holdScaleY.setDuration(DURATION_AGUANTAR);

        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(circleView, "scaleX", SCALE_MAX, SCALE_MIN);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(circleView, "scaleY", SCALE_MAX, SCALE_MIN);
        scaleDownX.setDuration(DURATION_EXHALAR);
        scaleDownY.setDuration(DURATION_EXHALAR);

        animatorSet = new AnimatorSet();
        animatorSet.playSequentially(scaleUpX, holdScaleX, scaleDownX);
        animatorSet.start();
    }

    private void cambiarColorCirculo(int color) {
        if (circleView.getBackground() instanceof GradientDrawable) {
            GradientDrawable drawable = (GradientDrawable) circleView.getBackground().mutate();
            drawable.setColor(color);
        }
    }

    private void animarColorCirculo(int colorInicio, int colorFin, long duracion) {
        ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorInicio, colorFin);
        colorAnimator.setDuration(duracion);
        colorAnimator.addUpdateListener(animator -> {
            int colorActual = (int) animator.getAnimatedValue();
            cambiarColorCirculo(colorActual);
        });
        colorAnimator.start();
    }

    private void iniciarTemporizadorFase(long duracionMs, int segundosTotales, boolean esInhalacion, Runnable onFinishCallback) {
        countDownTimer = new CountDownTimer(duracionMs, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int segundoActual = (int) (millisUntilFinished / 1000) + 1;
                tvContador.setText(String.valueOf(segundoActual));

                int intensidad = esInhalacion ? (5 - segundoActual) * 50 : 100;
                if (intensidad < 30) intensidad = 30;
                hacerVibrar(300, Math.min(intensidad, 255));
            }

            @Override
            public void onFinish() {
                tvContador.setText("0");
                onFinishCallback.run();
            }
        }.start();
    }

    private void hacerVibrar(long milisegundos, int intensidad) {
        if (vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(milisegundos, intensidad));
            } else {
                vibrator.vibrate(milisegundos);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (tts != null) {
            tts.stop();
        }
        btnIniciarCiclo.setEnabled(true);
        btnIniciarCiclo.setText("INICIAR CICLO");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}