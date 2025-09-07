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
import androidx.core.content.ContextCompat;

public class JuegoActivity extends AppCompatActivity {

    private TextView tvPalabraOculta, tvResultado, tvComodin, tvTematica;
    private ImageView ivCabeza, ivTorso, ivBrazoDerecho, ivBrazoIzquierdo, ivPiernaIzquierda, ivPiernaDerecha;
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
        glAbecedario = findViewById(R.id.glAbecedario);
        btnNuevoJuego = findViewById(R.id.btnNuevoJuego);
        tvTematica = findViewById(R.id.tvTematica);
        tvComodin.setText(getString(R.string.comodin_text, 0, 0));

        // Inicializar partes del muñeco
        ivCabeza = findViewById(R.id.ivCabeza);
        ivTorso = findViewById(R.id.ivTorso);
        ivBrazoDerecho = findViewById(R.id.ivBrazoDerecho);
        ivBrazoIzquierdo = findViewById(R.id.ivBrazoIzquierdo);
        ivPiernaIzquierda = findViewById(R.id.ivPiernaIzquierda);
        ivPiernaDerecha = findViewById(R.id.ivPiernaDerecha);

        // Ocultar todas las partes del muñeco al inicio
        ocultarPartesDelMuñeco();

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

    private void ocultarPartesDelMuñeco() {
        ivCabeza.setVisibility(View.INVISIBLE);
        ivTorso.setVisibility(View.INVISIBLE);
        ivBrazoDerecho.setVisibility(View.INVISIBLE);
        ivBrazoIzquierdo.setVisibility(View.INVISIBLE);
        ivPiernaIzquierda.setVisibility(View.INVISIBLE);
        ivPiernaDerecha.setVisibility(View.INVISIBLE);
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
            tvComodin.setText(getString(R.string.comodin_text, comodines, aciertosConsecutivos));
            actualizarPalabraOculta();
            // Verifica si ganó
            if (palabraOculta.trim().equals(tvPalabraOculta.getText().toString().replace(" ", "").trim())) {
                runOnUiThread(() -> mostrarResultado(true));
            }
        } else {
            errores++;
            actualizarAhorcado();
            // Verifica si perdió
            if (errores >= 6) {
                runOnUiThread(() -> mostrarResultado(false));
            }
        }
    }

    private void actualizarAhorcado() {
        switch (errores) {
            case 1:
                ivCabeza.setVisibility(View.VISIBLE);
                break;
            case 2:
                ivTorso.setVisibility(View.VISIBLE);
                break;
            case 3:
                ivBrazoDerecho.setVisibility(View.VISIBLE);
                break;
            case 4:
                ivBrazoIzquierdo.setVisibility(View.VISIBLE);
                break;
            case 5:
                ivPiernaIzquierda.setVisibility(View.VISIBLE);
                break;
            case 6:
                ivPiernaDerecha.setVisibility(View.VISIBLE);
                mostrarResultado(false);
                break;
        }
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
            tvResultado.setText(getString(R.string.resultado_gano, tiempoFinal));
            tvResultado.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark));
        } else {
            tvResultado.setText(getString(R.string.resultado_perdio, tiempoFinal));
            tvResultado.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
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