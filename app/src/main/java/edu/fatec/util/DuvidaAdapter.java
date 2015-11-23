package edu.fatec.util;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.gqueiroz.androidtcc.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import edu.fatec.activity.RespostaActivity;
import edu.fatec.model.Duvida;
import edu.fatec.model.Materia;
import edu.fatec.model.Usuario;

public class DuvidaAdapter extends RecyclerView.Adapter<DuvidaAdapter.DuvidaViewHolder> {
    private List<Duvida> duvidaList;
    private Activity c;
    private SharedPreferences sharedPref;
    private List<Materia> materias;

    public DuvidaAdapter(List<Duvida> duvidaList, Activity a) {
        this.duvidaList = duvidaList;
        this.c = a;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(a.getApplicationContext());
        Type listType = new TypeToken<ArrayList<Materia>>() {
        }.getType();
        materias = new Gson().fromJson(sharedPref.getString("jsonMaterias", ""), listType);
    }


    @Override
    public int getItemCount() {
        return duvidaList.size();
    }

    @Override
    public void onBindViewHolder(final DuvidaViewHolder duvidaViewHolder, int position) {
        final Duvida d = duvidaList.get(position);
        duvidaViewHolder.duvidaTitulo.setText(d.getTitulo());
        duvidaViewHolder.duvidaConteudo.setText(d.getConteudo());
        duvidaViewHolder.infoDuvida.setText(Html.fromHtml("<b>" + d.getQtdRespostas() + "</b> Resposta(s)"));
        duvidaViewHolder.infoCriador.setText(Html.fromHtml("Criada por " + d.getCriador() + " em " + d.getDataCriacao()));

        String temp = " ";
        for (int i : d.getMaterias())
                for (Materia m : materias)
                    if (i == m.getIdMateria())
                        temp = temp + m.getMateria() + ";\n ";

        duvidaViewHolder.infoMateria.setText(Html.fromHtml("<b>Matérias:</b>" + temp));

        String tags = " ";
        if (d.getTags().length > 1)
            for (String tag : d.getTags())
                tags = tags + tag + ";\n ";
        else
            tags = tags + "Não possui tags.";

        duvidaViewHolder.infoTag.setText(Html.fromHtml("<b>Tags:</b> " + tags));

        duvidaViewHolder.replyDuvida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), RespostaActivity.class);
                Gson gson = new GsonBuilder().create();
                String jsonDuvida = gson.toJson(d);
                i.putExtra("duvida", jsonDuvida);
                v.getContext().startActivity(i);
            }
        });

        duvidaViewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alternaView(duvidaViewHolder.duvidaConteudo, duvidaViewHolder.infoMateria, duvidaViewHolder.infoTag, duvidaViewHolder.infoCriador);
            }
        });

    }

    @Override
    public DuvidaViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.layout_duvida_test, viewGroup, false);

        return new DuvidaViewHolder(itemView);
    }

    public static class DuvidaViewHolder extends RecyclerView.ViewHolder {

        protected TextView duvidaTitulo;
        protected TextView duvidaConteudo;
        protected TextView infoDuvida;
        protected TextView infoMateria;
        protected TextView infoTag;
        protected TextView infoCriador;
        protected Button replyDuvida;
        protected CardView card;

        public DuvidaViewHolder(View v) {
            super(v);
            duvidaTitulo = (TextView) v.findViewById(R.id.duvidaTitulo);
            duvidaConteudo = (TextView) v.findViewById(R.id.duvidaConteudo);
            infoDuvida = (TextView) v.findViewById(R.id.infoDuvida);
            infoMateria = (TextView) v.findViewById(R.id.materiasDuvida);
            infoTag = (TextView) v.findViewById(R.id.duvidasTag);
            infoCriador = (TextView) v.findViewById(R.id.infoCriador);
            replyDuvida = (Button) v.findViewById(R.id.replyDuvida);
            card = (CardView) v.findViewById(R.id.card_view);
        }
    }

    public void swap(List<Duvida> duvidas) {
        duvidaList.clear();
        duvidaList.addAll(duvidas);
        this.notifyDataSetChanged();
    }

    public void alternaView(TextView conteudo, TextView materias, TextView tags, TextView criador) {
        if (materias.getVisibility() == View.GONE) {
            materias.setVisibility(View.VISIBLE);
            tags.setVisibility(View.VISIBLE);
            criador.setVisibility(View.VISIBLE);
            conteudo.setMaxLines(999);
        } else {
            materias.setVisibility(View.GONE);
            tags.setVisibility(View.GONE);
            criador.setVisibility(View.GONE);
            conteudo.setMaxLines(3);
        }

    }
}
