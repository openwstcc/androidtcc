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
    private Button validaResposta;
    private Button apenasCurtir;

    public ValidaResposta(Activity a){
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_valida_resposta);

        findViewsById();

        validaResposta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });

        apenasCurtir = (Button) findViewById(R.id.apenasCurtir);
        apenasCurtir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
