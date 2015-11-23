package edu.fatec.util;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.gqueiroz.androidtcc.R;

public class RespostaHelp extends Dialog {
    private Activity c;
    private Button entendi;

    public RespostaHelp(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_resposta_ajuda);

        entendi = (Button) findViewById(R.id.entendi);
        entendi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });
    }

    public void closeDialog() {
        this.dismiss();
    }

}
