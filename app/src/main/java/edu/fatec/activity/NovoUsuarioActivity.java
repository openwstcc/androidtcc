package edu.fatec.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.NavUtils;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gqueiroz.androidtcc.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.fatec.model.Usuario;

public class NovoUsuarioActivity extends Activity {
    //View Objects
    private Button inserirUsuario;
    private EditText nome;
    private EditText sobreNome;
    private EditText telefone;
    private EditText dataNasc;
    private EditText email;
    private EditText senha;
    private ProgressBar progressBar;
    private LinearLayout infoNovoUsuario;
    private TextView textInfoNovoUsuario;

    //Date Objects
    private DatePickerDialog dataPicker;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_usuario);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        findViewsById();

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        setDateTimeField();

        dataNasc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                dataPicker.show();
            }
        });

        inserirUsuario.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  if(!validaCampos(novoUsuario()))
                      return;

                  infoNovoUsuario.setVisibility(View.VISIBLE);
                  progressBar.setVisibility(View.VISIBLE);
                  textInfoNovoUsuario.setText("Seu usuário está sendo criado.");
                  infoNovoUsuario.setBackgroundColor(Color.parseColor("#FFA726"));
                  inserirUsuario.setEnabled(false);

                  volleyRequest();
              }
          }
        );
    }

    public Usuario novoUsuario() {
        Usuario usuario = new Usuario();

        usuario.setNome(nome.getText().toString());
        usuario.setSobrenome(sobreNome.getText().toString());
        usuario.setEmail(email.getText().toString());
        usuario.setSenha(senha.getText().toString());
        usuario.setTelefone(telefone.getText().toString());

        try {
            String myFormat = "dd/MM/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
            usuario.setDataNasc(sdf.parse(dataNasc.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        usuario.setPerfil("A");

        return usuario;
    }

    private void findViewsById() {
        nome = (EditText) findViewById(R.id.nome);
        sobreNome = (EditText) findViewById(R.id.sobreNome);
        email = (EditText) findViewById(R.id.email);
        senha = (EditText) findViewById(R.id.senha);
        telefone = (EditText) findViewById(R.id.telefone);
        telefone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        dataNasc = (EditText) findViewById(R.id.dataNasc);
        dataNasc.setInputType(InputType.TYPE_NULL);
        inserirUsuario = (Button) findViewById(R.id.inserirUsuario);
        progressBar = (ProgressBar) findViewById(R.id.progressBarNovoUsuario);
        infoNovoUsuario = (LinearLayout) findViewById(R.id.infoNovoUsuario);
        textInfoNovoUsuario = (TextView) findViewById(R.id.textInfoNovoUsuario);
    }

    public void setDateTimeField() {
        Calendar calendar = Calendar.getInstance();
        dataPicker = new DatePickerDialog(NovoUsuarioActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dataNasc.setText(dateFormatter.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(dataNasc.getWindowToken(), 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void volleyRequest() {
        String server = getString(R.string.wstcc);

        RequestQueue queue = Volley.newRequestQueue(NovoUsuarioActivity.this);
        String url = server + "usuarios/inserirUsuario";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Usuário criado com sucesso!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(NovoUsuarioActivity.this, LoginActivity.class);
                        startActivity(i);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                inserirUsuario.setEnabled(true);
                textInfoNovoUsuario.setText("Por favor, tente novamente.");
                infoNovoUsuario.setBackgroundColor(Color.parseColor("#ff4444"));
                progressBar.setVisibility(View.GONE);
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                Gson gson = new GsonBuilder().create();
                String body = gson.toJson(novoUsuario());
                return body.getBytes();
            }
        };
        queue.add(stringRequest);
    }

    public boolean validaCampos(Usuario u){
        return true;
    }

}
