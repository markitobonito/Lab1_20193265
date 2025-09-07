package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Random;
import androidx.appcompat.app.AppCompatActivity;

public class SeleccionTematicaActivity extends AppCompatActivity {

    // Listas de palabras por temática
    private final String[] palabrasRedes = {
        "ROUTER", "SWITCH", "ETHERNET", "TCP", "UDP", "IPV6", "IPV4", "FIREWALL", "DNS", "DHCP",
        "SUBNET", "GATEWAY", "BROADBAND", "MODEM", "WIFI", "SSID", "MAC", "PACKET", "FRAME", "LAN",
        "WAN", "NAT", "PROXY", "BRIDGE", "HUB", "VLAN", "PING", "TRACEROUTE", "BGP", "OSPF"
    };
    private final String[] palabrasCiberseguridad = {
        "PHISHING", "MALWARE", "FIREWALL", "ENCRYPTION", "PASSWORD", "VIRUS", "TROJAN", "SPYWARE", "RANSOMWARE", "BACKDOOR",
        "ROOTKIT", "BOTNET", "HACKER", "CRYPTO", "SSL", "TLS", "AUTH", "HASH", "SPOOFING", "BRUTEFORCE",
        "SOC", "SIEM", "ZERO", "DAY", "PATCH", "VULNERABILITY", "ANTIVIRUS", "SECURITY", "PRIVACY", "FORENSICS"
    };
    private final String[] palabrasFibraOptica = {
        "FIBRA", "OPTICA", "LASER", "PULSO", "MODULACION", "MULTIMODO", "MONOMODO", "CONECTOR", "ATENUACION", "REFLEXION",
        "DISPERSION", "BANDA", "ANCHO", "AMPLIFICADOR", "EMISOR", "RECEPTOR", "CABLE", "NUCLEO", "REVESTIMIENTO", "LONGITUD",
        "ONDA", "TRANSMISION", "SEÑAL", "LUMINOSA", "SPLITTER", "FUSION", "EMPALME", "FTTH", "OLT", "ONT"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_tematica);

        String nombre = getIntent().getStringExtra("nombre");
        TextView tvBienvenido = findViewById(R.id.tvBienvenidoTematica);
        tvBienvenido.setText("Bienvenido " + nombre);

        ImageView btnUp = findViewById(R.id.btnUp);
        btnUp.setOnClickListener(v -> finish());

        LinearLayout opcionesLayout = findViewById(R.id.opcionesLayout);

        // Temáticas: Redes, Ciberseguridad, Fibra óptica
        TextView tvRedes = (TextView) opcionesLayout.getChildAt(0);
        TextView tvCiberseguridad = (TextView) opcionesLayout.getChildAt(1);
        TextView tvFibraOptica = (TextView) opcionesLayout.getChildAt(2);

        tvRedes.setOnClickListener(v -> seleccionarTematica("Redes", palabrasRedes, nombre));
        tvCiberseguridad.setOnClickListener(v -> seleccionarTematica("Ciberseguridad", palabrasCiberseguridad, nombre));
        tvFibraOptica.setOnClickListener(v -> seleccionarTematica("Fibra óptica", palabrasFibraOptica, nombre));
    }

    private void seleccionarTematica(String tematica, String[] palabras, String nombre) {
        // Selecciona una palabra aleatoria de la temática
        Random random = new Random();
        String palabraSeleccionada = palabras[random.nextInt(palabras.length)];

        // Envía la temática y la palabra a JuegoActivity
        Intent intent = new Intent(this, JuegoActivity.class);
        intent.putExtra("tematica", tematica);
        intent.putExtra("palabraOculta", palabraSeleccionada);
        intent.putExtra("nombre", nombre);
        startActivity(intent);
    }
}