package edu.fatec.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import com.example.gqueiroz.androidtcc.R;
import com.google.gson.Gson;

import edu.fatec.activity.RespostaActivity;
import edu.fatec.model.Duvida;

public class ValidaResposta extends Dialog{
    private Activity c;
    private RespostaAdapter respostaAdapter;
    private int idResposta;
    private Button validaResposta;
    private Button apenasCurtir;
    private boolean isFlagCriador;

    private SharedPreferences sharedPref;

    public ValidaResposta(Activity a, RespostaAdapter respostaAdapter, int idResposta, boolean isFlagCriador){
        super(a);
        this.c = a;
        this.respostaAdapter = respostaAdapter;
        this.idResposta = idResposta;
        this.isFlagCriador = isFlagCriador;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_valida_resposta);

        findViewsById();

        apenasCurtir = (Button) findViewById(R.id.apenasCurtir);
        apenasCurtir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                respostaAdapter.volleyLike(idResposta, c, isFlagCriador);
                closeDialog();
            }
        });

        validaResposta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                respostaAdapter.valida = true;
                respostaAdapter.volleyLike(idResposta, c, isFlagCriador);
                closeDialog();

                sharedPref = PreferenceManager.getDefaultSharedPreferences(c.getApplicationContext());
                String jsonDuvida = sharedPref.getString("jsonDuvidaTemp", "");

                Intent i = new Intent(v.getContext(), RespostaActivity.class);
                i.putExtra("duvida", jsonDuvida);
                v.getContext().startActivity(i);
           }
        });
    }

    public void closeDialog() {
        this.dismiss();
    }

    public void findViewsById(){
        validaResposta = (Button) findViewById(R.id.validarResposta);
        apenasCurtir = (Button) findViewById(R.id.apenasCurtir);
    }
}
