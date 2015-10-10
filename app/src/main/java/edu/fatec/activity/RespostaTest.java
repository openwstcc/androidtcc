package edu.fatec.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
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

public class RespostaTest extends Activity {
    private String server;
    private String jsonDuvida;
    private String idDuvida;
    private RespostaAdapter respostaAdapter;

    private TextView conteudoDuvida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resposta_test);

        Bundle extras = getIntent().getExtras();

        jsonDuvida = "";
        if (extras != null)
            jsonDuvida = extras.getString("duvida");

        Duvida d = new Gson().fromJson(jsonDuvida, Duvida.class);

        this.setTitle(d.getTitulo());
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

        RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(RespostaTest.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        List<JsonResposta> respostas = new ArrayList<>();
        respostaAdapter = new RespostaAdapter(respostas);
        recList.setAdapter(respostaAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_resposta_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
