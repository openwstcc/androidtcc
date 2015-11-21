package edu.fatec.util;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import com.example.gqueiroz.androidtcc.R;

public class ValidaResposta extends Dialog{
    private Activity c;
    private RespostaAdapter respostaAdapter;
    private int idResposta;
    private Button validaResposta;
    private Button apenasCurtir;

    public ValidaResposta(Activity a, RespostaAdapter respostaAdapter, int idResposta){
        super(a);
        this.c = a;
        this.respostaAdapter = respostaAdapter;
        this.idResposta = idResposta;
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
                respostaAdapter.volleyLike(idResposta, c, true);
                closeDialog();
            }
        });

        validaResposta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                respostaAdapter.valida = true;
                respostaAdapter.volleyLike(idResposta, c, true);
                closeDialog();
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
