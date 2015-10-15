package edu.fatec.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.gqueiroz.androidtcc.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import edu.fatec.model.Materia;

public class NovaDuvidaTestActivity extends Activity {
    private Spinner spinnerMaterias;
    private SharedPreferences SharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_duvida_test);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPref = getPreferences(MODE_PRIVATE);

        String todasMaterias = SharedPref.getString("jsonMaterias","");
        List<String> materias =  new ArrayList<>();

        if(todasMaterias.length()>1){
            Type listType = new TypeToken<ArrayList<Materia>>(){}.getType();
            List<Materia> materiasJson = new Gson().fromJson(todasMaterias, listType);

            for(Materia m : materiasJson)
                materias.add(m.getMateria());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, materias);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerMaterias = (Spinner) findViewById(R.id.spinnerMaterias);
            spinnerMaterias.setAdapter(adapter);
        } else {
            materias.add("Não foi possível carregar as matérias");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, materias);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerMaterias = (Spinner) findViewById(R.id.spinnerMaterias);
            spinnerMaterias.setAdapter(adapter);
        }

    }

}
