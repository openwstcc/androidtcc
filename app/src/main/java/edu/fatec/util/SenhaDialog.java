package edu.fatec.util;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
    private Button atualizaSenha;

    private Toolbar toolbar;

    private boolean visible = false;

    public SenhaDialog(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_senha);

        findViewsById();

        cancelaSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });

        atualizaSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validator())
                    return;
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
        atualizaSenha = (Button) findViewById(R.id.atualizaSenha);
    }

    private boolean validator(){
        if (!validaSenha(senhaAntiga))
            return false;
        else if (!validaSenha(senhaNova))
            return false;
        else if (!validaConfirmaSenha(senhaNova, senhaConfirma))
            return false;
        else
            return true;
    }

    private boolean validaSenha(View view) {
        EditText senha = (EditText) view;
        if (TextUtils.isEmpty(senha.getText())) {
            senha.setError("Insira uma senha");
            senha.setFocusable(true);
            return false;
        } else if (senha.getText().length() < 5) {
            senha.setError("Senha inválida. Tamanho mínimo de 5 caracteres.");
            senha.setFocusable(true);
            return false;
        } else if (senha.getText().length() > 60) {
            senha.setError("Senha inválida. Tamanho máximo de 60 caracteres");
            senha.setFocusable(true);
            return false;
        } else
            return true;
    }

    private boolean validaConfirmaSenha(View viewSenha, View viewSenhaConfirma) {
        EditText senha = (EditText) viewSenha;
        EditText senhaConfirma = (EditText) viewSenhaConfirma;
        if (TextUtils.isEmpty(senhaConfirma.getText())) {
            senhaConfirma.setError("Insira uma senha");
            senhaConfirma.setFocusable(true);
            return false;
        } else if (!senha.getText().toString().equals(senhaConfirma.getText().toString())) {
            senhaConfirma.setError("Senha não são iguais");
            senha.setFocusable(true);
            return false;
        } else
            return true;
    }
}
