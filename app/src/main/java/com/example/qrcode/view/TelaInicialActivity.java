package com.example.qrcode.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.qrcode.R;
import com.example.qrcode.helper.ConfiguracaoFirebase;
import com.example.qrcode.view.ar.ARActivity;
import com.google.firebase.auth.FirebaseAuth;

public class TelaInicialActivity extends AppCompatActivity {

    private Button btnEntrar, btnEncontrar, btnTutorial;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        } else {
            verificarVisitanteLogado();
        }

        btnEntrar = findViewById(R.id.btnEntrar);
        btnEncontrar = findViewById(R.id.btnEncontrarLogado);
        btnTutorial = findViewById(R.id.btnTutorial);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PreLoginActivity.class);
                startActivity(intent);
            }
        });

        btnEncontrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ARActivity.class);
                startActivity(intent);
            }
        });

        btnTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TutorialActivity.class);
                startActivity(intent);
            }
        });
    }

    public void verificarVisitanteLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if(autenticacao.getCurrentUser() != null){
            Intent intent = new Intent(getApplicationContext(), TelaInicialLogadoActivity.class);
            startActivity(intent);
        }
    }
}

