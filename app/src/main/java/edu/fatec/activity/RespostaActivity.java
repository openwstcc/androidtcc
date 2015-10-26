package edu.fatec.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import edu.fatec.json.JsonResposta;
import edu.fatec.model.Duvida;
import edu.fatec.model.Resposta;
import edu.fatec.model.Usuario;
import edu.fatec.util.RespostaAdapter;

public class RespostaActivity extends Activity {
    private Duvida duvida;

    private TextView conteudoDuvida;
    private EditText resposta;
    private LinearLayout backgroundDuvida;
    private RecyclerView recyclerView;
    private RespostaAdapter respostaAdapter;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swiperRefreshResposta;
    private FloatingActionButton inserirResposta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resposta);

        findViewsByID();

        Bundle extras = getIntent().getExtras();

        String jsonDuvida = "";
        if (extras != null)
            jsonDuvida = extras.getString("duvida");

        duvida = new Gson().fromJson(jsonDuvida, Duvida.class);

        conteudoDuvida.setText(duvida.getConteudo());

        getActionBar().setTitle(duvida.getTitulo());
        getActionBar().setDisplayHomeAsUpEnabled(true);

        swiperRefreshResposta.post(new Runnable() {
            @Override
            public void run() {
                swiperRefreshResposta.setRefreshing(true);
                volleyBuscarDuvidas();
            }
        });

        swiperRefreshResposta.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                volleyBuscarDuvidas();
            }
        });

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(RespostaActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        List<JsonResposta> respostas = new ArrayList<>();
        respostaAdapter = new RespostaAdapter(respostas);
        recyclerView.setAdapter(respostaAdapter);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int mLastFirstVisibleItem = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                final int currentFirstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                if (currentFirstVisibleItem > this.mLastFirstVisibleItem) {
                    RespostaActivity.this.getActionBar().hide();
                } else if (currentFirstVisibleItem < this.mLastFirstVisibleItem) {
                    RespostaActivity.this.getActionBar().show();
                }

                this.mLastFirstVisibleItem = currentFirstVisibleItem;
            }
        });

        inserirResposta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volleyNovaResposta();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void findViewsByID() {
        resposta = (EditText) findViewById(R.id.resposta);
        inserirResposta = (FloatingActionButton) findViewById(R.id.inserirResposta);
        backgroundDuvida = (LinearLayout) findViewById(R.id.backgroundDuvidaResposta);
        recyclerView = (RecyclerView) findViewById(R.id.cardList);
        conteudoDuvida = (TextView) findViewById(R.id.textConteudoDuvida);
        swiperRefreshResposta = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshResposta);
    }

    public void volleyBuscarDuvidas() {
        String server = getString(R.string.wstcc);
        String url = server + "respostas/buscarRespostas";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Type listType = new TypeToken<ArrayList<JsonResposta>>() {
                        }.getType();
                        List<JsonResposta> respostasJson = new Gson().fromJson(response, listType);
                        respostaAdapter.swap(respostasJson);
                        backgroundDuvida.setBackgroundColor(Color.parseColor("#00838F"));
                        swiperRefreshResposta.setRefreshing(false);
                        resposta.setEnabled(true);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Erro ao buscar Respostas.", Toast.LENGTH_SHORT).show();
                backgroundDuvida.setBackgroundColor(Color.parseColor("#FFA726"));
                swiperRefreshResposta.setRefreshing(false);
                resposta.setEnabled(false);
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                String idDuvida = "{idDuvida:" + duvida.getIdDuvida() + "}";
                return idDuvida.getBytes();
            }
        };
        queue.add(stringRequest);
    }

    public Resposta novaResposta() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String sharedUsuario = sharedPref.getString("jsonUsuario", "");
        Usuario usuario = new Gson().fromJson(sharedUsuario, Usuario.class);

        Resposta r = new Resposta();

        r.setResposta(resposta.getText().toString());
        r.setIdUsuario(usuario.getIdUsuario());
        r.setCriador(usuario.getNome());
        r.setIdDuvida(duvida.getIdDuvida());
        r.getDataCriacao();

        return r;
    }

    public void volleyNovaResposta() {
        String server = getString(R.string.wstcc);
        String url = server + "respostas/adicionarResposta";

        String resposta = new Gson().toJson(novaResposta());
        Toast.makeText(getApplicationContext(),"JSON: "+resposta,Toast.LENGTH_LONG).show();

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //On Response
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //On error
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                String resposta = new Gson().toJson(novaResposta());
                return resposta.getBytes();
            }
        };
        queue.add(stringRequest);
    }

}
