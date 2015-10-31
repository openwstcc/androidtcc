package edu.fatec.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.gqueiroz.androidtcc.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import edu.fatec.json.JsonDuvida;
import edu.fatec.model.Duvida;
import edu.fatec.model.Materia;
import edu.fatec.model.Usuario;

public class NovaDuvidaActivity extends Activity {
    private EditText tituloNovaDuvida;
    private EditText conteudoNovaDuvida;
    private EditText tagsNovaDuvida;
    private Spinner spinnerMaterias;
    private FloatingActionButton inserirNovaDuvida;

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_duvida);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        findViewsById();
        setSpinnerAdapter();

        inserirNovaDuvida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void findViewsById() {
        spinnerMaterias = (Spinner) findViewById(R.id.spinnerMaterias);
        tituloNovaDuvida = (EditText) findViewById(R.id.tituloNovaDuvida);
        conteudoNovaDuvida = (EditText) findViewById(R.id.conteudoDuvida);
        tagsNovaDuvida = (EditText) findViewById(R.id.tagsNovaDuvida);
        inserirNovaDuvida = (FloatingActionButton) findViewById(R.id.inserirNovaDuvida);
    }

    public void setSpinnerAdapter(){
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String todasMaterias = sharedPref.getString("jsonMaterias", "");
        List<String> materias = new ArrayList<>();

        if (todasMaterias.length() > 1) {
            Type listType = new TypeToken<ArrayList<Materia>>() {
            }.getType();
            List<Materia> materiasJson = new Gson().fromJson(todasMaterias, listType);

            for (Materia m : materiasJson)
                materias.add(m.getMateria());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, materias);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerMaterias.setAdapter(adapter);
        } else {
            materias.add("Não foi possível carregar as matérias");
            ArrayAdapter<String> adapter = new ArrayAdapter<String> (
                    this, android.R.layout.simple_spinner_item, materias);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerMaterias.setAdapter(adapter);
        }
    }

    public JsonDuvida novaDuvida() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String sharedUsuario = sharedPref.getString("jsonUsuario", "");
        Usuario usuario = new Gson().fromJson(sharedUsuario, Usuario.class);

        JsonDuvida d = new JsonDuvida();
        d.setIdUsuario(usuario.getIdUsuario());
        d.setTitulo(tituloNovaDuvida.getText().toString());
        d.setConteudo(conteudoNovaDuvida.getText().toString());
        d.setTags(tagsNovaDuvida.getText().toString().split("\\s+"));


        return null;
    }
}
