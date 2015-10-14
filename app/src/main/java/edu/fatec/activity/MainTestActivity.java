package edu.fatec.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import edu.fatec.model.Duvida;
import edu.fatec.util.DuvidaAdapter;

public class MainTestActivity extends Activity {

    private DuvidaAdapter duvidaAdapter;
    private LinearLayout infoDuvida;
    private FloatingActionButton novaDuvida;
    private TextView textInfoDuvida;
    private ProgressBar progressBar;

    private SharedPreferences SharedPref;
    private SharedPreferences.Editor SharedPrefEdit;

    private String server;
    private List<Duvida> jsonDuvidas;

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);

        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        addDrawerItems();
        setupDrawer();

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        SharedPref = getPreferences(MODE_PRIVATE);
        SharedPrefEdit = SharedPref.edit();

        infoDuvida = (LinearLayout) findViewById(R.id.infoDuvida);
        textInfoDuvida = (TextView) findViewById(R.id.textInfoDuvida);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        server = getString(R.string.wstcc);

        String url = server + "wstcc/duvidas/buscarDuvidas";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Type listType = new TypeToken<ArrayList<Duvida>>() {
                        }.getType();
                        jsonDuvidas.clear();
                        jsonDuvidas = new Gson().fromJson(response, listType);
                        duvidaAdapter.swap(jsonDuvidas);
                        SharedPrefEdit.putString("jsonDuvidas", response);
                        SharedPrefEdit.commit();

                        Toast.makeText(getApplicationContext(), "Lista atualizada.", Toast.LENGTH_SHORT).show();
                        infoDuvida.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Erro ao se conectar com o WebService. Tente Novamente.", Toast.LENGTH_SHORT).show();
                infoDuvida.setBackgroundColor(Color.parseColor("#ff4444"));
                textInfoDuvida.setText("Não foi possível se conectar com o servidor");
                progressBar.setVisibility(View.GONE);
            }
        });
        queue.add(stringRequest);

        RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(MainTestActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        String sharedDuvidas = SharedPref.getString("jsonDuvidas", "");
        if (sharedDuvidas.length() > 1) {
            Type listType = new TypeToken<ArrayList<Duvida>>() {
            }.getType();
            jsonDuvidas = new Gson().fromJson(sharedDuvidas, listType);
            duvidaAdapter = new DuvidaAdapter(jsonDuvidas);
            recList.setAdapter(duvidaAdapter);
        } else {
            jsonDuvidas = new ArrayList<>();
            duvidaAdapter = new DuvidaAdapter(jsonDuvidas);
            recList.setAdapter(duvidaAdapter);
        }

        novaDuvida = (FloatingActionButton) findViewById(R.id.novaDuvida);
        novaDuvida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainTestActivity.this, NovaDuvidaTestActivity.class);
                startActivity(i);
            }
        });
    }

    private void addDrawerItems() {
        String[] osArray = {"Meu Perfil", "Minhas Matérias", "Configurações"};
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(id==0){
                    Intent i = new Intent(MainTestActivity.this, LoginActivity.class);
                    startActivity(i);
                }
                if(id==1){
                    Intent i = new Intent(MainTestActivity.this, MateriaTestActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
