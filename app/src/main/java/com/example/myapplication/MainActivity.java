package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView teleAhorcadoTitle;
    private EditText nameEditText;
    private Button playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        teleAhorcadoTitle = findViewById(R.id.tvTeleAhorcado);
        nameEditText = findViewById(R.id.etNombre);
        playButton = findViewById(R.id.btnJugar);

        playButton.setEnabled(false);

        nameEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                playButton.setEnabled(s.length() > 0);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        playButton.setOnClickListener(v -> {
            String nombre = nameEditText.getText().toString();
            Intent intent = new Intent(MainActivity.this, SeleccionTematicaActivity.class);
            intent.putExtra("nombre", nombre);
            startActivity(intent);
        });

        registerForContextMenu(teleAhorcadoTitle);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Cambiar color");
        menu.add(0, v.getId(), 0, "Azul");
        menu.add(0, v.getId(), 1, "Verde");
        menu.add(0, v.getId(), 2, "Rojo");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        String title = item.getTitle().toString();
        if (title.equals("Azul")) {
            teleAhorcadoTitle.setTextColor(Color.BLUE);
        } else if (title.equals("Verde")) {
            teleAhorcadoTitle.setTextColor(Color.GREEN);
        } else if (title.equals("Rojo")) {
            teleAhorcadoTitle.setTextColor(Color.RED);
        }
        return true;
    }
}