package edu.fatec.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gqueiroz.androidtcc.R;

import java.util.List;

import edu.fatec.json.JsonResposta;
import edu.fatec.model.Resposta;

public class RespostaAdapter extends RecyclerView.Adapter<RespostaAdapter.RespostaViewHolder> {

    private List<JsonResposta> RespostaList;

    public RespostaAdapter(List<JsonResposta> RespostaList) {
        this.RespostaList = RespostaList;
    }

    @Override
    public int getItemCount() {
        return RespostaList.size();
    }

    @Override
    public void onBindViewHolder(RespostaViewHolder respostaViewHolder, int position) {
        JsonResposta r = RespostaList.get(position);
        respostaViewHolder.RespostaTitulo.setText(r.getCriador());
        respostaViewHolder.RespostaConteudo.setText(r.getResposta());
        respostaViewHolder.dataResposta.setText("Em "+r.getDataCriacao());
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
        protected TextView dataResposta;

        public RespostaViewHolder(View v) {
            super(v);
            RespostaTitulo = (TextView) v.findViewById(R.id.respostaTitulo);
            RespostaConteudo = (TextView) v.findViewById(R.id.respostaConteudo);
            dataResposta = (TextView) v.findViewById(R.id.dataResposta);
        }
    }

    public void swap(List<JsonResposta> Respostas) {
        RespostaList.clear();
        RespostaList.addAll(Respostas);
        this.notifyDataSetChanged();
    }

}
