package edu.fatec.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import edu.fatec.json.JsonResposta;
import edu.fatec.model.Resposta;
import edu.fatec.model.Usuario;

public class RespostaAdapter extends RecyclerView.Adapter<RespostaAdapter.RespostaViewHolder> {

    private List<JsonResposta> RespostaList;


    private SharedPreferences sharedPref;

    public RespostaAdapter(List<JsonResposta> RespostaList) {
        this.RespostaList = RespostaList;
    }

    @Override
    public int getItemCount() {
        return RespostaList.size();
    }

    @Override
    public void onBindViewHolder(final RespostaViewHolder respostaViewHolder, int position) {
        final JsonResposta r = RespostaList.get(position);
        respostaViewHolder.RespostaConteudo.setText(r.getResposta());
        respostaViewHolder.infoResposta.setText(r.getCriador() + " em " + r.getDataCriacao());

        respostaViewHolder.compartilharResposta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = r.getResposta() + "\nRespondida por " + r.getCriador();
                Intent i = new Intent();
                i.setAction(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_TEXT, msg);
                i.setType("text/plain");
                v.getContext().startActivity(i);

            }
        });
        respostaViewHolder.curtirResposta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volleyLike(r.getIdResposta(),v.getContext());
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
    public JsonResposta like(int idResposta,Context ct){
        sharedPref = PreferenceManager.getDefaultSharedPreferences(ct);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ct);
        String sharedUsuario = sharedPref.getString("jsonUsuario", "");
        Usuario usuario = new Gson().fromJson(sharedUsuario, Usuario.class);

        JsonResposta r = new JsonResposta();

        r.setIdUsuarioRank(usuario.getIdUsuario());
        r.setIdResposta(idResposta);
        return r;
    }

      public void volleyLike(final int idResposta, final Context cntx) {

        String url = "http://openwstcc-devbr.rhcloud.com/rest/respostas/adicionarRank";

        RequestQueue queue = Volley.newRequestQueue(cntx);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Falta IMPLEMENTAR OS EVENTOS APOS O LIKE

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(cntx, "Erro ao acesssar o servidor."+ new Gson().toJson(like(idResposta,cntx)), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                String resp = new Gson().toJson(like(idResposta,cntx));
                return resp.getBytes();
            }
        };
        queue.add(stringRequest);
    }

}
