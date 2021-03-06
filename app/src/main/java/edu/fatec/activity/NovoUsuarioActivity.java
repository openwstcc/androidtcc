package edu.fatec.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import java.util.Locale;

import edu.fatec.model.Usuario;

public class NovoUsuarioActivity extends AppCompatActivity {
    //View Objects
    private FloatingActionButton inserirUsuario;
    private EditText nome;
    private EditText sobreNome;
    private EditText telefone;
    private EditText dataNasc;
    private EditText email;
    private EditText senha;
    private EditText senhaConfirma;
    private ProgressBar progressBarNovoUsuario;
    private LinearLayout infoNovoUsuario;
    private TextView textInfoNovoUsuario;

    private Toolbar toolbar;

    //Date Objects
    private DatePickerDialog datePicker;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_usuario);

        findViewsById();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        setDateTimeField();

        dataNasc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                datePicker.show();
            }
        });

        inserirUsuario.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!validator())
                            return;

                        volleyRequest();
                    }
                }
        );
    }

    private Usuario novoUsuario() {
        Usuario usuario = new Usuario();

        usuario.setNome(nome.getText().toString());
        usuario.setSobrenome(sobreNome.getText().toString());
        usuario.setEmail(email.getText().toString());
        usuario.setSenha(senha.getText().toString());
        usuario.setTelefone(telefone.getText().toString());

        try {
            String myFormat = "dd/MM/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
            usuario.setDataNasc(sdf.parse(dataNasc.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        usuario.setPerfil("A");

        return usuario;
    }

    private void findViewsById() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        nome = (EditText) findViewById(R.id.nome);
        sobreNome = (EditText) findViewById(R.id.sobreNome);
        email = (EditText) findViewById(R.id.email);
        senha = (EditText) findViewById(R.id.senha);
        senhaConfirma = (EditText) findViewById(R.id.senhaConfirma);
        telefone = (EditText) findViewById(R.id.telefone);
        dataNasc = (EditText) findViewById(R.id.dataNasc);
        dataNasc.setInputType(InputType.TYPE_NULL);
        inserirUsuario = (FloatingActionButton  ) findViewById(R.id.inserirUsuario);
        progressBarNovoUsuario = (ProgressBar) findViewById(R.id.progressBarNovoUsuario);
        infoNovoUsuario = (LinearLayout) findViewById(R.id.infoNovoUsuario);
        textInfoNovoUsuario = (TextView) findViewById(R.id.textInfoNovoUsuario);
    }

    private void setDateTimeField() {
        Calendar calendar = Calendar.getInstance();
        datePicker = new DatePickerDialog(NovoUsuarioActivity.this, new DatePickerDialog.OnDateSetListener() {
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

    private void volleyRequest() {
        infoNovoUsuario.setVisibility(View.VISIBLE);
        progressBarNovoUsuario.setVisibility(View.VISIBLE);
        textInfoNovoUsuario.setText("Seu usuário está sendo criado");
        infoNovoUsuario.setBackgroundColor(Color.parseColor("#FFA726"));
        inserirUsuario.setEnabled(false);

        String server = getString(R.string.wstcc);

        RequestQueue queue = Volley.newRequestQueue(NovoUsuarioActivity.this);
        String url = server + "usuarios/inserirUsuario";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("1062")) {
                            email.setError("Endereço de emai já existe. Por favor, informe outro endereço de email");
                            email.setFocusable(true);
                            inserirUsuario.setEnabled(true);
                            textInfoNovoUsuario.setText("Endereço de email já existe");
                            infoNovoUsuario.setBackgroundColor(Color.parseColor("#ff4444"));
                            progressBarNovoUsuario.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(getApplicationContext(), "Usuário criado com sucesso!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(NovoUsuarioActivity.this, LoginActivity.class);
                            startActivity(i);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                inserirUsuario.setEnabled(true);
                textInfoNovoUsuario.setText("Por favor, tente novamente.");
                infoNovoUsuario.setBackgroundColor(Color.parseColor("#ff4444"));
                progressBarNovoUsuario.setVisibility(View.GONE);
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

    private boolean validator() {
        if (!validaNome(nome))
            return false;
        else if (!validaSobreNome(sobreNome))
            return false;
        else if (!validaTelefone(telefone))
            return false;
        else if (!validaDataNasc(dataNasc))
            return false;
        else if (!validaEmail(email))
            return false;
        else if (!validaSenha(senha))
            return false;
        else if (!validaConfirmaSenha(senha, senhaConfirma))
            return false;
        else
            return true;
    }

    private boolean validaNome(View v) {
        EditText nome = (EditText) v;
        if (TextUtils.isEmpty(nome.getText())) {
            nome.setError("Insira um nome");
            nome.setFocusable(true);
            return false;
        } else if (nome.getText().length() < 3) {
            nome.setError("Nome inválido. Tamanho mínimo de 3 caracteres.");
            nome.setFocusable(true);
            return false;
        } else if (nome.getText().length() > 60) {
            nome.setError("Nome inválido. Tamanho máximo de 60 caracteres");
            nome.setFocusable(true);
            return false;
        } else
            return true;
    }

    private boolean validaSobreNome(View v) {
        EditText sobreNome = (EditText) v;
        if (TextUtils.isEmpty(sobreNome.getText())) {
            sobreNome.setError("Insira um sobrenome");
            sobreNome.setFocusable(true);
            return false;
        } else if (sobreNome.getText().length() < 5) {
            sobreNome.setError("Sobrenome inválido. Tamanho mínimo de 5 caracteres.");
            sobreNome.setFocusable(true);
            return false;
        } else if (sobreNome.getText().length() > 60) {
            sobreNome.setError("Sobrenome inválido. Tamanho máximo de 60 caracteres");
            sobreNome.setFocusable(true);
            return false;
        } else
            return true;
    }

    private boolean validaTelefone(View view) {
        EditText telefone = (EditText) view;
        if (TextUtils.isEmpty(telefone.getText())) {
            telefone.setError("Insira um telefone");
            telefone.setFocusable(true);
            return false;
        } else if (telefone.getText().length() < 5) {
            telefone.setError("Telefone inválido. Verifique o numero digitado");
            telefone.setFocusable(true);
            return false;
        } else if (telefone.getText().length() > 15) {
            telefone.setError("Telefone inválido. Verifique o numero digitado");
            telefone.setFocusable(true);
            return false;
        } else
            return true;
    }

    private boolean validaDataNasc(View view) {
        EditText dataNasc = (EditText) view;
        if (TextUtils.isEmpty(dataNasc.getText())) {
            dataNasc.setError("Insira uma data de nascimento");
            dataNasc.setFocusable(true);
            return false;
        } else
            return true;
    }

    private boolean validaEmail(View view) {
        EditText email = (EditText) view;
        if (TextUtils.isEmpty(email.getText())) {
            email.setError("Insira um email");
            email.setFocusable(true);
            return false;
        } else if (email.getText().length() < 5) {
            email.setError("Email inválido. Tamanho mínimo de 5 caracteres.");
            email.setFocusable(true);
            return false;
        } else if (email.getText().length() > 60) {
            email.setError("Email inválido. Tamanho máximo de 60 caracteres");
            email.setFocusable(true);
            return false;
        } else
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches();
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
