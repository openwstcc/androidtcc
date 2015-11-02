package edu.fatec.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import edu.fatec.model.Duvida;
import edu.fatec.model.Usuario;
import edu.fatec.util.DuvidaAdapter;

public class MainActivity extends Activity {
    //View Objects
    private DuvidaAdapter duvidaAdapter;
    private LinearLayout infoDuvida;
    private FloatingActionButton novaDuvida;
    private TextView textInfoDuvida;
    private ProgressBar progressBar;

    private TextView infoNomeUsuario;
    private TextView infoEmailUsuario;
    private ListView drawerList;
    private RecyclerView recycleView;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshDuvida;

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;

    //SharedPreferences
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor sharedPrefEdit;

    //Volley Objects
    public static final String TAG = "duvidas";
    private RequestQueue queue;

    private List<Duvida> jsonDuvidas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewsByID();

        addDrawerItems();
        setupDrawer();
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);


        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sharedPrefEdit = sharedPref.edit();

        volleyRequest();

        recycleView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView.setLayoutManager(linearLayoutManager);

        String sharedDuvidas = sharedPref.getString("jsonDuvidas", "");
        if (sharedDuvidas.length() > 1) {
            Type listType = new TypeToken<ArrayList<Duvida>>() {
            }.getType();
            jsonDuvidas = new Gson().fromJson(sharedDuvidas, listType);
            duvidaAdapter = new DuvidaAdapter(jsonDuvidas);
            recycleView.setAdapter(duvidaAdapter);
        } else {
            jsonDuvidas = new ArrayList<>();
            duvidaAdapter = new DuvidaAdapter(jsonDuvidas);
            recycleView.setAdapter(duvidaAdapter);
        }

        novaDuvida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NovaDuvidaActivity.class);
                startActivity(i);
            }
        });

        recycleView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int mLastFirstVisibleItem = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                final int currentFirstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                if (currentFirstVisibleItem > this.mLastFirstVisibleItem) {
                    MainActivity.this.getActionBar().hide();
                } else if (currentFirstVisibleItem < this.mLastFirstVisibleItem) {
                    MainActivity.this.getActionBar().show();
                }

                this.mLastFirstVisibleItem = currentFirstVisibleItem;
            }
        });

        swipeRefreshDuvida.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                volleyRequest();
            }
        });
    }

    private void addDrawerItems() {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String sharedUsuario = sharedPref.getString("jsonUsuario", "");
        Usuario usuario = new Gson().fromJson(sharedUsuario, Usuario.class);
        infoNomeUsuario.setText(usuario.getNome() + " " + usuario.getSobrenome());
        infoEmailUsuario.setText(usuario.getEmail());

        String[] osArray = {"Meu Perfil", "Minhas Matérias", "Configurações"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, osArray);
        drawerList.setAdapter(arrayAdapter);


        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id == 0) {
                    Intent i = new Intent(MainActivity.this, NovoUsuarioActivity.class);

                }
                if (id == 1) {
                    Intent i = new Intent(MainActivity.this, MateriaActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    private void setupDrawer() {
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
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

        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings)
            return true;

        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (queue != null)
            queue.cancelAll(TAG);
    }

    public void findViewsByID() {
        novaDuvida = (FloatingActionButton) findViewById(R.id.novaDuvida);
        recycleView = (RecyclerView) findViewById(R.id.recycleViewDuvidas);
        infoNomeUsuario = (TextView) findViewById(R.id.infoNomeUsuario);
        infoEmailUsuario = (TextView) findViewById(R.id.infoEmailUsuario);
        drawerList = (ListView) findViewById(R.id.drawerList);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        infoDuvida = (LinearLayout) findViewById(R.id.infoDuvida);
        textInfoDuvida = (TextView) findViewById(R.id.textInfoDuvida);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        swipeRefreshDuvida = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshDuvida);
    }

    public void volleyRequest() {
        infoDuvida.setBackgroundColor(Color.parseColor("#FFA726"));
        textInfoDuvida.setText("Atualizando informações de dúvidas");
        infoDuvida.setVisibility(View.VISIBLE);

        String server = getString(R.string.wstcc);
        String url = server + "duvidas/buscarDuvidas";

        queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null)
                            return;
                        progressBar.setVisibility(View.GONE);

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                infoDuvida.setVisibility(View.GONE);
                                textInfoDuvida.setVisibility(View.GONE);
                                duvidaAdapter.notifyDataSetChanged();
                            }
                        }, 20);

                        Type listType = new TypeToken<ArrayList<Duvida>>() {
                        }.getType();
                        jsonDuvidas.clear();
                        jsonDuvidas = new Gson().fromJson(response, listType);
                        sharedPrefEdit.putString("jsonDuvidas", response);
                        duvidaAdapter.swap(jsonDuvidas);
                        sharedPrefEdit.commit();

                        infoDuvida.setBackgroundColor(Color.parseColor("#00E676"));
                        textInfoDuvida.setText("Lista de dúvidas atualizadas");
                        swipeRefreshDuvida.setRefreshing(false);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                infoDuvida.setBackgroundColor(Color.parseColor("#ff4444"));
                textInfoDuvida.setText("Não foi possível se conectar com o servidor");
                progressBar.setVisibility(View.GONE);
                swipeRefreshDuvida.setRefreshing(false);
            }
        });
        queue.add(stringRequest);
        stringRequest.setTag(TAG);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
