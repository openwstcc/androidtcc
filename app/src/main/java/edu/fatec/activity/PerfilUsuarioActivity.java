package edu.fatec.activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.example.gqueiroz.androidtcc.R;
import com.google.gson.Gson;

import edu.fatec.model.Usuario;
import edu.fatec.util.SenhaDialog;

public class PerfilUsuarioActivity extends AppCompatActivity {
    private TextView viewEmail;
    private EditText editNome;
    private EditText editSobreNome;
    private EditText editTelefone;

    private Toolbar toolbar;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor sharedPrefEdit;

    private SenhaDialog senhaDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        findViewsById();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String sharedUsuario = sharedPref.getString("jsonUsuario", "");
        Usuario usuario = new Gson().fromJson(sharedUsuario, Usuario.class);

        viewEmail.setText(usuario.getEmail());
        editNome.setText(usuario.getNome());
        editSobreNome.setText(usuario.getSobrenome());
        editTelefone.setText(usuario.getTelefone());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_perfil_usuario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.alterarSenha) {
            senhaDialog = new SenhaDialog(PerfilUsuarioActivity.this);
            senhaDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void findViewsById(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewEmail = (TextView) findViewById(R.id.viewEmail);
        editNome = (EditText) findViewById(R.id.editNome);
        editSobreNome = (EditText) findViewById(R.id.editSobreNome);
        editTelefone = (EditText) findViewById(R.id.editTelefone);
    }
}
