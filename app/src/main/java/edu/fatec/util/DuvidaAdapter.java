package edu.fatec.util;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gqueiroz.androidtcc.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.List;

import edu.fatec.activity.RespostaTestActivity;
import edu.fatec.model.Duvida;

public class DuvidaAdapter extends RecyclerView.Adapter<DuvidaAdapter.DuvidaViewHolder> {
    private View v;
    private List<Duvida> DuvidaList;

    public DuvidaAdapter(List<Duvida> DuvidaList) {
        this.DuvidaList = DuvidaList;
    }


    @Override
    public int getItemCount() {
        return DuvidaList.size();
    }

    @Override
    public void onBindViewHolder(DuvidaViewHolder duvidaViewHolder, int position) {
        Duvida d = DuvidaList.get(position);
        duvidaViewHolder.duvidaTitulo.setText(d.getTitulo());
        duvidaViewHolder.duvidaConteudo.setText(d.getConteudo());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        duvidaViewHolder.duvidaCriador.setText("Criada por " + d.getCriador()+" em "+d.getDataCriacao());
    }

    @Override
    public DuvidaViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.layout_duvida, viewGroup, false);
        final Duvida d = DuvidaList.get(position);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),RespostaTestActivity.class);
                Gson gson = new GsonBuilder().create();
                String jsonDuvida = gson.toJson(d);
                i.putExtra("duvida",jsonDuvida);
                v.getContext().startActivity(i);
            }
        });

        return new DuvidaViewHolder(itemView);
    }

    public static class DuvidaViewHolder extends RecyclerView.ViewHolder {

        protected TextView duvidaTitulo;
        protected TextView duvidaConteudo;
        protected TextView duvidaCriador;

        public DuvidaViewHolder(View v) {
            super(v);
            duvidaTitulo = (TextView) v.findViewById(R.id.duvidaTitulo);
            duvidaConteudo = (TextView) v.findViewById(R.id.duvidaConteudo);
            duvidaCriador = (TextView) v.findViewById(R.id.duvidaCriador);
        }
    }

    public void swap(List<Duvida> duvidas) {
        DuvidaList.clear();
        DuvidaList.addAll(duvidas);
        this.notifyDataSetChanged();
    }
}