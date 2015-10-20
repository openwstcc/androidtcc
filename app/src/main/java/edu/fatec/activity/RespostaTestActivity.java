package edu.fatec.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
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
import edu.fatec.util.RespostaAdapter;

public class RespostaTestActivity extends Activity {
    private String server;
    private String jsonDuvida;
    private String idDuvida;

    private TextView conteudoDuvida;
    private LinearLayout backgroundDuvida;
    private RecyclerView recyclerView;
    private RespostaAdapter respostaAdapter;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swiperRefreshResposta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resposta_test);

        findViewsByID();

        Bundle extras = getIntent().getExtras();

        jsonDuvida = "";
        if (extras != null)
            jsonDuvida = extras.getString("duvida");

        Duvida d = new Gson().fromJson(jsonDuvida, Duvida.class);

        getActionBar().setTitle(d.getTitulo());
        getActionBar().setDisplayHomeAsUpEnabled(true);

        conteudoDuvida.setText(d.getConteudo());
        idDuvida = "{idDuvida:"+d.getIdDuvida()+"}";

        volleyRequest();
        swiperRefreshResposta.setRefreshing(true);

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(RespostaTestActivity.this);
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
                    RespostaTestActivity.this.getActionBar().hide();
                } else if (currentFirstVisibleItem < this.mLastFirstVisibleItem) {
                    RespostaTestActivity.this.getActionBar().show();
                }

                this.mLastFirstVisibleItem = currentFirstVisibleItem;
            }
        });

        swiperRefreshResposta.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                volleyRequest();
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

    public void findViewsByID(){
        backgroundDuvida = (LinearLayout)findViewById(R.id.backgroundDuvidaResposta);
        recyclerView = (RecyclerView) findViewById(R.id.cardList);
        conteudoDuvida = (TextView)findViewById(R.id.textConteudoDuvida);
        swiperRefreshResposta = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshResposta);
    }

    public void volleyRequest(){
        server = getString(R.string.wstcc);

        String url = server + "wstcc/respostas/buscarRespostas";

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
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Erro ao buscar Respostas.", Toast.LENGTH_SHORT).show();
                backgroundDuvida.setBackgroundColor(Color.parseColor("#FFA726"));
                swiperRefreshResposta.setRefreshing(false);
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return idDuvida.getBytes();
            }
        };
        queue.add(stringRequest);

    }
}
