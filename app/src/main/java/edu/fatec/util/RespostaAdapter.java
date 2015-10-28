package edu.fatec.util;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.gqueiroz.androidtcc.R;

import java.util.List;

import edu.fatec.json.JsonResposta;

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
        final JsonResposta r = RespostaList.get(position);
        respostaViewHolder.RespostaConteudo.setText(r.getResposta());
        respostaViewHolder.infoResposta.setText(r.getCriador()+" em "+r.getDataCriacao());

        respostaViewHolder.compartilharResposta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = r.getResposta()+"\nRespondida por "+r.getCriador();
                Intent i = new Intent();
                i.setAction(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_TEXT, msg);
                i.setType("text/plain");
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public RespostaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.layout_resposta, viewGroup, false);

        return new RespostaViewHolder(itemView);
    }

    public static class RespostaViewHolder extends RecyclerView.ViewHolder {

        protected TextView RespostaConteudo;
        protected TextView infoResposta;
        protected TextView curtirResposta;
        protected TextView compartilharResposta;

        public RespostaViewHolder(View v) {
            super(v);
            RespostaConteudo = (TextView) v.findViewById(R.id.respostaConteudo);
            infoResposta = (TextView) v.findViewById(R.id.infoResposta);
            curtirResposta = (TextView) v.findViewById(R.id.curtirResposta);
            compartilharResposta = (TextView) v.findViewById(R.id.compartilharResposta);
        }
    }

    public void swap(List<JsonResposta> Respostas) {
        RespostaList.clear();
        RespostaList.addAll(Respostas);
        this.notifyDataSetChanged();
    }

}
