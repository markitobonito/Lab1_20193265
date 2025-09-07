package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class JuegoActivity extends AppCompatActivity {

    private TextView tvPalabraOculta, tvResultado, tvComodin, tvTematica;
    private ImageView ivAhorcado;
    private GridLayout glAbecedario;
    private Button btnNuevoJuego;
    private String palabraOculta;
    private int errores = 0;
    private int aciertosConsecutivos = 0;
    private int comodines = 0;
    private long tiempoInicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        tvPalabraOculta = findViewById(R.id.tvPalabraOculta);
        tvResultado = findViewById(R.id.tvResultado);
        tvComodin = findViewById(R.id.tvComodin);
        ivAhorcado = findViewById(R.id.ivAhorcado);
        glAbecedario = findViewById(R.id.glAbecedario);
        btnNuevoJuego = findViewById(R.id.btnNuevoJuego);
        tvTematica = findViewById(R.id.tvTematica);
        tvComodin.setText("0/0");
        // Recibe la palabra y temática
        palabraOculta = getIntent().getStringExtra("palabraOculta");
        String tematica = getIntent().getStringExtra("tematica");
        if (tematica != null) {
            tvTematica.setText("Temática: " + tematica);
        }

        // Configurar el juego
        configurarJuego();

        // Botón de nuevo juego
        btnNuevoJuego.setOnClickListener(v -> finish());

        findViewById(R.id.btnComodin).setOnClickListener(v -> usarComodin());
    }

    private void configurarJuego() {
        // Configurar palabra oculta y abecedario
        tiempoInicio = SystemClock.elapsedRealtime();
        actualizarPalabraOculta();
        configurarAbecedario();
    }

// 1. Inicializa el comodín correctamente en onCreate


    // 2. Modifica actualizarPalabraOculta para mostrar letras acertadas
    private String letrasAcertadas = "";

    private void actualizarPalabraOculta() {
        StringBuilder resultado = new StringBuilder();
        for (char c : palabraOculta.toCharArray()) {
            if (letrasAcertadas.contains(String.valueOf(c))) {
                resultado.append(c).append(" ");
            } else {
                resultado.append("_ ");
            }
        }
        tvPalabraOculta.setText(resultado.toString().trim());
    }

    // 3. Modifica procesarLetra para actualizar aciertos y comodines
    private void procesarLetra(View view) {
        Button btnLetra = (Button) view;
        String letra = btnLetra.getText().toString();
        btnLetra.setEnabled(false);

        if (palabraOculta.contains(letra)) {
            letrasAcertadas += letra;
            aciertosConsecutivos++;
            if (aciertosConsecutivos == 4) {
                comodines++;
                aciertosConsecutivos = 0;
            }
            tvComodin.setText(comodines + "/" + aciertosConsecutivos);
            actualizarPalabraOculta();
            // Verifica si ganó
            if (palabraOculta.equals(tvPalabraOculta.getText().toString().replace(" ", ""))) {
                mostrarResultado(true);
            }
        } else {
            errores++;
            actualizarAhorcado();
        }
    }

    // 4. Ajusta actualizarAhorcado para el orden y tamaño

    private void actualizarAhorcado() {
        int size = 100;
        if (errores == 1) {
            ivAhorcado.setImageResource(R.drawable.head1sin_fondo);
        } else if (errores == 2) {
            ivAhorcado.setImageResource(R.drawable.torso_sinfondo);
        } else if (errores == 3) {
            ivAhorcado.setImageResource(R.drawable.brazodere_piernaizq_sinfondo); // brazo derecho
        } else if (errores == 4) {
            ivAhorcado.setImageResource(R.drawable.brazoizq_piernadere_sinfondo); // brazo izquierdo
        } else if (errores == 5) {
            ivAhorcado.setImageResource(R.drawable.brazodere_piernaizq_sinfondo); // pierna izquierda
        } else if (errores == 6) {
            ivAhorcado.setImageResource(R.drawable.brazoizq_piernadere_sinfondo); // pierna derecha
            mostrarResultado(false);
        }
        ivAhorcado.getLayoutParams().width = size;
        ivAhorcado.getLayoutParams().height = size;
        ivAhorcado.requestLayout();

        // Mantener la imagen de fondo (antenna_sinfondo)
        ivAhorcado.setBackgroundResource(R.drawable.antenna_sinfondo);
    }


    private void configurarAbecedario() {
        glAbecedario.removeAllViews();
        for (char letra = 'A'; letra <= 'Z'; letra++) {
            Button btnLetra = new Button(this);
            btnLetra.setText(String.valueOf(letra));
            btnLetra.setOnClickListener(this::procesarLetra);
            glAbecedario.addView(btnLetra);
        }
    }


    private void mostrarResultado(boolean gano) {
        long tiempoFinal = (SystemClock.elapsedRealtime() - tiempoInicio) / 1000;
        tvResultado.setVisibility(View.VISIBLE);
        if (gano) {
            tvResultado.setText("Ganó / Tiempo: " + tiempoFinal + "s");
            tvResultado.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            tvResultado.setText("Perdió / Tiempo: " + tiempoFinal + "s");
            tvResultado.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }
    }

    // Método para usar el comodín
    private void usarComodin() {
        if (comodines > 0) {
            for (char c : palabraOculta.toCharArray()) {
                if (!letrasAcertadas.contains(String.valueOf(c))) {
                    letrasAcertadas += c;
                    actualizarPalabraOculta();
                    comodines--;
                    tvComodin.setText(comodines + "/" + aciertosConsecutivos);
                    return;
                }
            }
        }
    }
}