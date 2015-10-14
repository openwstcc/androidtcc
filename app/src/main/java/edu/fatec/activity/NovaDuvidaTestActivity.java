package edu.fatec.activity;

import android.app.Activity;
import android.os.Bundle;

import com.example.gqueiroz.androidtcc.R;

public class NovaDuvidaTestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_duvida_test);

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
