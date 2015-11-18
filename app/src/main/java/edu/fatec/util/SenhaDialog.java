package edu.fatec.util;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.gqueiroz.androidtcc.R;

public class SenhaDialog extends Dialog {
    private Activity c;
    private EditText senhaAntiga;
    private EditText senhaNova;
    private EditText senhaConfirma;
    private Button cancelaSenha;

    private boolean visible = false;

    public SenhaDialog(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_pesquisa);

        findViewsById();

        cancelaSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });
    }

    public void closeDialog(){
        this.dismiss();
    }

    public void findViewsById(){
        senhaAntiga = (EditText) findViewById(R.id.senhaAntiga);
        senhaNova = (EditText) findViewById(R.id.senhaNova);
        senhaConfirma = (EditText) findViewById(R.id.senhaConfirma);
        cancelaSenha = (Button) findViewById(R.id.cancelaSenha);
    }
}
