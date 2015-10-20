package edu.fatec.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    //View Objects
    private DuvidaAdapter duvidaAdapter;
    private LinearLayout infoDuvida;
    private FloatingActionButton novaDuvida;
    private TextView textInfoDuvida;
    private ProgressBar progressBar;

    private ListView drawerList;
    private ArrayAdapter<String> arrayAdapter;
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
        setContentView(R.layout.activity_main_test);

        findViewsByID();

        addDrawerItems();
        setupDrawer();
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPref = getPreferences(MODE_PRIVATE);
        sharedPrefEdit = sharedPref.edit();

        volleyRequest();

        recycleView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(MainTestActivity.this);
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
                Intent i = new Intent(MainTestActivity.this, NovaDuvidaTestActivity.class);
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
                    MainTestActivity.this.getActionBar().hide();
                } else if (currentFirstVisibleItem < this.mLastFirstVisibleItem) {
                    MainTestActivity.this.getActionBar().show();
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
        String[] osArray = {"Meu Perfil", "Minhas Matérias", "Configurações"};
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, osArray);
        drawerList.setAdapter(arrayAdapter);

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id == 0) {
                    Intent i = new Intent(MainTestActivity.this, LoginActivity.class);
                    startActivity(i);
                }
                if (id == 1) {
                    Intent i = new Intent(MainTestActivity.this, MateriaTestActivity.class);
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

        String server = getString(R.string.wstcc);
        String url = server + "wstcc/duvidas/buscarDuvidas";

        queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Type listType = new TypeToken<ArrayList<Duvida>>() {
                        }.getType();
                        jsonDuvidas.clear();
                        jsonDuvidas = new Gson().fromJson(response, listType);
                        duvidaAdapter.swap(jsonDuvidas);
                        sharedPrefEdit.putString("jsonDuvidas", response);
                        sharedPrefEdit.commit();

                        //Toast.makeText(getApplicationContext(), "Lista atualizada.", Toast.LENGTH_SHORT).show();
                        infoDuvida.setVisibility(View.GONE);
                        swipeRefreshDuvida.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), "Erro ao se conectar com o WebService. Tente Novamente.", Toast.LENGTH_SHORT).show();
                infoDuvida.setBackgroundColor(Color.parseColor("#ff4444"));
                textInfoDuvida.setText("Não foi possível se conectar com o servidor");
                progressBar.setVisibility(View.GONE);
                swipeRefreshDuvida.setRefreshing(false);
            }
        });

        queue.add(stringRequest);
        stringRequest.setTag(TAG);
    }
}
