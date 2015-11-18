package edu.fatec.activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.example.gqueiroz.androidtcc.R;
import com.google.gson.Gson;

import edu.fatec.model.Usuario;

public class PerfilUsuarioActivity extends AppCompatActivity {
    private TextView viewEmail;
    private EditText editNome;
    private EditText editSobreNome;
    private EditText editTelefone;
    private EditText editDataNasc;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor sharedPrefEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        findViewsById();

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String sharedUsuario = sharedPref.getString("jsonUsuario", "");
        Usuario usuario = new Gson().fromJson(sharedUsuario, Usuario.class);

        viewEmail.setText(usuario.getEmail());
        editNome.setText(usuario.getNome());
        editSobreNome.setText(usuario.getSobrenome());
        editTelefone.setText(usuario.getTelefone());
        editDataNasc.setText(usuario.getDataNasc().toString());
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void findViewsById(){
        viewEmail = (TextView) findViewById(R.id.viewEmail);
        editNome = (EditText) findViewById(R.id.editNome);
        editSobreNome = (EditText) findViewById(R.id.editSobreNome);
        editTelefone = (EditText) findViewById(R.id.editTelefone);
        editDataNasc = (EditText) findViewById(R.id.editDataNasc);
    }
}
