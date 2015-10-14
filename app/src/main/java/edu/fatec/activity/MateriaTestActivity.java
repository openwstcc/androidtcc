package edu.fatec.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class MateriaTestActivity extends Activity {
    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;

    private SharedPreferences SharedPref;
    private SharedPreferences.Editor SharedPrefEdit;

    private String server;
    private List<Materia> materiasJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materia_test);

        expListView = (ExpandableListView) findViewById(R.id.listaMaterias);

        SharedPref = getPreferences(MODE_PRIVATE);
        SharedPrefEdit = SharedPref.edit();

        String todasMaterias = SharedPref.getString("jsonMaterias","");
        if(todasMaterias.length()<1){
            server = getString(R.string.wstcc);

            RequestQueue queue = Volley.newRequestQueue(this);

            String url = server + "wstcc/materias/buscarMaterias";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Type listType = new TypeToken<ArrayList<Materia>>(){}.getType();
                            materiasJson = new Gson().fromJson(response, listType);
                            Toast.makeText(getApplicationContext(), "Materias atualizadas.", Toast.LENGTH_SHORT).show();
                            listAdapter = new ExpandableListAdapter(MateriaTestActivity.this, materiasJson);
                            expListView.setAdapter(listAdapter);
                            SharedPrefEdit.putString("jsonMaterias", response);
                            SharedPrefEdit.commit();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Erro ao se conectar com o WebService. Tente Novamente.", Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(stringRequest);
        } else {
            Type listType = new TypeToken<ArrayList<Materia>>(){}.getType();
            materiasJson = new Gson().fromJson(todasMaterias, listType);
            listAdapter = new ExpandableListAdapter(MateriaTestActivity.this, materiasJson);
            expListView.setAdapter(listAdapter);
        }

    }

}
