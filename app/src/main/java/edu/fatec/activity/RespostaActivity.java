package edu.fatec.activity;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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

public class RespostaActivity extends AppCompatActivity {
    private Duvida duvida;
    private boolean conexao = false;

    private Toolbar toolbar;

    private TextView conteudoDuvida;
    private EditText resposta;
    private LinearLayout backgroundDuvida;
    private RecyclerView recyclerView;
    private RespostaAdapter respostaAdapter;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swiperRefreshResposta;
    private FloatingActionButton inserirResposta;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor sharedPrefEdit;


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

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor SharedPrefEdit = sharedPref.edit();
        SharedPrefEdit.putString("jsonDuvidaTemp", jsonDuvida);
        SharedPrefEdit.commit();

        conteudoDuvida.setText(duvida.getConteudo());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(duvida.getTitulo());

        showViewRefresh();

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

        inserirResposta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validaResposta(resposta))
                    return;

                if(!getConexao()){
                    Toast.makeText(getApplicationContext(), "Verifique a conexão com a internet para realizar uma nova resposta.", Toast.LENGTH_SHORT).show();
                    return;
                }

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

    private void findViewsByID() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        resposta = (EditText) findViewById(R.id.resposta);
        inserirResposta = (FloatingActionButton) findViewById(R.id.inserirResposta);
        backgroundDuvida = (LinearLayout) findViewById(R.id.backgroundDuvidaResposta);
        recyclerView = (RecyclerView) findViewById(R.id.cardList);
        conteudoDuvida = (TextView) findViewById(R.id.textConteudoDuvida);
        swiperRefreshResposta = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshResposta);
    }

    private void showViewRefresh(){
        swiperRefreshResposta.post(new Runnable() {
            @Override
            public void run() {
                swiperRefreshResposta.setRefreshing(true);
                volleyBuscarDuvidas();
            }
        });
    }

    private void volleyBuscarDuvidas() {
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
                        backgroundDuvida.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        swiperRefreshResposta.setRefreshing(false);
                        resposta.setEnabled(true);
                        inserirResposta.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
                        setConexao(true);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Erro ao buscar Respostas.", Toast.LENGTH_SHORT).show();
                swiperRefreshResposta.setRefreshing(false);
                resposta.setEnabled(false);
                inserirResposta.setBackgroundTintList(getResources().getColorStateList(R.color.inactive));
                setConexao(false);
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String sharedUsuario = sharedPref.getString("jsonUsuario", "");
                Usuario usuario = new Gson().fromJson(sharedUsuario, Usuario.class);
                String idDuvida = "{idDuvida:" + duvida.getIdDuvida() +","+"usuarioLogado:"+usuario.getIdUsuario()+"}";
                return idDuvida.getBytes();
            }
        };
        queue.add(stringRequest);
    }

    private Resposta novaResposta() {
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

    private void volleyNovaResposta() {
        String server = getString(R.string.wstcc);
        String url = server + "respostas/adicionarResposta";

        RequestQueue queue = Volley.newRequestQueue(this);
        Drawable loop = getResources().getDrawable(R.drawable.ic_loop_white_24dp);
        inserirResposta.setImageDrawable(loop);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Resposta enviada com sucesso!", Toast.LENGTH_SHORT).show();
                        Drawable send = getResources().getDrawable(R.drawable.ic_send_white_24dp);
                        inserirResposta.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
                        inserirResposta.setImageDrawable(send);
                        resposta.setText("");
                        showViewRefresh();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Erro ao enviar resposta. Tente novamente", Toast.LENGTH_SHORT).show();
                inserirResposta.setBackgroundTintList(getResources().getColorStateList(R.color.colorFail));
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

    private boolean validaResposta(View v) {
        EditText resposta = (EditText) v;
        if (TextUtils.isEmpty(resposta.getText())) {
            resposta.setError("Insira um conteúdo para a resposta");
            resposta.setFocusable(true);
            return false;
        } else if (resposta.getText().length() < 5) {
            resposta.setError("Resposta inválida. Tamanho mínimo de 5 caracteres.");
            resposta.setFocusable(true);
            return false;
        } else
            return true;
    }

    private boolean getConexao() {
        return conexao;
    }

    private void setConexao(boolean conexao) {
        this.conexao = conexao;
    }
}
