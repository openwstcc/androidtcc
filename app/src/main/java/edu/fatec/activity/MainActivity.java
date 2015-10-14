package edu.fatec.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.gqueiroz.androidtcc.R;

public class MainActivity extends Activity {
    private Button materias;
    private Button usuario;
    private Button duvida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        materias = (Button)findViewById(R.id.materias);
        materias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MateriaTestActivity.class);
                startActivity(i);
            }
        });

        usuario = (Button)findViewById(R.id.usuario);
        usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

        duvida = (Button)findViewById(R.id.duvidas);
        duvida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,DuvidaTestActivity.class);
                startActivity(i);
            }
        });
    }

}
