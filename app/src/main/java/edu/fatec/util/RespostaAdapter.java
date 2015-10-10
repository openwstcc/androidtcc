package edu.fatec.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gqueiroz.androidtcc.R;

import java.util.List;

import edu.fatec.model.Resposta;

public class RespostaAdapter extends RecyclerView.Adapter<RespostaAdapter.RespostaViewHolder> {

    private List<Resposta> RespostaList;

    public RespostaAdapter(List<Resposta> RespostaList) {
        this.RespostaList = RespostaList;
    }

    @Override
    public int getItemCount() {
        return RespostaList.size();
    }

    @Override
    public void onBindViewHolder(RespostaViewHolder RespostaViewHolder, int position) {
        Resposta r = RespostaList.get(position);
    }

    @Override
    public RespostaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.layout_resposta, viewGroup, false);

        return new RespostaViewHolder(itemView);
    }

    public static class RespostaViewHolder extends RecyclerView.ViewHolder {

        protected TextView RespostaTitulo;
        protected TextView RespostaConteudo;
        protected TextView RespostaCriador;

        public RespostaViewHolder(View v) {
            super(v);
            RespostaTitulo = (TextView) v.findViewById(R.id.respostaTitulo);
            RespostaConteudo = (TextView) v.findViewById(R.id.respostaConteudo);
            RespostaCriador = (TextView) v.findViewById(R.id.respostaCriador);
        }
    }

    public void swap(List<Resposta> Respostas) {
        RespostaList.clear();
        RespostaList.addAll(Respostas);
        this.notifyDataSetChanged();
    }

}
