package edu.fatec.util;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.fatec.model.Materia;

import com.example.gqueiroz.androidtcc.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;

    private String[] semestres = {"1º Semestre", "2º Semestre", "3º Semestre", "4º Semestre", "5º Semestre", "6º Semestre"};
    private List<Materia> materias;
    private HashMap<String, List<String>> listValues = new HashMap<>();

    public ExpandableListAdapter(Context context, List<Materia> materias) {
        this.context = context;
        this.materias = materias;

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

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.layout_materia, null);
        }

        CheckBox itemMateria = (CheckBox) convertView.findViewById(R.id.itemMateria);
        itemMateria.setText(childText);

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
            if(m.getSemestre()==1)
                mat1Semestre.add(m.getMateria());
            if(m.getSemestre()==2)
                mat2Semestre.add(m.getMateria());
            if(m.getSemestre()==3)
                mat3Semestre.add(m.getMateria());
            if(m.getSemestre()==4)
                mat4Semestre.add(m.getMateria());
            if(m.getSemestre()==5)
                mat5Semestre.add(m.getMateria());
            if(m.getSemestre()==6)
                mat6Semestre.add(m.getMateria());
        }

        listValues.put(semestres[0],mat1Semestre);
        listValues.put(semestres[1],mat2Semestre);
        listValues.put(semestres[2],mat3Semestre);
        listValues.put(semestres[3],mat4Semestre);
        listValues.put(semestres[4],mat5Semestre);
        listValues.put(semestres[5],mat6Semestre);
    }

}
