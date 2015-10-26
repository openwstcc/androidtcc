package edu.fatec.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.fatec.activity.MainActivity;
import edu.fatec.model.Materia;
import edu.fatec.model.Usuario;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gqueiroz.androidtcc.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;

    private String server;
    private String[] semestres = {"1º Semestre", "2º Semestre", "3º Semestre", "4º Semestre", "5º Semestre", "6º Semestre"};
    private List<Materia> materias;
    private List<Materia> materiasUsuario;
    private HashMap<String, List<String>> listValues = new HashMap<>();
    private HashMap<String, Boolean> controleMaterias = new HashMap<>();

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor SharedPrefEdit;

    public ExpandableListAdapter(Context context, List<Materia> materias, List<Materia> materiasUsuario) {
        this.context = context;
        this.materias = materias;
        this.materiasUsuario = materiasUsuario;

        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPrefEdit = sharedPref.edit();

        server = context.getString(R.string.wstcc);

        iniciaHashMap();
        materiasPorSemestre();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listValues.get(this.semestres[groupPosition]).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.semestres[groupPosition];
    }

    @Override
    public int getGroupCount() {
        return semestres.length;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listValues.get(this.semestres[groupPosition]).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final String materia = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.layout_materia, null);
        }

        final CheckBox itemMateria = (CheckBox) convertView.findViewById(R.id.itemMateria);
        itemMateria.setText(materia);
        itemMateria.setChecked(controleMaterias.get(materia));

        itemMateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    controleMaterias.put(materia, itemMateria.isChecked());
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.layout_semestre, null);
        }

        TextView itemSemestre = (TextView) convertView
                .findViewById(R.id.itemSemestre);
        itemSemestre.setTypeface(null, Typeface.BOLD);
        itemSemestre.setText(headerTitle);

        return convertView;
    }

    public void materiasPorSemestre() {
        List<String> mat1Semestre = new ArrayList<>();
        List<String> mat2Semestre = new ArrayList<>();
        List<String> mat3Semestre = new ArrayList<>();
        List<String> mat4Semestre = new ArrayList<>();
        List<String> mat5Semestre = new ArrayList<>();
        List<String> mat6Semestre = new ArrayList<>();

        for (Materia m : materias) {
            if (m.getSemestre() == 1)
                mat1Semestre.add(m.getMateria());
            if (m.getSemestre() == 2)
                mat2Semestre.add(m.getMateria());
            if (m.getSemestre() == 3)
                mat3Semestre.add(m.getMateria());
            if (m.getSemestre() == 4)
                mat4Semestre.add(m.getMateria());
            if (m.getSemestre() == 5)
                mat5Semestre.add(m.getMateria());
            if (m.getSemestre() == 6)
                mat6Semestre.add(m.getMateria());
        }

        listValues.put(semestres[0], mat1Semestre);
        listValues.put(semestres[1], mat2Semestre);
        listValues.put(semestres[2], mat3Semestre);
        listValues.put(semestres[3], mat4Semestre);
        listValues.put(semestres[4], mat5Semestre);
        listValues.put(semestres[5], mat6Semestre);
    }

    public void iniciaHashMap(){
        String todasMaterias = sharedPref.getString("jsonMaterias", "");
        Type listType = new TypeToken<ArrayList<Materia>>() {
        }.getType();
        List<Materia> materiasJson = new Gson().fromJson(todasMaterias, listType);

        for(Materia m : materiasJson)
            controleMaterias.put(m.getMateria(),false);

        for(Materia m : materiasUsuario)
            controleMaterias.put(m.getMateria(),true);
    }

    public void volleyAtualizarMateriasUsuario() {
        final List<Materia> materias = new ArrayList<>();
        for(Map.Entry<String, Boolean> entry : controleMaterias.entrySet()) {
            String key = entry.getKey();
            Boolean value = entry.getValue();

            String todasMaterias = sharedPref.getString("jsonMaterias", "");
            Type listType = new TypeToken<ArrayList<Materia>>() {}.getType();
            List<Materia> materiasJson = new Gson().fromJson(todasMaterias, listType);

            if(value){
                Materia materia = new Materia();
                materia.setMateria(key);
                int id;
                for(Materia m : materiasJson){
                    if(m.getMateria()==key)
                        id = m.getIdMateria();
                }
                materias.add(materia);
            }

        }

        RequestQueue queue = Volley.newRequestQueue(context);

        String url = server + "materias/atualizarMaterias";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("ON RESPONSE:",response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ON ERROR:",error.toString());
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                Gson gson = new GsonBuilder().create();
                String json = gson.toJson(materias);
                return json.getBytes();
            }
        };
        queue.add(stringRequest);
    }

    public void swap(List<Materia> materias, List<Materia> materiasUsuario){
        this.materias.clear();
        this.materias.addAll(materias);
        this.materiasUsuario.clear();
        this.materiasUsuario.addAll(materiasUsuario);
        this.notifyDataSetChanged();
    }

}
