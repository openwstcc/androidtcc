package edu.fatec.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
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
    private RespostaAdapter respostaAdapter;

    private TextView conteudoDuvida;
    private RecyclerView recList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resposta_test);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();

        jsonDuvida = "";
        if (extras != null)
            jsonDuvida = extras.getString("duvida");

        Duvida d = new Gson().fromJson(jsonDuvida, Duvida.class);

        getActionBar().setTitle(d.getTitulo());
        conteudoDuvida = (TextView)findViewById(R.id.textConteudoDuvida);
        conteudoDuvida.setText(d.getConteudo());
        idDuvida = "{idDuvida:"+d.getIdDuvida()+"}";

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
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Erro ao buscar Respostas.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return idDuvida.getBytes();
            }
        };
        queue.add(stringRequest);

        recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        final LinearLayoutManager llm = new LinearLayoutManager(RespostaTestActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        List<JsonResposta> respostas = new ArrayList<>();
        respostaAdapter = new RespostaAdapter(respostas);
        recList.setAdapter(respostaAdapter);

        this.recList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int mLastFirstVisibleItem = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                final int currentFirstVisibleItem = llm.findFirstVisibleItemPosition();

                if (currentFirstVisibleItem > this.mLastFirstVisibleItem) {
                    RespostaTestActivity.this.getActionBar().hide();
                } else if (currentFirstVisibleItem < this.mLastFirstVisibleItem) {
                    RespostaTestActivity.this.getActionBar().show();
                }

                this.mLastFirstVisibleItem = currentFirstVisibleItem;
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
}
