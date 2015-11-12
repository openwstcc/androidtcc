package edu.fatec.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.preference.PreferenceManager;
import android.support.v4.graphics.drawable.DrawableCompat;
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

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import edu.fatec.json.JsonResposta;
import edu.fatec.model.Resposta;
import edu.fatec.model.Usuario;

public class RespostaAdapter extends RecyclerView.Adapter<RespostaAdapter.RespostaViewHolder> {

    private List<JsonResposta> RespostaList;

    private SharedPreferences sharedPref;
    private String curtiuDescurtiu;
    private RespostaViewHolder respostaViewHolderAux;

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
        respostaViewHolder.textRank.setText(String.valueOf(r.getRank()));

        if (r.isDeuLike()) {
            respostaViewHolder.curtirResposta.setTextColor(respostaViewHolder.accent);
            respostaViewHolder.textRank.setTextColor(respostaViewHolder.accent);
            for (Drawable d : respostaViewHolder.curtirResposta.getCompoundDrawables()) {
                if (d != null){
                    Drawable wrapDrawable = DrawableCompat.wrap(d);
                    DrawableCompat.setTint(wrapDrawable, respostaViewHolder.accent);
                }
            }
        } else {
            for (Drawable d : respostaViewHolder.curtirResposta.getCompoundDrawables()) {
                if (d != null){
                    Drawable wrapDrawable = DrawableCompat.wrap(d);
                    DrawableCompat.setTint(wrapDrawable, respostaViewHolder.textColor);
                }
            }
        }

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
                respostaViewHolderAux = respostaViewHolder;
                volleyLike(r.getIdResposta(), v.getContext());
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
        protected TextView textRank;
        protected int accent;
        protected int textColor;


        public RespostaViewHolder(View v) {
            super(v);
            RespostaConteudo = (TextView) v.findViewById(R.id.respostaConteudo);
            infoResposta = (TextView) v.findViewById(R.id.infoResposta);
            curtirResposta = (TextView) v.findViewById(R.id.curtirResposta);
            compartilharResposta = (TextView) v.findViewById(R.id.compartilharResposta);
            textRank = (TextView) v.findViewById(R.id.textRank);
            accent = v.getResources().getColor(R.color.colorAccentRipple);
            textColor = v.getResources().getColor(R.color.textColor);
        }
    }

    public void swap(List<JsonResposta> Respostas) {
        RespostaList.clear();
        RespostaList.addAll(Respostas);
        this.notifyDataSetChanged();
    }

    public JsonResposta like(int idResposta, Context ct) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(ct);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ct);
        String sharedUsuario = sharedPref.getString("jsonUsuario", "");
        Usuario usuario = new Gson().fromJson(sharedUsuario, Usuario.class);

        JsonResposta r = new JsonResposta();

        r.setUsuarioLogado(usuario.getIdUsuario());
        r.setIdResposta(idResposta);
        return r;
    }

    public void volleyLike(final int idResposta, final Context cntx) {
        String server = cntx.getString(R.string.wstcc);
        String url = server + "respostas/adicionarRank";

        RequestQueue queue = Volley.newRequestQueue(cntx);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        curtiuDescurtiu = response;
                        int rankAtual;
                        if (curtiuDescurtiu.equals("true")) {
                            respostaViewHolderAux.curtirResposta.setTextColor(respostaViewHolderAux.accent);
                            respostaViewHolderAux.textRank.setTextColor(respostaViewHolderAux.accent);
                            for (Drawable d : respostaViewHolderAux.curtirResposta.getCompoundDrawables()) {
                                if (d != null){
                                    Drawable wrapDrawable = DrawableCompat.wrap(d);
                                    DrawableCompat.setTint(wrapDrawable, respostaViewHolderAux.accent);
                                }
                            }
                            rankAtual = Integer.valueOf(respostaViewHolderAux.textRank.getText().toString()) + 1;
                            respostaViewHolderAux.textRank.setText(Integer.toString(rankAtual));
                        } else {
                            respostaViewHolderAux.curtirResposta.setTextColor(respostaViewHolderAux.textColor);
                            respostaViewHolderAux.textRank.setTextColor(respostaViewHolderAux.textColor);
                            for (Drawable d : respostaViewHolderAux.curtirResposta.getCompoundDrawables()) {
                                if (d != null){
                                    Drawable wrapDrawable = DrawableCompat.wrap(d);
                                    DrawableCompat.setTint(wrapDrawable, respostaViewHolderAux.textColor);
                                }
                            }
                            rankAtual = Integer.valueOf(respostaViewHolderAux.textRank.getText().toString()) - 1;
                            respostaViewHolderAux.textRank.setText(Integer.toString(rankAtual));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(cntx, "Erro ao acesssar o servidor." + new Gson().toJson(like(idResposta, cntx)), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                String resp = new Gson().toJson(like(idResposta, cntx));
                return resp.getBytes();
            }
        };
        queue.add(stringRequest);
    }

}
