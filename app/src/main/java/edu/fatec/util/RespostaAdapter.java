package edu.fatec.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.List;

import edu.fatec.json.JsonResposta;
import edu.fatec.model.Duvida;
import edu.fatec.model.Usuario;

public class RespostaAdapter extends RecyclerView.Adapter<RespostaAdapter.RespostaViewHolder> {

    private List<JsonResposta> RespostaList;
    private Usuario usuarioAtual;
    private Duvida duvidaAtual;

    private SharedPreferences sharedPref;
    private String curtiuDescurtiu;
    private RespostaViewHolder respostaViewHolderAux;

    private Activity c;
    private ValidaResposta validaResposta;

    public boolean valida = false;

    public RespostaAdapter(List<JsonResposta> RespostaList, Activity a) {
        this.RespostaList = RespostaList;
        this.c = a;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(a.getApplicationContext());
        usuarioAtual = new Gson().fromJson(sharedPref.getString("jsonUsuario", ""), Usuario.class);
        duvidaAtual = new Gson().fromJson(sharedPref.getString("jsonDuvidaTemp", ""), Duvida.class);
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
            respostaViewHolder.curtirIcon.setColorFilter(respostaViewHolder.accent);
        }

        if (r.isFlagProfessor()) {
            respostaViewHolder.backgroundReposta.setBackgroundColor(respostaViewHolder.success);
            respostaViewHolder.iconProf.setVisibility(View.VISIBLE);
        }

        if (r.isFlagCriador()) {
            respostaViewHolder.backgroundReposta.setBackgroundColor(respostaViewHolder.success);
            respostaViewHolder.iconAluno.setVisibility(View.VISIBLE);
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
                if (usuarioAtual.getIdUsuario() == duvidaAtual.getIdUsuario()) {
                    validaResposta = new ValidaResposta(c, RespostaAdapter.this, r.getIdResposta());
                    validaResposta.show();
                } else
                    volleyLike(r.getIdResposta(), v.getContext(), true);
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

        protected CardView cardView;
        protected LinearLayout backgroundReposta;
        protected TextView RespostaConteudo;
        protected TextView infoResposta;
        protected TextView curtirResposta;
        protected TextView compartilharResposta;
        protected TextView iconProf;
        protected TextView iconAluno;
        protected TextView textRank;
        protected ImageView curtirIcon;
        protected int accent;
        protected int textColor;
        protected int success;

        public RespostaViewHolder(View v) {
            super(v);
            cardView = (CardView) v.findViewById(R.id.card_view);
            RespostaConteudo = (TextView) v.findViewById(R.id.respostaConteudo);
            infoResposta = (TextView) v.findViewById(R.id.infoResposta);
            curtirResposta = (TextView) v.findViewById(R.id.curtirResposta);
            compartilharResposta = (TextView) v.findViewById(R.id.compartilharResposta);
            textRank = (TextView) v.findViewById(R.id.textRank);
            accent = v.getResources().getColor(R.color.colorAccentRipple);
            success = v.getResources().getColor(R.color.colorAccentRipple);
            textColor = v.getResources().getColor(R.color.textColor);
            iconProf = (TextView) v.findViewById(R.id.flagProf);
            iconAluno = (TextView) v.findViewById(R.id.flagCriador);
            backgroundReposta = (LinearLayout) v.findViewById(R.id.backgroundReposta);
            curtirIcon = (ImageView) v.findViewById(R.id.curtirIcon);
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

        if (usuarioAtual.getPerfil().equals("A"))
            r.setFlagCriador(valida);
        else
            r.setFlagProfessor(valida);

        return r;
    }

    public void volleyLike(final int idResposta, final Context cntx, final boolean likeValidacao) {
        String server = cntx.getString(R.string.wstcc);
        String url = server + "respostas/adicionarRank";

        RequestQueue queue = Volley.newRequestQueue(cntx);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        curtiuDescurtiu = response;
                        int rankAtual;
                        if (curtiuDescurtiu.equals("true") && valida==false) {
                            respostaViewHolderAux.curtirResposta.setTextColor(respostaViewHolderAux.accent);
                            respostaViewHolderAux.textRank.setTextColor(respostaViewHolderAux.accent);
                            respostaViewHolderAux.curtirIcon.setColorFilter(respostaViewHolderAux.accent);
                            rankAtual = Integer.valueOf(respostaViewHolderAux.textRank.getText().toString()) + 1;
                            respostaViewHolderAux.textRank.setText(Integer.toString(rankAtual));
                        } else {
                            respostaViewHolderAux.curtirResposta.setTextColor(respostaViewHolderAux.textColor);
                            respostaViewHolderAux.textRank.setTextColor(respostaViewHolderAux.textColor);
                            respostaViewHolderAux.curtirIcon.setColorFilter(respostaViewHolderAux.textColor);
                            rankAtual = Integer.valueOf(respostaViewHolderAux.textRank.getText().toString()) - 1;
                            respostaViewHolderAux.textRank.setText(Integer.toString(rankAtual));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(cntx, "Verifique sua conexão com a internet para curitr uma resposta.", Toast.LENGTH_SHORT).show();
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
