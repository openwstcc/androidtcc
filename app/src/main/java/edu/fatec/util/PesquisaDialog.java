package edu.fatec.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gqueiroz.androidtcc.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import edu.fatec.activity.MainActivity;
import edu.fatec.json.JsonPesquisa;
import edu.fatec.model.Duvida;

public class PesquisaDialog extends Dialog {
    private MainActivity c;
    private EditText conteudoPesquisa;
    private CheckBox checkTitulo;
    private CheckBox checkConteudo;
    private CheckBox checkMateria;
    private CheckBox checkTag;
    private Button refinarPesquisa;
    private Button novaPesquisa;
    private Button cancelaPesquisa;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor sharedPrefEdit;

    private ProgressBar progressBar;

    private boolean visible = false;

    public PesquisaDialog(MainActivity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_pesquisa);

        findViewsById();

        checkTitulo.setVisibility(View.GONE);
        checkConteudo.setVisibility(View.GONE);
        checkMateria.setVisibility(View.GONE);
        checkTag.setVisibility(View.GONE);

        refinarPesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!visible) {
                    checkTitulo.setVisibility(View.VISIBLE);
                    checkConteudo.setVisibility(View.VISIBLE);
                    checkMateria.setVisibility(View.VISIBLE);
                    checkTag.setVisibility(View.VISIBLE);
                    visible = true;
                } else {
                    checkTitulo.setVisibility(View.GONE);
                    checkConteudo.setVisibility(View.GONE);
                    checkMateria.setVisibility(View.GONE);
                    checkTag.setVisibility(View.GONE);
                    visible = false;
                }
            }
        });

        cancelaPesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });

        novaPesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volleyRequest();
            }
        });

    }

    private void closeDialog() {
        this.dismiss();
    }

    private void findViewsById() {
        conteudoPesquisa = (EditText) findViewById(R.id.conteudoPesquisa);
        checkTitulo = (CheckBox) findViewById(R.id.checkTitulo);
        checkConteudo = (CheckBox) findViewById(R.id.checkConteudo);
        checkMateria = (CheckBox) findViewById(R.id.checkMateria);
        checkTag = (CheckBox) findViewById(R.id.checkTag);
        refinarPesquisa = (Button) findViewById(R.id.refinarPesquisa);
        novaPesquisa = (Button) findViewById(R.id.novaPesquisa);
        cancelaPesquisa = (Button) findViewById(R.id.cancelaPesquisa);
        progressBar = (ProgressBar) findViewById(R.id.progressBarPesquisa);

    }

    private JsonPesquisa jsonPesquisa() {
        JsonPesquisa jsonPesquisa = new JsonPesquisa();
        if (conteudoPesquisa.getText().toString().startsWith("\"") && conteudoPesquisa.getText().toString().endsWith("\""))
            jsonPesquisa.setPesquisa(new String[]{conteudoPesquisa.getText().toString()});
        else
            jsonPesquisa.setPesquisa(conteudoPesquisa.getText().toString().split("\\s+"));

        jsonPesquisa.setTitulo(checkTitulo.isChecked());
        jsonPesquisa.setConteudo(checkConteudo.isChecked());
        jsonPesquisa.setMateria(checkMateria.isChecked());
        jsonPesquisa.setTag(checkTag.isChecked());

        return jsonPesquisa;
    }

    private void volleyRequest() {
        progressBar.setVisibility(View.VISIBLE);
        String server = c.getString(R.string.wstcc);

        RequestQueue queue = Volley.newRequestQueue(c);
        String url = server + "duvidas/buscarDuvidasFiltro";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null)
                            closeDialog();

                        Type listType = new TypeToken<ArrayList<Duvida>>() {}.getType();
                        List<Duvida> jsonDuvidas = new Gson().fromJson(response, listType);
                        c.posPesquisa(conteudoPesquisa.getText().toString(), jsonDuvidas);
                        closeDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(c.getApplicationContext(), "Não foi possível realizar a pesquisa.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                Gson gson = new GsonBuilder().create();
                String body = gson.toJson(jsonPesquisa());
                return body.getBytes();
            }
        };
        queue.add(stringRequest);
    }
}
