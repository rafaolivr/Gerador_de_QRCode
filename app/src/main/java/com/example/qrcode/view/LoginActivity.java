package com.example.qrcode.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.qrcode.R;
import com.example.qrcode.helper.ConfiguracaoFirebase;
import com.example.qrcode.model.Visitante;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText etLoginEmail, etLoginSenha;
    private Button btnEntrar;
    private Visitante visitante;
    private FirebaseAuth autenticacao;
    private ProgressBar pbEntrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Configura toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("");
        setSupportActionBar( toolbar );

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_green_24dp);

        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginSenha = findViewById(R.id.etLoginSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        pbEntrar = findViewById(R.id.pbEntrar);

        etLoginEmail.requestFocus();

        //Fazer login do visitante
        pbEntrar.setVisibility(View.GONE);
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etLoginEmail.getText().toString();
                String senha = etLoginSenha.getText().toString();

                if(!email.isEmpty()) {
                    if (!senha.isEmpty()) {

                        visitante = new Visitante();

                        visitante.setEmail(email);
                        visitante.setSenha(senha);
                        validarLogin(visitante);

                    } else {
                        Toast.makeText(LoginActivity.this, "Preencha a senha!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Preencha o e-mail!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void validarLogin(Visitante visitante){

        pbEntrar.setVisibility(View.VISIBLE);
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                visitante.getEmail(),
                visitante.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    pbEntrar.setVisibility(View.GONE);
                    Intent intent = new Intent(getApplicationContext(), TelaInicialLogadoActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(LoginActivity.this, "Erro ao fazer login", Toast.LENGTH_SHORT).show();
                    pbEntrar.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return false;

    }
}