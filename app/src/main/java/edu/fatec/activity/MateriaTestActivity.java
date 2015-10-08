package edu.fatec.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.gqueiroz.androidtcc.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Type;

import edu.fatec.model.Materia;

public class MateriaTestActivity extends Activity {
    private LinearLayout linearMain;
    private CheckBox checkBox;
    private String jsonMaterias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materia_test);

        linearMain = (LinearLayout) findViewById(R.id.linearMain);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.167.118:8080/wstcc/materias/buscarMaterias";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Type listType = new TypeToken<ArrayList<Materia>>() {
                        }.getType();
                        List<Materia> materiasJson = new Gson().fromJson(response, listType);

                        for (Materia m : materiasJson) {
                            checkBox = new CheckBox(MateriaTestActivity.this);
                            checkBox.setId(m.getIdMateria());
                            checkBox.setText(m.getMateria());
                            linearMain.addView(checkBox);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_materia_test, menu);
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
