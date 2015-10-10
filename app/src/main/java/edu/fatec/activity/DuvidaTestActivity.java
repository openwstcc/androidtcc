package edu.fatec.activity;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import edu.fatec.model.Duvida;
import edu.fatec.util.DuvidaAdapter;

public class DuvidaTestActivity extends Activity {
    private String server;
    private DuvidaAdapter duvidaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duvida_test);

        server = getString(R.string.wstcc);

        String url = server + "wstcc/duvidas/buscarDuvidas";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Type listType = new TypeToken<ArrayList<Duvida>>() {
                        }.getType();
                        List<Duvida> duvidasJson = new Gson().fromJson(response, listType);
                        duvidaAdapter.swap(duvidasJson);
                        Toast.makeText(getApplicationContext(), "Lista atualizada.", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Erro ao se conectar com o WebService. Tente Novamente.", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);

        RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(DuvidaTestActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        List<Duvida> duvidas = new ArrayList<>();
        duvidaAdapter = new DuvidaAdapter(duvidas);
        recList.setAdapter(duvidaAdapter);
    }
}