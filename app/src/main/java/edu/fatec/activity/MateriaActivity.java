package edu.fatec.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gqueiroz.androidtcc.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Type;

import edu.fatec.model.Materia;
import edu.fatec.util.ExpandableListAdapter;

public class MateriaActivity extends Activity {
    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private LinearLayout infoMateria;
    private TextView textInfoMateria;
    private ProgressBar progressBarMateria;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor SharedPrefEdit;

    private List<Materia> materiasJson = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materia);

        findViewsById();

        expListView = (ExpandableListView) findViewById(R.id.listaMaterias);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPrefEdit = sharedPref.edit();

        listAdapter = new ExpandableListAdapter(MateriaActivity.this, materiasJson);
        expListView.setAdapter(listAdapter);

        volleyBuscarMateriasUsuario();
    }

    public void findViewsById(){
        infoMateria = (LinearLayout) findViewById(R.id.infoMateria);
        textInfoMateria = (TextView) findViewById(R.id.textInfoMateria);
        progressBarMateria = (ProgressBar) findViewById(R.id.progressBarMateria);
    }

    public void volleyBuscarMateriasUsuario() {
        final String sharedUsuario = sharedPref.getString("jsonUsuario", "");

        RequestQueue queue = Volley.newRequestQueue(MateriaActivity.this);

        String server = getString(R.string.wstcc);
        String url = server + "materias/buscarMateriasUsuario";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Type listType = new TypeToken<ArrayList<Materia>>(){}.getType();
                        List<Materia> materiasUsuario = new Gson().fromJson(response, listType);

                        listAdapter.swap(materiasUsuario);
                        textInfoMateria.setText(response);
                        infoMateria.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                infoMateria.setBackgroundColor(Color.parseColor("#ff4444"));
                textInfoMateria.setText("Não foi possível se conectar com o servidor");
                progressBarMateria.setVisibility(View.GONE);
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return sharedUsuario.getBytes();
            }
        };
        queue.add(stringRequest);
    }


}
