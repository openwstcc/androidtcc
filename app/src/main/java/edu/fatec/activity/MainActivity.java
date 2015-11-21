package edu.fatec.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import edu.fatec.model.Duvida;
import edu.fatec.model.Usuario;
import edu.fatec.util.DuvidaAdapter;
import edu.fatec.util.PesquisaDialog;

public class MainActivity extends AppCompatActivity {
    //View Objects
    private LinearLayout infoDuvida;
    private FloatingActionButton novaDuvida;
    private ProgressBar progressBar;
    private TextView textInfoDuvida;
    private TextView infoNomeUsuario;
    private TextView infoEmailUsuario;
    private ListView drawerList;
    private DuvidaAdapter duvidaAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshDuvida;
    private PesquisaDialog pesquisaDialog;

    //Action Bar Objects
    private Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;

    //SharedPreferences
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor sharedPrefEdit;

    //Volley Objects
    private static final String TAG = "duvidas";
    private RequestQueue queue;

    private String actualRest;
    private List<Duvida> jsonDuvidas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewsById();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Todas as Dúvidas");

        addDrawerItems();
        setupDrawer();

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sharedPrefEdit = sharedPref.edit();

        actualRest = "duvidas/buscarDuvidas";
        volleyRequest(actualRest);

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        String sharedDuvidas = sharedPref.getString("jsonDuvidas", "");
        if (sharedDuvidas.length() > 1) {
            Type listType = new TypeToken<ArrayList<Duvida>>() {
            }.getType();
            jsonDuvidas = new Gson().fromJson(sharedDuvidas, listType);
            duvidaAdapter = new DuvidaAdapter(jsonDuvidas);
            recyclerView.setAdapter(duvidaAdapter);
        } else {
            jsonDuvidas = new ArrayList<>();
            duvidaAdapter = new DuvidaAdapter(jsonDuvidas);
            recyclerView.setAdapter(duvidaAdapter);
        }

        novaDuvida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NovaDuvidaActivity.class);
                startActivity(i);
            }
        });

        swipeRefreshDuvida.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
                public void onRefresh () {
                    volleyRequest(actualRest);
                }
            }

            );
        }

    private void addDrawerItems() {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String sharedUsuario = sharedPref.getString("jsonUsuario", "");
        Usuario usuario = new Gson().fromJson(sharedUsuario, Usuario.class);
        infoNomeUsuario.setText(usuario.getNome() + " " + usuario.getSobrenome());
        infoEmailUsuario.setText(usuario.getEmail());

        String[] osArray = {"Minhas Matérias", "Todas as Dúvidas", "Relacionadas", "Minhas Dúvidas", "Meu Perfil", "Desconectar"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, osArray);
        drawerList.setAdapter(arrayAdapter);


        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (id == 0) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    Intent i = new Intent(MainActivity.this, MateriaActivity.class);
                    startActivity(i);
                }

                if (id == 1) {
                    getSupportActionBar().setTitle("Todas as Dúvidas");
                    swipeRefreshDuvida.setRefreshing(true);
                    actualRest = "duvidas/buscarDuvidas";
                    volleyRequest(actualRest);
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }

                if (id == 2) {
                    getSupportActionBar().setTitle("Dúvidas Relacionadas");
                    swipeRefreshDuvida.setRefreshing(true);
                    actualRest = "duvidas/buscarDuvidasMateriaPorUsuario";
                    volleyRequest(actualRest);
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }

                if (id == 3) {
                    getSupportActionBar().setTitle("Minhas Dúvidas");
                    swipeRefreshDuvida.setRefreshing(true);
                    actualRest = "duvidas/buscarDuvidasUsuario";
                    volleyRequest(actualRest);
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }

                if (id == 4) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    Intent i = new Intent(MainActivity.this, PerfilUsuarioActivity.class);
                    startActivity(i);
                }

                if (id == 5) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor SharedPrefEdit = sharedPref.edit();
                    SharedPrefEdit.putString("jsonUsuario", "");
                    SharedPrefEdit.apply();
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings)
            return true;

        if (id == R.id.menu_pesquisa){
            pesquisaDialog = new PesquisaDialog(MainActivity.this);
            pesquisaDialog.show();
        }

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

    private void findViewsById() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        novaDuvida = (FloatingActionButton) findViewById(R.id.novaDuvida);
        recyclerView = (RecyclerView) findViewById(R.id.recycleViewDuvidas);
        infoNomeUsuario = (TextView) findViewById(R.id.infoNomeUsuario);
        infoEmailUsuario = (TextView) findViewById(R.id.infoEmailUsuario);
        drawerList = (ListView) findViewById(R.id.drawerList);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        infoDuvida = (LinearLayout) findViewById(R.id.infoDuvida);
        textInfoDuvida = (TextView) findViewById(R.id.textInfoDuvida);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        swipeRefreshDuvida = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshDuvida);
    }

    private void volleyRequest(String restRequest) {
        infoDuvida.setBackgroundColor(Color.parseColor("#FFA726"));
        textInfoDuvida.setText("Atualizando informações de dúvidas");
        infoDuvida.setVisibility(View.VISIBLE);

        String server = getString(R.string.wstcc);
        String url = server + restRequest;

        queue = Volley.newRequestQueue(this);

        StringRequest stringRequest;
        if(restRequest.equals("duvidas/buscarDuvidas")){
            stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response == null)
                                return;

                            Type listType = new TypeToken<ArrayList<Duvida>>() {
                            }.getType();
                            jsonDuvidas.clear();
                            jsonDuvidas = new Gson().fromJson(response, listType);
                            sharedPrefEdit.putString("jsonDuvidas", response);
                            duvidaAdapter.swap(jsonDuvidas);
                            sharedPrefEdit.commit();
                            infoDuvida.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            swipeRefreshDuvida.setRefreshing(false);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    infoDuvida.setBackgroundColor(Color.parseColor("#ff4444"));
                    infoDuvida.setVisibility(View.VISIBLE);
                    textInfoDuvida.setText("Não foi possível se conectar com o servidor");
                    progressBar.setVisibility(View.GONE);
                    swipeRefreshDuvida.setRefreshing(false);
                }
            });
        } else {
            stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response == null)
                                return;

                            Type listType = new TypeToken<ArrayList<Duvida>>() {
                            }.getType();
                            jsonDuvidas.clear();
                            jsonDuvidas = new Gson().fromJson(response, listType);
                            sharedPrefEdit.putString("jsonDuvidas", response);
                            duvidaAdapter.swap(jsonDuvidas);
                            sharedPrefEdit.commit();
                            infoDuvida.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            swipeRefreshDuvida.setRefreshing(false);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    infoDuvida.setVisibility(View.VISIBLE);
                    infoDuvida.setBackgroundColor(Color.parseColor("#ff4444"));
                    textInfoDuvida.setText("Não foi possível se conectar com o servidor");
                    progressBar.setVisibility(View.GONE);
                    swipeRefreshDuvida.setRefreshing(false);
                }
            }){
                @Override
                public byte[] getBody() throws AuthFailureError {
                    String sharedUsuario = sharedPref.getString("jsonUsuario", "");
                    return sharedUsuario.getBytes();
                }
            };
        }
        queue.add(stringRequest);
        stringRequest.setTag(TAG);
    }

    @Override
    public void onBackPressed() {
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
        System.exit(0);
    }

    public void posPesquisa(String pesquisa, List<Duvida> retorno){
        duvidaAdapter.swap(retorno);
        getSupportActionBar().setTitle(pesquisa);
    }

}
