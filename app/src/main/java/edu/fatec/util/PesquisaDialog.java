package edu.fatec.util;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.gqueiroz.androidtcc.R;

public class PesquisaDialog extends Dialog {
    private Activity c;
    private EditText conteudoPesquisa;
    private CheckBox checkTitulo;
    private CheckBox checkConteudo;
    private CheckBox checkMateria;
    private CheckBox checkTag;
    private Button refinarPesquisa;
    private Button novaPesquisa;
    private Button cancelaPesquisa;

    private boolean visible = false;

    public PesquisaDialog(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_pesquisa);

        findViewsById();

        checkTitulo.setVisibility(View.GONE);
        checkConteudo.setVisibility(View.GONE);
        checkMateria.setVisibility(View.GONE);
        checkTag.setVisibility(View.GONE);

        refinarPesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!visible) {
                    checkTitulo.setVisibility(View.VISIBLE);
                    checkConteudo.setVisibility(View.VISIBLE);
                    checkMateria.setVisibility(View.VISIBLE);
                    checkTag.setVisibility(View.VISIBLE);
                    visible = true;
                } else {
                    checkTitulo.setVisibility(View.GONE);
                    checkConteudo.setVisibility(View.GONE);
                    checkMateria.setVisibility(View.GONE);
                    checkTag.setVisibility(View.GONE);
                    visible = false;
                }
            }
        });

        cancelaPesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });

        novaPesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public void closeDialog(){
        this.dismiss();
    }

    public void findViewsById(){
        conteudoPesquisa = (EditText) findViewById(R.id.conteudoPesquisa);
        checkTitulo = (CheckBox) findViewById(R.id.checkTitulo);
        checkConteudo = (CheckBox) findViewById(R.id.checkConteudo);
        checkMateria = (CheckBox) findViewById(R.id.checkMateria);
        checkTag = (CheckBox) findViewById(R.id.checkTag);
        refinarPesquisa = (Button) findViewById(R.id.refinarPesquisa);
        novaPesquisa = (Button) findViewById(R.id.novaPesquisa);
        cancelaPesquisa = (Button) findViewById(R.id.cancelaPesquisa);
    }
}
